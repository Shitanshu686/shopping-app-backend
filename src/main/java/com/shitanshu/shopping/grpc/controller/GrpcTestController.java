package com.shitanshu.shopping.grpc.controller;

import com.shitanshu.shopping.grpc.ProductGrpcResponse;
import com.shitanshu.shopping.grpc.client.ProductGrpcClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/grpc/test")
public class GrpcTestController {

    @Autowired
    private ProductGrpcClient grpcClient;

    @GetMapping("/product/{id}")
    public Map<String, Object> testGrpcCall(@PathVariable long id) {
        // Internal gRPC call over port 9090 (HTTP/2 binary)
        ProductGrpcResponse res = grpcClient.getProduct(id);

        Map<String, Object> result = new HashMap<>();
        result.put("protocol", "gRPC / Protobuf / HTTP/2");
        result.put("id", res.getId());
        result.put("name", res.getName());
        result.put("price", res.getPrice());
        result.put("stock", res.getStock());
        result.put("category", res.getCategory());
        result.put("isAvailable", res.getIsAvailable());

        return result;
    }
}
