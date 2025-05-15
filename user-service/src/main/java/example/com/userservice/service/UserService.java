package example.com.userservice.service;

import example.com.userservice.dto.Company;
import example.com.userservice.dto.UserResponse;
import example.com.userservice.exception.NotFoundException;
import example.com.userservice.exception.ValidationException;
import example.com.userservice.mapper.UserMapper;
import example.com.userservice.model.UserEntity;
import example.com.userservice.provider.CompanyProviderClient;
import example.com.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;


    private final CompanyProviderClient companyProviderClient;


    @Transactional
    public UserEntity  createUser(UserEntity user) {
        validateUser(user);
        Boolean exists =  companyProviderClient.checkCompanyById(user.getCompanyId());
        if (!exists) {
            throw new ValidationException("company not exists");
        }

        UserEntity createdUser = userRepository.save(user);
        companyProviderClient.addEmployee(user.getCompanyId(), createdUser.getId());
        return createdUser;
    }

    @Transactional(readOnly = true)
    public List<UserEntity> findAll() {
        return userRepository.findAll();
    }

    @Transactional
    public UserEntity updatedUser(Long id, UserEntity user) {
        Boolean exists =  companyProviderClient.checkCompanyById(user.getCompanyId());
        if (!exists) {
            throw new ValidationException("company not exists");
        }
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
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("User not found");
        }
        userRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<UserResponse>  getUserByIds(List<Long> ids){
        List<UserEntity> userEntities = userRepository.findByIdIn(ids);
        return userEntities.stream()
                .map(userMapper::toResponse)
                .collect(Collectors.toList());
    }

    private void validateUser(UserEntity user){
        if(user == null){
            throw new ValidationException("user is null");
        }
        if(user.getCompanyId() == null){
            throw new ValidationException("companyId is null");
        }

    }

    public List<UserResponse> getAllUsersAndCompanies(){
        List<UserEntity> userEntities = userRepository.findAll();


        List<Long> allCompaniesIds = userEntities.stream()
                .map(user-> user.getCompanyId())
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

        Map<Long, Company>  companyMap = new HashMap<>();
        if(!allCompaniesIds.isEmpty()){
           List<Company> allCompanies = companyProviderClient.getCompaniesByIds(allCompaniesIds);

            allCompanies.forEach(company ->
                    companyMap.put(company.getId(), company)
            );
        }
        return userEntities.stream()
                .map(user->{
                    UserResponse userResponse = userMapper.toResponse(user);
                    Long companyId = user.getCompanyId();
                    if(companyId != null && companyMap.containsKey(companyId)){
                        userResponse.setCompany(companyMap.get(companyId));
                    }
                    return userResponse;
                })
                .collect(Collectors.toList());

    }

}
