package com.shitanshu.shopping.grpc.client;

import com.shitanshu.shopping.grpc.ProductGrpcRequest;
import com.shitanshu.shopping.grpc.ProductGrpcResponse;
import com.shitanshu.shopping.grpc.ProductGrpcServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Service
public class ProductGrpcClient {

    @GrpcClient("product-service")
    private ProductGrpcServiceGrpc.ProductGrpcServiceBlockingStub productBlockingStub;

    /**
     * Unary RPC: Sends single ProductGrpcRequest and receives single ProductGrpcResponse
     */
    public ProductGrpcResponse getProduct(long id) {
        ProductGrpcRequest request = ProductGrpcRequest.newBuilder()
                .setId(id)
                .build();

        return productBlockingStub.getProductById(request);
    }
}
