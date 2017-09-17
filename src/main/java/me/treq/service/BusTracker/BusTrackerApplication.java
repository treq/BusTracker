package me.treq.service.BusTracker;

import me.treq.service.BusTracker.dao.BusLocationDao;
import me.treq.service.BusTracker.dao.BusLocationDaoImpl;
import me.treq.service.BusTracker.dao.BusRouteDao;
import me.treq.service.BusTracker.dao.BusRouteDaoImpl;
import me.treq.service.BusTracker.model.BusRoute;
import me.treq.service.BusTracker.model.config.ApplicationConfig;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.TrustStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.net.ssl.SSLContext;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
public class BusTrackerApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(BusTrackerApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(BusTrackerApplication.class, args);
	}


	/**
	 * This implementation makes sure the SSL certification is disbled. Otherwise, an exception will throw when
	 * connecting to "https" endpoint.
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

		return new RestTemplate(requestFactory);
	}

	@Bean
	public BusLocationDao busLocationDao(RestTemplate restTemplate, ApplicationConfig appConfig) {
		URI busLocationBaseUri =  UriComponentsBuilder
				.fromUriString(appConfig.getBusLocationUri())
				.build().toUri();
		URI mapTranslationBaseUri = UriComponentsBuilder
				.fromUriString(appConfig.getMapTranslationUri())
				.build().toUri();

		List<BusRoute> busRoutes = appConfig.getBusRoutes();

        LOGGER.info("Initialized bus routes: {}", busRoutes);

		List<String> routeIds =
				appConfig.getBusRoutes().stream().map(busRoute -> busRoute.getRouteId()).collect(Collectors.toList());


		return new BusLocationDaoImpl(restTemplate, busLocationBaseUri, mapTranslationBaseUri, routeIds);
	}

	@Bean
    public BusRouteDao busRouteDao(ApplicationConfig applicationConfig, RestTemplate restTemplate) {
        URI mapTranslationBaseUri = UriComponentsBuilder
                .fromUriString(applicationConfig.getMapTranslationUri())
                .build().toUri();
	    return new BusRouteDaoImpl(applicationConfig.getBusRoutes(), restTemplate, mapTranslationBaseUri);
    }

    @Bean
	public CommandLineRunner run(BusLocationDao busLocationDao, ApplicationConfig appConfig) throws Exception {
		return args -> {
			System.out.println("Health check: " + busLocationDao.getBuses("1"));
		};
	}

}
