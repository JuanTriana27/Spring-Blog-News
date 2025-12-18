package co.com.blog.blognoticias.mapper;

import co.com.blog.blognoticias.dto.UserDTO;
import co.com.blog.blognoticias.dto.request.CreateUserRequest;
import co.com.blog.blognoticias.dto.response.CreateUserResponse;
import co.com.blog.blognoticias.model.User;

public class UserMapper {

    // Model → DTO (lectura)
    public static UserDTO modelToDTO(User user) {
        return UserDTO.builder()
                .idUser(user.getIdUser())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .enabled(user.getEnabled())
                .createdAt(user.getCreatedAt())
                .build();
    }

    // Create Request → Model
    public static User createRequestToModel(CreateUserRequest request) {
        return User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .enabled(true)
                .build();
    }

    // Model → Create Response
    public static CreateUserResponse modelToCreateResponse(User user) {
        return CreateUserResponse.builder()
                .idUser(user.getIdUser())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .enabled(user.getEnabled())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
