package me.treq.service.BusTracker.service;

import me.treq.service.BusTracker.dao.BusLocationDao;
import me.treq.service.BusTracker.dao.BusRouteDao;
import me.treq.service.BusTracker.model.Bus;
import me.treq.service.BusTracker.model.BusRoute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class BusServiceImpl implements BusService {
    Logger log = LoggerFactory.getLogger(BusServiceImpl.class);

    private final Map<String, BusLocationDao> busLocationDaoByName;

    private final Map<String, BusRouteDao> busRouteDaoByName;

    public BusServiceImpl(Map<String, BusLocationDao> busLocationDaoByName, Map<String, BusRouteDao> busRouteDaoByName) {
        this.busLocationDaoByName = busLocationDaoByName;
        this.busRouteDaoByName = busRouteDaoByName;

        log.info("BusServiceImpl is initialized: {} {}", this.busLocationDaoByName, this.busRouteDaoByName);
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
    public BusRoute getRouteById(String system, String routeId) {
        return this.busRouteDaoByName.get(system).getRouteById(routeId);
    }

    @Override
    public List<BusRoute> getActiveBusRoutes(String system) {
        Set<String> allRouteIds = this.busRouteDaoByName.get(system).getAvailableRoutes();

        List<BusRoute> activeRoutes = new ArrayList<>();

        for (String routeId : allRouteIds) {
            BusRoute busRoute = this.busRouteDaoByName.get(system).getRouteById(routeId);
            if (busRoute != null) {
                activeRoutes.add(busRoute);
            }
        }

        return activeRoutes;
    }
}
