package example.com.companyservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "companies")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CompanyEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private double budget;


    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "company_employees",
            joinColumns = @JoinColumn(name = "company_id")
    )
    @Column(name = "employee_id")
    private List<Long> employeeIds = new ArrayList<>();

    public void addEmployee(Long employeeId) {
        this.employeeIds.add(employeeId);
    }

}
