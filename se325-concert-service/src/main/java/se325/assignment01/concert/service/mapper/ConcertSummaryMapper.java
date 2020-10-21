package se325.assignment01.concert.service.mapper;

import se325.assignment01.concert.common.dto.ConcertSummaryDTO;
import se325.assignment01.concert.service.domain.Concert;

/**
 * Helper class to convert between domain-model and DTO objects representing
 * Concerts and their summaries.
 */
public class ConcertSummaryMapper {

    public static ConcertSummaryDTO toDto(Concert concert) {
        ConcertSummaryDTO dtoConcertSummary = new ConcertSummaryDTO(concert.getId(), concert.getTitle(), concert.getImageName());

        return dtoConcertSummary;
    }
}
