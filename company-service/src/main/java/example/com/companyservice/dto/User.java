package example.com.companyservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    private Long id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
//    private CompanyResponse companyResponse;
}
