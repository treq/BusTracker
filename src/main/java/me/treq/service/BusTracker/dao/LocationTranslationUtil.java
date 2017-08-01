package me.treq.service.BusTracker.dao;

import me.treq.service.BusTracker.model.Location;
import me.treq.service.BusTracker.model.nywportal.BusLocation;
import me.treq.service.BusTracker.model.nywportal.MapTranslation;

public class LocationTranslationUtil {
    private static final double OFFSET = 0.5;

    protected static Location translateBitMapLocationToGpsLocation(BusLocation busLocation, MapTranslation mapTranslation) {
        double latitude =
                mapTranslation.getMapBoundsMaxY() - (busLocation.getY() - OFFSET) * mapTranslation.getMapConversionY();

        double longitude =
                mapTranslation.getMapBoundsMinX() + mapTranslation.getMapConversionX() * (busLocation.getX() - OFFSET);

        return new Location(longitude, latitude);
    }

    protected static Location getCentralGeoLocation(MapTranslation mapTranslation) {
        return new Location((mapTranslation.getMapBoundsMaxX() + mapTranslation.getMapBoundsMinX()) / 2,
                (mapTranslation.getMapBoundsMaxY() + mapTranslation.getMapBoundsMinY()) / 2);
    }
}
