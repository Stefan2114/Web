package dosqas.javaweb.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // clients subscribe to endpoints prefixed with topic to receive updates on it
        config.enableSimpleBroker("/topic");
        // clients send messages to endpoints prefixed with /app and app receives it
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Register a WebSocket endpoint at "/ws" for clients to connect to
        // Allow all origins to connect (useful for development)
        // Enable SockJS as a fallback for browsers that do not support WebSocket
        // on /ws clients establish the connection and on /topic they receive updates
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }
}