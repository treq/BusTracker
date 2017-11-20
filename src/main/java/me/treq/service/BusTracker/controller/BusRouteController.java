package me.treq.service.BusTracker.controller;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import me.treq.service.BusTracker.model.BusRoute;
import me.treq.service.BusTracker.njtransit.NJTransitRouteDao;
import me.treq.service.BusTracker.nywaterway.NYWaterwayRouteDao;
import me.treq.service.BusTracker.service.BusService;

@RestController()
@RequestMapping("/routes")
public class BusRouteController {

    private final BusService busService;

    public BusRouteController(BusService busService) {
        this.busService = busService;
    }

    public List<BusRoute> getActiveBusRoutes(@PathVariable String system) {
        return this.busService.getActiveBusRoutes(system);
    }

    @RequestMapping("/{busSystem}")
    public List<BusRoute> getNjtActiveBusRoutes(@PathVariable String busSystem) {
        switch (busSystem) {
            case "njt":
                return this.busService.getActiveBusRoutes(NJTransitRouteDao.class.getSimpleName());
            case "nyw":
                return this.busService.getActiveBusRoutes(NYWaterwayRouteDao.class.getSimpleName());
            default:
                throw new IllegalArgumentException("bus system is not supported: " + busSystem);
        }
    }

    @RequestMapping("/{busSystem}/{routeId}")
    public BusRoute getNjtRouteById(@PathVariable String busSystem, @PathVariable String routeId) {
        switch (busSystem) {
            case "njt":
                return this.busService.getRouteById(NJTransitRouteDao.class.getSimpleName(), routeId);
            case "nyw":
                return this.busService.getRouteById(NYWaterwayRouteDao.class.getSimpleName(), routeId);
            default:
                throw new IllegalArgumentException("bus system is not supported: " + busSystem);

        }
    }

}
