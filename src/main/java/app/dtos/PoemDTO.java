package app.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PoemDTO {

    // _______________________________________________________
    // Expected result
    // _______________
    //
    //      {
    //          "id": 1,
    //          "title": "Mountain peaks stand tall",
    //          "poem": "Mountain peaks stand tall, Wrapped in mist and morning sun, Silence in the call.",
    //          "style": "Haiku"
    //      }
    //
    // _______________
    // Tested: NO
    // By: N/A
    // _______________________________________________________

    private Integer id;
    private String title;
    private String poem;
    private String style;

    public PoemDTO(String title, String poem, String style){
        this.title = title;
        this.poem = poem;
        this.style = style;
    }

}