package org.lightfish.business.monitoring.control.collectors;

import java.util.List;
import java.util.logging.Logger;
import org.lightfish.business.monitoring.entity.Application;
import org.lightfish.business.monitoring.entity.ConnectionPool;
import org.lightfish.business.monitoring.entity.Snapshot;

/**
 *
 * @author Rob Veldpaus
 */
public class DataPointToSnapshotMapper {
    private static final Logger LOG = Logger.getLogger(DataPointToSnapshotMapper.class.getName());
    
    public void mapDataPointToSnapshot(DataPoint dataPoint, Snapshot snapshot) {
        switch (dataPoint.getName()) {
            case "applications":
                LOG.finest("Applications mapped to snapshot");
                snapshot.setApps((List<Application>) dataPoint.getValue());
                break;
            case "busyThreadCount":
                snapshot.setCurrentThreadBusy((Integer) dataPoint.getValue());
                break;
            case "commitedTransactions":
                snapshot.setCommittedTX((Integer) dataPoint.getValue());
                break;
            case "expiredSessionCount":
                snapshot.setExpiredSessions((Integer) dataPoint.getValue());
                break;
            case "sessionCount":
                snapshot.setActiveSessions((Integer) dataPoint.getValue());
                break;
            case "rolledBackTransactions":
                snapshot.setRolledBackTX((Integer) dataPoint.getValue());
                break;
            case "resources":
                snapshot.setPools((List<ConnectionPool>) dataPoint.getValue());
                break;
            case "queuedConnectionCount":
                snapshot.setQueuedConnections((Integer) dataPoint.getValue());
                break;
            case "errorCount":
                snapshot.setTotalErrors((Integer) dataPoint.getValue());
                break;
            case "deadLockedThreads":
                snapshot.setDeadlockedThreads((String) dataPoint.getValue());
                break;
            case "usedHeap":
                snapshot.setUsedHeapSize((Long) dataPoint.getValue());
                break;
            case "threadCount":
                snapshot.setThreadCount((Integer) dataPoint.getValue());
                break;
            case "peakThreadCount":
                snapshot.setPeakThreadCount((Integer) dataPoint.getValue());
                break;
            default:
                LOG.info("Data Point " + dataPoint.getName() + " could not be mapped to Snapshot");
                break;
        }
    }
}
