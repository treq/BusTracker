package me.treq.service.BusTracker.controller;

import me.treq.service.BusTracker.model.Bus;
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

    @RequestMapping("/{system}/{routeId}")
    public Collection<Bus> getBuses(@PathVariable String system,
                                          @PathVariable String routeId) {
        return this.busService.getBuses(system, routeId);
    }

}
