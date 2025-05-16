package example.com.companyservice.repository;

import example.com.companyservice.model.CompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<CompanyEntity, Long> {
    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Long id);
    List<CompanyEntity> findAllByIdIn(List<Long> ids);
}
