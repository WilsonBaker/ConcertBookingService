package se325.assignment01.concert.service.services;

import se325.assignment01.concert.common.dto.PerformerDTO;
import se325.assignment01.concert.service.domain.Concert;
import se325.assignment01.concert.service.domain.Performer;
import se325.assignment01.concert.service.mapper.ConcertMapper;
import se325.assignment01.concert.service.mapper.PerformerMapper;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.ResponseBuilder;
import java.util.ArrayList;
import java.util.List;

@Path("/concert-service")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PerformerResource {

    @GET
    @Path("/performers/{id}")
    public Response getPerformerById(@PathParam("id") long id) {
        EntityManager em = PersistenceManager.instance().createEntityManager();

        try {
            //Start transaction for persisting audit data
            em.getTransaction().begin();

            //Or load object by ID
            Performer performer = em.find(Performer.class, id);

            if (performer == null) {
                //Return HTTP 404 response if Performer is not found
                return Response.status(Response.Status.NOT_FOUND).build();
            }

            PerformerDTO performerDTO = PerformerMapper.toDto(performer);

            return Response.ok(performerDTO).build();
        } finally {
            em.close();
        }
    }

    @GET
    @Path("/performers")
    public Response getPerformers() {
        EntityManager em = PersistenceManager.instance().createEntityManager();

        try {
            TypedQuery<Performer> query = em.createQuery("select p from Performer p", Performer.class);
            List<Performer> performers = query.getResultList();
            List<PerformerDTO> dtoPerformers = new ArrayList<>();

            for (Performer p: performers) {
                dtoPerformers.add(PerformerMapper.toDto(p));
            }

            ResponseBuilder builder = Response.ok(dtoPerformers);

            Response response = builder.build();

            return response;
        } finally {
            em.close();
        }
    }

}
