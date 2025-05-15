package example.com.userservice.controller;

import example.com.userservice.dto.Company;
import example.com.userservice.dto.UserRequest;
import example.com.userservice.dto.UserResponse;
import example.com.userservice.mapper.UserMapper;
import example.com.userservice.model.UserEntity;
import example.com.userservice.provider.CompanyProviderClient;
import example.com.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;
    private final CompanyProviderClient companyProviderClient;


    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest request) {
        UserEntity userEntity = userMapper.toEntity(request);
        UserEntity createdUser = userService.createUser(userEntity);
        Company company = companyProviderClient.getCompanyById(userEntity.getCompanyId());
        UserResponse userResponse = userMapper.toResponse(createdUser);
        userResponse.setCompany(company);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsersAndCompanies(){
        List<UserResponse> usersAndCompanies = userService.getAllUsersAndCompanies();
        return ResponseEntity.ok(usersAndCompanies);
    }


    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        UserEntity userEntity = userService.getUserById(id);
        Company company = companyProviderClient.getCompanyById(userEntity.getCompanyId());
        UserResponse userResponse = userMapper.toResponse(userEntity);
        userResponse.setCompany(company);
        return ResponseEntity.ok(userResponse);
    }


    @GetMapping("/by-ids")
    public ResponseEntity<List<UserResponse>> getUsersByIds(@RequestParam("ids") List<Long> ids) {
        List<UserResponse> users = userService.getUserByIds(ids);
        return ResponseEntity.ok(users);
    }


    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id,
                                                   @Valid @RequestBody UserRequest request) {
        UserEntity userEntity = userMapper.toEntity(request);
        UserEntity updatedUser  = userService.updatedUser(id, userEntity);
        Company company = companyProviderClient.getCompanyById(userEntity.getCompanyId());
        UserResponse userResponse = userMapper.toResponse(updatedUser);
        userResponse.setCompany(company);
        return ResponseEntity.ok(userResponse);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

}