package me.treq.service.BusTracker.njtransit;

import me.treq.service.BusTracker.dao.BusRouteDao;
import me.treq.service.BusTracker.model.BusRoute;
import me.treq.service.BusTracker.model.Location;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@Qualifier("njt")
public class NJTransitRouteDao implements BusRouteDao {
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
        ResponseEntity<NJBusRoute> route = this.restTemplate.getForEntity(this.urlBase, NJBusRoute.class, params);

        NJBusRoute njBusRoute = route.getBody();

        System.out.println("Got route: " + njBusRoute);

        if (njBusRoute == null) {
            return null;
        }

        BusRoute busRoute = new BusRoute();
        busRoute.setRouteId(String.valueOf(njBusRoute.getRouteId()));
        busRoute.setRouteDescription(njBusRoute.getDescription());

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
