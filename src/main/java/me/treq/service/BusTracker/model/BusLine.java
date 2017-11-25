package me.treq.service.BusTracker.model;

import java.util.List;

import lombok.Data;

@Data
public class BusLine {
    private long id;

    private String description;

    private List<Location> polyline;
}


