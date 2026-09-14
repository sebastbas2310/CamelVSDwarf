package com.example.camelvsdwarf.user.service;

import com.example.camelvsdwarf.shared.exception.BusinessConflictException;
import com.example.camelvsdwarf.user.AppUser;
import com.example.camelvsdwarf.user.AppUserRepository;
import com.example.camelvsdwarf.user.Role;
import com.example.camelvsdwarf.user.UserProfileRequest;
import com.example.camelvsdwarf.user.UserRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private AppUserRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService service;

    @Test
    void createUser_shouldPersistUserAndHashPassword() {
        UserRequest request = new UserRequest("Ana García", "ANA@MAIL.COM", "UnSecreto123", Role.ORGANIZER);
        when(repository.existsByEmailIgnoreCase("ANA@MAIL.COM")).thenReturn(false);
        when(passwordEncoder.encode("UnSecreto123")).thenReturn("hashed-password");
        when(repository.save(any(AppUser.class))).thenAnswer(invocation -> {
            AppUser user = invocation.getArgument(0);
            user.setId(11L);
            return user;
        });

        var response = service.createUser(request);

        assertAll(
                () -> assertEquals(11L, response.id()),
                () -> assertEquals("Ana García", response.fullName()),
                () -> assertEquals("ana@mail.com", response.email()),
                () -> assertEquals(Role.ORGANIZER, response.role()),
                () -> assertTrue(response.enabled())
        );

        ArgumentCaptor<AppUser> captor = ArgumentCaptor.forClass(AppUser.class);
        verify(repository).save(captor.capture());
        AppUser savedUser = captor.getValue();
        assertEquals("hashed-password", savedUser.getPasswordHash());
    }

    @Test
    void createUser_shouldRejectExistingEmail() {
        UserRequest request = new UserRequest("Carlos", "carlos@test.com", "Secret123", Role.VIEWER);
        when(repository.existsByEmailIgnoreCase("carlos@test.com")).thenReturn(true);

        BusinessConflictException exception = assertThrows(BusinessConflictException.class,
                () -> service.createUser(request));

        assertEquals("Email is already in use", exception.getMessage());
        verify(repository, never()).save(any(AppUser.class));
    }

    @Test
    void createOrGetProfile_shouldCreateViewerProfileWhenUserDoesNotExist() {
        UUID supabaseUserId = UUID.randomUUID();
        UserProfileRequest profile = new UserProfileRequest("  María López  ");
        when(repository.findBySupabaseUserId(supabaseUserId)).thenReturn(Optional.empty());
        when(repository.findByEmailIgnoreCase("maria@example.com")).thenReturn(Optional.empty());
        when(repository.save(any(AppUser.class))).thenAnswer(invocation -> {
            AppUser user = invocation.getArgument(0);
            user.setId(77L);
            return user;
        });

        var response = service.createOrGetProfile(supabaseUserId, "maria@example.com", profile);

        assertAll(
                () -> assertEquals(77L, response.id()),
                () -> assertEquals("María López", response.fullName()),
                () -> assertEquals("maria@example.com", response.email()),
                () -> assertEquals(Role.VIEWER, response.role()),
                () -> assertTrue(response.enabled())
        );

        ArgumentCaptor<AppUser> captor = ArgumentCaptor.forClass(AppUser.class);
        verify(repository).save(captor.capture());
        assertEquals(supabaseUserId, captor.getValue().getSupabaseUserId());
        assertEquals("maria@example.com", captor.getValue().getEmail());
    }
}
