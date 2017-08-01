package me.treq.service.BusTracker.service;

import me.treq.service.BusTracker.model.Bus;
import me.treq.service.BusTracker.model.Location;

import java.util.Collection;

public interface BusService {
    Collection<Bus> getBuses(int routeId);

    Bus getBus(long busId);

    Location getCentralGeoLocation(int routeId);

}
