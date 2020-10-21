package se325.assignment01.concert.service.services;

import se325.assignment01.concert.common.dto.UserDTO;
import se325.assignment01.concert.service.domain.AuthToken;
import se325.assignment01.concert.service.domain.User;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.ResponseBuilder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Path("/concert-service")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    @POST
    @Path("/login")
    public Response login(UserDTO userDTO) {
        EntityManager em = PersistenceManager.instance().createEntityManager();

        try {
            TypedQuery<User> query = em.createQuery("select u from User u where u.username = :username and u.password = :password", User.class).setParameter("username", userDTO.getUsername()).setParameter("password", userDTO.getPassword());
            User user = query.getResultList().stream().findFirst().orElse(null);

            if (user == null) {
                //check user with entered details exists in database
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }

            em.getTransaction().begin();

            AuthToken authToken = em.find(AuthToken.class, user.getUsername());

            String authTokenId;
            if (authToken == null || LocalDateTime.now().isAfter(authToken.getExpiry())) {
                if (authToken != null) {
                    //If token expired then remove from database
                    em.remove(authToken);
                }
                //Create new unique token and set expiry as 30 minutes from current time.
                authTokenId = UUID.randomUUID().toString();
                AuthToken newAuthToken = new AuthToken(user, authTokenId, LocalDateTime.now().plusMinutes(30));

                //Update database
                em.persist(newAuthToken);
                em.getTransaction().commit();
            } else {
                authTokenId = authToken.getTokenId();
            }

            //Create new cookie based on token
            NewCookie cookie = new NewCookie("auth", authTokenId);

            ResponseBuilder builder = Response.ok().cookie(cookie);
            Response response = builder.build();

            return response;

        } finally {
            em.close();
        }
    }
}
