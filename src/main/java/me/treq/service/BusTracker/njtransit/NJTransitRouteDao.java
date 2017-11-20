package me.treq.service.BusTracker.njtransit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import me.treq.service.BusTracker.dao.BusRouteDao;
import me.treq.service.BusTracker.model.BusRoute;
import me.treq.service.BusTracker.model.Location;

@Repository
@Qualifier("njt")
public class NJTransitRouteDao implements BusRouteDao {
    Logger log = LoggerFactory.getLogger(NJTransitRouteDao.class);

    private final RestTemplate restTemplate;

    private final String urlBase;

    public NJTransitRouteDao(RestTemplate restTemplate,
                             @Value("${njTransitBusRouteUrlTemplate}") String urlBase) {
        this.restTemplate = restTemplate;
        this.urlBase = urlBase;
    }

    @Override
    public BusRoute getRouteById(String routeId) {
        Map<String, String> params = new HashMap<>();
        params.put("route", routeId);
        ResponseEntity<NJBusRoute> route = null;
        try {
            route = this.restTemplate.getForEntity(this.urlBase, NJBusRoute.class, params);
        } catch (RuntimeException e) {
            log.warn("Throw exception when calling getForEntity for route {}.", routeId, e);
            return null;
        }

        NJBusRoute njBusRoute = route.getBody();

        System.out.println("Got route: " + njBusRoute);

        if (njBusRoute == null) {
            return null;
        }

        BusRoute busRoute = new BusRoute();
        busRoute.setRouteId(String.valueOf(njBusRoute.getRouteId()));
        busRoute.setRouteDescription(njBusRoute.getDescription());

        if (njBusRoute.getPas() == null) {
            return null;
        }

        List<NJBusRoute.Location> locations =
                njBusRoute.getPas().getPaList().stream().map(pa -> pa.getLocations()).flatMap(locs -> locs.stream()).collect(Collectors.toList());
        if (locations != null) {
            List<Location> locs = locations.stream().map(loc -> new Location(loc.getLon(), loc.getLat())).collect(Collectors.toList());
            busRoute.setPolylineArray(locs);
        }

        return busRoute;
    }

    @Override
    public Set<String> getAvailableRoutes() {
        throw new UnsupportedOperationException("getAvailableRoutes is not supported for NJT");
    }
}
