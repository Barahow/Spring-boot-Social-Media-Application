package dev.social.media.security.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UpdateCommentContent {
    @NotBlank(message = "The content must not be blank")
    @Size(max = 500, message = "The content must not exceed 500 characters")
    private String content;

    public String getContent() {
        return content;
    }
}
