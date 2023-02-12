package ent;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class TranslateResponse {
    private String status;
    private String text;
    private String source;
    private String target;
}

