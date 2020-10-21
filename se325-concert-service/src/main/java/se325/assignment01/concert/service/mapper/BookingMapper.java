package se325.assignment01.concert.service.mapper;

import se325.assignment01.concert.common.dto.BookingDTO;
import se325.assignment01.concert.common.dto.BookingRequestDTO;
import se325.assignment01.concert.common.dto.SeatDTO;
import se325.assignment01.concert.service.domain.Booking;
import se325.assignment01.concert.service.domain.Seat;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Helper class to convert between domain-model and DTO objects representing
 * Bookings.
 */
public class BookingMapper {

    public static BookingDTO toDto(Booking booking) {

        List<SeatDTO> dtoSeats = new ArrayList<>();

        List<Seat> seats = booking.getSeatList();

        for (Seat s: seats) {
            dtoSeats.add(SeatMapper.toDto(s));
        }

        BookingDTO dtoBooking = new BookingDTO(booking.getConcertId(), booking.getDate(), dtoSeats);

        return dtoBooking;
    }

    public static Booking bookingRequestToDomain(BookingRequestDTO bookingRequestDTO) {
        Booking booking = new Booking(bookingRequestDTO.getConcertId(), bookingRequestDTO.getDate());

        return booking;
    }
}
