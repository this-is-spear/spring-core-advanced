package hello.advanced.trace.hellotrace;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import hello.advanced.trace.TraceStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class HelloTraceV2Test {
    Logger log;
    ListAppender<ILoggingEvent> listAppender;

    @BeforeEach
    void setUp() {
        log = (Logger) LoggerFactory.getLogger(HelloTraceV2.class);
        listAppender = new ListAppender<>();
        listAppender.start();
        log.addAppender(listAppender);
    }

    @Test
    void beginEnd() {
        // given
        HelloTraceV2 trace = new HelloTraceV2();

        // when
        TraceStatus status1 = trace.begin("hello1");
        TraceStatus status2 = trace.beginSync(status1.getTraceId(), "hello2");
        trace.end(status2);
        trace.end(status1);

        // then
        String traceId = status1.getTraceId().getId();

        assertAll(
            () -> assertThat(listAppender.list.get(0).getFormattedMessage()).contains(String.format("[%s] hello1", traceId)),
            () -> assertThat(listAppender.list.get(1).getFormattedMessage()).contains(String.format("[%s] |-->hello2", traceId)),
            () -> assertThat(listAppender.list.get(2).getFormattedMessage()).contains(String.format("[%s] |<--hello2", traceId)),
            () -> assertThat(listAppender.list.get(3).getFormattedMessage()).contains(String.format("[%s] hello1", traceId))
        );
    }

    @Test
    void beginException() {
        // given
        HelloTraceV2 trace = new HelloTraceV2();

        // when
        TraceStatus status1 = trace.begin("hello1");
        TraceStatus status2 = trace.beginSync(status1.getTraceId(), "hello2");
        trace.exception(status2, new IllegalArgumentException());
        trace.exception(status1, new IllegalArgumentException());

        // then
        String traceId = status1.getTraceId().getId();

        assertAll(
            () -> assertThat(listAppender.list.get(0).getFormattedMessage()).contains(String.format("[%s] hello1", traceId)),
            () -> assertThat(listAppender.list.get(1).getFormattedMessage()).contains(String.format("[%s] |-->hello2", traceId)),
            () -> assertThat(listAppender.list.get(2).getFormattedMessage()).contains(String.format("[%s] |<X-hello2", traceId)),
            () -> assertThat(listAppender.list.get(3).getFormattedMessage()).contains(String.format("[%s] hello1", traceId))
        );
    }
}
