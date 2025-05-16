package example.com.userservice.controller;

import example.com.userservice.dto.Company;
import example.com.userservice.dto.UserRequest;
import example.com.userservice.dto.UserResponse;
import example.com.userservice.mapper.UserMapper;
import example.com.userservice.model.UserEntity;
import example.com.userservice.provider.CompanyProviderClient;
import example.com.userservice.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Validated
@Slf4j
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final CompanyProviderClient companyProviderClient;

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest userRequest) {
        log.debug("POST /api/users called with body={}", userRequest);
        UserEntity createdUser = userService.createUser(userRequest);
        Company company = companyProviderClient.getCompanyById(userRequest.getCompanyId());
        UserResponse userResponse = userMapper.toResponse(createdUser);
        userResponse.setCompany(company);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }

    @GetMapping
    public ResponseEntity<Page<UserResponse>> getAllUsersAndCompanies(
            @PageableDefault(page = 0, size = 20, sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable
    ) {
        log.debug("GET /api/users called, pageable={}", pageable);
        Page<UserResponse> page = userService.getAllUsersAndCompanies(pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable @Positive Long id) {
        log.debug("GET /api/users/{} called", id);
        UserEntity userEntity = userService.getUserById(id);
        Company company = companyProviderClient.getCompanyById(userEntity.getCompanyId());
        UserResponse userResponse = userMapper.toResponse(userEntity);
        userResponse.setCompany(company);
        return ResponseEntity.ok(userResponse);
    }

    @GetMapping("/by-ids")
    public ResponseEntity<List<UserResponse>> getUsersByIds(
            @RequestParam("ids") List<@Positive Long> ids) {
        log.debug("GET /api/users/by-ids called with ids={}", ids);
        List<UserResponse> users = userService.getUserByIds(ids);
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable @Positive Long id,
                                                   @Valid @RequestBody UserRequest userRequest) {
        log.debug("PUT /api/users/{} called with body={}", id, userRequest);
        UserEntity updatedUser = userService.updatedUser(id, userRequest);
        Company company = companyProviderClient.getCompanyById(userRequest.getCompanyId());
        UserResponse userResponse = userMapper.toResponse(updatedUser);
        userResponse.setCompany(company);
        return ResponseEntity.ok(userResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable @Positive Long id) {
        log.debug("DELETE /api/users/{} called", id);
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
