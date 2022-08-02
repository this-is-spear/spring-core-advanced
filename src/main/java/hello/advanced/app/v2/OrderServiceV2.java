package hello.advanced.app.v2;

import hello.advanced.trace.TraceId;
import hello.advanced.trace.TraceStatus;
import hello.advanced.trace.hellotrace.HelloTraceV2;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceV2 {

    private final OrderRepositoryV2 orderRepositoryV2;
    private final HelloTraceV2 trace;

    public void orderItem(String itemId, TraceId traceId) {
        TraceStatus status = trace.beginSync(traceId, "OrderService.orderItem()");
        try {
            orderRepositoryV2.save(itemId, status.getTraceId());
            trace.end(status);
        } catch (Exception e) {
            trace.exception(status, e);
            throw e;
        }
    }

}
