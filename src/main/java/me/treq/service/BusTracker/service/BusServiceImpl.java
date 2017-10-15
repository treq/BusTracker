package me.treq.service.BusTracker.service;

import me.treq.service.BusTracker.dao.BusLocationDao;
import me.treq.service.BusTracker.dao.BusRouteDao;
import me.treq.service.BusTracker.model.Bus;
import me.treq.service.BusTracker.model.BusRoute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BusServiceImpl implements BusService {
    Logger log = LoggerFactory.getLogger(BusServiceImpl.class);

    private final Map<String, BusLocationDao> busLocationDaoByName;

    private final BusRouteDao busRouteDao;

    @Autowired
    public BusServiceImpl(Map<String, BusLocationDao> busLocationDaoByName, BusRouteDao busRouteDao) {
        this.busLocationDaoByName = busLocationDaoByName;
        this.busRouteDao = busRouteDao;
    }

    @Override
    public Collection<Bus> getBuses(String busSystem, String routeId) {
        BusLocationDao dao = this.busLocationDaoByName.get(busSystem);
        if (dao == null) {
            log.warn("Bus system not supported {}", busSystem);
        }
        return dao.getBuses(routeId);
    }

    @Override
    public BusRoute getRouteById(String routeId) {
        return this.busRouteDao.getRouteById(routeId);
    }

    @Override
    public List<BusRoute> getActiveBusRoutes() {
        Set<String> allRouteIds = this.busRouteDao.getAvailableRoutes();
        List<BusRoute> activeRoutes = new ArrayList<>();

        for (String routeId : allRouteIds) {
            BusRoute busRoute = this.busRouteDao.getRouteById(routeId);
            if (busRoute != null) {
                activeRoutes.add(busRoute);
            }
        }

        return activeRoutes;
    }
}
