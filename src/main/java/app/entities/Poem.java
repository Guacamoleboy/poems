package app.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Builder
public class Poem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name="title", length = 100, nullable = false)
    private String title;
    @Column(name="poem", length = 1000, nullable = false)
    private String poem;
    @Column(name="style", length = 30, nullable = false)
    private String style;

}