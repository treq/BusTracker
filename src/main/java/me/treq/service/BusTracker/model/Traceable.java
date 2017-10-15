package me.treq.service.BusTracker.model;

/**
 * Objects that can be traced.
 */
public interface Traceable {
    /**
     * Direction of the traceable object.
     * @return 0 - 359 where 0 is north.
     * @T the type representing the direction system
     */
    String direction();

    /**
     * Coordination with x and y.
     * @return location.
     */
    Location location();
}
