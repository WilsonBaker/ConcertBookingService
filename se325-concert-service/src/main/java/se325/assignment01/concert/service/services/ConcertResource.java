package se325.assignment01.concert.service.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se325.assignment01.concert.common.dto.ConcertDTO;
import se325.assignment01.concert.common.dto.ConcertSummaryDTO;
import se325.assignment01.concert.service.domain.Concert;
import se325.assignment01.concert.service.mapper.ConcertMapper;
import se325.assignment01.concert.service.mapper.ConcertSummaryMapper;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.ResponseBuilder;
import java.util.ArrayList;
import java.util.List;

@Path("/concert-service")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ConcertResource {

    private static Logger LOGGER = LoggerFactory.getLogger(ConcertResource.class);

    @GET
    @Path("/concerts/{id}")
    public Response getConcertById(@PathParam("id") long id) {

        EntityManager em = PersistenceManager.instance().createEntityManager();

        try {
            //Start transaction for persisting audit data
            em.getTransaction().begin();

            //Or load object by ID
            Concert concert = em.find(Concert.class, id);
            em.getTransaction().commit();

            if (concert == null) {
                //Return HTTP 404 response if Concert is not found
                return Response.status(Response.Status.NOT_FOUND).build();
            }

            return Response.ok(ConcertMapper.toDto(concert)).build();
        } finally {
            em.close();
        }
    }

    @GET
    @Path("/concerts")
    public Response getConcerts() {

        EntityManager em = PersistenceManager.instance().createEntityManager();

        try {
            em.getTransaction().begin();
            TypedQuery<Concert> query = em.createQuery("select c from Concert c", Concert.class);
            List<Concert> concerts = query.getResultList();

            List<ConcertDTO> dtoConcerts = new ArrayList<>();

            for (Concert c: concerts) {
                dtoConcerts.add(ConcertMapper.toDto(c));
            }

            GenericEntity<List<ConcertDTO>> entity = new GenericEntity<List<ConcertDTO>>(dtoConcerts) {};


            return Response.ok(entity).build();
        } finally {
            em.close();
        }

    }

    @GET
    @Path("/concerts/summaries")
    public Response getConcertSummaries() {
        EntityManager em = PersistenceManager.instance().createEntityManager();

        try {
            TypedQuery<Concert> query = em.createQuery("select c from Concert c", Concert.class);
            List<Concert> concerts = query.getResultList();
            List<ConcertSummaryDTO> dtoConcertSummaries = new ArrayList<>();

            for (Concert c : concerts) {
                dtoConcertSummaries.add(ConcertSummaryMapper.toDto(c));
            }

            GenericEntity<List<ConcertSummaryDTO>> entity = new GenericEntity<List<ConcertSummaryDTO>>(dtoConcertSummaries) {};


            return Response.ok(entity).build();

        }finally {
            em.close();
        }
    }

}
