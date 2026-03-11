package app.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("title")
    private String title;
    @JsonProperty("poem")
    private String poem;
    @JsonProperty("style")
    private String style;

    // ________________________________________________________________________________________________

    public PoemDTO(String title, String poem, String style){
        this.title = title;
        this.poem = poem;
        this.style = style;
    }

}