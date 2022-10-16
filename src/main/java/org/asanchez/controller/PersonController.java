package org.asanchez.controller;

import org.asanchez.model.Person;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;
import java.lang.RuntimeException;
import java.lang.Thread;
import java.lang.Exception;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Bulkhead;

@Path("/persons")
@Produces(MediaType.APPLICATION_JSON)
public class PersonController {
    List<Person> personList = new ArrayList<>();
    Logger LOGGER = Logger.getLogger("DemoLogger");

    @GET
    //@Timeout(value = 5000L)
    //@Retry(maxRetries = 4)
    //@CircuitBreaker(failureRatio = 0.1, delay = 15000L)
    //@Bulkhead(value = 0)
    @Fallback(fallbackMethod = "getPersonFallbackList")
    public List<Person> getPersonList() {
        LOGGER.info("Ejecutando person list");
        doFail();
        //doWait();
        return this.personList;
    }

    public List<Person> getPersonFallbackList() {
        LOGGER.info("Se llama al fallback");
        var person = new Person(-1L, "Andres", "andres.ssalcedo@alumnos.udg.mx");
        return List.of(person);
    }

    public void doWait() {
        var random = new Random();
        try {
            LOGGER.warning("Haciendo un sleep");
            Thread.sleep((random.nextInt(15) + 4) * 1000L);
        } catch (Exception ex) {

        }
    }

    public void doFail() {
        var random = new Random();
        if (random.nextBoolean()) {
            LOGGER.warning("Se produce una falla");
            throw new RuntimeException("Generando un fallo de forma aleatoria");
        }
    }
}
