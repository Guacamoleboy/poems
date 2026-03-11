package app.services;

import app.daos.PoemDAO;
import app.dtos.PoemDTO;
import app.entities.Poem;
import app.mappers.PoemMapper;
import io.javalin.http.NotFoundResponse;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

public class PoemService extends EntityManagerService<Poem> {

    // Attributes
    private final PoemDAO poemDAO;

    // _______________________________________________________________________________

    public PoemService(EntityManager em) {
        super(new PoemDAO(em), Poem.class);
        this.poemDAO = (PoemDAO) this.entityManagerDAO;
    }

    // _______________________________________________________________________________

    public List<PoemDTO> getAllPoems() {
        List<Poem> poems = poemDAO.getAll();
        return PoemMapper.toDTOList(poems);
    }

    // _______________________________________________________________________________

    public PoemDTO createPoem(PoemDTO dto) {
        validateDTO(dto, "PoemDTO");
        Poem poem = PoemMapper.toEntity(dto);
        Poem createdPoem = poemDAO.create(poem);
        return PoemMapper.toDTO(createdPoem);
    }

    // _______________________________________________________________________________

    public List<PoemDTO> createPoems(List<PoemDTO> dtos) {
        dtos.forEach(dto -> validateDTO(dto, "PoemDTO"));

        List<Poem> poems = dtos.stream()
                .map(PoemMapper::toEntity)
                .collect(Collectors.toList());
        List<Poem> createdPoems = poems.stream()
                .map(poemDAO::create)
                .collect(Collectors.toList());

        return PoemMapper.toDTOList(createdPoems);
    }

    // _______________________________________________________________________________

    public PoemDTO updatePoem(int id, PoemDTO dto) {
        validateNotEmpty(id, "Poem.id");
        validateDTO(dto, "PoemDTO");

        Poem poem = poemDAO.getById(id);
        if (poem == null) throw new IllegalArgumentException("No poem found with id: " + id);

        Poem updatedEntity = PoemMapper.toEntity(dto);
        updatedEntity.setId(id);

        Poem updatedPoem = poemDAO.update(updatedEntity);
        return PoemMapper.toDTO(updatedPoem);
    }

    // _______________________________________________________________________________

    public void deletePoemById(int id) {
        validateNotEmpty(id, "Poem.id");
        Poem poem = poemDAO.getById(id);
        if (poem == null) {
            throw new NotFoundResponse("No poem found with id: " + id);
        }
        poemDAO.delete(poem);
    }

    // _______________________________________________________________________________

    public PoemDTO getPoemById(int id) {
        validateNotEmpty(id, "Poem.id");
        Poem poem = poemDAO.getById(id);
        if (poem == null) {
            throw new NotFoundResponse("No poem found with id: " + id);
        }
        return PoemMapper.toDTO(poem);
    }

}