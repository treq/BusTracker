package me.treq.service.BusTracker.njtransit;

import me.treq.service.BusTracker.dao.BusLocationDao;
import me.treq.service.BusTracker.model.Bus;
import me.treq.service.BusTracker.model.Location;
import me.treq.service.BusTracker.model.Traceable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository("njTransit")
public class NJTransitBusLocationDao implements BusLocationDao {
    private final RestTemplate restTemplate;

    private final String urlBase;

    public NJTransitBusLocationDao(RestTemplate restTemplate, String urlBase) {
        this.restTemplate = restTemplate;
        this.urlBase = urlBase;
    }

    @Override
    public List<Bus> getBuses(String routeId) {
        Map<String, String> params = new HashMap<>();
        params.put("route", routeId);
        ResponseEntity<NJBuses> buses = this.restTemplate.getForEntity(this.urlBase, NJBuses.class, params);

        return buses.getBody().getBuses()
                .stream()
                .map(bus ->
                        new Bus(bus.getId(), new Location(bus.getLon(), bus.getLat()), bus.getDirection()))
                .collect(Collectors.toList());
    }
}
