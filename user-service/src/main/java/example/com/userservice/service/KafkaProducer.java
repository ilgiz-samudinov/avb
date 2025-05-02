//package example.com.userservice.service;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class KafkaProducer {
//    private final KafkaTemplate<String, String> kafkaTemplate;
//
//    public void existsById(String exists_topic, Long companyId){
//         kafkaTemplate.send(exists_topic, companyId.toString());
//    }
//}
