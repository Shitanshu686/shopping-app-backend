package com.shitanshu.paymentservice.controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.razorpay.RazorpayException;
import com.shitanshu.paymentservice.service.PaymentService;


@RestController
@RequestMapping("/payments")
@CrossOrigin(origins = "*")
public class PaymentController {


    @Autowired
    private PaymentService paymentService;


    // =========================================================
    // CREATE RAZORPAY ORDER
    // =========================================================

    @PostMapping("/create")
    public ResponseEntity<?> createPaymentOrder(
            @RequestParam Integer orderId,
            @RequestParam Double amount) {

        try {

            String razorpayOrderId =
                    paymentService.createRazorpayOrder(
                            amount,
                            orderId
                    );


            Map<String, Object> response =
                    new HashMap<>();


            response.put(
                    "success",
                    true
            );

            response.put(
                    "message",
                    "Razorpay order created successfully"
            );

            response.put(
                    "orderId",
                    orderId
            );

            response.put(
                    "razorpayOrderId",
                    razorpayOrderId
            );

            response.put(
                    "amount",
                    amount
            );

            response.put(
                    "currency",
                    "INR"
            );


            return ResponseEntity.ok(
                    response
            );

        }
        catch (RazorpayException e) {

            Map<String, Object> response =
                    new HashMap<>();

            response.put(
                    "success",
                    false
            );

            response.put(
                    "message",
                    "Failed to create Razorpay order"
            );


            return ResponseEntity
                    .internalServerError()
                    .body(response);
        }
    }


    // =========================================================
    // VERIFY PAYMENT
    // =========================================================

    @PostMapping("/verify")
    public ResponseEntity<?> verifyPayment(
            @RequestParam String razorpayOrderId,
            @RequestParam String razorpayPaymentId,
            @RequestParam String razorpaySignature) {

        try {

            boolean verified =
                    paymentService.verifyPayment(
                            razorpayOrderId,
                            razorpayPaymentId,
                            razorpaySignature
                    );


            Map<String, Object> response =
                    new HashMap<>();


            if (verified) {

                response.put(
                        "success",
                        true
                );

                response.put(
                        "message",
                        "Payment verified successfully"
                );

                response.put(
                        "razorpayOrderId",
                        razorpayOrderId
                );

                response.put(
                        "razorpayPaymentId",
                        razorpayPaymentId
                );


                return ResponseEntity.ok(
                        response
                );
            }


            response.put(
                    "success",
                    false
            );

            response.put(
                    "message",
                    "Payment verification failed"
            );


            return ResponseEntity
                    .badRequest()
                    .body(response);

        }
        catch (Exception e) {

            Map<String, Object> response =
                    new HashMap<>();

            response.put(
                    "success",
                    false
            );

            response.put(
                    "message",
                    e.getMessage()
            );


            return ResponseEntity
                    .badRequest()
                    .body(response);
        }
    }
 // =========================================================
 // MARK PAYMENT AS FAILED
 // =========================================================

 @PostMapping("/fail")
 public ResponseEntity<?> markPaymentAsFailed(
         @RequestParam String razorpayOrderId) {

     try {

         paymentService.markPaymentAsFailed(
                 razorpayOrderId
         );

         Map<String, Object> response =
                 new HashMap<>();

         response.put(
                 "success",
                 true
         );

         response.put(
                 "message",
                 "Payment marked as failed"
         );

         response.put(
                 "razorpayOrderId",
                 razorpayOrderId
         );

         return ResponseEntity.ok(
                 response
         );

     }
     catch (Exception e) {

         Map<String, Object> response =
                 new HashMap<>();

         response.put(
                 "success",
                 false
         );

         response.put(
                 "message",
                 e.getMessage()
         );

         return ResponseEntity
                 .badRequest()
                 .body(response);
     }
 }
//=========================================================
//RAZORPAY WEBHOOK
//=========================================================

@PostMapping("/webhook")
public ResponseEntity<?> handleWebhook(
      @RequestBody String payload,
      @RequestHeader("X-Razorpay-Signature") String signature,
      @RequestHeader(
              value = "x-razorpay-event-id",
              required = false
      ) String eventId) {

  try {

	  boolean verified =
		        paymentService.verifyWebhookSignature(
		                payload,
		                signature
		        );

		if (!verified) {
		    throw new SecurityException(
		            "Invalid webhook signature"
		    );
		}

		String eventType =
	            paymentService.getWebhookEventType(payload);
		if (eventId != null &&
		        paymentService.isWebhookAlreadyProcessed(eventId)) {

		    return ResponseEntity.ok(
		            Map.of(
		                    "success", true,
		                    "message", "Webhook already processed",
		                    "event", eventType
		            )
		    );
		}
		if (eventType.equals("payment.captured")) {

		    paymentService.handlePaymentCaptured(payload);

		} else if (eventType.equals("payment.failed")) {

		    paymentService.handlePaymentFailed(payload);
		}
		if (eventId != null) {

		    paymentService.saveWebhookEvent(
		            eventId,
		            eventType
		    );
		}

	    return ResponseEntity.ok(
	            Map.of(
	                    "success", true,
	                    "message", "Webhook signature verified",
	                    "event", eventType
	            )
	    );

  } catch (SecurityException e) {

      return ResponseEntity
              .status(401)
              .body(
                      Map.of(
                              "success", false,
                              "message", "Invalid webhook signature"
                      )
              );

  } catch (Exception e) {

      return ResponseEntity
              .badRequest()
              .body(
                      Map.of(
                              "success", false,
                              "message", e.getMessage()
                      )
              );
  }
}

}