package com.shitanshu.shopping.proxy;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/proxy")
public class ReverseProxyController {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String[] upstreamInstances = {
            "http://localhost:8080",
            "http://localhost:8081"
    };
    private final AtomicInteger counter = new AtomicInteger(0);

    @GetMapping("/product/{id}")
    public ResponseEntity<String> forwardProductRequest(@PathVariable String id, HttpServletRequest request) {
        // Reverse Proxy: Upstream instance select karna (Round-Robin via Proxy)
        int index = Math.abs(counter.getAndIncrement() % upstreamInstances.length);
        String targetHost = upstreamInstances[index];
        String targetUrl = targetHost + "/api/grpc/test/product/" + id;

        System.out.println("===> [REVERSE PROXY] Client IP: " + request.getRemoteAddr() 
                + " -> Forwarding to Upstream Target: " + targetUrl);

        // Forward Proxy / Reverse Proxy standard headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Forwarded-For", request.getRemoteAddr());
        headers.set("X-Forwarded-Host", request.getHeader("Host"));
        headers.set("X-Forwarded-Proto", request.getScheme());
        headers.set("X-Reverse-Proxy-By", "Shopease-Gateway-Proxy-v1");

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    URI.create(targetUrl),
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.putAll(response.getHeaders());
            responseHeaders.set("X-Upstream-Server", targetHost);
            responseHeaders.set("X-Proxy-Engine", "ReverseProxyController");

            return new ResponseEntity<>(response.getBody(), responseHeaders, response.getStatusCode());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                    .body("{\"error\": \"Bad Gateway: Upstream instance unreachable\", \"details\": \"" + e.getMessage() + "\"}");
        }
    }
}
