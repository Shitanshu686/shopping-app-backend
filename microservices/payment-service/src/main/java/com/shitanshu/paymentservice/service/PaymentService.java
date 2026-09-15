package com.shitanshu.paymentservice.service;
import org.springframework.beans.factory.annotation.Value;
import java.time.LocalDateTime;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;

import com.shitanshu.paymentservice.model.Cart;
import com.shitanshu.paymentservice.model.CartItem;
import com.shitanshu.paymentservice.model.Order;
import com.shitanshu.paymentservice.model.OrderStatus;
import com.shitanshu.paymentservice.model.Payment;
import com.shitanshu.paymentservice.model.PaymentStatus;

import com.shitanshu.paymentservice.repository.CartItemRepository;
import com.shitanshu.paymentservice.repository.CartRepository;
import com.shitanshu.paymentservice.repository.OrderRepository;
import com.shitanshu.paymentservice.repository.PaymentRepository;
import com.shitanshu.paymentservice.repository.WebhookEventRepository;
import com.shitanshu.paymentservice.repository.WebhookEventRepository;
import com.shitanshu.paymentservice.model.WebhookEvent;
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
    
    @Autowired
    private WebhookEventRepository webhookEventRepository;
    
    @Value("${razorpay.webhook.secret}")
    private String webhookSecret;


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
 // =========================================================
 // RAZORPAY WEBHOOK - SIGNATURE VERIFICATION
 // =========================================================

 public boolean verifyWebhookSignature(
         String payload,
         String signature) {

     try {

         return Utils.verifyWebhookSignature(
                 payload,
                 signature,
                 webhookSecret
         );

     } catch (RazorpayException e) {

         throw new RuntimeException(
                 "Webhook signature verification failed",
                 e
         );
     }
 }
 public String getWebhookEventType(String payload) {

	    JSONObject webhook =
	            new JSONObject(payload);

	    return webhook.getString("event");
	}
 public void handlePaymentCaptured(String payload) {

	    JSONObject webhook =
	            new JSONObject(payload);

	    JSONObject paymentEntity =
	            webhook
	                    .getJSONObject("payload")
	                    .getJSONObject("payment")
	                    .getJSONObject("entity");

	    String razorpayPaymentId =
	            paymentEntity.getString("id");

	    String razorpayOrderId =
	            paymentEntity.getString("order_id");

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

	    payment.setRazorpayPaymentId(
	            razorpayPaymentId
	    );

	    payment.setStatus(
	            PaymentStatus.SUCCESS
	    );

	    paymentRepository.save(payment);
	}
 public void handlePaymentFailed(String payload) {

	    JSONObject webhook =
	            new JSONObject(payload);

	    JSONObject paymentEntity =
	            webhook
	                    .getJSONObject("payload")
	                    .getJSONObject("payment")
	                    .getJSONObject("entity");

	    String razorpayPaymentId =
	            paymentEntity.getString("id");

	    String razorpayOrderId =
	            paymentEntity.getString("order_id");

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

	    payment.setRazorpayPaymentId(
	            razorpayPaymentId
	    );

	    payment.setStatus(
	            PaymentStatus.FAILED
	    );

	    paymentRepository.save(payment);
	}
 public boolean isWebhookAlreadyProcessed(String eventId) {

	    return webhookEventRepository
	            .findByEventId(eventId)
	            .isPresent();
	}
 public void saveWebhookEvent(
	        String eventId,
	        String eventType) {

	    WebhookEvent webhookEvent =
	            new WebhookEvent();

	    webhookEvent.setEventId(eventId);

	    webhookEvent.setEventType(eventType);

	    webhookEvent.setProcessedAt(
	            LocalDateTime.now()
	    );

	    webhookEventRepository.save(
	            webhookEvent
	    );
	}

}