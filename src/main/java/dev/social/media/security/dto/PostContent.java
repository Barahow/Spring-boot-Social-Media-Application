package dev.social.media.security.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class PostContent {
    @NotBlank(message = "The content must not be blank")
    @Size(max = 500, message = "The content must not exceed 500 characters")
    private String content;

    @NotBlank(message = "The title must not be blank")
    @Size(max = 100, message = "The title must not exceed 100 characters")
    private String title;

    public String getContent() {
        return content;
    }
}
