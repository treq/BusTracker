package me.treq.service.BusTracker.dao;

import me.treq.service.BusTracker.model.Bus;

import java.util.List;

public interface BusLocationDao {
    List<Bus> getBuses(String routeId);
}
