package email.backend.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // Allow all endpoints
            .allowedOriginPatterns("http://localhost:3000")  // Adjust the frontend URL
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
            .allowedHeaders("Authorization", "Content-Type", "Accept")  // Allow specific headers
            .exposedHeaders("Authorization");  // Expose Authorization header to the frontend
            // .allowCredentials(true);  // Allow credentials (cookies, HTTP authentication, etc.)
    }
}
