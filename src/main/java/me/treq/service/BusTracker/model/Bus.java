package me.treq.service.BusTracker.model;

import lombok.Data;

@Data
public class Bus {
    private final long id;

    private final Location location;
}
