package app.services;

import app.daos.PoemDAO;
import app.dtos.PoemDTO;
import app.entities.Poem;
import app.mappers.PoemMapper;
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
        Poem created = poemDAO.create(poem);
        return PoemMapper.toDTO(created);
    }

    // _______________________________________________________________________________

    public List<PoemDTO> createPoems(List<PoemDTO> dtos) {
        dtos.forEach(dto -> validateDTO(dto, "PoemDTO"));

        List<Poem> poems = dtos.stream()
                .map(PoemMapper::toEntity)
                .collect(Collectors.toList());
        List<Poem> created = poems.stream()
                .map(poemDAO::create)
                .collect(Collectors.toList());

        return PoemMapper.toDTOList(created);
    }

    // _______________________________________________________________________________

    public PoemDTO updatePoem(int id, PoemDTO dto) {
        validateNotEmpty(id, "Poem.id");
        validateDTO(dto, "PoemDTO");

        Poem existing = poemDAO.getById(id);
        if (existing == null) throw new IllegalArgumentException("No poem found with id: " + id);

        Poem updatedEntity = PoemMapper.toEntity(dto);
        updatedEntity.setId(id);

        Poem updated = poemDAO.update(updatedEntity);
        return PoemMapper.toDTO(updated);
    }

    // _______________________________________________________________________________

    public void deletePoemById(int id) {
        validateNotEmpty(id, "Poem.id");
        Poem deleted = poemDAO.deleteById(id);
        if (deleted == null) throw new IllegalArgumentException("No poem found with id: " + id);
    }

    // _______________________________________________________________________________

    public PoemDTO getPoemById(int id) {
        validateNotEmpty(id, "Poem.id");
        Poem poem = poemDAO.getById(id);
        if (poem == null) throw new IllegalArgumentException("No poem found with id: " + id);
        return PoemMapper.toDTO(poem);
    }

}