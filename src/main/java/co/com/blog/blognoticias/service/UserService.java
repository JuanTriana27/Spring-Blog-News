package co.com.blog.blognoticias.service;

import co.com.blog.blognoticias.dto.UserDTO;
import co.com.blog.blognoticias.dto.request.CreateUserRequest;
import co.com.blog.blognoticias.dto.response.CreateUserResponse;
import co.com.blog.blognoticias.model.User;
import java.util.List;

public interface UserService {

    // Obtener todos los usuarios
    List<User> getAllUsers();

    // Metodo Para Consultar Por ID
    UserDTO getUserById(Long id);

    // Metodo Para Crear User
    CreateUserResponse createUser(CreateUserRequest createUserRequest) throws Exception;

    // Metodo Para Actualizar User
    CreateUserResponse updateUser(Long id, CreateUserRequest createUserRequest) throws Exception;

    // Metodo Para Eliminar User
    void deleteUser(Long id) throws Exception;
}