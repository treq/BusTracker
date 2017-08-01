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
public class RestBusLocationDaoImpl implements BusLocationDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(RestBusLocationDaoImpl.class);

    private final RestTemplate restTemplate;

    private final Map<Integer, URI> busLocationUriByRouteId;

    private final Map<Integer, URI> mapTranslationUriByRouteId;

    @Autowired
    public RestBusLocationDaoImpl(RestTemplate restTemplate, URI busLocationBaseUri,
                                  URI mapTranslationBaseUri, Collection<Integer> routeIds) {
        this.restTemplate = restTemplate;

        Map<Integer, URI> busLocationUriByRouteId = new HashMap<>();
        Map<Integer, URI> mapTranslationUriByRouteId = new HashMap<>();

        for (Integer routeId : routeIds) {
            busLocationUriByRouteId.put(routeId,
                    UriComponentsBuilder.fromUri(busLocationBaseUri).queryParam("id", routeId).build().toUri());
            mapTranslationUriByRouteId.put(routeId,
                UriComponentsBuilder.fromUri(mapTranslationBaseUri).queryParam("id", routeId).build().toUri());
        }

        this.busLocationUriByRouteId = ImmutableMap.copyOf(busLocationUriByRouteId);
        this.mapTranslationUriByRouteId = ImmutableMap.copyOf(mapTranslationUriByRouteId);

        LOGGER.info("Initialized RestBusLocationDaoImpl with {} and {}",
                this.busLocationUriByRouteId, this.mapTranslationUriByRouteId);
    }

    @Override
    public List<Bus> getBuses(int routeId) {
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
            location = LocationTranslationUtil.translateBitMapLocationToGpsLocation(busLocation, mapTranslation);

            // Uses the "o" field from NY Waterway portal as Bus#id. This might be wrong.
            buses.add(new Bus(busLocation.getO(), location));
        }

        return buses;
    }

    @Override
    public Location getCentralGeoLocation(int routeId) {
        URI mapTranslationUri = this.mapTranslationUriByRouteId.get(routeId);
        if (mapTranslationUri == null) {
            LOGGER.info("Cannot find map translation for route {}", routeId);
            return null;
        }

        MapTranslation mapTranslation = this.restTemplate.getForObject(mapTranslationUri, MapTranslation.class);
        return LocationTranslationUtil.getCentralGeoLocation(mapTranslation);
    }
}
