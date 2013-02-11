package org.lightfish.business.monitoring.control.collectors.jvm;

import org.lightfish.business.monitoring.control.collectors.AbstractRestDataCollector;
import org.lightfish.business.monitoring.control.collectors.DataPoint;
import org.lightfish.business.monitoring.control.collectors.SnapshotDataCollector;

/**
 *
 * @author Rob Veldpaus
 */
@SnapshotDataCollector
public class PeakThreadCountCollector extends AbstractRestDataCollector<Integer> {
    
    public static final String PEAK_THREAD_COUNT_URI = "jvm/thread-system/peakthreadcount";

    @Override
    public DataPoint<Integer> collect() throws Exception{
        return new DataPoint<>("peakThreadCount",getInt(PEAK_THREAD_COUNT_URI, "peakthreadcount"));
    }
    
}
