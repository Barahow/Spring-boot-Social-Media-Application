package dev.social.media.security.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PostContent {
    @NotBlank(message = "The content must not be blank")
    @Size(max = 500, message = "The content must not exceed 500 characters")
    private String content;

    @NotBlank(message = "The title must not be blank")
    @Size(max = 100, message = "The title must not exceed 100 characters")
    private String title;

    private List<String> hashTag;

    public String getContent() {
        return content;
    }
}
