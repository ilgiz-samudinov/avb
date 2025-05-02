package example.com.companyservice.service;

import example.com.companyservice.dto.CompanyResponse;
import example.com.companyservice.dto.User;
import example.com.companyservice.exception.NotFoundException;
import example.com.companyservice.exception.ValidationException;
import example.com.companyservice.feign.UserClient;
import example.com.companyservice.mapper.CompanyMapper;
import example.com.companyservice.model.CompanyEntity;
import example.com.companyservice.repository.CompanyRepository;
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
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;
    private final UserClient userClient;


    @Transactional
    public CompanyEntity createCompany(CompanyEntity companyEntity) {
        validateCompany(companyEntity);
        return companyRepository.save(companyEntity);
    }


    @Transactional(readOnly = true)
    public List<CompanyEntity> getAllCompanies() {



        return companyRepository.findAll();
    }

    @Transactional
    public CompanyEntity updateCompany(Long id, CompanyEntity companyEntity) {
        validateCompanyForUpdate(id, companyEntity);
        CompanyEntity existingCompany = getCompanyById(id);
        companyMapper.mergeCompany(existingCompany, companyEntity);
        return companyRepository.save(existingCompany);
    }


    @Transactional
    public void deleteCompany(Long id) {
        if(!companyRepository.existsById(id)) {
            throw new NotFoundException("Company not found");
        }
        companyRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public CompanyEntity getCompanyById(Long id) {
        return companyRepository.findById(id)
                .orElseThrow(()->{
                    return new NotFoundException("Company not found");
                });
    }


    @Transactional(readOnly = true)
    public CompanyResponse getCompanyAndEmployees(Long id) {
        CompanyEntity companyEntity =  getCompanyById(id);


        List<User>  users = companyEntity.getEmployeeIds().isEmpty()
                ?List.of()
                :userClient.getUserByIds(companyEntity.getEmployeeIds());


        CompanyResponse companyResponse = companyMapper.toResponse(companyEntity);
        companyResponse.setUsers(users);
        return companyResponse;
    }


    @Transactional(readOnly = true)
    public List<CompanyResponse> getCompaniesByIds(List<Long> ids) {
        List<CompanyEntity> companyEntities = companyRepository.findAllByIdIn(ids);
        return companyEntities.stream()
                .map(companyMapper::toResponse)
                .collect(Collectors.toList());
    }



    @Transactional(readOnly = true)
    public List<CompanyResponse> getAllCompaniesAndEmployees() {
        List<CompanyEntity> companyEntities = companyRepository.findAll();

        List<Long> allEmployeeIds = companyEntities.stream()
                .flatMap(company -> company.getEmployeeIds().stream())
                .distinct() // Убираем дубликаты
                .collect(Collectors.toList());

        Map<Long, User> userMap  = new HashMap<>();
        if (!allEmployeeIds.isEmpty()) {
            List<User> allUsers = userClient.getUserByIds(allEmployeeIds);
            allUsers.forEach(u -> userMap.put(u.getId(), u));
        }

        return companyEntities.stream()
                .map(company -> {
                    CompanyResponse response = companyMapper.toResponse(company);

                    List<User> companyUsers = company.getEmployeeIds().stream()
                            .map(userMap::get)
                            .filter(Objects::nonNull) // На случай, если какой-то пользователь не найден
                            .collect(Collectors.toList());

                    response.setUsers(companyUsers);
                    return response;
                })
                .collect(Collectors.toList());
    }


    @Transactional
    public CompanyEntity addEmployee(Long id, Long employeeId ){
        CompanyEntity companyEntity = getCompanyById(id);
        companyEntity.addEmployee(employeeId);
        return companyRepository.save(companyEntity);
    }


    private void validateCompany(CompanyEntity companyEntity) {
        if(companyRepository.existsByName(companyEntity.getName())){
            throw  new ValidationException("A company by that name already exists");
        }
        if(companyEntity.getBudget() < 0 ){
            throw new  ValidationException("A company's budget cannot be negative");
        }
    }
    private void validateCompanyForUpdate(Long id, CompanyEntity companyEntity) {
        if(companyRepository.existsByNameAndIdNot(companyEntity.getName(), id)){
            throw  new ValidationException("A company by that name already exists");
        }
        if(companyEntity.getBudget() < 0 ){
            throw new  ValidationException("A company's budget cannot be negative");
        }
    }
}
