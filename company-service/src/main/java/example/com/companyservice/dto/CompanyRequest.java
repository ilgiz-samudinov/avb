package example.com.companyservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyRequest {
    @NotBlank(message = "Company name is required")
    private String name;
    @Min(value = 0, message = "Company budget must be a positive number")
    private double budget;

    private Long employeeId;
}
