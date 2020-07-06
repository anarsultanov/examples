package dev.sultanov.keycloak.server.provider;

import dev.sultanov.keycloak.server.provider.dto.PasswordDto;
import dev.sultanov.keycloak.server.provider.dto.UserDetailsDto;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface RemoteUserService {

    @GET
    @Path("/{username}")
    UserDetailsDto getUserDetails(@PathParam("username") String username);

    @POST
    @Path("/{username}/validate-password")
    Response validateLogin(@PathParam("username") String username, PasswordDto passwordDto);
}
