package coderead.nio.metric;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class TimeTest {
    public String responseName = null;
    public ConsoleReporter consoleReporter = null;
    public static final MetricRegistry metrics = new MetricRegistry();
    @Rule
    public TestRule testRule = new TestWatcher(){
        @Override
        protected void starting(Description description) {

            responseName = description.getMethodName();
            responses = metrics.timer(MetricRegistry.name(this.getClass(), responseName));
            startReport();
            time = responses.time();
        }

        @Override
        protected void succeeded(Description description) {
            if(time != null) {
                time.stop();
            }
            if(consoleReporter != null) {
                consoleReporter.report();
                consoleReporter.stop();
                consoleReporter = null;
            }

        }
    };

    public Timer responses = null;
    protected Timer.Context time = null;

    void startReport(){
        consoleReporter = ConsoleReporter.forRegistry(metrics)
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.NANOSECONDS)
                .build();
//        consoleReporter.start(1, TimeUnit.SECONDS);
//        consoleReporter.report();
    }

//    @After
//    public void stop() throws InterruptedException {
//        if(time != null) {
//            time.stop();
//        }
//        if(consoleReporter != null) {
//            consoleReporter.stop();
//        }
//        TimeUnit.SECONDS.sleep(1);
//    }
}
