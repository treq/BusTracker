package me.treq.service.BusTracker.controller;

import java.util.Collection;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import me.treq.service.BusTracker.model.Bus;
import me.treq.service.BusTracker.njtransit.NJTransitBusLocationDao;
import me.treq.service.BusTracker.nywaterway.NYWaterBusLocationDao;
import me.treq.service.BusTracker.service.BusService;

@RestController()
@RequestMapping("/v1/buses")
public class BusController {

    private final BusService busService;

    public BusController(BusService busService) {
        this.busService = busService;
    }

    @RequestMapping("/{busSystem}/{routeId}")
    public Collection<Bus> getNjtBuses(@PathVariable String busSystem, @PathVariable String routeId) {
        switch (busSystem) {
            case "njt":
                return this.busService.getBuses(NJTransitBusLocationDao.class.getSimpleName(), routeId);
            case "nyw":
                return this.busService.getBuses(NYWaterBusLocationDao.class.getSimpleName(), routeId);
            default:
                throw new IllegalArgumentException("bus system is not supported: " + busSystem);
        }
    }

}
