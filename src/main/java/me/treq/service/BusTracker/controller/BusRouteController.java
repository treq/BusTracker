package me.treq.service.BusTracker.controller;

import me.treq.service.BusTracker.model.BusRoute;
import me.treq.service.BusTracker.njtransit.NJTransitRouteDao;
import me.treq.service.BusTracker.nywaterway.NYWaterwayRouteDao;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
@RequestMapping(value = "/v1/routes", produces = "application/json")
public class BusRouteController {

    private final NJTransitRouteDao njTransitRouteDao;
    private final NYWaterwayRouteDao nyWaterwayRouteDao;

    public BusRouteController(NJTransitRouteDao njTransitRouteDao, NYWaterwayRouteDao nyWaterwayRouteDao) {
        this.njTransitRouteDao = njTransitRouteDao;
        this.nyWaterwayRouteDao = nyWaterwayRouteDao;
    }

    public List<BusRoute> getActiveBusRoutes(@PathVariable String system) {
        throw new IllegalArgumentException("not supported: " + system);
    }

    @GetMapping(value = "/{busSystem}")
    public List<BusRoute> getBusRoutes(@PathVariable String busSystem) {
        throw new IllegalArgumentException("not supported: " + busSystem);
    }

    @GetMapping("/{busSystem}/{routeId}")
    public BusRoute getBusRouteById(@PathVariable String busSystem, @PathVariable String routeId) {
        switch (busSystem) {
            case "njt":
                return this.njTransitRouteDao.getRouteById(routeId);
            case "nyw":
                return this.nyWaterwayRouteDao.getRouteById(routeId);
            default:
                throw new IllegalArgumentException("bus system is not supported: " + busSystem);

        }
    }

}
