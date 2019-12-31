package me.treq.service.BusTracker.njtransit;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableSet;
import me.treq.service.BusTracker.dao.BusRouteDao;
import me.treq.service.BusTracker.model.BusLine;
import me.treq.service.BusTracker.model.BusRoute;
import me.treq.service.BusTracker.model.BusStop;
import me.treq.service.BusTracker.model.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class NJTransitRouteDao implements BusRouteDao {
    Logger log = LoggerFactory.getLogger(NJTransitRouteDao.class);

    Set<String> DUMMY_ROUTES = ImmutableSet.of("158", "159");

    private final RestTemplate restTemplate;

    private final String urlBase;

    private final LoadingCache<String, BusRoute> busRoutesCache;

    public NJTransitRouteDao(RestTemplate restTemplate,
                             String urlBase) {
        this.restTemplate = restTemplate;
        this.urlBase = urlBase;

        this.busRoutesCache = CacheBuilder.newBuilder()
                .expireAfterWrite(1, TimeUnit.MINUTES)
                .build(new CacheLoader<String, BusRoute>() {
                    @Override
                    public BusRoute load(String routeId) throws Exception {
                        return getRouteByIdWithoutCache(routeId);
                    }
                });
    }

    @Override
    public BusRoute getRouteById(String routeId) {
        return this.busRoutesCache.getUnchecked(routeId);
    }

    private BusRoute getRouteByIdWithoutCache(String routeId) {
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
                    busLine.setPolyline(pa.getLocations().stream().map(loc -> {
                        if (loc.stop != null) {
                            return new BusStop(loc.getLon(), loc.getLat(), loc.getStop().getId(), loc.getStop().getStopName());
                        } else {
                            return new Location(loc.getLon(), loc.getLat());
                        }
                    }).collect(Collectors.toList()));
                }
                busLines.add(busLine);
            }
        }

        busRoute.setBusLines(busLines);
        busRoute.setCenter(calculateCenter(busLines));

        return busRoute;
    }

    private Location calculateCenter(List<BusLine> busLines) {
        double minLat = 360, minLong = 360;
        double maxLat = -360, maxLong = -360;

        for (BusLine eachBusLine : busLines) {
            for (Location eachLocation : eachBusLine.getPolyline()) {
                if (eachLocation.getLatitude() > maxLat) {
                    maxLat = eachLocation.getLatitude();
                }
                if (eachLocation.getLongitude() > maxLong) {
                    maxLong = eachLocation.getLongitude();
                }
                if (eachLocation.getLatitude() < minLat) {
                    minLat = eachLocation.getLatitude();
                }
                if (eachLocation.getLongitude() < minLong) {
                    minLong = eachLocation.getLongitude();
                }
            }
        }

        return new Location((maxLong + minLong) / 2, (maxLat + minLat) / 2);
    }

    @Override
    public Set<String> getAvailableRoutes() {
        // TODO temporarily return same dummy value
        return DUMMY_ROUTES;
    }
}
