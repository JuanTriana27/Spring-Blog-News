package co.com.blog.blognoticias.repository;

import co.com.blog.blognoticias.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Debe guardar un usuario correctamente")
    void shouldSaveUser() {

        // GIVEN
        User user = User.builder()
                .firstName("Juan")
                .lastName("Pérez")
                .email("juan@test.com")
                .password("123456")
                .enabled(true)
                .build();

        // WHEN
        User savedUser = userRepository.save(user);

        // THEN
        assertThat(savedUser.getIdUser()).isNotNull();
        assertThat(savedUser.getCreatedAt()).isNotNull();
        assertThat(savedUser.getEmail()).isEqualTo("juan@test.com");
    }
}
