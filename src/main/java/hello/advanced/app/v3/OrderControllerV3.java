package hello.advanced.app.v3;

import hello.advanced.app.v2.OrderServiceV2;
import hello.advanced.trace.TraceStatus;
import hello.advanced.trace.hellotrace.HelloTraceV2;
import hello.advanced.trace.logtrace.LogTrace;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderControllerV3 {
    private final OrderServiceV3 orderService;
    private final LogTrace trace;

    @GetMapping("/v3/request")
    public ResponseEntity<Void> request(@RequestParam String itemId) {
        TraceStatus status = trace.begin("OrderController.request()");
        try {
            orderService.orderItem(itemId);
            trace.end(status);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            trace.exception(status, e);
            throw e;
        }
    }
}
