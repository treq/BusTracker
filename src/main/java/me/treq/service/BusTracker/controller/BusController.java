package me.treq.service.BusTracker.controller;

import me.treq.service.BusTracker.model.Bus;
import me.treq.service.BusTracker.njtransit.NJTransitBusLocationDao;
import me.treq.service.BusTracker.nywaterway.NYWaterBusLocationDao;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping(value = "/v1/buses", produces = "application/json")
public class BusController {

    private final NJTransitBusLocationDao njTransitBusLocationDao;
    private final NYWaterBusLocationDao nyWaterBusLocationDao;

    public BusController(NJTransitBusLocationDao njTransitBusLocationDao, NYWaterBusLocationDao nyWaterBusLocationDao) {
        this.njTransitBusLocationDao = njTransitBusLocationDao;
        this.nyWaterBusLocationDao = nyWaterBusLocationDao;
    }

    @GetMapping("/{busSystem}/{routeId}")
    public Collection<Bus> getBusesById(@PathVariable String busSystem, @PathVariable String routeId) {
        switch (busSystem) {
            case "njt":
                return this.njTransitBusLocationDao.getBuses(routeId);
            case "nyw":
                return this.nyWaterBusLocationDao.getBuses(routeId);
            default:
                throw new IllegalArgumentException("bus system is not supported: " + busSystem);
        }
    }

}
