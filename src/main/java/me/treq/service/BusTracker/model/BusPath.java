package me.treq.service.BusTracker.model;

public class BusPath {
    private final long pathId;
    private final String pathDescription;

    public BusPath(long pathId, String pathDescription) {
        this.pathId = pathId;
        this.pathDescription = pathDescription;
    }

    public long getPathId() {
        return pathId;
    }

    public String getPathDescription() {
        return pathDescription;
    }
}
