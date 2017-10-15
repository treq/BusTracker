package me.treq.service.BusTracker.model;

import lombok.Data;

@Data
public class Bus implements Traceable {
    private final String id;

    private final Location location;

    private final String direction;

    @Override
    public String direction() {
        return this.direction;
    }

    @Override
    public Location location() {
        return this.location;
    }
}
