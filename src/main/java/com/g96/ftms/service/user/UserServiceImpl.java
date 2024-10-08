package com.g96.ftms.service.user;

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
import org.springframework.http.HttpStatus;
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
            return new JwtResponeDTO(token, refreshToken, user.getAccount(),  user.getRoleNames(), expire, roles);
        } else {
            throw new RuntimeException(ErrorCode.INVALID_CREDENTIALS.getMessage()); // Ném ra ngoại lệ nếu thông tin không hợp lệ
        }
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


}
