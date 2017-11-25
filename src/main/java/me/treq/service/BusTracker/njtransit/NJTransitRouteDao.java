package me.treq.service.BusTracker.njtransit;

import java.util.ArrayList;
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

import com.google.common.collect.ImmutableSet;

import me.treq.service.BusTracker.dao.BusRouteDao;
import me.treq.service.BusTracker.model.BusLine;
import me.treq.service.BusTracker.model.BusRoute;
import me.treq.service.BusTracker.model.Location;

@Repository
@Qualifier("njt")
public class NJTransitRouteDao implements BusRouteDao {
    Logger log = LoggerFactory.getLogger(NJTransitRouteDao.class);

    Set<String> DUMMY_ROUTES = ImmutableSet.of("158", "159");

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

        if (njBusRoute == null) {
            return null;
        }

        BusRoute busRoute = new BusRoute();
        busRoute.setRouteId(String.valueOf(njBusRoute.getRouteId()));
        busRoute.setRouteDescription(njBusRoute.getDescription());

        if (njBusRoute.getPas() == null) {
            return null;
        }

        List<NJBusRoute.PA> paList = njBusRoute.getPas().getPaList();

        List<BusLine> busLines = new ArrayList<>();
        if (paList != null && !paList.isEmpty()) {
            for (NJBusRoute.PA pa : paList) {
                BusLine busLine = new BusLine();
                busLine.setId(pa.getId());
                busLine.setDescription(pa.getDescription());

                if (pa.getLocations() != null) {
                    busLine.setPolyline(pa.getLocations().stream().map(loc -> new Location(loc.getLon(), loc.getLat())).collect(Collectors.toList()));
                }
                busLines.add(busLine);
            }
        }

        busRoute.setBusLines(busLines);

        return busRoute;
    }

    @Override
    public Set<String> getAvailableRoutes() {
        // TODO temporarily return same dummy value
        return DUMMY_ROUTES;
    }
}
