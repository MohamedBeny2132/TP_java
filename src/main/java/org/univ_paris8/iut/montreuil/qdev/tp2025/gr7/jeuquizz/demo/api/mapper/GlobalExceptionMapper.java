package org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.api.mapper;

import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.api.dto.ErrorResponse;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable exception) {
        if (exception instanceof WebApplicationException) {
            WebApplicationException wae = (WebApplicationException) exception;
            return wae.getResponse();
        }

        ErrorResponse error = new ErrorResponse("INTERNAL_SERVER_ERROR",
                "Erreur [" + exception.getClass().getCanonicalName() + "] : " + exception.getMessage());
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(error)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
