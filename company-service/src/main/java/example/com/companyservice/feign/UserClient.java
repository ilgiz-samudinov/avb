package example.com.companyservice.feign;

import example.com.companyservice.dto.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "user-service")
public interface UserClient {
    @GetMapping("/api/users/by-ids")
    List<User> getUserByIds(@RequestParam("ids") List<Long> ids);
}
