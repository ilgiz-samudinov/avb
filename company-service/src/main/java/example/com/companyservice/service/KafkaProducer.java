//package example.com.companyservice.service;
//
//import example.com.companyservice.exception.NotFoundException;
//import example.com.companyservice.repository.CompanyRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class KafkaProducer {
//
//    private final KafkaTemplate<String, String> kafkatemplate;
//    private final CompanyRepository companyRepository;
//
//    public void returnResponse(String existing_topic, Long companyId){
//        if(!companyRepository.existsById(companyId)) {
//            throw new NotFoundException("Company not found");
//        }
//        kafkatemplate.send(existing_topic, companyId.toString());
//    }
//}
