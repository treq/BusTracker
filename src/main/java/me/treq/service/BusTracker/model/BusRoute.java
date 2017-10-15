package me.treq.service.BusTracker.model;

import lombok.Data;

import java.util.List;

/**
 * Provides bus route information. The data are populated from application.yml.
 * Don't break the schema.
 */
@Data
public class BusRoute {
    private String routeId;

    private String routeDescription;

    private List<Location> polylineArray;

    // This is a concept from the external portal which defines the map view boundary
    // and ratios of how bus point and bus geolocation are converted.
    private Location routeViewCenter;

    // The recommanded latitude delta in order to display the route
    private double routeSpanLatitude;

    // The recommanded longitude delta in order to display the route
    private double routeSpanLongitude;
}
