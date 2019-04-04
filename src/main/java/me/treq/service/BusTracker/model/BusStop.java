package me.treq.service.BusTracker.model;

import lombok.Data;

@Data
public class BusStop extends Location {
    public BusStop(double lon, double lat, long id, String name) {
        super(lon, lat);

        this.id = id;
        this.name = name;
    }

    final private long id;

    final private String name;
}
