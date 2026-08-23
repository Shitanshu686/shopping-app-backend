package com.shitanshu.shopping.service;

import java.time.LocalDateTime;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;

import com.shitanshu.shopping.model.Cart;
import com.shitanshu.shopping.model.CartItem;
import com.shitanshu.shopping.model.Order;
import com.shitanshu.shopping.model.OrderStatus;
import com.shitanshu.shopping.model.Payment;
import com.shitanshu.shopping.model.PaymentStatus;

import com.shitanshu.shopping.repository.CartItemRepository;
import com.shitanshu.shopping.repository.CartRepository;
import com.shitanshu.shopping.repository.OrderRepository;
import com.shitanshu.shopping.repository.PaymentRepository;


@Service
public class PaymentService {


    @Autowired
    private RazorpayClient razorpayClient;


    @Autowired
    private OrderRepository orderRepository;


    @Autowired
    private PaymentRepository paymentRepository;


    @Autowired
    private CartItemRepository cartItemRepository;


    @Autowired
    private CartRepository cartRepository;


    // =========================================================
    // CREATE RAZORPAY ORDER
    // =========================================================

    public String createRazorpayOrder(
            Double amount,
            Integer orderId)
            throws RazorpayException {


        // =========================
        // FIND SHOP EASE ORDER
        // =========================

        Order order =
                orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Order not found"
                        )
                );


        // =========================
        // VALIDATE AMOUNT
        // =========================

        if (!order.getTotalAmount().equals(amount)) {

            throw new RuntimeException(
                    "Payment amount does not match order amount"
            );

        }


        // =========================
        // CHECK DUPLICATE PAYMENT
        // =========================

        if (paymentRepository
                .findByOrder(order)
                .isPresent()) {

            throw new RuntimeException(
                    "Payment already created for this order"
            );

        }


        // =========================
        // CONVERT TO PAISE
        // =========================

        int amountInPaise =
                (int) Math.round(
                        amount * 100
                );


        // =========================
        // CREATE RAZORPAY REQUEST
        // =========================

        JSONObject orderRequest =
                new JSONObject();

        orderRequest.put(
                "amount",
                amountInPaise
        );

        orderRequest.put(
                "currency",
                "INR"
        );

        orderRequest.put(
                "receipt",
                "order_" + orderId
        );


        // =========================
        // CREATE RAZORPAY ORDER
        // =========================

        com.razorpay.Order razorpayOrder =
                razorpayClient.orders.create(
                        orderRequest
                );


        String razorpayOrderId =
                razorpayOrder.get("id");


        // =========================
        // SAVE PAYMENT
        // =========================

        Payment payment =
                new Payment();

        payment.setOrder(
                order
        );

        payment.setRazorpayOrderId(
                razorpayOrderId
        );

        payment.setAmount(
                amount
        );

        payment.setStatus(
                PaymentStatus.CREATED
        );

        payment.setCreatedAt(
                LocalDateTime.now()
        );


        paymentRepository.save(
                payment
        );


        return razorpayOrderId;
    }


    // =========================================================
    // VERIFY PAYMENT
    // =========================================================

    public boolean verifyPayment(
            String razorpayOrderId,
            String razorpayPaymentId,
            String razorpaySignature) {


        // =========================
        // FIND PAYMENT
        // =========================

        Payment payment =
                paymentRepository
                .findByRazorpayOrderId(
                        razorpayOrderId
                )
                .orElseThrow(() ->
                        new RuntimeException(
                                "Payment record not found"
                        )
                );


        // =========================
        // CREATE SIGNATURE PAYLOAD
        // =========================

        String payload =
                razorpayOrderId
                + "|"
                + razorpayPaymentId;


        // =========================
        // VERIFY SIGNATURE
        // =========================

        boolean isValid;

        try {

            isValid =
                    Utils.verifySignature(
                            payload,
                            razorpaySignature,
                            System.getenv(
                                    "RAZORPAY_KEY_SECRET"
                            )
                    );

        }
        catch (RazorpayException e) {

            throw new RuntimeException(
                    "Payment signature verification failed",
                    e
            );

        }


        // =========================
        // PAYMENT FAILED
        // =========================

        if (!isValid) {

            payment.setStatus(
                    PaymentStatus.FAILED
            );

            paymentRepository.save(
                    payment
            );

            return false;
        }


        // =========================
        // PAYMENT SUCCESS
        // =========================

        payment.setRazorpayPaymentId(
                razorpayPaymentId
        );

        payment.setStatus(
                PaymentStatus.SUCCESS
        );

        paymentRepository.save(
                payment
        );


        // =========================
        // CONFIRM ORDER
        // =========================

        Order order =
                payment.getOrder();

        order.setStatus(
                OrderStatus.CONFIRMED
        );

        orderRepository.save(
                order
        );


        // =========================
        // CLEAR CART AFTER PAYMENT SUCCESS
        // =========================

        Cart cart =
                cartRepository
                .findByUser(
                        order.getUser()
                )
                .orElse(null);


        if (cart != null) {

            List<CartItem> cartItems =
                    cartItemRepository
                    .findByCart(
                            cart
                    );

            cartItemRepository.deleteAll(
                    cartItems
            );
        }


        return true;
    }


    // =========================================================
    // MARK PAYMENT AS FAILED
    // =========================================================

    public void markPaymentAsFailed(
            String razorpayOrderId) {


        // =========================
        // FIND PAYMENT
        // =========================

        Payment payment =
                paymentRepository
                .findByRazorpayOrderId(
                        razorpayOrderId
                )
                .orElseThrow(() ->
                        new RuntimeException(
                                "Payment record not found"
                        )
                );


        // =========================
        // UPDATE PAYMENT STATUS
        // =========================

        payment.setStatus(
                PaymentStatus.FAILED
        );


        // =========================
        // SAVE PAYMENT
        // =========================

        paymentRepository.save(
                payment
        );
    }

}