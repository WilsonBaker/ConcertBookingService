package se325.assignment01.concert.service.mapper;

import se325.assignment01.concert.common.dto.SeatDTO;
import se325.assignment01.concert.service.domain.Seat;

/**
 * Helper class to convert between domain-model and DTO objects representing
 * Seats.
 */
public class SeatMapper {

    public static SeatDTO toDto(Seat seat) {
        SeatDTO dtoSeat = new SeatDTO(seat.getLabel(), seat.getPrice());

        return dtoSeat;
    }
}
