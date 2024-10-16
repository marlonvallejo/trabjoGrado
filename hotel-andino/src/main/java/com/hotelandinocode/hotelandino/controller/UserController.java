package com.hotelandinocode.hotelandino.controller;

import com.hotelandinocode.hotelandino.constants.HttpConstant;
import com.hotelandinocode.hotelandino.dto.LoginResponseDto;
import com.hotelandinocode.hotelandino.dto.ResponseDto;
import com.hotelandinocode.hotelandino.dto.UserDto;
import com.hotelandinocode.hotelandino.entities.User;
import com.hotelandinocode.hotelandino.entities.UserRole;
import com.hotelandinocode.hotelandino.enums.UserRoleEnum;
import com.hotelandinocode.hotelandino.jwt.JwtTokenProvider;
import com.hotelandinocode.hotelandino.service.UserRoleService;
import com.hotelandinocode.hotelandino.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(HttpConstant.PATH_USER)
@Tag(name = "Usuario", description = "Controlador para el servicio de autenticación")

public class UserController {
    private UserService userService;
    private UserRoleService userRoleService;



    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;



    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setUserRoleService(UserRoleService userRoleService) {
        this.userRoleService = userRoleService;
    }

    @Operation(summary = "login de usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario logeado correctamente"),
            @ApiResponse(responseCode = "400", description = "Campo inválido"),
            @ApiResponse(responseCode = "403", description = "Nombre de usuario o correo electrónico duplicado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDto userDto) {
        try {
            // Autenticar al usuario usando los detalles de seguridad configurados
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword())
            );

            // Si la autenticación es exitosa, generar el token JWT
            String token = jwtTokenProvider.createToken(userDto.getUsername());

            // Devolver el token en la respuesta
            return ResponseEntity.ok(new LoginResponseDto(token));
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(new ResponseDto(false, "Credenciales incorrectas", null),
                    HttpStatus.UNAUTHORIZED);
        }
    }

    @Operation(summary = "Crear usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Campo inválido"),
            @ApiResponse(responseCode = "403", description = "Nombre de usuario o correo electrónico duplicado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/create")
    public ResponseEntity<?> create(@Valid @RequestBody UserDto userDto, BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                return new ResponseEntity<>(new ResponseDto(false, "Campo inválido", bindingResult.getAllErrors()),
                        HttpStatus.BAD_REQUEST);
            }

            if (this.userService.existsByEmail(userDto.getEmail())) {
                return new ResponseEntity<>(new ResponseDto(false, "Correo electrónico ya existe", null), HttpStatus.FORBIDDEN);
            }

            // Si no hay roles especificados, asignar ROLE_CLIENT por defecto
            Set<UserRole> roles = new HashSet<>();
            if (userDto.getRoles().isEmpty()) {
                roles.add(this.userRoleService.getByRoleName(UserRoleEnum.ROLE_CLIENT).get());
            } else {
                if (userDto.getRoles().contains(UserRoleEnum.ROLE_ADMIN.toString())) {
                    roles.add(this.userRoleService.getByRoleName(UserRoleEnum.ROLE_ADMIN).get());
                }
                if (userDto.getRoles().contains(UserRoleEnum.ROLE_CLIENT.toString())) {
                    roles.add(this.userRoleService.getByRoleName(UserRoleEnum.ROLE_CLIENT).get());
                }
            }

            User user = User.builder()
                    .name(userDto.getName())
                    .lastname(userDto.getLastname())
                    .username(userDto.getUsername())
                    .email(userDto.getEmail())
                    .password(passwordEncoder.encode(userDto.getPassword()))
                    .roles(roles)
                    .build();

            return new ResponseEntity<>(new ResponseDto(true, "Usuario creado con éxito", this.userService.save(user)),
                    HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseDto(false, "Error al crear el usuario", null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Obtener todos los usuarios")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuarios obtenidos correctamente"),
            @ApiResponse(responseCode = "404", description = "Usuarios no encontrados"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/get/all")
    public ResponseEntity<?> getAll() {
        try {
            List<User> userList = this.userService.getAll();
            if (userList.isEmpty())
                return new ResponseEntity<>(new ResponseDto(false, "Usuarios no encontrados", null),
                        HttpStatus.NOT_FOUND);

            return new ResponseEntity<>(new ResponseDto(true, "Usuarios encontrados con exito", userList),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseDto(false, "Error al recuperar los usuarios", null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Obtener un único usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado correctamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/get/user/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        try {
            User user = this.userService.getUserById(id).orElseThrow();
            if (user == null)
                return new ResponseEntity<>(new ResponseDto(false, "Usuario no encontrado", null),
                        HttpStatus.NOT_FOUND);

            return new ResponseEntity<>(new ResponseDto(true, "Usuario encontrado con exito", user),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseDto(false, "Error al recuperar el usuario", null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Actualizar usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Campo inválido"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/update")
    public ResponseEntity<?> update(@Valid @RequestBody UserDto userDto, BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors())
                return new ResponseEntity<>(new ResponseDto(false, "Campo inválido", bindingResult.getAllErrors()),
                        HttpStatus.BAD_REQUEST);

            if (!userService.existsById(userDto.getId()))
                return new ResponseEntity<>(new ResponseDto(false, "Usuario no encontrado", null),
                        HttpStatus.NOT_FOUND);

            Set<UserRole> roles = new HashSet<>();
            if (userDto.getRoles().contains(UserRoleEnum.ROLE_ADMIN.toString()))
                roles.add(this.userRoleService.getByRoleName(UserRoleEnum.ROLE_ADMIN).get());
            if (userDto.getRoles().contains(UserRoleEnum.ROLE_CLIENT.toString()))
                roles.add(this.userRoleService.getByRoleName(UserRoleEnum.ROLE_CLIENT).get());


            User user = User.builder()
                    .id(userDto.getId())
                    .name(userDto.getName())
                    .lastname(userDto.getLastname())
                    .username(userDto.getUsername())
                    .email(userDto.getEmail())
                    .password(passwordEncoder.encode(userDto.getPassword()))
                    .roles(roles)
                    .build();
            userService.save(user);
            return new ResponseEntity<>(new ResponseDto(true, "Usuario actualizado correctamente", user),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseDto(false, "Error al actualizar el usuario", null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
