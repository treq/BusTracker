package me.treq.service.BusTracker.dao;

import me.treq.service.BusTracker.model.BusRoute;

import java.util.Set;

public interface BusRouteDao {
    BusRoute getRouteById(String routeId);

    Set<String> getAvailableRoutes();
}
