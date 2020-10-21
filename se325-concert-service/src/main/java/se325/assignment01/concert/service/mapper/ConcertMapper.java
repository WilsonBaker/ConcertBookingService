package se325.assignment01.concert.service.mapper;

import se325.assignment01.concert.common.dto.ConcertDTO;
import se325.assignment01.concert.common.dto.PerformerDTO;

import se325.assignment01.concert.service.domain.Concert;
import se325.assignment01.concert.service.domain.Performer;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Helper class to convert between domain-model and DTO objects representing
 * Concerts.
 */
public class ConcertMapper {

    public static Concert toDomainModel(ConcertDTO dtoConcert) {
        Concert concert = new Concert(dtoConcert.getId(), dtoConcert.getTitle(), dtoConcert.getBlurb(), dtoConcert.getImageName());

        return concert;
    }

    public static ConcertDTO toDto (Concert concert) {
        ConcertDTO dtoConcert = new ConcertDTO(concert.getId(), concert.getTitle(), concert.getImageName(), concert.getBlurb());

        List<PerformerDTO> dtoPerformers = new ArrayList<>();

        Set<Performer> performers = concert.getPerformers();

        for (Performer p: performers) {
            dtoPerformers.add(PerformerMapper.toDto(p));
        }

        dtoConcert.setPerformers(dtoPerformers);

        dtoConcert.setDates(new ArrayList<LocalDateTime>(concert.getDates()));

        return dtoConcert;
    }

}
