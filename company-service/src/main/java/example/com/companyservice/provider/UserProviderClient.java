package example.com.companyservice.provider;

import example.com.companyservice.dto.User;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserProviderClient {
    private final WebClient webClient;

    public List<User> getUserByIds(List<Long>ids){
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/by-ids")
                        .queryParam("ids", ids)
                        .build(ids))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<User>>() {})
                .block();
    }

}
