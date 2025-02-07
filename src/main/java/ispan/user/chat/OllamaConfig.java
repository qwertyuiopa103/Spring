//package ispan.user.chat;
//
//import dev.langchain4j.model.ollama.OllamaChatModel;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class OllamaConfig {
//
//    @Value("${langchain4j.ollama.chat-model.base-url}")
//    private String baseUrl;
//
//    @Value("${langchain4j.ollama.chat-model.model-name}")
//    private String modelName;
//
//    @Value("${langchain4j.ollama.chat-model.temperature}")
//    private Double temperature;
//
//    @Bean
//    public OllamaChatModel ollamaChatModel() {
//        return OllamaChatModel.builder()
//                .baseUrl(baseUrl)
//                .modelName(modelName)
//                .temperature(temperature)
//                .build();
//    }
//}
