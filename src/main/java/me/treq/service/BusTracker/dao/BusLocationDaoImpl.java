package me.treq.service.BusTracker.dao;

import com.google.common.collect.ImmutableMap;
import me.treq.service.BusTracker.model.Bus;
import me.treq.service.BusTracker.model.Location;
import me.treq.service.BusTracker.model.nywportal.BusLocation;
import me.treq.service.BusTracker.model.nywportal.MapTranslation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.*;

@Repository("busLocationDao")
public class BusLocationDaoImpl implements BusLocationDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(BusLocationDaoImpl.class);

    private final RestTemplate restTemplate;

    private final Map<String, URI> busLocationUriByRouteId;

    private final Map<String, URI> mapTranslationUriByRouteId;

    @Autowired
    public BusLocationDaoImpl(RestTemplate restTemplate, URI busLocationBaseUri,
                              URI mapTranslationBaseUri, Collection<String> routeIds) {
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

        LOGGER.info("Initialized BusLocationDaoImpl with {} and {}",
                this.busLocationUriByRouteId, this.mapTranslationUriByRouteId);
    }

    @Override
    public List<Bus> getBuses(String routeId) {
        List<Bus> buses = new ArrayList<>();

        URI busLocationUri = this.busLocationUriByRouteId.get(routeId);
        URI mapTranslationUri = this.mapTranslationUriByRouteId.get(routeId);

        if (busLocationUri == null || mapTranslationUri == null) {
            LOGGER.warn("Invalid routeId {}", routeId);
            return buses;
        }

        ResponseEntity<BusLocation[]> busLocations = this.restTemplate.getForEntity(busLocationUri, BusLocation[].class);
        MapTranslation mapTranslation = this.restTemplate.getForObject(mapTranslationUri, MapTranslation.class);

        if (mapTranslation == null) {
            LOGGER.warn("Failed to get mapTranslation for route {}", routeId);
            return buses;
        }

        Location location = null;
        for (BusLocation busLocation : busLocations.getBody()) {
            location = ExternalModelsTranslationUtil.translateBitMapLocationToGpsLocation(busLocation, mapTranslation);

            // Uses the "o" field from NY Waterway portal as Bus#id. This might be wrong.
            buses.add(new Bus(busLocation.getO(), location, ExternalModelsTranslationUtil.calculateBusDirection(busLocation)));
        }

        return buses;
    }

}
