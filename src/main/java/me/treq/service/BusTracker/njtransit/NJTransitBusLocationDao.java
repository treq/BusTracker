package me.treq.service.BusTracker.njtransit;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import me.treq.service.BusTracker.dao.BusLocationDao;
import me.treq.service.BusTracker.model.Bus;
import me.treq.service.BusTracker.model.Location;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class NJTransitBusLocationDao implements BusLocationDao {
    private final RestTemplate restTemplate;

    private final String urlBase;

    private final LoadingCache<String, List<Bus>> busLocationsCache;

    public NJTransitBusLocationDao(RestTemplate restTemplate,
                                   String urlBase) {
        this.restTemplate = restTemplate;

        this.urlBase = urlBase;

        this.busLocationsCache = CacheBuilder.newBuilder()
                .expireAfterWrite(3, TimeUnit.SECONDS)
                .build(new CacheLoader<String, List<Bus>>() {
                    @Override
                    public List<Bus> load(String routeId) throws Exception {
                        return getBusesWthoutCache(routeId);
                    }
                });
    }

    @Override
    public List<Bus> getBuses(String routeId) {
        return this.busLocationsCache.getUnchecked(routeId);
    }

    private List<Bus> getBusesWthoutCache(String routeId) {
        Map<String, String> params = new HashMap<>();
        params.put("route", routeId);
        ResponseEntity<NJBuses> buses = this.restTemplate.getForEntity(this.urlBase, NJBuses.class, params);

        List<NJBus> result = buses.getBody().getBuses();
        if (result != null) {
            return result.stream()
                    .map(bus -> new Bus(bus.getId(), new Location(bus.getLon(), bus.getLat()), bus.getDirection(), bus.getDest()))
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }
}
