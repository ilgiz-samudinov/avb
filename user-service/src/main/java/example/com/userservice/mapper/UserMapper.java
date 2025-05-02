package example.com.userservice.mapper;


import example.com.userservice.dto.UserRequest;
import example.com.userservice.dto.UserResponse;
import example.com.userservice.model.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public  interface UserMapper {

    UserEntity toEntity(UserRequest request);
    @Mapping(target = "id", source = "id")
    UserResponse toResponse(UserEntity entity);

    @Mapping(target  = "id" , ignore = true)
    UserEntity mergeUser(@MappingTarget UserEntity existing, UserEntity updated);

}
