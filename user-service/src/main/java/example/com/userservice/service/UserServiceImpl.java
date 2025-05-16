package example.com.userservice.service;

import example.com.userservice.dto.Company;
import example.com.userservice.dto.UserRequest;
import example.com.userservice.dto.UserResponse;
import example.com.userservice.exception.NotFoundException;
import example.com.userservice.exception.ValidationException;
import example.com.userservice.mapper.UserMapper;
import example.com.userservice.model.UserEntity;
import example.com.userservice.provider.CompanyProviderClient;
import example.com.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final CompanyProviderClient companyProviderClient;


    @Transactional
    public UserEntity  createUser(UserRequest userRequest) {
        UserEntity user = userMapper.toEntity(userRequest);
        validateUser(user);
        ensureCompanyExists(user.getCompanyId());

        UserEntity createdUser = userRepository.save(user);
        companyProviderClient.addEmployee(user.getCompanyId(), createdUser.getId());
        return createdUser;
    }


    @Override
    public List<UserEntity> findAll() {
        return userRepository.findAll();
    }

    @Transactional
    public UserEntity updatedUser(Long id, UserRequest userRequest) {
        UserEntity user = userMapper.toEntity(userRequest);
        ensureCompanyExists(user.getCompanyId());
        validateUser(user);
        UserEntity existingUser = getUserById(id);
        userMapper.mergeUser(existingUser, user);
        UserEntity updatedUser = userRepository.save(existingUser);
        companyProviderClient.addEmployee(user.getCompanyId(), updatedUser.getId());
        return updatedUser;
    }

    @Transactional(readOnly = true)
    public UserEntity getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(()->{
                    return new NotFoundException("User not found");
                });
    }

    @Transactional
    public void deleteUser(Long id) {
        ensureUserExists(id);
        userRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<UserResponse>  getUserByIds(List<Long> ids){
        List<UserEntity> userEntities = userRepository.findByIdIn(ids);
        return userEntities.stream()
                .map(userMapper::toResponse)
                .collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public Page<UserResponse> getAllUsersAndCompanies(Pageable pageable) {
        Page<UserEntity> page = userRepository.findAll(pageable);

        Set<Long> companyIds = page.getContent().stream()
                .map(UserEntity::getCompanyId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Map<Long, Company> companyMap = new HashMap<>();
        if (!companyIds.isEmpty()) {
            List<Company> companies = companyProviderClient.getCompaniesByIds(
                    new ArrayList<>(companyIds)
            );
            for (Company c : companies) {
                companyMap.put(c.getId(), c);
            }
        }

        List<UserResponse> dtos = page.getContent().stream()
                .map(u -> {
                    UserResponse resp = userMapper.toResponse(u);
                    Long cid = u.getCompanyId();
                    if (cid != null && companyMap.containsKey(cid)) {
                        resp.setCompany(companyMap.get(cid));
                    }
                    return resp;
                })
                .collect(Collectors.toList());

        return new PageImpl<>(
                dtos,
                pageable,
                page.getTotalElements()
        );
    }



    private void validateUser(UserEntity user){
        if(user == null){
            throw new ValidationException("user is null");
        }
        if(user.getCompanyId() == null){
            throw new ValidationException("companyId is null");
        }

    }


    private void ensureCompanyExists(Long companyId) {
        Boolean exists = companyProviderClient.checkCompanyById(companyId);
        if (!exists) {
            throw new ValidationException("Company does not exist");
        }
    }

    private void ensureUserExists(Long id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("User not found");
        }
    }

}
