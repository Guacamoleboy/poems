package app.servers;

import app.config.HibernateConfig;
import app.routes.Routes;
import io.javalin.Javalin;
import io.javalin.http.HttpStatus;
import jakarta.persistence.EntityManagerFactory;

import java.util.Map;

public class Server {

    // Attributes
    private Javalin app;
    private final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();

    // ____________________________________________________

    public void start(int port){

        // Singleton check
        if (app != null){
            return;
        }

        // Javalin setup
        app = Javalin.create(config -> {
            config.router.contextPath = "/api";
            config.routes.apiBuilder(Routes.registerRoutes(emf));
            config.bundledPlugins.enableRouteOverview("/routes");
            config.routes.exception(RuntimeException.class, (e, ctx) -> {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json(Map.of(
                                "status", "error",
                                "message", e.getMessage() != null ? e.getMessage() : "Internal Server Error"
                        ));
            });
        }).start(port);

        System.out.println("\nServer started on port " + port);

    }

    // ____________________________________________________

    public void stop(){
        if (app != null) {
            app.stop();
            app = null;
        }
    }

    // ____________________________________________________

    public Javalin getApp() {
        return app;
    }

}