package org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.api;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;

/**
 * Exercice 1 – Endpoints de test JAX-RS.
 *
 * GET /api/helloWorld → { "message": "Hello World" }
 * GET /api/params?name=X → { "QueryParam_name": "X" }
 * GET /api/params/{value} → { "PathParam_value": "X" }
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class HelloResource {

    // ─── GET /api/helloWorld ────────────────────────────────────────────────

    @GET
    @Path("helloWorld")
    public Response helloWorld() {
        return Response.ok(Map.of("message", "Hello World !")).build();
    }

    // ─── GET /api/params?name=X (QueryParam) ───────────────────────────────

    @GET
    @Path("params")
    public Response paramsQuery(@QueryParam("name") String name) {
        if (name == null || name.isEmpty()) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "Le paramètre 'name' est obligatoire"))
                    .build();
        }
        return Response.ok(Map.of("QueryParam_name", name)).build();
    }

    // ─── GET /api/params/{value} (PathParam) ───────────────────────────────

    @GET
    @Path("params/{value}")
    public Response paramsPath(@PathParam("value") String value) {
        return Response.ok(Map.of("PathParam_value", value)).build();
    }
}
