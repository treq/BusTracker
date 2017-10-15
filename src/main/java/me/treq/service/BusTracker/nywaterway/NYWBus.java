package me.treq.service.BusTracker.nywaterway;

import lombok.Data;
import me.treq.service.BusTracker.model.Location;
import me.treq.service.BusTracker.model.Traceable;

@Data
public class NYWBus implements Traceable {

    private final Location location;

    private final String direction;

    @Override
    public String direction() {
        return direction;
    }

    @Override
    public Location location() {
        return location;
    }
}
