package app.daos;

import app.ATest;
import app.entities.Poem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EntityManagerDAOTest extends ATest {

    // Attributes
    private EntityManagerDAO<Poem> entityManagerDAO;

    // ____________________________________________________

    @BeforeEach
    public void setupDAO() {
        this.entityManagerDAO = new EntityManagerDAO<>(em, Poem.class);
        entityManagerDAO.deleteAll();
    }

    // ____________________________________________________

    @Test
    public void shouldCreatePoem() {
        // Arrange
        Poem poem = new Poem();
        poem.setTitle("Sunrise paints the sky");
        poem.setPoem("Sunrise paints the sky, Gentle waves kiss sandy shores, Day awakens slow.");
        poem.setStyle("Haiku");

        // Act
        Poem createdPoem = entityManagerDAO.create(poem);

        // Assert
        assertNotNull(createdPoem);
        assertNotNull(createdPoem.getId());
        assertEquals("Sunrise paints the sky", createdPoem.getTitle());
        assertEquals("Sunrise paints the sky, Gentle waves kiss sandy shores, Day awakens slow.", createdPoem.getPoem());
        assertEquals("Haiku", createdPoem.getStyle());
    }

    // ____________________________________________________

    @Test
    public void shouldGetById() {
        // Arrange
        Poem poem = new Poem();
        poem.setTitle("Evening sky in red");
        poem.setPoem("Evening sky in red, Clouds float like soft cotton wool, Sun dips, shadows spread.");
        poem.setStyle("Haiku");
        entityManagerDAO.create(poem);

        // Act
        Poem retrievedPoem = entityManagerDAO.getById(poem.getId());

        // Assert
        assertNotNull(retrievedPoem);
        assertEquals(poem.getId(), retrievedPoem.getId());
        assertEquals("Evening sky in red", retrievedPoem.getTitle());
    }

    // ____________________________________________________

    @Test
    public void shouldUpdatePoem() {
        // Arrange
        Poem poem = new Poem();
        poem.setTitle("Morning dew sparkles");
        poem.setPoem("Morning dew sparkles, Grass glistens in soft sunlight, Day breaks, fresh and new.");
        poem.setStyle("Haiku");
        entityManagerDAO.create(poem);

        // Act
        poem.setPoem("Updated poem");
        Poem updatedPoem = entityManagerDAO.update(poem);

        // Assert
        assertNotNull(updatedPoem);
        assertEquals(poem.getId(), updatedPoem.getId());
        assertEquals("Updated poem", updatedPoem.getPoem());
    }

    // ____________________________________________________

    @Test
    public void shouldDeletePoem() {
        // Arrange
        Poem poem = new Poem();
        poem.setTitle("Stars fill midnight sky");
        poem.setPoem("Stars fill midnight sky, Whispering tales of old light, Dreams take flight and fly.");
        poem.setStyle("Haiku");
        entityManagerDAO.create(poem);

        // Act
        Poem deletedPoem = entityManagerDAO.delete(poem);
        Poem retrievedPoemAfterDelete = entityManagerDAO.getById(poem.getId());

        // Assert
        assertNotNull(deletedPoem);
        assertNull(retrievedPoemAfterDelete);
    }

    // ____________________________________________________

    @Test
    public void shouldGetAllPoems() {
        // Arrange
        Poem poem1 = new Poem(null, "Poem 1", "First poem", "Haiku");
        Poem poem2 = new Poem(null, "Poem 2", "Second poem", "Poetry Slam");
        entityManagerDAO.create(poem1);
        entityManagerDAO.create(poem2);

        // Act
        List<Poem> allPoems = entityManagerDAO.getAll();

        // Assert
        assertEquals(2, allPoems.size());
        assertTrue(allPoems.stream().anyMatch(p -> p.getTitle().equals("Poem 1")));
        assertTrue(allPoems.stream().anyMatch(p -> p.getTitle().equals("Poem 2")));
    }

    // ____________________________________________________

    @Test
    public void shouldGetColumnById() {
        // Arrange
        Poem poem = new Poem(null, "Column Poem", "Test poem", "Haiku");
        entityManagerDAO.create(poem);

        // Act
        String retrievedTitle = entityManagerDAO.getColumnById(poem.getId(), "title");
        String retrievedPoem = entityManagerDAO.getColumnById(poem.getId(), "poem");

        // Assert
        assertNotNull(retrievedTitle);
        assertNotNull(retrievedPoem);
        assertEquals("Column Poem", retrievedTitle);
        assertEquals("Test poem", retrievedPoem);
    }

    // ____________________________________________________

    @Test
    public void shouldDeleteById() {
        // Arrange
        Poem poem = new Poem(null, "Temp Poem", "Temporary poem", "Haiku");
        entityManagerDAO.create(poem);

        // Act
        Poem deletedById = entityManagerDAO.deleteById(poem.getId());
        Poem retrievedAfterDelete = entityManagerDAO.getById(poem.getId());

        // Assert
        assertNotNull(deletedById);
        assertNull(retrievedAfterDelete);
    }

    // ____________________________________________________

    @Test
    public void shouldUpdateColumnById() {
        // Arrange
        Poem poem = new Poem(null, "Initial Poem", "Initial content", "Haiku");
        entityManagerDAO.create(poem);

        // Act
        int updatedCount = entityManagerDAO.updateColumnById(poem.getId(), "poem", "Updated content");
        String updatedPoem = entityManagerDAO.getColumnById(poem.getId(), "poem");

        // Assert
        assertEquals(1, updatedCount);
        assertEquals("Updated content", updatedPoem);
    }

    // ____________________________________________________

    @Test
    public void shouldFindEntityByColumn() {
        // Arrange
        Poem poem = new Poem(null, "this is a title", "Poem description", "Haiku");
        entityManagerDAO.create(poem);

        // Act
        Poem foundPoem = entityManagerDAO.findEntityByColumn("this is a title", "title");

        // Assert
        assertNotNull(foundPoem);
        assertEquals(poem.getId(), foundPoem.getId());
        assertEquals("this is a title", foundPoem.getTitle());
    }

    // ____________________________________________________

    @Test
    public void shouldReturnTrueIfColumnExists() {
        // Arrange
        Poem poem = new Poem(null, "Title exists", "Description", "Haiku");
        entityManagerDAO.create(poem);

        // Act
        boolean exists = entityManagerDAO.existByColumn("Title exists", "title");
        boolean notExists = entityManagerDAO.existByColumn("NonExistent", "title");

        // Assert
        assertTrue(exists);
        assertFalse(notExists);
    }

    // ____________________________________________________

    @Test
    public void shouldExecuteQuerySupplier() {
        // Arrange
        Poem poem = new Poem(null, "Tester", "Test poem content", "Haiku");
        entityManagerDAO.create(poem);

        // Act
        Poem result = entityManagerDAO.executeQuery(() -> entityManagerDAO.getById(poem.getId()));

        // Assert
        assertNotNull(result);
        assertEquals("Tester", result.getTitle());
    }

    // ____________________________________________________

    @Test
    public void shouldExecuteQueryRunnable() {
        // Arrange
        Poem poem = new Poem(null, "Runnable Poem", "Runnable content", "Haiku");

        // Act
        entityManagerDAO.executeQuery(() -> entityManagerDAO.create(poem));
        Poem retrieved = entityManagerDAO.getById(poem.getId());

        // Assert
        assertNotNull(retrieved);
        assertEquals("Runnable Poem", retrieved.getTitle());
    }

}