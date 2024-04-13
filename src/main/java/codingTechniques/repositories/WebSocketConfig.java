//package codingTechniques.repositories;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.messaging.MessageChannel;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.messaging.support.channel.DirectChannel;
//
//@Configuration
//public class WebSocketConfig {
//
//    @Bean
//    public MessageChannel messageChannel() {
//        return new DirectChannel();
//    }
//
//    @Bean
//    public SimpMessagingTemplate messagingTemplate(MessageChannel messageChannel) {
//        return new SimpMessagingTemplate(messageChannel);
//    }
//
//}
