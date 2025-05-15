package example.com.userservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Company {
    private Long id;
    private String name;
    private double budget;
 //   private List<Long> employeeIds;
}
