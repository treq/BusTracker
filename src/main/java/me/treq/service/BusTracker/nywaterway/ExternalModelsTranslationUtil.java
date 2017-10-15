package me.treq.service.BusTracker.nywaterway;

import me.treq.service.BusTracker.model.Location;
import me.treq.service.BusTracker.nywaterway.BusLocation;
import me.treq.service.BusTracker.nywaterway.MapTranslation;

/**
 * Utility class that helps translation between website models and application models.
 */
public final class ExternalModelsTranslationUtil {
    private ExternalModelsTranslationUtil() {
        // static class.
    }

    private static final double OFFSET = 0.5;

    /**
     * Translate bit map location to gps location location.
     *
     * @param busLocation    the bus location
     * @param mapTranslation the map translation
     * @return the location
     */
    public static Location translateBitMapLocationToGpsLocation(BusLocation busLocation, MapTranslation mapTranslation) {
        double latitude =
                mapTranslation.getMapBoundsMaxY() - (busLocation.getY() - OFFSET) * mapTranslation.getMapConversionY();

        double longitude =
                mapTranslation.getMapBoundsMinX() + mapTranslation.getMapConversionX() * (busLocation.getX() - OFFSET);

        return new Location(longitude, latitude);
    }

    /**
     * Gets central geo location.
     *
     * @param mapTranslation the map translation
     * @return the central geo location
     */
    public static Location getCentralGeoLocation(MapTranslation mapTranslation) {
        return new Location((mapTranslation.getMapBoundsMaxX() + mapTranslation.getMapBoundsMinX()) / 2,
                (mapTranslation.getMapBoundsMaxY() + mapTranslation.getMapBoundsMinY()) / 2);
    }

    /**
     * Return bus direction in angle (0 - 359) where 0 represents North, with an incremental of 10 degree.
     * <p>
     * {@code busLocation offset} represents the horizontal pixel offset of the direction image.
     * The direction image is consist of a row of arrows. Each arrow has a wide of 54 pixels.
     * The first arrow is facing north and then turn clockwise with an increment of 10 degree.
     *
     * @param busLocation bus Location that has the same fields as in the JSON response from the website rest APIs
     * @return int a number that represents the direction of the bus - (0 - 359) where 0 represents North.
     * @throws IllegalArgumentException if {@code busLocation} doesn't provide enough of corretion information to         calculation the direction.
     */
    public static String calculateBusDirection(BusLocation busLocation) {
        int directionOffset = busLocation.getDirectionOffset();
        if (directionOffset % 54 != 0) {
            throw new IllegalArgumentException("direction Offset should be a multiple of 54");
        }

        return String.valueOf(directionOffset / 54 * 10);
    }

}
