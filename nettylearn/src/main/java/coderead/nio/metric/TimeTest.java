package coderead.nio.metric;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import org.junit.After;
import org.junit.Before;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class TimeTest {

    public static final MetricRegistry metrics = new MetricRegistry();

    public Timer responses = metrics.timer(MetricRegistry.name(this.getClass(), "responses"));

    Timer.Context time = null;
    @Before
    public void start(){
        startReport();
        time = responses.time();
    }
    static void startReport(){
        ConsoleReporter consoleReporter = ConsoleReporter.forRegistry(metrics)
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .build();
        consoleReporter.start(1, TimeUnit.SECONDS);
    }

    @After
    public void stop() throws InterruptedException {
        if(time != null) {
            time.stop();
        }
        TimeUnit.SECONDS.sleep(10);
    }
}
