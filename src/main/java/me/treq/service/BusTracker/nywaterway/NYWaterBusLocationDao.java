package me.treq.service.BusTracker.nywaterway;

import com.google.common.collect.ImmutableMap;
import me.treq.service.BusTracker.dao.BusLocationDao;
import me.treq.service.BusTracker.model.Bus;
import me.treq.service.BusTracker.model.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.*;

@Repository("nyWaterway")
public class NYWaterBusLocationDao implements BusLocationDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(NYWaterBusLocationDao.class);

    private final RestTemplate restTemplate;

    private final Map<String, URI> busLocationUriByRouteId;

    private final Map<String, URI> mapTranslationUriByRouteId;

    public NYWaterBusLocationDao(RestTemplate restTemplate,
                                 @Qualifier("nyWaterwayBusLocation") URI busLocationBaseUri,
                                 @Qualifier("nyWaterwayMapTranslation") URI mapTranslationBaseUri,
                                 @Qualifier("nyWaterwayRoutes") Collection<String> routeIds) {
        this.restTemplate = restTemplate;

        Map<String, URI> busLocationUriByRouteId = new HashMap<>();
        Map<String, URI> mapTranslationUriByRouteId = new HashMap<>();

        for (String routeId : routeIds) {
            busLocationUriByRouteId.put(routeId,
                    UriComponentsBuilder.fromUri(busLocationBaseUri).queryParam("id", routeId).build().toUri());
            mapTranslationUriByRouteId.put(routeId,
                UriComponentsBuilder.fromUri(mapTranslationBaseUri).queryParam("id", routeId).build().toUri());
        }

        this.busLocationUriByRouteId = ImmutableMap.copyOf(busLocationUriByRouteId);
        this.mapTranslationUriByRouteId = ImmutableMap.copyOf(mapTranslationUriByRouteId);

        LOGGER.info("Initialized NYWaterBusLocationDao with {} and {}",
                this.busLocationUriByRouteId, this.mapTranslationUriByRouteId);
    }

    @Override
    public List<Bus> getBuses(String routeId) {
        List<Bus> NYWBuses = new ArrayList<>();

        URI busLocationUri = this.busLocationUriByRouteId.get(routeId);
        URI mapTranslationUri = this.mapTranslationUriByRouteId.get(routeId);

        if (busLocationUri == null || mapTranslationUri == null) {
            LOGGER.warn("Invalid routeId {}", routeId);
            return NYWBuses;
        }

        ResponseEntity<BusLocation[]> busLocations = this.restTemplate.getForEntity(busLocationUri, BusLocation[].class);
        MapTranslation mapTranslation = this.restTemplate.getForObject(mapTranslationUri, MapTranslation.class);

        if (mapTranslation == null) {
            LOGGER.warn("Failed to get mapTranslation for route {}", routeId);
            return NYWBuses;
        }

        Location location = null;
        for (BusLocation busLocation : busLocations.getBody()) {
            location = ExternalModelsTranslationUtil.translateBitMapLocationToGpsLocation(busLocation, mapTranslation);

            // Uses the "o" field from NY Waterway portal as NYWBus#id. This might be wrong.
            NYWBuses.add(new Bus(String.valueOf(busLocation.getO()), location, ExternalModelsTranslationUtil.calculateBusDirection(busLocation)));
        }

        return NYWBuses;
    }

}
