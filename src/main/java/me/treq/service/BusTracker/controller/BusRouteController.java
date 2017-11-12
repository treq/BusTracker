package me.treq.service.BusTracker.controller;

import me.treq.service.BusTracker.model.BusRoute;
import me.treq.service.BusTracker.njtransit.NJTransitRouteDao;
import me.treq.service.BusTracker.nywaterway.NYWaterwayRouteDao;
import me.treq.service.BusTracker.service.BusService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @RequestMapping("/njt")
    public List<BusRoute> getNjtActiveBusRoutes() {
        return this.busService.getActiveBusRoutes(NJTransitRouteDao.class.getSimpleName());
    }

    @RequestMapping("/nyw")
    public List<BusRoute> getNywActiveBusRoutes() {
        return this.busService.getActiveBusRoutes(NYWaterwayRouteDao.class.getSimpleName());
    }

    @RequestMapping("/njt/{routeId}")
    public BusRoute getNjtRouteById(@PathVariable String routeId) {
        return this.busService.getRouteById(NJTransitRouteDao.class.getSimpleName(), routeId);
    }

    @RequestMapping("/nyw/{routeId}")
    public BusRoute getNywRouteById(@PathVariable String routeId) {
        return this.busService.getRouteById(NYWaterwayRouteDao.class.getSimpleName(), routeId);
    }

}
