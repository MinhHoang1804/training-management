package com.g96.ftms.service.user;

import com.g96.ftms.dto.ChangePasswordDTO;
import com.g96.ftms.dto.JwtResponeDTO;
import com.g96.ftms.dto.LoginDTO;
import com.g96.ftms.dto.UserDTO;
import com.g96.ftms.dto.common.PagedResponse;
import com.g96.ftms.dto.request.UserRequest;
import com.g96.ftms.dto.response.ApiResponse;
import com.g96.ftms.dto.response.UserResponse;
import com.g96.ftms.entity.Role;
import com.g96.ftms.entity.User;
import com.g96.ftms.exception.AppException;
import com.g96.ftms.exception.ErrorCode;
import com.g96.ftms.mapper.Mapper;
import com.g96.ftms.repository.RoleRepository;
import com.g96.ftms.repository.UserRepository;
import com.g96.ftms.security.JwtTokenProvider;
import com.g96.ftms.service.file.IImageStorageService;
import com.g96.ftms.util.constants.CONTAINER_UPLOAD_ENUM;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;


    private final RoleRepository roleRepository;


    private final PasswordEncoder passwordEncoder;


    private final JwtTokenProvider jwtTokenProvider;

    private final ModelMapper mapper;

    private final IImageStorageService imageStorageService;

    @Override
    public User findByAccount(String account) {
        return userRepository.findByAccount(account);
    }

    @Override
    public Map<String, Object> getPagedUsers(Pageable pageable) {
        Page<User> usersPage = userRepository.findAll(pageable);
        List<UserDTO> userDTOs = usersPage.getContent().stream()
                .map(user -> Mapper.mapEntityToDto(user, UserDTO.class))
                .collect(Collectors.toList());
        return Map.of(
                "users", userDTOs,
                "currentPage", usersPage.getNumber(),
                "totalItems", usersPage.getTotalElements(),
                "totalPages", usersPage.getTotalPages()
        );
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> {
                    UserDTO userDTO = Mapper.mapEntityToDto(user, UserDTO.class);

                    // Chuyển đổi từ Set<Role> sang Set<String> chứa tên vai trò
                    Set<String> roleNames = user.getRoles().stream()
                            .map(role -> role.getRoleName())  // Lấy tên vai trò
                            .collect(Collectors.toSet());

                    userDTO.setRoles(roleNames);  // Gán danh sách tên vai trò vào DTO
                    return userDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Page<UserDTO> getAllUsers(Pageable pageable) {
        Page<User> usersPage = userRepository.findAll(pageable);
        return usersPage.map(user -> Mapper.mapEntityToDto(user, UserDTO.class));
    }

    @Override
    public JwtResponeDTO login(LoginDTO loginDTO) {
        User user = findByAccount(loginDTO.getAccount());
        if (user != null && passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            // Lấy danh sách vai trò của người dùng
            Set<String> roles = user.getRoles().stream()
                    .map(role -> role.getRoleName())
                    .collect(Collectors.toSet());
            String token = jwtTokenProvider.generateToken(user.getAccount(), roles);
            String refreshToken = jwtTokenProvider.generateRefreshToken(user.getAccount());


            Long expire = jwtTokenProvider.getExpirationDateFromToken(token);

            return new JwtResponeDTO(token, refreshToken, user.getAccount(), user.getRoleNames(), expire, roles);
        } else {
            throw new AppException(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_CREDENTIALS); // Ném ra ngoại lệ nếu thông tin không hợp lệ
        }
    }

    @Override
    public boolean changePassword(String account, ChangePasswordDTO changePasswordDTO) {
        User user = userRepository.findByAccount(account);
        if (user == null) {
            throw new AppException(HttpStatus.BAD_REQUEST, ErrorCode.USER_NOT_FOUND);
        }
        // Kiểm tra mật khẩu cũ
        if (!passwordEncoder.matches(changePasswordDTO.getOldPassword(), user.getPassword())) {
            throw new AppException(HttpStatus.BAD_REQUEST, ErrorCode.OLD_PASSWORD_INCORRECT);
        }
        // Kiểm tra mật khẩu mới và xác nhận
        if (!changePasswordDTO.getNewPassword().equals(changePasswordDTO.getConfirmPassword())) {
            throw new AppException(HttpStatus.BAD_REQUEST, ErrorCode.CONFIRM_PASSWORD_MISMATCH);
        }
        // Mã hóa và lưu mật khẩu mới
        user.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
        userRepository.save(user);
        return true;
    }

    public ResponseEntity<?> changeUserPassword(ChangePasswordDTO changePasswordDTO, Authentication authentication) {
        // Kiểm tra xác thực người dùng
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AppException(HttpStatus.BAD_REQUEST, ErrorCode.UNAUTHORIZED); // Hoặc một cách xử lý khác
        }

        String account = authentication.getName();

        try {
            changePassword(account, changePasswordDTO); // Gọi phương thức đã có để thay đổi mật khẩu
        } catch (AppException e) {
            throw e; // Ném lại ngoại lệ
        } catch (RuntimeException e) {
            throw new AppException(HttpStatus.BAD_REQUEST, ErrorCode.PASSWORD_CHANGE_ERROR);
        }
        return ResponseEntity.ok("Change password successful");

    }

    @Override
    public void saveUser(UserDTO userDTO) {
        User user = Mapper.mapDtoToEntity(userDTO, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Mã hóa mật khẩu trước khi lưu
        userRepository.save(user);
    }


    @Override
    public UserDTO getUserProfileByAccount(String account) {
        User user = userRepository.findByAccount(account);
        if (user == null) {
            throw new AppException(HttpStatus.BAD_REQUEST, ErrorCode.USER_NOT_FOUND);
        }
        return Mapper.mapEntityToDto(user, UserDTO.class);
    }

    private int getRoleLevelByName(String roleName) {
        return roleRepository.findByRoleName(roleName)
                .map(Role::getRoleLevel)
                .orElse(0);
    }

    @Override
    public UserDTO getUserDetails(Long userId, Authentication authentication) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, ErrorCode.USER_NOT_FOUND));

        Optional<Integer> userRoleLevels = authentication.getAuthorities().stream()
                .findFirst()
                .map(authority -> authority.getAuthority())
                .map(this::getRoleLevelByName);

        int targetUserLevel = user.getHighestRoleLevel();

        if (userRoleLevels.isPresent() && userRoleLevels.get() <= targetUserLevel) {
            return Mapper.mapEntityToDto(user, UserDTO.class);
        }

        throw new AppException(HttpStatus.FORBIDDEN, ErrorCode.ACCESS_DENIED);
    }


    @Override
    public ResponseEntity<?> addUser(UserDTO userDTO, Authentication authentication) {
        Set<String> userRoles = authentication.getAuthorities().stream()
                .map(authority -> authority.getAuthority())
                .collect(Collectors.toSet());

        if (!userRoles.contains("ROLE_ADMIN")) {
            throw new AppException(HttpStatus.FORBIDDEN, ErrorCode.ACCESS_DENIED);
        }

        Set<Role> roles = userDTO.getRoles().stream()
                .map(roleName -> roleRepository.findByRoleName(roleName)
                        .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_INPUT)))
                .collect(Collectors.toSet());

        User user = Mapper.mapDtoToEntity(userDTO, User.class);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword())); // Encode password
        user.setCreatedDate(new java.util.Date()); // Set created date
        user.setRoles(roles); // Set roles

        userRepository.save(user);
        return ResponseEntity.ok("User created successfully");
    }

    @Override
    public ApiResponse<PagedResponse<UserResponse.UserInfoDTO>> search(UserRequest.UserPagingRequest model) {
        String keywordFilter = model.getKeyword() == null ? null : "%" + model.getKeyword() + "%";
        Page<User> pages = userRepository.searchFilter(keywordFilter, model.getStatus(), model.getRoleId(), model.getPageable());
        List<UserResponse.UserInfoDTO> list = pages.getContent().stream().map(item -> {
            UserResponse.UserInfoDTO map = mapper.map(item, UserResponse.UserInfoDTO.class);
            map.setRole(item.getRole());
            return map;
        }).toList();
        PagedResponse<UserResponse.UserInfoDTO> response = new PagedResponse<>(list, pages.getNumber(), pages.getSize(), pages.getTotalElements(), pages.getTotalPages(), pages.isLast());
        return new ApiResponse<>(ErrorCode.OK.getCode(), ErrorCode.OK.getMessage(), response);
    }

    @Override
    public ApiResponse<?> updateProfile(UserRequest.UserEditProfileRequest model) {
        // Lấy thông tin người dùng hiện tại từ SecurityContext
        User user = getCurrentUser();
//                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, ErrorCode.USER_NOT_FOUND));

        // Cập nhật thông tin từ model
        if (model.getPhone() != null) user.setPhone(model.getPhone());
        if (model.getEmergencyPhone() != null) user.setEmergencyPhone(model.getEmergencyPhone());
        if (model.getAddress() != null) user.setAddress(model.getAddress());
        if (model.getDateOfBirth() != null) user.setDateOfBirth(model.getDateOfBirth());

        // Lưu lại thông tin đã được cập nhật
        userRepository.save(user);

        // Tạo response
        return new ApiResponse<>(ErrorCode.OK.getCode(), ErrorCode.OK.getMessage(), "Success");
    }

    @Override
    public ApiResponse<?> updateAvatar(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            String imageUrl = this.imageStorageService.uploadImage(CONTAINER_UPLOAD_ENUM.AVATAR.getValue(), file.getOriginalFilename(), inputStream, file.getSize());
            User user = getCurrentUser();
            user.setImgAva(imageUrl);
            userRepository.save(user);
            return new ApiResponse(HttpStatus.OK.toString(), imageUrl);
        } catch (IOException e) {
            e.printStackTrace();
            throw new AppException(HttpStatus.BAD_REQUEST, ErrorCode.FILE_WRONG_FORMAT);
        } catch (AppException e) {
            throw e;
        }
    }

    @Override
    public ApiResponse<List<UserResponse.UserInfoDTO>> findAdmin() {
        List<User> list = userRepository.findByRole("ROLE_CLASS_ADMIN");
        List<UserResponse.UserInfoDTO> response = list.stream().map(item -> {
            UserResponse.UserInfoDTO map = mapper.map(item, UserResponse.UserInfoDTO.class);
            map.setRole(item.getRole());
            return map;
        }).toList();
        return new ApiResponse<>(ErrorCode.OK.getCode(), ErrorCode.OK.getMessage(), response);
    }

    @Override
    public User getCurrentUser() {
        // Lấy thông tin người dùng hiện tại từ SecurityContext
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByAccount(currentUsername);
        if (user == null) {
            throw new AppException(HttpStatus.BAD_REQUEST, ErrorCode.SESSION_EXPIRED);
        }
        return user;
    }
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
