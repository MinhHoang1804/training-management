package com.g96.ftms.service.user;

import com.g96.ftms.dto.ChangePasswordDTO;
import com.g96.ftms.dto.JwtResponeDTO;
import com.g96.ftms.dto.LoginDTO;
import com.g96.ftms.dto.UserDTO;
import com.g96.ftms.entity.User;
import com.g96.ftms.exception.AppException;
import com.g96.ftms.exception.ErrorCode;
import com.g96.ftms.mapper.Mapper;
import com.g96.ftms.repository.UserRepository;
import com.g96.ftms.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    public User findByAccount(String account) {
        return userRepository.findByAccount(account);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll(); // Lấy danh sách người dùng từ repository
        return users.stream().map(user -> {
            UserDTO userDTO = new UserDTO();
            userDTO.setUserId(user.getUserId());
            userDTO.setFullName(user.getFullName());
            userDTO.setEmail(user.getEmail());
            userDTO.setPhone(user.getPhone());
            return userDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public Page<UserDTO> getAllUsers(Pageable pageable) {
        return null;
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

            // Trả về JwtResponeDTO với tất cả thông tin cần thiết
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
            throw new AppException(HttpStatus.NOT_FOUND, ErrorCode.USER_NOT_FOUND);
        }
        return Mapper.mapEntityToDto(user, UserDTO.class);
    }

    @Override
    public UserDTO getUserDetails(Long userId) {
        return null;
    }


}
