package me.treq.service.BusTracker.model;

public interface Movable {
    Location getCurrentLocation();

    Location getPreviousLocation();

    void moveTo(Location loc);
}
