//package example.com.userservice.controller;
//
//import example.com.userservice.dto.MessageDto;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.reactive.function.client.WebClient;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/api/message")
//public class SimpleController {
//
//    private  final WebClient webClient;
//
//
//
//    @GetMapping("/check/{id}")
//    public Boolean getCompany(@PathVariable Long id){
//        return webClient.get()
//                .uri("/check/{id}", id)
//                .retrieve()
//                .bodyToMono(Boolean.class)
//                .block();
//    }
//
//    public Boolean checkCompanyById(Long id){
//        return webClient.get()
//                .uri("/check/{id}", id)
//                .retrieve()
//                .bodyToMono(Boolean.class)
//                .block();
//    }
//
//
//
//    @GetMapping("/get")
//    public String getMessage(){
//        return webClient.get()
//                .uri("/message")
//                .retrieve()
//                .bodyToMono(String.class)
//                .block();
//    }
//
//    @PostMapping("/send")
//    public String sendMessage(@RequestBody MessageDto messageDto){
//        return webClient.post()
//                .uri("/receive")
//                .bodyValue(messageDto)
//                .retrieve()
//                .bodyToMono(String.class)
//                .block();
//    }
//
//
//
//
//
//
//
//}
