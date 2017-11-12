package me.treq.service.BusTracker;

import me.treq.service.BusTracker.model.BusRoute;
import me.treq.service.BusTracker.model.config.ApplicationConfig;
import me.treq.service.BusTracker.nywaterway.NYWaterBusLocationDao;
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
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
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
	public CommandLineRunner run(NYWaterBusLocationDao busLocationDao, ApplicationConfig appConfig) throws Exception {
		return args -> {
			System.out.println("Health check: " + busLocationDao.getBuses("1"));
		};
	}

}
