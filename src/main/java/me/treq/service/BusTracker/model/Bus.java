package me.treq.service.BusTracker.model;

public class Bus {

    private Location location;

    private long id;

    public Bus() {

    }

    public Bus(long id, Location location) {
        this.location = location;
        this.id = id;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Bus{" +
                "location=" + location +
                ", id=" + id +
                '}';
    }
}
