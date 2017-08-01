package me.treq.service.BusTracker;

import me.treq.service.BusTracker.dao.BusLocationDao;
import me.treq.service.BusTracker.dao.RestBusLocationDaoImpl;
import me.treq.service.BusTracker.model.ApplicationConfig;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.TrustStrategy;
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

@SpringBootApplication
public class BusTrackerApplication {

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
		return restTemplate;
	}

	@Bean
	public BusLocationDao busLocationDao(RestTemplate restTemplate, ApplicationConfig appConfig) {
		String busLocationUriStr;
		URI busLocationBaseUri =  UriComponentsBuilder
				.fromUriString("https://services.saucontds.com/tds-map/nyw/nywvehiclePositions.do")
				.build().toUri();
		URI mapTranslationBaseUri = UriComponentsBuilder
				.fromUriString("https://services.saucontds.com/tds-map/nyw/nywmapTranslation.do")
				.build().toUri();
		Collection<Integer> routeIds = appConfig.getBusRoutes().keySet();
		return new RestBusLocationDaoImpl(restTemplate, busLocationBaseUri, mapTranslationBaseUri, routeIds);
	}

	@Bean
	public CommandLineRunner run(BusLocationDao busLocationDao, ApplicationConfig appConfig) throws Exception {
		return args -> {
			System.out.println("yin " + busLocationDao.getBuses(1));
			System.out.println("yinnnnnn " + appConfig);
		};
	}

}
