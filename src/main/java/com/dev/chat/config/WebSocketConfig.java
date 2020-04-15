package com.dev.chat.config;

import com.dev.chat.handle.MyHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
public class WebSocketConfig  implements WebSocketConfigurer {

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
      return  new ServerEndpointExporter();
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(myHandler(), "websocket")
                .setAllowedOrigins("*");
    }
    public WebSocketHandler myHandler() {
        return new MyHandler();
    }
}
