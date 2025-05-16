package example.com.userservice.service;



import example.com.userservice.dto.UserRequest;
import example.com.userservice.dto.UserResponse;
import example.com.userservice.model.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    UserEntity createUser(UserRequest userRequest);

    List<UserEntity> findAll();

    UserEntity updatedUser(Long id, UserRequest userRequest);

    UserEntity getUserById(Long id);

    void deleteUser(Long id);

    List<UserResponse> getUserByIds(List<Long> ids);

    Page<UserResponse> getAllUsersAndCompanies(Pageable pageable);

}
