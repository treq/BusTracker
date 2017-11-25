package me.treq.service.BusTracker.model;

import java.util.List;

import lombok.Data;

/**
 * Provides bus route information. The data are populated from application.yml.
 * Don't break the schema.
 */
@Data
public class BusRoute {
    private String routeId;

    private String routeDescription;

    private List<BusLine> busLines;

    // This is a concept from the external portal which defines the map view boundary
    // and ratios of how bus point and bus geolocation are converted.
    private Location routeViewCenter;

    // The recommanded latitude delta in order to display the route
    private double routeSpanLatitude;

    // The recommanded longitude delta in order to display the route
    private double routeSpanLongitude;
}
