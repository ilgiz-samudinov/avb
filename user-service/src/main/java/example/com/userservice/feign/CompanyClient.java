package example.com.userservice.feign;

import example.com.userservice.dto.Company;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@FeignClient(name = "company-service")
public interface CompanyClient {
    @GetMapping("api/companies/{id}")
    void checkExists(@PathVariable Long id);

    @PostMapping("/api/companies/{id}/employees")
    void addEmployee(@PathVariable("id") Long companyId,
                     @RequestParam("employeeId") Long employeeId);

    @GetMapping("/api/companies/{id}")
    Company getCompany(@PathVariable Long id);

    @GetMapping("/api/companies/by-ids")
    List<Company> getCompaniesByIds(@RequestParam("ids") List<Long> ids);

}
