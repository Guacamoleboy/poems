package app.mappers;

import app.dtos.PoemDTO;
import app.entities.Poem;
import java.util.List;
import java.util.stream.Collectors;

public class PoemMapper {

    public static PoemDTO toDTO(Poem poem) {
        if (poem == null) return null;
        return new PoemDTO(poem.getId(), poem.getTitle(), poem.getPoem(), poem.getStyle());
    }

    public static List<PoemDTO> toDTOList(List<Poem> poems) {
        return poems.stream().map(PoemMapper::toDTO).collect(Collectors.toList());
    }

    public static Poem toEntity(PoemDTO dto) {
        if (dto == null) return null;
        Poem poem = new Poem();
        poem.setId(dto.getId());
        poem.setTitle(dto.getTitle());
        poem.setPoem(dto.getPoem());
        poem.setStyle(dto.getStyle());
        return poem;
    }

    public static List<Poem> toEntityList(List<PoemDTO> dtos) {
        return dtos.stream().map(PoemMapper::toEntity).collect(Collectors.toList());
    }

}