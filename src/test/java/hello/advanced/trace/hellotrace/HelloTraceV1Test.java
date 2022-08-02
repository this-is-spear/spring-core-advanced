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

class HelloTraceV1Test {
    private Logger log;
    private ListAppender<ILoggingEvent> listAppender;

    @BeforeEach
    void setUp() {
        log = (Logger) LoggerFactory.getLogger(HelloTraceV1.class);
        listAppender = new ListAppender<>();
        listAppender.start();
        log.addAppender(listAppender);
    }

    @Test
    void beginEnd() {
        // given
        HelloTraceV1 trace = new HelloTraceV1();

        // when
        TraceStatus status = trace.begin("hello");
        trace.end(status);

        // then
        String traceId = status.getTraceId().getId();

        assertAll(
            () -> assertThat(listAppender.list.get(0).getFormattedMessage()).contains(String.format("[%s] hello", traceId)),
            () -> assertThat(listAppender.list.get(1).getFormattedMessage()).contains(String.format("[%s] hello", traceId))
        );
    }

    @Test
    void beginException() {
        // given
        HelloTraceV1 trace = new HelloTraceV1();

        // when
        TraceStatus status = trace.begin("hello");
        trace.exception(status, new IllegalArgumentException());

        // then
        assertAll(
            () -> assertThat(listAppender.list.get(0).getFormattedMessage()).contains(String.format("[%s] hello", status.getTraceId().getId())),
            () -> assertThat(listAppender.list.get(1).getFormattedMessage()).contains(String.format("[%s] hello", status.getTraceId().getId()))
        );
    }
}
