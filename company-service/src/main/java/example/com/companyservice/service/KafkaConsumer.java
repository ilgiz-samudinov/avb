//package example.com.companyservice.service;
//import lombok.RequiredArgsConstructor;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class KafkaConsumer {
//
//    private final KafkaProducer kafkaProducer;
//
//    @KafkaListener(topics = "exists_topic")
//    public void listen(String companyId){
//        Long id = Long.parseLong(companyId);
//        kafkaProducer.returnResponse("existing_topic", id);
//    }
//}
