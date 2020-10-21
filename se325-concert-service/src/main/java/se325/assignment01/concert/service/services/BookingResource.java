package se325.assignment01.concert.service.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se325.assignment01.concert.common.dto.BookingDTO;
import se325.assignment01.concert.common.dto.BookingRequestDTO;
import se325.assignment01.concert.service.domain.AuthToken;
import se325.assignment01.concert.service.domain.Booking;
import se325.assignment01.concert.service.domain.Concert;
import se325.assignment01.concert.service.domain.Seat;
import se325.assignment01.concert.service.mapper.BookingMapper;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.OptimisticLockException;
import javax.persistence.TypedQuery;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.ResponseBuilder;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Path("/concert-service")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookingResource {

    private static Logger LOGGER = LoggerFactory.getLogger(BookingResource.class);

    @POST
    @Path("/bookings")
    public Response createBooking(BookingRequestDTO bookingRequestDTO, @CookieParam("auth") Cookie cookie) {
        System.out.println("Start booking");
        EntityManager em = PersistenceManager.instance().createEntityManager();
        try {
            em.getTransaction().begin();
            Booking booking = BookingMapper.bookingRequestToDomain(bookingRequestDTO);
            Concert concert = em.find(Concert.class, bookingRequestDTO.getConcertId());

            if (concert == null) {
                //check if concert exists for booking or if the booking date is valid for the chosen concert
                LOGGER.info("concert null");
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            if (!concert.getDates().contains(bookingRequestDTO.getDate())) {
                LOGGER.info("dates bad");
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            if (cookie == null) {
                //Check user cookie to make sure they are authorised
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }

            AuthToken authToken = em.find(AuthToken.class, cookie.getValue());

            if (authToken == null) {
                //Check token given to user is still valid
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }

            List<Seat> seats = new ArrayList<>();

            //Create a query to obtain each set separately and make sure optimistic locking is on so we check version No
            TypedQuery<Seat> seatTypedQuery = em.createQuery("select s from Seat s where s.date = :date and s.label in :labels and s.isBooked = false", Seat.class).setParameter("date", bookingRequestDTO.getDate()).setParameter("labels", bookingRequestDTO.getSeatLabels()).setLockMode(LockModeType.OPTIMISTIC);
            seats = seatTypedQuery.getResultList();

            if (seats.size() != bookingRequestDTO.getSeatLabels().size()) {
                //Check that there aren't any booked seats in request
                return Response.status(Response.Status.FORBIDDEN).build();
            }

            for (Seat s : seats) {
                s.setBooked(true);
                s.setBooking(booking);
                em.persist(s);
            }
            booking.setUser(authToken.getUser());
            booking.setSeatList(seats);

            em.persist(booking);
            em.getTransaction().commit();

            //check notification


            URI location = new URI("http://localhost:10000/services/concert-service/bookings/" + booking.getId().toString());

            return Response.status(Response.Status.CREATED).location(location).build();

        } catch (OptimisticLockException e) {
            //Failure to book due to out-of-date version
            em.close();
            //Recall same function to retry booking
            createBooking(bookingRequestDTO, cookie);
        } catch (URISyntaxException e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        } finally {
            em.close();
        }

        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    @GET
    @Path("/bookings")
    public Response getBookings(@CookieParam("auth") Cookie cookie) {
        EntityManager em = PersistenceManager.instance().createEntityManager();

        try {
            if (cookie == null) {
                //Check user cookie to make sure they are authorised
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }

            AuthToken authToken = em.find(AuthToken.class, cookie.getValue());
            if (authToken == null) {
                //Check token is valid
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }

            TypedQuery<Booking> query = em.createQuery("select b from Booking b where b.user = :user", Booking.class).setParameter("user", authToken.getUser());
            List<Booking> bookings = query.getResultList();
            List<BookingDTO> dtoBookings = new ArrayList<>();

            for (Booking b: bookings) {
                dtoBookings.add(BookingMapper.toDto(b));
            }

            ResponseBuilder builder = Response.ok(dtoBookings);
            Response response = builder.build();

            return response;
        } finally {
            em.close();
        }
    }

    @GET
    @Path("/bookings/{id}")
    public Response getBookingById(@PathParam("id") long id, @CookieParam("auth") Cookie cookie) {
        EntityManager em = PersistenceManager.instance().createEntityManager();

        try {
            if (cookie == null) {
                //Check user cookie to make sure they are authorised
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }

            AuthToken authToken = em.find(AuthToken.class, cookie.getValue());
            if (authToken == null) {
                //Check token is valid
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }

            Booking booking = em.find(Booking.class, id);
            if (booking == null) {
                //Check booking id is valid
                return Response.status(Response.Status.NOT_FOUND).build();
            }

            if (!authToken.getUser().equals(booking.getUser())) {
                //Check that booking belongs to the user calling
                return Response.status(Response.Status.FORBIDDEN).build();
            }

            ResponseBuilder builder = Response.ok(BookingMapper.toDto(booking));
            Response response = builder.build();

            return response;
        } finally {
            em.close();
        }

    }
}
