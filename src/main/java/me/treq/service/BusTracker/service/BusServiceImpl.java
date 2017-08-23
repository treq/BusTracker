package me.treq.service.BusTracker.service;

import me.treq.service.BusTracker.dao.BusLocationDao;
import me.treq.service.BusTracker.dao.BusRouteDao;
import me.treq.service.BusTracker.model.Bus;
import me.treq.service.BusTracker.model.BusRoute;
import me.treq.service.BusTracker.model.Location;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Service("busService")
public class BusServiceImpl implements BusService {

    private final BusLocationDao busLocationDao;

    private final BusRouteDao busRouteDao;

    public BusServiceImpl(BusLocationDao busLocationDao, BusRouteDao busRouteDao) {
        this.busLocationDao = busLocationDao;
        this.busRouteDao = busRouteDao;
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

    @Override
    public BusRoute getRouteById(int routeId) {
        return this.busRouteDao.getRouteById(routeId);
    }

    @Override
    public List<BusRoute> getActiveBusRoutes() {
        Set<Integer> allRouteIds = this.busRouteDao.getAvailableRoutes();
        List<BusRoute> activeRoutes = new ArrayList<>();

        for (Integer routeId : allRouteIds) {
            BusRoute busRoute = this.busRouteDao.getRouteById(routeId);
            if (busRoute != null) {
                activeRoutes.add(busRoute);
            }
        }

        return activeRoutes;
    }
}
