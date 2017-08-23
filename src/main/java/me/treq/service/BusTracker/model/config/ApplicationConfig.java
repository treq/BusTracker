package me.treq.service.BusTracker.model.config;

import lombok.Data;
import me.treq.service.BusTracker.model.BusRoute;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@Configuration
@ConfigurationProperties
public class ApplicationConfig {
    private String mapTranslationUri;
    private String busLocationUri;
    private List<BusRoute> busRoutes;
}
