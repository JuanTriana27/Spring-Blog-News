package co.com.blog.blognoticias.controller;

import co.com.blog.blognoticias.dto.MessageResponse;
import co.com.blog.blognoticias.dto.UserDTO;
import co.com.blog.blognoticias.dto.request.CreateUserRequest;
import co.com.blog.blognoticias.dto.response.CreateUserResponse;
import co.com.blog.blognoticias.model.User;
import co.com.blog.blognoticias.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private ObjectMapper objectMapper;
    private User testUser;
    private UserDTO testUserDTO;
    private CreateUserRequest testRequest;
    private CreateUserResponse testResponse;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        objectMapper = new ObjectMapper();

        // Configurar objetos de prueba
        testUser = User.builder()
                .idUser(1L)
                .firstName("Juan")
                .lastName("Perez")
                .email("juan@test.com")
                .password("hashedPassword")
                .enabled(true)
                .createdAt(LocalDateTime.now())
                .build();

        testUserDTO = UserDTO.builder()
                .idUser(1L)
                .firstName("Juan")
                .lastName("Perez")
                .email("juan@test.com")
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

        testResponse = CreateUserResponse.builder()
                .idUser(1L)
                .firstName("Juan")
                .lastName("Perez")
                .email("juan@test.com")
                .enabled(true)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    void obtenerTodosLosUsuarios_ShouldReturnUsersList() throws Exception {
        // Arrange
        List<User> users = Arrays.asList(testUser);
        when(userService.getAllUsers()).thenReturn(users);

        // Act & Assert
        mockMvc.perform(get("/user/todos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje", is("Usuarios obtenidos correctamente")))
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].idUser", is(1)))
                .andExpect(jsonPath("$.data[0].firstName", is("Juan")))
                .andExpect(jsonPath("$.data[0].lastName", is("Perez")));

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void obtenerTodosLosUsuarios_WhenServiceThrowsException_ShouldReturnInternalServerError() throws Exception {
        // Arrange
        when(userService.getAllUsers()).thenThrow(new RuntimeException("Error de base de datos"));

        // Act & Assert
        mockMvc.perform(get("/user/todos"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.mensaje", containsString("Error al obtener usuarios")));

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void buscarPorId_WhenUserExists_ShouldReturnUser() throws Exception {
        // Arrange
        when(userService.getUserById(1L)).thenReturn(testUserDTO);

        // Act & Assert
        mockMvc.perform(get("/user/buscar-por-id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje", is("Usuario encontrado")))
                .andExpect(jsonPath("$.data.idUser", is(1)))
                .andExpect(jsonPath("$.data.email", is("juan@test.com")));

        verify(userService, times(1)).getUserById(1L);
    }

    @Test
    void buscarPorId_WhenUserNotFound_ShouldReturnNotFound() throws Exception {
        // Arrange
        when(userService.getUserById(99L))
                .thenThrow(new RuntimeException("Usuario no encontrado"));

        // Act & Assert
        mockMvc.perform(get("/user/buscar-por-id/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensaje", containsString("Usuario no encontrado")));

        verify(userService, times(1)).getUserById(99L);
    }

    @Test
    void guardarNuevo_WithValidRequest_ShouldReturnCreatedUser() throws Exception {
        // Arrange
        when(userService.createUser(any(CreateUserRequest.class))).thenReturn(testResponse);

        // Act & Assert
        mockMvc.perform(post("/user/guardar-nuevo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.mensaje", is("Usuario creado exitosamente")))
                .andExpect(jsonPath("$.data.idUser", is(1)))
                .andExpect(jsonPath("$.data.firstName", is("Juan")));

        verify(userService, times(1)).createUser(any(CreateUserRequest.class));
    }

    @Test
    void guardarNuevo_WithInvalidRequest_ShouldReturnBadRequest() throws Exception {
        // Arrange
        when(userService.createUser(any(CreateUserRequest.class)))
                .thenThrow(new Exception("El email no puede ser nulo"));

        // Act & Assert
        mockMvc.perform(post("/user/guardar-nuevo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje", containsString("Error al crear usuario")));

        verify(userService, times(1)).createUser(any(CreateUserRequest.class));
    }

    @Test
    void guardarNuevo_WithNullRequest_ShouldReturnBadRequest() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/user/guardar-nuevo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void actualizarUsuario_WhenUserExists_ShouldReturnUpdatedUser() throws Exception {
        // Arrange
        when(userService.updateUser(eq(1L), any(CreateUserRequest.class))).thenReturn(testResponse);

        // Act & Assert
        mockMvc.perform(put("/user/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje", is("Usuario actualizado exitosamente")))
                .andExpect(jsonPath("$.data.idUser", is(1)));

        verify(userService, times(1)).updateUser(eq(1L), any(CreateUserRequest.class));
    }

    @Test
    void actualizarUsuario_WhenUserNotFound_ShouldReturnBadRequest() throws Exception {
        // Arrange
        when(userService.updateUser(eq(99L), any(CreateUserRequest.class)))
                .thenThrow(new Exception("Usuario no encontrado"));

        // Act & Assert
        mockMvc.perform(put("/user/update/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje", containsString("Error al actualizar usuario")));

        verify(userService, times(1)).updateUser(eq(99L), any(CreateUserRequest.class));
    }

    @Test
    void eliminarUsuario_WhenUserExists_ShouldReturnOk() throws Exception {
        // Arrange
        doNothing().when(userService).deleteUser(1L);

        // Act & Assert
        mockMvc.perform(delete("/user/delete/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje", is("Usuario eliminado correctamente")));

        verify(userService, times(1)).deleteUser(1L);
    }

    @Test
    void eliminarUsuario_WhenUserNotFound_ShouldReturnNotFound() throws Exception {
        // Arrange
        doThrow(new Exception("El usuario no existe")).when(userService).deleteUser(99L);

        // Act & Assert
        mockMvc.perform(delete("/user/delete/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensaje", containsString("Error al eliminar usuario")));

        verify(userService, times(1)).deleteUser(99L);
    }

    @Test
    void eliminarUsuario_WithInvalidId_ShouldReturnBadRequest() throws Exception {
        // Arrange
        doThrow(new IllegalArgumentException("ID inválido")).when(userService).deleteUser(-1L);

        // Act & Assert
        mockMvc.perform(delete("/user/delete/-1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensaje", containsString("Error al eliminar usuario")));

        verify(userService, times(1)).deleteUser(-1L);
    }

    @Test
    void controller_ShouldHandleCorsHeaders() throws Exception {
        // Arrange
        List<User> users = Arrays.asList(testUser);
        when(userService.getAllUsers()).thenReturn(users);

        // Act & Assert - Verificar que incluye headers CORS
        mockMvc.perform(get("/user/todos")
                        .header("Origin", "http://localhost:3000"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:3000"));
    }
}