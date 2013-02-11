package org.lightfish.business.monitoring.boundary;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.lightfish.business.logging.Log;
import org.lightfish.business.monitoring.control.SnapshotProvider;
import org.lightfish.business.monitoring.entity.Snapshot;

/**
 *
 * @author rveldpau
 */
@Stateless
public class MonitoringAsyncGatherer {

    @Inject
    private Logger LOG;
    @Inject
    SnapshotProvider dataProvider;
    @PersistenceContext
    EntityManager em;
    @Inject
    @Severity(Severity.Level.HEARTBEAT)
    Event<Snapshot> heartBeat;

    @Asynchronous
    public void gatherAndPersist() {
        Snapshot current;
        try {
            current = dataProvider.fetchSnapshot();
        } catch (Exception ex) {
            LOG.log(Level.WARNING,"Could not retrieve snapshot", ex);
            return;
        }
        em.persist(current);
        try {
            heartBeat.fire(current);
        } catch (Exception e) {
            LOG.log(Level.WARNING,"Cannot fire heartbeat", e);
        }
        LOG.info(".");
    }
}
