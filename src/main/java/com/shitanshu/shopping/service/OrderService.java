package com.shitanshu.shopping.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shitanshu.shopping.dto.CreateOrderRequestDTO;
import com.shitanshu.shopping.dto.OrderItemResponseDTO;
import com.shitanshu.shopping.dto.OrderResponseDTO;
import com.shitanshu.shopping.dto.ShippingAddressDTO;
import com.shitanshu.shopping.dto.UpdateOrderStatusDTO;

import com.shitanshu.shopping.exception.CartItemNotFoundException;
import com.shitanshu.shopping.exception.InsufficientStockException;
import com.shitanshu.shopping.exception.UserNotFoundException;
import com.shitanshu.shopping.exception.OrderNotFoundException;
import com.shitanshu.shopping.exception.OrderNotBelongToUserException;
import com.shitanshu.shopping.exception.InvalidOrderStatusException;

import com.shitanshu.shopping.model.Cart;
import com.shitanshu.shopping.model.CartItem;
import com.shitanshu.shopping.model.Order;
import com.shitanshu.shopping.model.OrderItem;
import com.shitanshu.shopping.model.OrderStatus;
import com.shitanshu.shopping.model.Product;
import com.shitanshu.shopping.model.User;

import com.shitanshu.shopping.repository.CartItemRepository;
import com.shitanshu.shopping.repository.CartRepository;
import com.shitanshu.shopping.repository.OrderItemRepository;
import com.shitanshu.shopping.repository.OrderRepository;
import com.shitanshu.shopping.repository.UserRepository;


@Service
public class OrderService {


    @Autowired
    private UserRepository userRepository;


    @Autowired
    private CartRepository cartRepository;


    @Autowired
    private CartItemRepository cartItemRepository;


    @Autowired
    private OrderRepository orderRepository;


    @Autowired
    private OrderItemRepository orderItemRepository;


    // =====================================================
    // CREATE ORDER / CHECKOUT
    // =====================================================

    public OrderResponseDTO createOrder(
            String email,
            CreateOrderRequestDTO request) {


        // =========================
        // FIND USER
        // =========================

        User user =
                userRepository
                        .findByEmail(email)
                        .orElseThrow(() ->
                                new UserNotFoundException(
                                        "User not found"
                                ));


        // =========================
        // FIND CART
        // =========================

        Cart cart =
                cartRepository
                        .findByUser(user)
                        .orElseThrow(() ->
                                new CartItemNotFoundException(
                                        "Cart not found"
                                ));


        // =========================
        // FIND CART ITEMS
        // =========================

        List<CartItem> cartItems =
                cartItemRepository.findByCart(cart);


        // =========================
        // EMPTY CART CHECK
        // =========================

        if (cartItems.isEmpty()) {

            throw new CartItemNotFoundException(
                    "Cannot place order with an empty cart"
            );
        }


        // =========================
        // VALIDATE STOCK
        // =========================

        validateStock(cartItems);


        // =========================
        // CREATE ORDER
        // =========================

        Order order =
                buildOrder(
                        user,
                        request,
                        cartItems
                );


        Order savedOrder =
                orderRepository.save(order);


        // =========================
        // CREATE ORDER ITEMS
        // =========================

        createOrderItems(
                savedOrder,
                cartItems
        );


        // =========================
        // CLEAR CART
        // =========================

        //cartItemRepository.deleteAll(cartItems);


        // =========================
        // BUILD RESPONSE
        // =========================

        return buildOrderResponse(
                savedOrder
        );
    }


    // =====================================================
    // VALIDATE STOCK
    // =====================================================

    private void validateStock(
            List<CartItem> cartItems) {


        for (CartItem cartItem : cartItems) {

            Product product =
                    cartItem.getProduct();


            if (cartItem.getQuantity()
                    > product.getStock()) {

                throw new InsufficientStockException(
                        "Insufficient stock for product '"
                                + product.getName()
                                + "'"
                );
            }
        }
    }


    // =====================================================
    // BUILD ORDER
    // =====================================================

    private Order buildOrder(
            User user,
            CreateOrderRequestDTO request,
            List<CartItem> cartItems) {


        ShippingAddressDTO address =
                request.getShippingAddress();


        Order order =
                new Order();


        order.setUser(user);

        order.setFullName(
                address.getFullName()
        );

        order.setPhone(
                address.getPhone()
        );

        order.setAddress(
                address.getAddress()
        );

        order.setCity(
                address.getCity()
        );

        order.setState(
                address.getState()
        );

        order.setPincode(
                address.getPincode()
        );


        // =========================
        // CALCULATE TOTAL
        // =========================

        double total = 0;


        for (CartItem cartItem : cartItems) {

            Product product =
                    cartItem.getProduct();


            double subtotal =
                    product.getPrice()
                    * cartItem.getQuantity();


            total += subtotal;
        }


        order.setTotalAmount(total);


        // =========================
        // INITIAL STATUS
        // =========================

        order.setStatus(
                OrderStatus.PENDING
        );


        order.setCreatedAt(
                LocalDateTime.now()
        );


        return order;
    }


    // =====================================================
    // CREATE ORDER ITEMS
    // =====================================================

    private void createOrderItems(
            Order order,
            List<CartItem> cartItems) {


        List<OrderItem> orderItems =
                new ArrayList<>();


        for (CartItem cartItem : cartItems) {

            Product product =
                    cartItem.getProduct();


            OrderItem orderItem =
                    new OrderItem();


            orderItem.setOrder(order);


            // =========================
            // PRODUCT SNAPSHOT
            // =========================

            orderItem.setProductId(
                    product.getId()
            );

            orderItem.setProductName(
                    product.getName()
            );

            orderItem.setPrice(
                    product.getPrice()
            );


            orderItem.setQuantity(
                    cartItem.getQuantity()
            );


            double subtotal =
                    product.getPrice()
                    * cartItem.getQuantity();


            orderItem.setSubtotal(
                    subtotal
            );


            orderItems.add(orderItem);
        }


        orderItemRepository.saveAll(
                orderItems
        );
    }


    // =====================================================
    // GET USER ORDER HISTORY
    // =====================================================

    public List<OrderResponseDTO> getOrderHistory(
            String email) {


        User user =
                userRepository
                        .findByEmail(email)
                        .orElseThrow(() ->
                                new UserNotFoundException(
                                        "User not found"
                                ));


        List<Order> orders =
                orderRepository
                        .findByUserOrderByCreatedAtDesc(
                                user
                        );


        List<OrderResponseDTO> responses =
                new ArrayList<>();


        for (Order order : orders) {

            responses.add(
                    buildOrderResponse(order)
            );
        }


        return responses;
    }


    // =====================================================
    // GET SINGLE ORDER
    // =====================================================

    public OrderResponseDTO getOrderById(
            String email,
            Integer orderId) {


        User user =
                userRepository
                        .findByEmail(email)
                        .orElseThrow(() ->
                                new UserNotFoundException(
                                        "User not found"
                                ));


        Order order =
                orderRepository
                        .findById(orderId)
                        .orElseThrow(() ->
                                new OrderNotFoundException(
                                        "Order with ID "
                                        + orderId
                                        + " not found"
                                ));


        // =========================
        // USER OWNERSHIP CHECK
        // =========================

        if (!order.getUser()
                .getId()
                .equals(user.getId())) {

            throw new OrderNotBelongToUserException(
                    "Order does not belong to this user"
            );
        }


        return buildOrderResponse(order);
    }


    // =====================================================
    // BUILD ORDER RESPONSE
    // =====================================================

    private OrderResponseDTO buildOrderResponse(
            Order order) {


        List<OrderItem> orderItems =
                orderItemRepository
                        .findByOrder(order);


        List<OrderItemResponseDTO> responseItems =
                new ArrayList<>();


        for (OrderItem item : orderItems) {

            OrderItemResponseDTO response =
                    new OrderItemResponseDTO();


            response.setId(
                    item.getId()
            );

            response.setProductId(
                    item.getProductId()
            );

            response.setProductName(
                    item.getProductName()
            );

            response.setPrice(
                    item.getPrice()
            );

            response.setQuantity(
                    item.getQuantity()
            );

            response.setSubtotal(
                    item.getSubtotal()
            );


            responseItems.add(
                    response
            );
        }


        OrderResponseDTO response =
                new OrderResponseDTO();


        response.setOrderId(
                order.getId()
        );

        response.setFullName(
                order.getFullName()
        );

        response.setPhone(
                order.getPhone()
        );

        response.setAddress(
                order.getAddress()
        );

        response.setCity(
                order.getCity()
        );

        response.setState(
                order.getState()
        );

        response.setPincode(
                order.getPincode()
        );

        response.setTotalAmount(
                order.getTotalAmount()
        );

        response.setStatus(
                order.getStatus()
        );

        response.setCreatedAt(
                order.getCreatedAt()
        );

        response.setItems(
                responseItems
        );


        return response;
    }


    // =====================================================
    // UPDATE ORDER STATUS
    // =====================================================

    public OrderResponseDTO updateOrderStatus(
            Integer orderId,
            UpdateOrderStatusDTO request) {


        // =========================
        // FIND ORDER
        // =========================

        Order order =
                orderRepository
                        .findById(orderId)
                        .orElseThrow(() ->
                                new OrderNotFoundException(
                                        "Order with ID "
                                        + orderId
                                        + " not found"
                                ));


        // =========================
        // CURRENT STATUS
        // =========================

        OrderStatus currentStatus =
                order.getStatus();


        // =========================
        // NEW STATUS
        // =========================

        OrderStatus newStatus =
                request.getStatus();


        // =========================
        // VALIDATE STATUS TRANSITION
        // =========================

        validateStatusTransition(
                currentStatus,
                newStatus
        );


        // =========================
        // UPDATE STATUS
        // =========================

        order.setStatus(
                newStatus
        );


        // =========================
        // SAVE ORDER
        // =========================

        Order updatedOrder =
                orderRepository.save(order);


        // =========================
        // RETURN UPDATED ORDER
        // =========================

        return buildOrderResponse(
                updatedOrder
        );
    }


    // =====================================================
    // VALIDATE ORDER STATUS TRANSITION
    // =====================================================

    private void validateStatusTransition(
            OrderStatus currentStatus,
            OrderStatus newStatus) {


        // =========================
        // SAME STATUS
        // =========================

        if (currentStatus == newStatus) {

            throw new InvalidOrderStatusException(
                    "Order is already in "
                    + currentStatus
                    + " status"
            );
        }


        // =========================
        // PENDING
        // =========================

        if (currentStatus == OrderStatus.PENDING) {

            if (newStatus == OrderStatus.CONFIRMED ||
                newStatus == OrderStatus.CANCELLED) {

                return;
            }
        }


        // =========================
        // CONFIRMED
        // =========================

        if (currentStatus == OrderStatus.CONFIRMED) {

            if (newStatus == OrderStatus.SHIPPED ||
                newStatus == OrderStatus.CANCELLED) {

                return;
            }
        }


        // =========================
        // SHIPPED
        // =========================

        if (currentStatus == OrderStatus.SHIPPED) {

            if (newStatus == OrderStatus.DELIVERED) {

                return;
            }
        }


        // =========================
        // INVALID TRANSITION
        // =========================

        throw new InvalidOrderStatusException(
                "Invalid order status transition from "
                + currentStatus
                + " to "
                + newStatus
        );
    }

}