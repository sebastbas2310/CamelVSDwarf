package com.example.camelvsdwarf.user.service;

import com.example.camelvsdwarf.shared.dto.PageResponse;
import com.example.camelvsdwarf.shared.exception.BusinessConflictException;
import com.example.camelvsdwarf.shared.exception.ResourceNotFoundException;
import com.example.camelvsdwarf.user.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final AppUserRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public PageResponse<UserResponse> getAllUsers(String query, UserStatus status, int page, int size, String sortBy, String sortDir) {
        var users = repository.findAll(PageRequest.of(page, size, Sort.by(Sort.Direction.fromOptionalString(sortDir).orElse(Sort.Direction.ASC), sortBy)));
        return PageResponse.from(users.map(this::toResponse));
    }
    @Transactional(readOnly = true) public UserResponse getUserById(Long id) { return toResponse(get(id)); }
    @Transactional
    public UserResponse createOrGetProfile(UUID supabaseUserId, String email, UserProfileRequest request) {
        AppUser user = repository.findBySupabaseUserId(supabaseUserId)
                .orElseGet(() -> repository.findByEmailIgnoreCase(email).orElseGet(AppUser::new));

        user.setSupabaseUserId(supabaseUserId);
        user.setEmail(email.trim().toLowerCase());
        user.setFullName(request.fullName().trim());
        if (user.getRole() == null) user.setRole(Role.VIEWER);
        user.setEnabled(true);
        return toResponse(repository.save(user));
    }
    @Transactional public UserResponse createUser(UserRequest request) { if (repository.existsByEmailIgnoreCase(request.email())) throw new BusinessConflictException("Email is already in use"); AppUser u = new AppUser(); apply(u, request); return toResponse(repository.save(u)); }
    @Transactional public UserResponse updateUser(Long id, UserRequest request) { AppUser u = get(id); apply(u, request); return toResponse(repository.save(u)); }
    @Transactional public UserResponse updateUserStatus(Long id, UserStatusRequest request) { AppUser u = get(id); u.setEnabled(request.status() == UserStatus.ACTIVE); return toResponse(repository.save(u)); }
    @Transactional public void deleteUser(Long id) { AppUser u = get(id); u.setEnabled(false); repository.save(u); }
    private AppUser get(Long id) { return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User with ID " + id + " was not found")); }
    private void apply(AppUser u, UserRequest r) { u.setFullName(r.fullName().trim()); u.setEmail(r.email().trim().toLowerCase()); u.setRole(r.role()); if (r.password() != null && !r.password().isBlank()) u.setPasswordHash(passwordEncoder.encode(r.password())); }
    private UserResponse toResponse(AppUser u) { return new UserResponse(u.getId(), u.getFullName(), u.getEmail(), u.getRole(), u.isEnabled(), u.getCreatedAt()); }
}
