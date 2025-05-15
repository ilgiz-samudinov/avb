//package example.com.companyservice.controller;
//
//import example.com.companyservice.dto.MessageDto;
//import example.com.companyservice.service.CompanyService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequiredArgsConstructor
//public class SimpleController {
//
//    private final CompanyService companyService;
//
//
//
//    @GetMapping("check/{id}")
//    public Boolean check(@PathVariable Long id){
//        return companyService.checkCompanyExist(id);
//    }
//
//
//
//
//
//
//
//    @GetMapping("/message")
//    public String getData(){
//        return "this is company-service";
//    }
//
//    @PostMapping("/receive")
//    public String receiveMessage(@RequestBody MessageDto messageDto){
//        return "Получено сообщение: " + messageDto.getContent();
//    }
//
//
//
//}
