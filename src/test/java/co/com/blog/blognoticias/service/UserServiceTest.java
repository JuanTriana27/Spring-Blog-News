package co.com.blog.blognoticias.service;

import co.com.blog.blognoticias.dto.UserDTO;
import co.com.blog.blognoticias.dto.request.CreateUserRequest;
import co.com.blog.blognoticias.dto.response.CreateUserResponse;
import co.com.blog.blognoticias.mapper.UserMapper;
import co.com.blog.blognoticias.model.User;
import co.com.blog.blognoticias.repository.UserRepository;
import co.com.blog.blognoticias.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;
    private CreateUserRequest testRequest;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .idUser(1L)
                .firstName("Juan")
                .lastName("Perez")
                .email("juan@test.com")
                .password("hashedPassword")
                .enabled(true)
                .createdAt(LocalDateTime.now())
                .build();

        testRequest = CreateUserRequest.builder()
                .firstName("Juan")
                .lastName("Perez")
                .email("juan@test.com")
                .password("password123")
                .enabled(true)
                .build();
    }

    @Test
    void getAllUsers_ShouldReturnListOfUsers() {
        // Arrange
        User user2 = User.builder()
                .idUser(2L)
                .firstName("Ana")
                .lastName("Gomez")
                .email("ana@test.com")
                .build();
        List<User> expectedUsers = Arrays.asList(testUser, user2);
        when(userRepository.findAll()).thenReturn(expectedUsers);

        // Act
        List<User> result = userService.getAllUsers();

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(testUser, user2);
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getUserById_WhenUserExists_ShouldReturnUserDTO() {
        // Arrange
        when(userRepository.getReferenceById(1L)).thenReturn(testUser);

        // Act
        UserDTO result = userService.getUserById(1L);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getIdUser()).isEqualTo(1L);
        assertThat(result.getEmail()).isEqualTo("juan@test.com");
        verify(userRepository, times(1)).getReferenceById(1L);
    }

    @Test
    void createUser_WithValidRequest_ShouldReturnCreateUserResponse() throws Exception {
        // Arrange
        when(passwordEncoder.encode(testRequest.getPassword())).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Act
        CreateUserResponse result = userService.createUser(testRequest);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getIdUser()).isEqualTo(1L);
        assertThat(result.getEmail()).isEqualTo("juan@test.com");
        assertThat(result.getEnabled()).isTrue();
        verify(passwordEncoder, times(1)).encode(testRequest.getPassword());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void createUser_WithNullRequest_ShouldThrowException() {
        // Act & Assert
        assertThatThrownBy(() -> userService.createUser(null))
                .isInstanceOf(Exception.class)
                .hasMessage("El estado no puede ser nulo");

        verify(userRepository, never()).save(any());
    }

    @Test
    void createUser_WithBlankFirstName_ShouldThrowException() {
        // Arrange
        CreateUserRequest invalidRequest = CreateUserRequest.builder()
                .firstName("")
                .lastName("Perez")
                .email("juan@test.com")
                .password("password123")
                .enabled(true)
                .build();

        // Act & Assert
        assertThatThrownBy(() -> userService.createUser(invalidRequest))
                .isInstanceOf(Exception.class)
                .hasMessage("El nombre no puede ser nulo ni estar vacio");

        verify(userRepository, never()).save(any());
    }

    @Test
    void createUser_WithNullEmail_ShouldThrowException() {
        // Arrange
        CreateUserRequest invalidRequest = CreateUserRequest.builder()
                .firstName("Juan")
                .lastName("Perez")
                .email(null)
                .password("password123")
                .enabled(true)
                .build();

        // Act & Assert
        assertThatThrownBy(() -> userService.createUser(invalidRequest))
                .isInstanceOf(Exception.class)
                .hasMessage("El email no puede ser nulo ni estar vacio");

        verify(userRepository, never()).save(any());
    }

    @Test
    void createUser_WithNullPassword_ShouldThrowException() {
        // Arrange
        CreateUserRequest invalidRequest = CreateUserRequest.builder()
                .firstName("Juan")
                .lastName("Perez")
                .email("juan@test.com")
                .password(null)
                .enabled(true)
                .build();

        // Act & Assert
        assertThatThrownBy(() -> userService.createUser(invalidRequest))
                .isInstanceOf(Exception.class)
                .hasMessage("La contraseña no puede ser nula ni estar vacia");

        verify(userRepository, never()).save(any());
    }

    @Test
    void createUser_WithBlankPassword_ShouldThrowException() {
        // Arrange
        CreateUserRequest invalidRequest = CreateUserRequest.builder()
                .firstName("Juan")
                .lastName("Perez")
                .email("juan@test.com")
                .password("")
                .enabled(true)
                .build();

        // Act & Assert
        assertThatThrownBy(() -> userService.createUser(invalidRequest))
                .isInstanceOf(Exception.class)
                .hasMessage("La contraseña no puede ser nula ni estar vacia");

        verify(userRepository, never()).save(any());
    }

    @Test
    void createUser_WithNullEnabled_ShouldThrowException() {
        // Arrange
        CreateUserRequest invalidRequest = CreateUserRequest.builder()
                .firstName("Juan")
                .lastName("Perez")
                .email("juan@test.com")
                .password("password123")
                .enabled(null)
                .build();

        // Act & Assert
        assertThatThrownBy(() -> userService.createUser(invalidRequest))
                .isInstanceOf(Exception.class)
                .hasMessage("El estado no puede ser nulo");

        verify(userRepository, never()).save(any());
    }

    @Test
    void updateUser_WhenUserExists_ShouldReturnUpdatedUser() throws Exception {
        // Arrange
        User existingUser = User.builder()
                .idUser(1L)
                .firstName("OldName")
                .lastName("OldLastName")
                .email("old@test.com")
                .password("oldHashedPassword")
                .enabled(false)
                .createdAt(LocalDateTime.now().minusDays(1))
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.encode(testRequest.getPassword())).thenReturn("newHashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Act
        CreateUserResponse result = userService.updateUser(1L, testRequest);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo("juan@test.com");
        verify(userRepository, times(1)).findById(1L);
        verify(passwordEncoder, times(1)).encode(testRequest.getPassword());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void updateUser_WhenUserNotFound_ShouldThrowException() {
        // Arrange
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> userService.updateUser(99L, testRequest))
                .isInstanceOf(Exception.class)
                .hasMessage("Usuario no encontrado");

        verify(userRepository, never()).save(any());
    }

    @Test
    void deleteUser_WhenUserExists_ShouldDeleteUser() {
        // Arrange
        when(userRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1L);

        // Act
        userService.deleteUser(1L);

        // Assert
        verify(userRepository, times(1)).existsById(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteUser_WhenUserDoesNotExist_ShouldThrowRuntimeException() {
        // Arrange
        when(userRepository.existsById(99L)).thenReturn(false);

        // Act & Assert
        assertThatThrownBy(() -> userService.deleteUser(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("El usuario no existe");

        verify(userRepository, never()).deleteById(any());
    }

    @Test
    void createUser_ShouldHashPasswordBeforeSaving() throws Exception {
        // Arrange
        String rawPassword = "password123";
        String hashedPassword = "$2a$10$hashedPassword123";

        CreateUserRequest request = CreateUserRequest.builder()
                .firstName("Juan")
                .lastName("Perez")
                .email("juan@test.com")
                .password(rawPassword)
                .enabled(true)
                .build();

        when(passwordEncoder.encode(rawPassword)).thenReturn(hashedPassword);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User userToSave = invocation.getArgument(0);
            assertThat(userToSave.getPassword()).isEqualTo(hashedPassword);
            return testUser;
        });

        // Act
        userService.createUser(request);

        // Assert
        verify(passwordEncoder, times(1)).encode(rawPassword);
        verify(userRepository, times(1)).save(any(User.class));
    }
}