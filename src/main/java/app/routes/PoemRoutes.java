package app.routes;

import app.controllers.PoemController;
import app.services.PoemService;
import io.javalin.apibuilder.EndpointGroup;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import static io.javalin.apibuilder.ApiBuilder.*;

public class PoemRoutes {

    // Attributes
    private final EntityManager entityManager;
    private final PoemController poemController;

    // _________________________________________________________________________________

    public PoemRoutes(EntityManagerFactory emf) {
        entityManager = emf.createEntityManager();
        PoemService poemService = new PoemService(entityManager);
        poemController = new PoemController(poemService);
    }

    // _________________________________________________________________________________

    public EndpointGroup routes() {
        return () -> path("poems", () -> {
            get("/", poemController::getPoems);
            get("/{id}", poemController::getById);
            post("/", poemController::createPoem);
            post("/batch", poemController::createPoems);
            put("/{id}", poemController::update);
            delete("/{id}", poemController::delete);
        });
    }

}