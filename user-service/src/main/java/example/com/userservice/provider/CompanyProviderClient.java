package example.com.userservice.provider;

import example.com.userservice.dto.Company;
import example.com.userservice.dto.MessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CompanyProviderClient {
    private final WebClient webClient;


    public Boolean checkCompanyById(Long id){
        return webClient.get()
                .uri("/check/{id}", id)
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();
    }


    public String addEmployee(Long companyId, Long employeeId) {
        return webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/{companyId}/employees")
                        .queryParam("employeeId", employeeId)
                        .build(companyId)
                )
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }


    public Company getCompanyById(Long id){
        return webClient.get()
                .uri("/{id}", id)
                .retrieve()
                .bodyToMono(Company.class)
                .block();

    }


    public List<Company> getCompaniesByIds(List<Long> ids){
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/by-ids")
                        .queryParam("ids", ids)
                        .build(ids))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Company>>() {})
                .block();

    }


    @PostMapping("/send")
    public String sendMessage(@RequestBody MessageDto messageDto){
        return webClient.post()
                .uri("/receive")
                .bodyValue(messageDto)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
