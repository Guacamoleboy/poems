package app.routes;

import io.javalin.apibuilder.EndpointGroup;
import jakarta.persistence.EntityManagerFactory;

public class Routes {

    // Attributes

    // _______________________________________________________________________

    public static EndpointGroup registerRoutes(EntityManagerFactory emf) {

        // Routings
        PoemRoutes poemRoute = new PoemRoutes(emf);

        // EndpointGroup Return to server
        return () -> {
            poemRoute.routes().addEndpoints();
        };

    }

}