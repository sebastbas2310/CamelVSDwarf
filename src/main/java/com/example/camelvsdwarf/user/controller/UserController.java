package com.example.camelvsdwarf.user.controller;

import com.example.camelvsdwarf.user.UserRequest;
import com.example.camelvsdwarf.user.UserResponse;
import com.example.camelvsdwarf.user.UserStatus;
import com.example.camelvsdwarf.user.UserStatusRequest;
import com.example.camelvsdwarf.user.service.UserService;
import com.example.camelvsdwarf.shared.dto.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor

public class UserController {

    //Atributos
    private final UserService userService;


    @GetMapping
    public ResponseEntity<PageResponse<UserResponse>> getAllUsers(
            @RequestParam(required=false)String query,
            @RequestParam(required=false) UserStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "fullName") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

            return ResponseEntity.ok(userService.getAllUsers(query, status, page, size, sortBy, sortDir));

    }

    //Obtener usuario por ID
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    //Crear usuario
    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest userRequest){
        UserResponse userResponse = userService.createUser(userRequest);
        return ResponseEntity.created(URI.create("/api/v1/users/" + userResponse.getId())).body(userResponse);
    }

    //Actualizar usuario
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id, @Valid @RequestBody UserRequest userRequest) {
        return ResponseEntity.ok(userService.updateUser(id, userRequest));
    }

    //Actualizar estado de usuario
    @PatchMapping("/{id}/status")
    public ResponseEntity<UserResponse> updateUserStatus(@PathVariable Long id, @Valid @RequestBody UserStatusRequest userStatusRequest) {
        return ResponseEntity.ok(userService.updateUserStatus(id, userStatusRequest));
    }

    //Eliminar usuario
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
