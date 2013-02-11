package org.lightfish.business.monitoring.control.collectors.jvm;

import org.lightfish.business.monitoring.control.collectors.AbstractRestDataCollector;
import org.lightfish.business.monitoring.control.collectors.DataPoint;
import org.lightfish.business.SnapshotData;

/**
 *
 * @author Rob Veldpaus
 */
@SnapshotData
public class ThreadCountCollector extends AbstractRestDataCollector<Integer> {
    
    public static final String THREAD_COUNT_URI = "jvm/thread-system/threadcount";

    @Override
    public DataPoint<Integer> collect() throws Exception{
        return new DataPoint<>("threadCount",getInt(THREAD_COUNT_URI, "threadcount"));
    }
    
}
