package me.treq.service.BusTracker.model;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Data
@Component
@ConfigurationProperties
public class ApplicationConfig {
    private String mapTranslationUri;
    private String busLocationUri;
    private Map<Integer, String> busRoutes;
}
