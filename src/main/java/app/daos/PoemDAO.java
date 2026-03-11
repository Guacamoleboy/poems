package app.daos;

import app.entities.Poem;
import jakarta.persistence.EntityManager;

public class PoemDAO extends EntityManagerDAO<Poem> {

    // Attributes

    // ___________________________________________________________________

    public PoemDAO(EntityManager em) {
        super(em, Poem.class);
    }

}