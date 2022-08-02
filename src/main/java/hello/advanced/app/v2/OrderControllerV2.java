package hello.advanced.app.v2;

import hello.advanced.trace.TraceStatus;
import hello.advanced.trace.hellotrace.HelloTraceV2;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderControllerV2 {
    private final OrderServiceV2 orderServiceV2;
    private final HelloTraceV2 trace;

    @GetMapping("/v2/request")
    public ResponseEntity<Void> request(@RequestParam String itemId) {
        TraceStatus status = trace.begin("OrderController.request()");
        try {
            orderServiceV2.orderItem(itemId, status.getTraceId());
            trace.end(status);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            trace.exception(status, e);
            throw e;
        }
    }
}
