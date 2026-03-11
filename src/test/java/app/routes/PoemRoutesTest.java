package app.routes;

import app.ATest;
import app.daos.PoemDAO;
import app.entities.Poem;
import org.junit.jupiter.api.*;
import java.util.List;
import java.util.stream.Collectors;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PoemRoutesTest extends ATest {

    // Attributes
    private PoemDAO poemDAO;
    private List<Poem> poemList;

    // _____________________________________________________________________

    @BeforeAll
    void init() {
        startServer(7070, "/api/poems");
    }

    // _____________________________________________________________________

    @BeforeEach
    void setUp() {
        beginTransactionIfNeeded();

        poemDAO = new PoemDAO(em);
        poemDAO.deleteAll();

        poemList = List.of(
                new Poem(null, "Sunrise paints the sky", "Sunrise paints the sky, Gentle waves kiss sandy shores, Day awakens slow.", "Haiku"),
                new Poem(null, "Whispers of the breeze", "Whispers of the breeze, Autumn leaves dance on the ground, Silent moonrise glow.", "Haiku"),
                new Poem(null, "Winter's first soft snow", "Winter's first soft snow, Blankets fields in quiet white, Footsteps lost below.", "Haiku"),
                new Poem(null, "Spring blossoms unfold", "Spring blossoms unfold, Petals drift in morning light, Bees hum, day is bright.", "Haiku"),
                new Poem(null, "Evening sky in red", "Evening sky in red, Clouds float like soft cotton wool, Sun dips, shadows spread.", "Haiku"),
                new Poem(null, "Morning dew sparkles", "Morning dew sparkles, Grass glistens in soft sunlight, Day breaks, fresh and new.", "Haiku"),
                new Poem(null, "Mountain peaks stand tall", "Mountain peaks stand tall, Wrapped in mist and morning sun, Silence in the call.", "Haiku"),
                new Poem(null, "Ocean waves crash loud", "Ocean waves crash loud, Seagulls cry and salt air stings, Endless water shroud.", "Haiku"),
                new Poem(null, "Autumn's golden hue", "Autumn's golden hue, Leaves fall softly to the ground, Earth in warm embrace.", "Haiku"),
                new Poem(null, "Stars fill midnight sky", "Stars fill midnight sky, Whispering tales of old light, Dreams take flight and fly.", "Haiku")
        )
        .stream()
        .map(poemDAO::create)
        .collect(Collectors.toList());

        commitTransactionIfActive();
    }

    // _____________________________________________________________________

    @AfterEach
    void tearDown() {
        poemDAO.deleteAll();
    }

    // _____________________________________________________________________

    @Test
    void getAllPoems() {
        List<Poem> result = given()
                .when()
                .get("/")
                .then()
                .statusCode(200)
                .body("data.size()", is(poemList.size()))
                .extract()
                .jsonPath()
                .getList("data", Poem.class);

        assertThat(result, containsInAnyOrder(poemList.toArray(new Poem[0])));
    }

    // _____________________________________________________________________

    @Test
    void createPoem() {
        Poem poem = Poem.builder()
                .title("New dawn rises")
                .poem("New dawn rises softly over hills.")
                .style("Haiku")
                .build();

        Poem poemAssert = given()
                .contentType("application/json")
                .body(poem)
                .when()
                .post("/")
                .then()
                .statusCode(200)
                .body("data.title", equalTo(poem.getTitle()))
                .extract()
                .jsonPath()
                .getObject("data", Poem.class);

        assertThat(poemAssert.getId(), notNullValue());
        assertThat(poemAssert.getTitle(), is(poem.getTitle()));
        assertThat(poemAssert.getPoem(), is(poem.getPoem()));
        assertThat(poemAssert.getStyle(), is(poem.getStyle()));
    }

    // _____________________________________________________________________

    @Test
    void createPoemsBatch() {

        List<Poem> newPoems = List.of(
                new Poem(null, "Batch 1", "Batch poem one", "Haiku"),
                new Poem(null, "Batch 2", "Batch poem two", "Haiku")
        );

        List<Poem> result = given()
                .contentType("application/json")
                .body(newPoems)
                .when()
                .post("/batch")
                .then()
                .statusCode(200)
                .body("data.size()", is(newPoems.size()))
                .extract()
                .jsonPath()
                .getList("data", Poem.class);

        assertThat(result.size(), is(newPoems.size()));
    }

    // _____________________________________________________________________

    @Test
    void deletePoem() {
        Poem poem = poemList.get(0);

        given()
                .when()
                .delete("/{id}", poem.getId())
                .then()
                .statusCode(200);

        given()
                .when()
                .get("/{id}", poem.getId())
                .then()
                .statusCode(anyOf(is(404), is(400)));
    }

    // _____________________________________________________________________

    @Test
    void updatePoem() {
        Poem poem = poemList.get(0);

        Poem updatedPoem = new Poem(
                poem.getId(),
                "Updated title",
                "Updated poem text",
                "Free verse"
        );

        Poem result =
                given()
                .contentType("application/json")
                .body(updatedPoem)
                .when()
                .put("/{id}", poem.getId())
                .then()
                .statusCode(200)
                .body("data.title", equalTo("Updated title"))
                .extract()
                .jsonPath()
                .getObject("data", Poem.class);

        assertThat(result.getTitle(), is("Updated title"));
        assertThat(result.getStyle(), is("Free verse"));
    }

    // _____________________________________________________________________

    @Test
    void getPoemById() {
        Poem poem = poemList.get(0);

        Poem result = given()
                .when()
                .get("/{id}", poem.getId())
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getObject("data", Poem.class);

        assertThat(result.getTitle(), is(poem.getTitle()));
        assertThat(result.getPoem(), is(poem.getPoem()));
    }

}