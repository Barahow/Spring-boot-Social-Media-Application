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
public class PrivateMessageContent {

        @NotBlank(message = "The content must not be blank")
        @Size(max = 500, message = "The content must not exceed 500 characters")
        private String content;

        private List<String> pictureGallery;
        private List<String> gif;

    public String getContent() {
        return content;
    }
}

