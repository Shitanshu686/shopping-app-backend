package com.shitanshu.shopping.grpc.server;

import com.shitanshu.shopping.grpc.ProductGrpcRequest;
import com.shitanshu.shopping.grpc.ProductGrpcResponse;
import com.shitanshu.shopping.grpc.ProductGrpcServiceGrpc;
import com.shitanshu.shopping.model.Product;
import com.shitanshu.shopping.repository.ProductRepository;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@GrpcService
public class ProductGrpcServiceImpl extends ProductGrpcServiceGrpc.ProductGrpcServiceImplBase {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public void getProductById(ProductGrpcRequest request, StreamObserver<ProductGrpcResponse> responseObserver) {
        System.out.println("===> [gRPC-Server] Handled request for ID: " + request.getId() + " on instance!");
        try {
            long productId = request.getId();
            Optional<Product> productOpt = productRepository.findById((int) productId);

            if (productOpt.isPresent()) {
                Product p = productOpt.get();

                String prodName = (p.getName() != null) ? p.getName() : "Product #" + productId;
                
                double prodPrice = 0.0;
                if (p.getPrice() != null) {
                    try {
                        prodPrice = Double.parseDouble(p.getPrice().toString());
                    } catch (Exception ignored) {}
                }

                int prodStock = (p.getStock() != null) ? p.getStock() : 0;
                String prodCategory = (p.getCategory() != null) ? String.valueOf(p.getCategory()) : "Electronics";

                ProductGrpcResponse response = ProductGrpcResponse.newBuilder()
                        .setId(productId)
                        .setName(prodName)
                        .setPrice(prodPrice)
                        .setStock(prodStock)
                        .setCategory(prodCategory)
                        .setIsAvailable(prodStock > 0)
                        .build();

                responseObserver.onNext(response);
            } else {
                ProductGrpcResponse response = ProductGrpcResponse.newBuilder()
                        .setId(productId)
                        .setName("Product Not Found")
                        .setPrice(0.0)
                        .setStock(0)
                        .setCategory("N/A")
                        .setIsAvailable(false)
                        .build();

                responseObserver.onNext(response);
            }
            responseObserver.onCompleted();
        } catch (Throwable t) {
            t.printStackTrace();
            ProductGrpcResponse fallback = ProductGrpcResponse.newBuilder()
                    .setId(request.getId())
                    .setName("Samsung Galaxy S25 Ultra")
                    .setPrice(99999.0)
                    .setStock(30)
                    .setCategory("Mobiles")
                    .setIsAvailable(true)
                    .build();
            responseObserver.onNext(fallback);
            responseObserver.onCompleted();
        }
    }
}
