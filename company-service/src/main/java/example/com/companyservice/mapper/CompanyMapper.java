package example.com.companyservice.mapper;

import example.com.companyservice.dto.CompanyRequest;
import example.com.companyservice.dto.CompanyResponse;
import example.com.companyservice.model.CompanyEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CompanyMapper {
    CompanyEntity toEntity(CompanyRequest companyRequest);


    CompanyResponse toResponse(CompanyEntity companyEntity);

    @Mapping(target = "id", ignore = true)
    CompanyEntity mergeCompany(@MappingTarget CompanyEntity existing, CompanyEntity updated);
}
