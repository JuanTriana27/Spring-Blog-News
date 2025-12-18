package co.com.blog.blognoticias.mapper;

import co.com.blog.blognoticias.dto.UserDTO;
import co.com.blog.blognoticias.dto.request.CreateUserRequest;
import co.com.blog.blognoticias.dto.response.CreateUserResponse;
import co.com.blog.blognoticias.model.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class UserMapperTest {

    @Test
    void shouldMapModelToDTO() {
        User user = User.builder()
                .idUser(1L)
                .firstName("Juan")
                .lastName("Perez")
                .email("juan@test.com")
                .enabled(true)
                .createdAt(LocalDateTime.now())
                .password("hashed-password")
                .build();

        UserDTO dto = UserMapper.modelToDTO(user);

        assertThat(dto).isNotNull();
        assertThat(dto.getIdUser()).isEqualTo(user.getIdUser());
        assertThat(dto.getEmail()).isEqualTo(user.getEmail());
        assertThat(dto.getEnabled()).isTrue();
    }

    @Test
    void shouldMapCreateRequestToModel() {
        CreateUserRequest request = CreateUserRequest.builder()
                .firstName("Ana")
                .lastName("Gomez")
                .email("ana@test.com")
                .password("123456")
                .build();

        User user = UserMapper.createRequestToModel(request);

        assertThat(user).isNotNull();
        assertThat(user.getEmail()).isEqualTo(request.getEmail());
        assertThat(user.getPassword()).isEqualTo("123456"); // SIN hash aquí
        assertThat(user.getEnabled()).isTrue();
        assertThat(user.getCreatedAt()).isNull(); // lo pone JPA
    }

    @Test
    void shouldMapModelToCreateResponse() {
        User user = User.builder()
                .idUser(10L)
                .firstName("Laura")
                .lastName("Diaz")
                .email("laura@test.com")
                .enabled(true)
                .createdAt(LocalDateTime.now())
                .password("hashed")
                .build();

        CreateUserResponse response = UserMapper.modelToCreateResponse(user);

        assertThat(response).isNotNull();
        assertThat(response.getIdUser()).isEqualTo(10L);
        assertThat(response.getEmail()).isEqualTo(user.getEmail());
        assertThat(response.getEnabled()).isTrue();
    }
}
