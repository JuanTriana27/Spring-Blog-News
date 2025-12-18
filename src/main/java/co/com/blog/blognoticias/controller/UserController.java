package co.com.blog.blognoticias.controller;

import co.com.blog.blognoticias.dto.MessageResponse;
import co.com.blog.blognoticias.dto.UserDTO;
import co.com.blog.blognoticias.dto.request.CreateUserRequest;
import co.com.blog.blognoticias.dto.response.CreateUserResponse;
import co.com.blog.blognoticias.model.User;
import co.com.blog.blognoticias.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/todos")
    public ResponseEntity<MessageResponse> obtenerTodosLosUsuarios() {
        try {
            List<User> usuarios = userService.getAllUsers();
            return ResponseEntity.ok(new MessageResponse("Usuarios obtenidos correctamente", usuarios));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Error al obtener usuarios: " + e.getMessage()));
        }
    }

    @GetMapping("/buscar-por-id/{id}")
    public ResponseEntity<MessageResponse> buscarPorId(@PathVariable Long id) {
        try {
            UserDTO userDTO = userService.getUserById(id);
            return ResponseEntity.ok(new MessageResponse("Usuario encontrado", userDTO));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse("Usuario no encontrado: " + e.getMessage()));
        }
    }

    @PostMapping("/guardar-nuevo")
    public ResponseEntity<MessageResponse> guardarNuevo(@Valid @RequestBody CreateUserRequest createUserRequest) {
        try {
            CreateUserResponse response = userService.createUser(createUserRequest);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new MessageResponse("Usuario creado exitosamente", response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse("Error al crear usuario: " + e.getMessage()));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<MessageResponse> actualizarUsuario(
            @PathVariable Long id,
            @RequestBody CreateUserRequest createUserRequest) {

        try {
            CreateUserResponse response = userService.updateUser(id, createUserRequest);
            return ResponseEntity.ok(new MessageResponse("Usuario actualizado exitosamente", response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse("Error al actualizar usuario: " + e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<MessageResponse> eliminarUsuario(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok(new MessageResponse("Usuario eliminado correctamente"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse("Error al eliminar usuario: " + e.getMessage()));
        }
    }
}