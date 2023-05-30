package example.dev.social.media.model;

import example.dev.social.media.model.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "app_users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppUser {

     @Id
     private ObjectId id;
     @NotBlank(message = "User Name is mandatory")
     private String userName;
     @NotBlank(message = "Email is mandatory")
     @Email(message = "email should be valid")
     private String email;
     @NotBlank(message = "password is mandatory")
     private String password;
     private LocalDateTime createAt;
     private LocalDateTime updatedAt;
     private String address;
     @NotBlank(message = "first Name is mandatory")
     private String firstName;
     @NotBlank(message = "Last name is mandatory")
     private String lastName;
     @NotBlank(message = "Date of birth is mandatory")
     private LocalDate dateOfBirth;

     private String profilePicture;

     @DBRef
     private List<UserRole> roleList;



/*
     AppUser:

     One-to-many with Post: A user can have multiple posts.
     One-to-many with Comment: A user can have multiple comments.
     One-to-many with Follow: A user can have multiple followers.
     One-to-many with Like: A user can have multiple likes.
     Comment:

     Many-to-one with AppUser: A comment belongs to a user.
     Many-to-one with Post: A comment belongs to a post.
     Follow:

     Many-to-one with AppUser (Follower): The follow record belongs to a user.
     Many-to-one with AppUser (Following): The follow record belongs to another user.

     Like:

     Many-to-one with AppUser: The like record belongs to a user.
     Many-to-one with Post: The like record belongs to a post.

     Post:

     Many-to-one with AppUser: A post belongs to a user.
     One-to-many with Comment: A post can have multiple comments.
     One-to-many with Like: A post can have multiple likes.*/


}
