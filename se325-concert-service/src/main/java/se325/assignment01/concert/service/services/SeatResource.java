package se325.assignment01.concert.service.services;

import se325.assignment01.concert.common.dto.SeatDTO;
import se325.assignment01.concert.common.types.BookingStatus;
import se325.assignment01.concert.service.domain.Seat;
import se325.assignment01.concert.service.jaxrs.LocalDateTimeParam;
import se325.assignment01.concert.service.mapper.SeatMapper;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Path("/concert-service")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SeatResource {

    @GET
    @Path("/seats/{date}")
    public Response getSeats(@PathParam("date") LocalDateTimeParam dateTimeParam, @QueryParam("status") BookingStatus status) {
        EntityManager em = PersistenceManager.instance().createEntityManager();
        LocalDateTime date = dateTimeParam.getLocalDateTime();

       try {
           em.getTransaction().begin();
           TypedQuery<Seat> query;

           if (status == BookingStatus.Any) {
               query = em.createQuery("select s from Seat s where s.date = :date", Seat.class).setParameter("date", date);

           } else {
               boolean isBooked;
               if (status == BookingStatus.Booked) {
                   isBooked = true;
               } else {
                   isBooked = false;
               }
               query = em.createQuery("select s from Seat s where s.date = :date and s.isBooked = :isBooked", Seat.class).setParameter("date", date).setParameter("isBooked", isBooked);

           }

           List<Seat> bookedSeats = query.getResultList();
           List<SeatDTO> dtoBookedSeats = new ArrayList<>();

           for (Seat s: bookedSeats) {
               dtoBookedSeats.add(SeatMapper.toDto(s));
           }

           GenericEntity<List<SeatDTO>> entity = new GenericEntity<List<SeatDTO>>(dtoBookedSeats) {};

           return Response.ok(entity).build();

        } finally {
            em.close();
        }
    }
}
