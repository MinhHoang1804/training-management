package com.g96.ftms.service.user;

import com.g96.ftms.dto.ChangePasswordDTO;
import com.g96.ftms.dto.JwtResponeDTO;
import com.g96.ftms.dto.LoginDTO;
import com.g96.ftms.dto.UserDTO;
import com.g96.ftms.dto.common.PagedResponse;
import com.g96.ftms.dto.request.UserRequest;
import com.g96.ftms.dto.response.ApiResponse;
import com.g96.ftms.dto.response.UserResponse;
import com.g96.ftms.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Service
public interface UserService {
    User findByAccount(String account);

    Map<String, Object> getPagedUsers(Pageable pageable);

    List<UserDTO> getAllUsers();

    Page<UserDTO> getAllUsers(Pageable pageable);

    JwtResponeDTO login(LoginDTO loginDTO);
    void saveUser(UserDTO userDTO);
    boolean changePassword(String account, ChangePasswordDTO changePasswordDTO);

    ResponseEntity<?> changeUserPassword(ChangePasswordDTO changePasswordDTO, Authentication authentication);

    UserDTO getUserProfileByAccount(String account);
    UserDTO getUserDetails(Long userId, Authentication authentication);
    ResponseEntity<?> addUser(UserDTO userDTO, Authentication authentication);

    ApiResponse<PagedResponse<UserResponse.UserInfoDTO>> search(UserRequest.UserPagingRequest model);

    ApiResponse<?> updateProfile(UserRequest.UserEditProfileRequest model);

    ApiResponse<?> updateAvatar(MultipartFile file);

    ApiResponse<List<UserResponse.UserInfoDTO>> findAdmin();
}

