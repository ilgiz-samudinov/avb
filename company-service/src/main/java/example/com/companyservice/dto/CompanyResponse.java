package example.com.companyservice.dto;


import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CompanyResponse {
    private Long id;
    private String name;
    private double budget;
    private List<User> users = new ArrayList<>();
}
