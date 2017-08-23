package me.treq.service.BusTracker.service;

import me.treq.service.BusTracker.model.Bus;
import me.treq.service.BusTracker.model.BusRoute;
import me.treq.service.BusTracker.model.Location;

import java.util.Collection;
import java.util.List;

public interface BusService {
    Collection<Bus> getBuses(int routeId);

    Bus getBus(long busId);

    Location getCentralGeoLocation(int routeId);

    BusRoute getRouteById(int routeId);

    List<BusRoute> getActiveBusRoutes();
}
