package ent;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class TranslateRequest {
    private String text;
    private String source;
    private String target;
    @JsonProperty("translation_client")
    private String translationClient;
    private Certification certification;
}

