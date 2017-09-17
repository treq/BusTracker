package me.treq.service.BusTracker.controller;

import me.treq.service.BusTracker.model.BusRoute;
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

    @RequestMapping
    public List<BusRoute> getActiveBusRoutes() {
        return this.busService.getActiveBusRoutes();
    }

    @RequestMapping("/{routeId}")
    public BusRoute getRouteById(@PathVariable String routeId) {
        return this.busService.getRouteById(routeId);
    }

}
