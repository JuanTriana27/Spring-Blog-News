package co.com.blog.blognoticias.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserResponse {
    private Long idUser;
    private String firstName;
    private String lastName;
    private String email;
    private Boolean enabled = true;
    private LocalDateTime createdAt;
}
