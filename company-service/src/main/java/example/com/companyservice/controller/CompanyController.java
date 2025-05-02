package example.com.companyservice.controller;

import example.com.companyservice.dto.CompanyRequest;
import example.com.companyservice.dto.CompanyResponse;
import example.com.companyservice.mapper.CompanyMapper;
import example.com.companyservice.model.CompanyEntity;
import example.com.companyservice.service.CompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/companies")
public class CompanyController {
    private final CompanyService companyService;
    private final CompanyMapper companyMapper;

    @PostMapping
    public ResponseEntity<CompanyResponse> createCompany(@Valid @RequestBody CompanyRequest companyRequest) {
        CompanyEntity companyEntity =  companyMapper.toEntity(companyRequest);
        CompanyEntity createdCompany = companyService.createCompany(companyEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(companyMapper.toResponse(createdCompany));
    }

    @PostMapping("/{id}/employees")
    public ResponseEntity<CompanyResponse> addEmployee(@PathVariable Long id,
                                                       @RequestParam("employeeId") Long employeeId ) {

        CompanyEntity companyEntity = companyService.addEmployee(id, employeeId);
        return ResponseEntity.ok(companyMapper.toResponse(companyEntity));
    }



    @GetMapping("/{id}")
    public  ResponseEntity<CompanyResponse> getCompanyAndEmployees(@PathVariable Long id) {
        return ResponseEntity.ok(companyService.getCompanyAndEmployees(id));
    }


    @GetMapping("/by-ids")
    public ResponseEntity<List<CompanyResponse>> getCompaniesByIds(@RequestParam("ids") List<Long> ids) {
        List<CompanyResponse> companies = companyService.getCompaniesByIds(ids);
        return ResponseEntity.ok(companies);
    }


    @GetMapping
    public ResponseEntity<List<CompanyResponse>> getAllCompaniesAndUsers() {
        List<CompanyResponse> companies = companyService.getAllCompaniesAndEmployees();
        return ResponseEntity.ok(companies);
    }


    @PutMapping("/{id}")
    public ResponseEntity<CompanyResponse> updateCompany(@PathVariable Long id,
                                                         @Valid @RequestBody CompanyRequest companyRequest) {
        CompanyEntity companyEntity = companyMapper.toEntity(companyRequest);
        CompanyEntity updatedCompany = companyService.updateCompany(id, companyEntity);
        return ResponseEntity.ok(companyMapper.toResponse(updatedCompany));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable Long id) {
        companyService.deleteCompany(id);
        return ResponseEntity.noContent().build();
    }
}
