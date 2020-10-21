package se325.assignment01.concert.service.mapper;

import se325.assignment01.concert.common.dto.PerformerDTO;
import se325.assignment01.concert.service.domain.Performer;

/**
 * Helper class to convert between domain-model and DTO objects representing
 * Performers.
 */
public class PerformerMapper {

    public static Performer toDomainModel(PerformerDTO dtoPerformer) {
        Performer performer = new Performer(dtoPerformer.getId(), dtoPerformer.getName(), dtoPerformer.getImageName(), dtoPerformer.getGenre(), dtoPerformer.getBlurb());

        return performer;
    }

    public static PerformerDTO toDto(Performer performer) {
        PerformerDTO dtoPerformer = new PerformerDTO(performer.getId(), performer.getName(), performer.getImageName(), performer.getGenre(), performer.getBlurb());

        return dtoPerformer;
    }
}
