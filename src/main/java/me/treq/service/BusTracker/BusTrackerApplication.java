package me.treq.service.BusTracker;

import me.treq.service.BusTracker.model.BusRoute;
import me.treq.service.BusTracker.model.config.ApplicationConfig;
import me.treq.service.BusTracker.njtransit.NJTransitBusLocationDao;
import me.treq.service.BusTracker.njtransit.NJTransitRouteDao;
import me.treq.service.BusTracker.nywaterway.NYWaterBusLocationDao;
import me.treq.service.BusTracker.nywaterway.NYWaterwayRouteDao;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.TrustStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
@ConfigurationProperties
public class BusTrackerApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(BusTrackerApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(BusTrackerApplication.class, args);
    }


    /**
     * This implementation makes sure the SSL certification is disbled. Otherwise, an exception will throw when
     * connecting to "https" endpoint.
     *
     * @return
     * @throws KeyStoreException
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    @Bean
    public RestTemplate restTemplate()
            throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;

        SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom()
                .loadTrustMaterial(null, acceptingTrustStrategy)
                .build();

        SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);

        CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLSocketFactory(csf)
                .build();

        HttpComponentsClientHttpRequestFactory requestFactory =
                new HttpComponentsClientHttpRequestFactory();

        requestFactory.setHttpClient(httpClient);

        RestTemplate restTemplate = new RestTemplate(requestFactory);
        restTemplate.setMessageConverters(Arrays.asList(
                new MappingJackson2XmlHttpMessageConverter(),
                new MappingJackson2HttpMessageConverter()));
        return restTemplate;
    }

    @Bean
    List<BusRoute> nyWaterwayBusRoutes(ApplicationConfig applicationConfig) {
        return applicationConfig.getBusRoutes();
    }

    @Bean
    List<String> nyWaterwayBusRouteIds(ApplicationConfig applicationConfig) {
        return applicationConfig.getBusRoutes().stream().map(busRoute -> busRoute.getRouteId()).collect(Collectors.toList());
    }

    @Bean
    public CommandLineRunner run(NJTransitRouteDao njTransitRouteDao, ApplicationConfig appConfig) throws Exception {
        return args -> {
            System.out.println("Health check: " + njTransitRouteDao.getRouteById("158"));
        };
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("*").allowCredentials(true)
                        .allowedMethods("GET", "POST", "PUT", "OPTIONS");
            }
        };
    }

    @Bean
    public NJTransitRouteDao njTransitRouteDao(RestTemplate restTemplate,
                                               @Value("${njTransitBusRouteUrlTemplate}") String urlBase) {
        return new NJTransitRouteDao(restTemplate, urlBase);
    }

    @Bean
    public NJTransitBusLocationDao njTransitBusLocationDao(RestTemplate restTemplate,
                                                           @Value("${njTransitBusLocationUrlTemplate}") String urlBase) {
        return new NJTransitBusLocationDao(restTemplate, urlBase);
    }

    @Bean
    public NYWaterwayRouteDao nyWaterwayRouteDao(@Qualifier("nyWaterwayBusRoutes") List<BusRoute> busRoutes,
                                                 RestTemplate restTemplate,
                                                 @Value("${nyWaterwayBusMapTranslationUri}") String mapTranslationBaseUri) {
        return new NYWaterwayRouteDao(busRoutes, restTemplate, mapTranslationBaseUri);
    }

    @Bean
    public NYWaterBusLocationDao nyWaterBusLocationDao(RestTemplate restTemplate,
                @Value("${nyWaterwayBusLocationUri}") String busLocationBaseUri,
                @Value("${nyWaterwayBusMapTranslationUri}") String mapTranslationBaseUri,
                @Qualifier("nyWaterwayBusRouteIds") Collection<String> routeIds) {
        return new NYWaterBusLocationDao(restTemplate, busLocationBaseUri, mapTranslationBaseUri, routeIds);
    }

}
