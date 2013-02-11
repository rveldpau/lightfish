/*
Copyright 2012 Adam Bien, adam-bien.com

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package org.lightfish.business.monitoring.boundary;

import java.util.Date;
import org.lightfish.business.monitoring.control.SnapshotProvider;
import org.lightfish.business.monitoring.entity.Snapshot;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.*;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Adam Bien, blog.adam-bien.com
 */

@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
@Path("snapshots")
@Produces(MediaType.APPLICATION_JSON)
public class MonitoringController {
    
    @Inject
    private Logger LOG;
    
    @Inject
    SnapshotProvider dataProvider;
    
    @PersistenceContext
    EntityManager em;

    @Inject @Severity(Severity.Level.HEARTBEAT)
    Event<Snapshot> heartBeat;

    @Inject MonitoringAsyncGatherer gatherer;
    
    @Resource
    TimerService timerService;

    private Timer timer;
    
    private Date lastTimeStarted = new Date();
    
    @Inject
    private Instance<Integer> interval;
    
    public void startTimer(){
        ScheduleExpression expression = new ScheduleExpression();
        expression.minute("*").second("*/"+interval.get()).hour("*");
        this.timer = this.timerService.createCalendarTimer(expression);
    }


    @Timeout 
    public void gatherAndPersist(){
        Date startTime = new Date();
        long timeDifference = startTime.getTime() - lastTimeStarted.getTime();
        LOG.fine("Elapsed time between starts = " + timeDifference);
        lastTimeStarted = startTime;
        gatherer.gatherAndPersist();
    }
    
    @GET
    public List<Snapshot> all(){
        CriteriaBuilder cb = this.em.getCriteriaBuilder();
        CriteriaQuery q = cb.createQuery();
        CriteriaQuery<Snapshot> select = q.select(q.from(Snapshot.class));
        return this.em.createQuery(select).getResultList();
        
    }
    
    @PreDestroy
    public void stopTimer(){
        if(timer != null){
            try{
                this.timer.cancel();
            }catch(Exception e){
                LOG.log(Level.WARNING, "Cannot cancel timer " + this.timer, e);
            }finally{
                this.timer  = null;
            }
        }
    }

    public boolean isRunning() {
        return (this.timer != null);
    }
}
