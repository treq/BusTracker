package me.treq.service.BusTracker.njtransit;

import me.treq.service.BusTracker.dao.BusLocationDao;
import me.treq.service.BusTracker.model.Bus;
import me.treq.service.BusTracker.model.Location;
import me.treq.service.BusTracker.model.Traceable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@Qualifier("njt")
public class NJTransitBusLocationDao implements BusLocationDao {
    private final RestTemplate restTemplate;

    private final String urlBase;

    public NJTransitBusLocationDao(RestTemplate restTemplate,
                                   @Value("${njTransitBusLocationUrlTemplate}") String urlBase) {
        this.restTemplate = restTemplate;
        this.urlBase = urlBase;
    }

    @Override
    public List<Bus> getBuses(String routeId) {
        Map<String, String> params = new HashMap<>();
        params.put("route", routeId);
        ResponseEntity<NJBuses> buses = this.restTemplate.getForEntity(this.urlBase, NJBuses.class, params);

        List<NJBus> result = buses.getBody().getBuses();
        if (result != null) {
            return result.stream()
                         .map(bus -> new Bus(bus.getId(), new Location(bus.getLon(), bus.getLat()), bus.getDirection()))
                         .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }
}
