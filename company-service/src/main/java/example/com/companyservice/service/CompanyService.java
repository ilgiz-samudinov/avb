package example.com.companyservice.service;

import example.com.companyservice.dto.CompanyRequest;
import example.com.companyservice.dto.CompanyResponse;
import example.com.companyservice.model.CompanyEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CompanyService {

    CompanyEntity createCompany(CompanyRequest companyRequest);

    List<CompanyEntity> getAllCompanies();

    CompanyEntity updateCompany(Long id, CompanyRequest companyRequest);

    void deleteCompany(Long id);

    CompanyEntity getCompanyById(Long id);

    Boolean checkCompanyExist(Long id);

    CompanyResponse getCompanyAndEmployees(Long id);

    List<CompanyResponse> getCompaniesByIds(List<Long> ids);

    Page<CompanyResponse> getAllCompaniesAndEmployees(Pageable pageable);

    CompanyEntity addEmployee(Long id, Long employeeId);

}
