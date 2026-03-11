package app;

import app.config.HibernateConfig;
import app.servers.Server;
import io.javalin.Javalin;
import io.restassured.RestAssured;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class ATest {

    // Attributes
    protected EntityManagerFactory emf;
    protected EntityManager em;
    protected static Server restServer;
    protected static Javalin restApp;

    // ______________________________________________

    @BeforeAll
    protected void setupAll() {
        emf = HibernateConfig.getEntityManagerFactoryForTest();
    }

    // ______________________________________________

    @BeforeEach
    protected void setup() {
        em = emf.createEntityManager();
        em.clear();
    }

    // ______________________________________________

    @AfterEach
    protected void cleanup() {
        rollbackTransactionIfActive();
        if (em != null && em.isOpen()) {
            em.close();
        }
    }

    // ______________________________________________

    @AfterAll
    protected void closeAll() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }

    // ______________________________________________

    @AfterAll
    protected void stopServer() {
        if (restServer != null) {
            restServer.stop();
            restServer = null;
            restApp = null;
        }
    }

    // ______________________________________________

    protected void startServer(int port, String endpoint) {
        if (restServer == null) {
            restServer = new Server();
            restServer.start(port);
            restApp = restServer.getApp();
            RestAssured.baseURI = "http://localhost";
            RestAssured.port = port;
            RestAssured.basePath = endpoint;
        }
    }

    // ______________________________________________

    protected void beginTransactionIfNeeded() {
        if (!em.getTransaction().isActive()) {
            em.getTransaction().begin();
        }
    }

    // ______________________________________________

    protected void commitTransactionIfActive() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().commit();
        }
    }

    // ______________________________________________

    protected void rollbackTransactionIfActive() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
    }

}