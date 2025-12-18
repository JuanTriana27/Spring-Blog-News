package co.com.blog.blognoticias.service.impl;

import co.com.blog.blognoticias.dto.UserDTO;
import co.com.blog.blognoticias.dto.request.CreateUserRequest;
import co.com.blog.blognoticias.dto.response.CreateUserResponse;
import co.com.blog.blognoticias.mapper.UserMapper;
import co.com.blog.blognoticias.model.User;
import co.com.blog.blognoticias.repository.UserRepository;
import co.com.blog.blognoticias.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Metodo Para Obtener Todos los Usuarios
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Metodo Para Obtener Por ID
    @Override
    public UserDTO getUserById(Long id){

        // Consultar en DB user por ID
        User user = userRepository.getReferenceById(id);

        // Mapear Hacia DTO el resultado que trae el modelo
        UserDTO userDTO = UserMapper.modelToDTO(user);

        // Retornar el objeto mapeado a DTO
        return userDTO;
    }

    // Metodo Para Crear User
    @Override
    public CreateUserResponse createUser(CreateUserRequest createUserRequest) throws Exception{

        // Validar que el estado no sea nulo
        if(createUserRequest == null){
            throw new Exception("El estado no puede ser nulo");
        }

        // Validar Nombre
        if(createUserRequest.getFirstName() == null ||
        createUserRequest.getFirstName().isBlank() == true ){
            throw new Exception("El nombre no puede ser nulo ni estar vacio");
        }

        // Validar Apellido
        if(createUserRequest.getLastName() == null ||
        createUserRequest.getLastName().isBlank() == true ){
            throw new Exception("El apellido no puede ser nulo ni estar vacio");
        }

        // Validar Email
        if(createUserRequest.getEmail() == null ||
        createUserRequest.getEmail().isBlank() == true ){
            throw new Exception("El email no puede ser nulo ni estar vacio");
        }

        // Validar Password
        if(createUserRequest.getPassword() == null ||
        createUserRequest.getPassword().isBlank() == true ){
            throw new Exception("La contraseña no puede ser nula ni estar vacia");
        }

        // Validar Enabled
        if(createUserRequest.getEnabled() == null){
            throw new Exception("El estado no puede ser nulo");
        }

        // Convertir Request a Model
        User user = UserMapper.createRequestToModel(createUserRequest);

        // HASHEAR LA PASSWORD ANTES DE GUARDAR
        String hashedPassword = passwordEncoder.encode(createUserRequest.getPassword());
        user.setPassword(hashedPassword);

        // Persistir en DB
        user = userRepository.save(user);

        // Convertir a Response para retornar
        CreateUserResponse createUserResponse = UserMapper.modelToCreateResponse(user);

        // Retornar el response persistido como solicita el metodo
        return createUserResponse;
    }

    // Metodo Para Actualizar Por ID
    @Override
    public CreateUserResponse updateUser(Long id, CreateUserRequest createUserRequest) throws Exception{

        // Verificamos que exista el User
        User user = userRepository.findById(id)
                .orElseThrow(() -> new Exception("Usuario no encontrado"));

        // Validar que el estado no sea nulo
        if(createUserRequest == null){
            throw new Exception("El estado no puede ser nulo");
        }

        // Validar Nombre
        if(createUserRequest.getFirstName() == null ||
                createUserRequest.getFirstName().isBlank() == true ){
            throw new Exception("El nombre no puede ser nulo ni estar vacio");
        }

        // Validar Apellido
        if(createUserRequest.getLastName() == null ||
                createUserRequest.getLastName().isBlank() == true ){
            throw new Exception("El apellido no puede ser nulo ni estar vacio");
        }

        // Validar Email
        if(createUserRequest.getEmail() == null ||
                createUserRequest.getEmail().isBlank() == true ){
            throw new Exception("El email no puede ser nulo ni estar vacio");
        }

        // Validar Password
        if(createUserRequest.getPassword() == null ||
                createUserRequest.getPassword().isBlank() == true ){
            throw new Exception("La contraseña no puede ser nula ni estar vacia");
        }

        // Validar Enabled
        if(createUserRequest.getEnabled() == null){
            throw new Exception("El estado no puede ser nulo");
        }

        // Actualizar Datos del User
        user.setFirstName(createUserRequest.getFirstName());
        user.setLastName(createUserRequest.getLastName());
        user.setEmail(createUserRequest.getEmail());

        // HASHEAR LA NUEVA PASSWORD
        String hashedPassword = passwordEncoder.encode(createUserRequest.getPassword());
        user.setPassword(hashedPassword);

        user.setEnabled(createUserRequest.getEnabled());

        // Guardar user actualizado en db
        user = userRepository.save(user);

        // Mapear y retornar
        return UserMapper.modelToCreateResponse(user);
    }

    // Metodo Para Eliminar Por ID
    @Override
    public void deleteUser(Long id) {

        // Consultar en DB que exista
        if(!userRepository.existsById(id)){
            throw new RuntimeException("El usuario no existe");
        }

        // Eliminamos
        userRepository.deleteById(id);
    }
}
