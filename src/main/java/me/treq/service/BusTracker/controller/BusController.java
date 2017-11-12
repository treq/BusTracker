package me.treq.service.BusTracker.controller;

import me.treq.service.BusTracker.model.Bus;
import me.treq.service.BusTracker.model.Traceable;
import me.treq.service.BusTracker.njtransit.NJTransitBusLocationDao;
import me.treq.service.BusTracker.njtransit.NJTransitRouteDao;
import me.treq.service.BusTracker.nywaterway.NYWBus;
import me.treq.service.BusTracker.nywaterway.NYWaterBusLocationDao;
import me.treq.service.BusTracker.service.BusService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController()
@RequestMapping("/buses")
public class BusController {

    private final BusService busService;

    public BusController(BusService busService) {
        this.busService = busService;
    }

    @RequestMapping
    public Collection<Bus> getBuses(@RequestParam(defaultValue = "1") String routeId) {
        return this.busService.getBuses("nyWaterway", routeId);
    }

    @RequestMapping("/njt/{routeId}")
    public Collection<Bus> getNjtBuses(@PathVariable String routeId) {
        return this.busService.getBuses(NJTransitBusLocationDao.class.getSimpleName(), routeId);
    }

    @RequestMapping("/nyw/{routeId}")
    public Collection<Bus> getNywBuses(@PathVariable String routeId) {
        return this.busService.getBuses(NYWaterBusLocationDao.class.getSimpleName(), routeId);
    }

}
