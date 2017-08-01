package me.treq.service.BusTracker.service;

import me.treq.service.BusTracker.dao.BusLocationDao;
import me.treq.service.BusTracker.model.Bus;
import me.treq.service.BusTracker.model.Location;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service("busService")
public class BusServiceImpl implements BusService {

    private final BusLocationDao busLocationDao;

    public BusServiceImpl(BusLocationDao busLocationDao) {
        this.busLocationDao = busLocationDao;
    }

    @Override
    public Collection<Bus> getBuses(int routeId) {
        return this.busLocationDao.getBuses(routeId);
    }

    @Override
    public Bus getBus(long busId) {
        return null;
    }

    @Override
    public Location getCentralGeoLocation(int routeId) {
        return this.busLocationDao.getCentralGeoLocation(routeId);
    }
}
