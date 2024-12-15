package com.g96.ftms.controller;

import com.g96.ftms.dto.UserDTO;
import com.g96.ftms.dto.request.UserRequest;
import com.g96.ftms.dto.response.ApiResponse;
import com.g96.ftms.service.file.IImageStorageService;
import com.g96.ftms.service.user.UserService;
import com.g96.ftms.util.constants.CONTAINER_UPLOAD_ENUM;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;


@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<UserDTO> getLoggedInUserProfile(Authentication authentication) {
        String account = authentication.getName();
        UserDTO userDTO = userService.getUserProfileByAccount(account);
        return ResponseEntity.ok(userDTO);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_COORDINATOR', 'ROLE_CLASS_ADMIN','ROLE_TRAINER')")
    @GetMapping("/management/list")
    public ResponseEntity<Map<String, Object>> getAllUsers(@PageableDefault(size = 10) Pageable pageable) {
        Map<String, Object> response = userService.getPagedUsers(pageable);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_COORDINATOR', 'ROLE_CLASS_ADMIN','ROLE_TRAINER')")
    @GetMapping("/management/list/{userId}")
    public ResponseEntity<UserDTO> getUserDetails(@PathVariable Long userId, Authentication authentication) {
        UserDTO userDTO = userService.getUserDetails(userId, authentication);
        return ResponseEntity.ok(userDTO);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/management/add")
    public ResponseEntity<?> addUser(@Valid @RequestBody UserDTO userDTO, Authentication authentication) {
        return userService.addUser(userDTO, authentication);
    }

    @PostMapping("/search")
    public ApiResponse<?> getSubjectList(@RequestBody UserRequest.UserPagingRequest model) {
        return userService.search(model);
    }

    @PostMapping("/update-profile")
    public ApiResponse<?> updateProfile(@RequestBody UserRequest.UserEditProfileRequest model) {
        return userService.updateProfile(model);
    }

    @PostMapping(value = "/upload-avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<?> uploadImage(@RequestParam MultipartFile file) throws IOException {
        return userService.updateAvatar(file);
    }

    @GetMapping("/get-admin-class")
    public ApiResponse<?> getAdmin() {
        return userService.findAdmin();
    }

}
