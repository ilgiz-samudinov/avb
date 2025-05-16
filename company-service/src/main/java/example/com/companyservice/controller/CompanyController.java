package example.com.companyservice.controller;

import example.com.companyservice.dto.CompanyRequest;
import example.com.companyservice.dto.CompanyResponse;
import example.com.companyservice.mapper.CompanyMapper;
import example.com.companyservice.model.CompanyEntity;
import example.com.companyservice.service.CompanyService;
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
@RequestMapping("/api/companies")
@Validated
@Slf4j
public class CompanyController {

    private final CompanyService companyService;
    private final CompanyMapper companyMapper;

    @PostMapping
    public ResponseEntity<CompanyResponse> createCompany(@Valid @RequestBody CompanyRequest companyRequest) {
        log.debug("POST /api/companies called with body={}", companyRequest);
        CompanyEntity createdCompany = companyService.createCompany(companyRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(companyMapper.toResponse(createdCompany));
    }

    @PostMapping("/{id}/employees")
    public ResponseEntity<CompanyResponse> addEmployee(@PathVariable @Positive Long id,
                                                       @RequestParam("employeeId") @Positive Long employeeId) {
        log.debug("POST /api/companies/{}/employees called with employeeId={}", id, employeeId);
        CompanyEntity companyEntity = companyService.addEmployee(id, employeeId);
        return ResponseEntity.ok(companyMapper.toResponse(companyEntity));
    }

    @GetMapping("check/{id}")
    public Boolean check(@PathVariable @Positive Long id) {
        log.debug("GET /api/companies/check/{} called", id);
        return companyService.checkCompanyExist(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyResponse> getCompanyById(@PathVariable @Positive Long id) {
        log.debug("GET /api/companies/{} called", id);
        return ResponseEntity.ok(companyMapper.toResponse(companyService.getCompanyById(id)));
    }

    @GetMapping("/{id}/with-employees")
    public ResponseEntity<CompanyResponse> getCompanyAndEmployees(@PathVariable @Positive Long id) {
        log.debug("GET /api/companies/{}/with-employees called", id);
        return ResponseEntity.ok(companyService.getCompanyAndEmployees(id));
    }

    @GetMapping("/by-ids")
    public ResponseEntity<List<CompanyResponse>> getCompaniesByIds(
            @RequestParam("ids") List<@Positive Long> ids) {
        log.debug("GET /api/companies/by-ids called with ids={}", ids);
        List<CompanyResponse> companies = companyService.getCompaniesByIds(ids);
        return ResponseEntity.ok(companies);
    }

    @GetMapping
    public ResponseEntity<Page<CompanyResponse>> getAllCompaniesAndUsers(
            @PageableDefault(page = 0, size = 20, sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable
    ) {
        log.debug("GET /api/companies called, pageable = {}", pageable);
        Page<CompanyResponse> page = companyService.getAllCompaniesAndEmployees(pageable);
        return ResponseEntity.ok(page);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompanyResponse> updateCompany(@PathVariable @Positive Long id,
                                                         @Valid @RequestBody CompanyRequest companyRequest) {
        log.debug("PUT /api/companies/{} called with body={}", id, companyRequest);
        CompanyEntity updatedCompany = companyService.updateCompany(id, companyRequest);
        return ResponseEntity.ok(companyMapper.toResponse(updatedCompany));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable @Positive Long id) {
        log.debug("DELETE /api/companies/{} called", id);
        companyService.deleteCompany(id);
        return ResponseEntity.noContent().build();
    }
}
