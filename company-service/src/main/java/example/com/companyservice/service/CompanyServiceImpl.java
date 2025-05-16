package example.com.companyservice.service;

import example.com.companyservice.dto.CompanyRequest;
import example.com.companyservice.dto.CompanyResponse;
import example.com.companyservice.dto.User;
import example.com.companyservice.exception.NotFoundException;
import example.com.companyservice.exception.ValidationException;
import example.com.companyservice.mapper.CompanyMapper;
import example.com.companyservice.model.CompanyEntity;
import example.com.companyservice.provider.UserProviderClient;
import example.com.companyservice.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {
    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;
    private final UserProviderClient userProviderClient;

    @Transactional
    public CompanyEntity createCompany(CompanyRequest companyRequest) {
        CompanyEntity companyEntity = companyMapper.toEntity(companyRequest);
        validateCompany(companyEntity);
        return companyRepository.save(companyEntity);
    }


    @Transactional(readOnly = true)
    public List<CompanyEntity> getAllCompanies() {
        return companyRepository.findAll();
    }

    @Transactional
    public CompanyEntity updateCompany(Long id, CompanyRequest companyRequest) {
        CompanyEntity companyEntity = companyMapper.toEntity(companyRequest);
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
    public Boolean checkCompanyExist(Long id) {
        if(companyRepository.existsById(id)) {
            return true;
        }
        return false;
    }




    @Transactional(readOnly = true)
    public CompanyResponse getCompanyAndEmployees(Long id) {
        CompanyEntity companyEntity =  getCompanyById(id);


        List<User>  users = companyEntity.getEmployeeIds().isEmpty()
                ?List.of()
                :userProviderClient.getUserByIds(companyEntity.getEmployeeIds());


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
    public Page<CompanyResponse> getAllCompaniesAndEmployees(Pageable pageable) {
        Page<CompanyEntity> page = companyRepository.findAll(pageable);
        Set<Long> allEmployeeIds = page.getContent().stream()
                .flatMap(c -> c.getEmployeeIds().stream())
                .collect(Collectors.toSet());

        // ← объявляем сразу финальной и инициализируем пустым Map
        final Map<Long, User> userMap = new HashMap<>();

        if (!allEmployeeIds.isEmpty()) {
            // не переопределяем переменную, а просто заполняем её
            List<User> allUsers = userProviderClient.getUserByIds(
                    new ArrayList<>(allEmployeeIds)
            );
            for (User u : allUsers) {
                userMap.put(u.getId(), u);
            }
        }

        List<CompanyResponse> dtos = page.getContent().stream()
                .map(c -> {
                    CompanyResponse r = companyMapper.toResponse(c);
                    List<User> users = c.getEmployeeIds().stream()
                            .map(userMap::get)      // теперь userMap — final и захват корректен
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());
                    r.setUsers(users);
                    return r;
                })
                .collect(Collectors.toList());

        return new PageImpl<>(dtos, pageable, page.getTotalElements());
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
    }
    private void validateCompanyForUpdate(Long id, CompanyEntity companyEntity) {
        if(companyRepository.existsByNameAndIdNot(companyEntity.getName(), id)){
            throw  new ValidationException("A company by that name already exists");
        }
    }
}
