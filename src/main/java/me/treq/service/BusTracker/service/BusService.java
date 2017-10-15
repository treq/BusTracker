package me.treq.service.BusTracker.service;

import me.treq.service.BusTracker.model.Bus;
import me.treq.service.BusTracker.model.BusRoute;

import java.util.Collection;
import java.util.List;

public interface BusService {
    Collection<Bus> getBuses(String busSystem, String routeId);

    BusRoute getRouteById(String routeId);

    List<BusRoute> getActiveBusRoutes();
}
