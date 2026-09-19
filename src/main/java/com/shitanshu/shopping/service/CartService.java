package com.shitanshu.shopping.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shitanshu.shopping.dto.AddToCartRequestDTO;
import com.shitanshu.shopping.dto.CartItemResponseDTO;
import com.shitanshu.shopping.dto.CartResponseDTO;
import com.shitanshu.shopping.dto.UpdateCartItemDTO;
import com.shitanshu.shopping.exception.ProductNotFoundException;

import com.shitanshu.shopping.exception.InsufficientStockException;
import com.shitanshu.shopping.exception.UserNotFoundException;
import com.shitanshu.shopping.exception.CartItemNotFoundException;
import com.shitanshu.shopping.exception.CartItemNotBelongToUserException;
import com.shitanshu.shopping.model.Cart;
import com.shitanshu.shopping.model.CartItem;
import com.shitanshu.shopping.model.Product;
import com.shitanshu.shopping.model.User;
import com.shitanshu.shopping.repository.CartItemRepository;
import com.shitanshu.shopping.repository.CartRepository;
import com.shitanshu.shopping.repository.ProductRepository;
import com.shitanshu.shopping.repository.UserRepository;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;


    // =========================
    // GET OR CREATE CART
    // =========================

    private Cart getOrCreateCart(String email) {

    	User user = userRepository.findByEmail(email)
    	        .orElseThrow(() ->
    	                new UserNotFoundException("User not found"));

        return cartRepository.findByUser(user)
                .orElseGet(() -> {

                    Cart cart = new Cart();

                    cart.setUser(user);

                    return cartRepository.save(cart);
                });
    }


    // =========================
    // ADD TO CART
    // =========================

    public CartResponseDTO addToCart(
            String email,
            AddToCartRequestDTO request) {

        Cart cart = getOrCreateCart(email);

        Product product = productRepository
                .findById(request.getProductId())
                .orElseThrow(() ->
                        new ProductNotFoundException(
                                "Product with ID "
                                + request.getProductId()
                                + " not found"));

        if (product.getStock() < request.getQuantity()) {

            throw new InsufficientStockException(
                "Insufficient stock for product '" + product.getName() + "'"
            );

        }

        CartItem cartItem =
                cartItemRepository
                        .findByCartAndProduct(cart, product)
                        .orElse(null);

        boolean isFlash = Boolean.TRUE.equals(request.getFlashSale());
        double origPrice = product.getPrice() != null ? product.getPrice() : 0.0;
        double sPrice = isFlash ? Math.round(origPrice * 0.90 * 100.0) / 100.0 : origPrice;

        if (cartItem != null) {

            int newQuantity =
                    cartItem.getQuantity()
                    + request.getQuantity();

            if (newQuantity > product.getStock()) {

                throw new InsufficientStockException(
                        "Insufficient stock for product '" + product.getName() + "'"
                );

            }

            cartItem.setQuantity(newQuantity);
            if (isFlash) {
                cartItem.setFlashSale(true);
                cartItem.setOriginalPrice(origPrice);
                cartItem.setSalePrice(sPrice);
                cartItem.setDiscountPercent(10);
            }

        } else {

            cartItem = new CartItem();

            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(request.getQuantity());
            cartItem.setFlashSale(isFlash);
            cartItem.setOriginalPrice(isFlash ? origPrice : null);
            cartItem.setSalePrice(isFlash ? sPrice : null);
            cartItem.setDiscountPercent(isFlash ? 10 : null);

        }

        cartItemRepository.save(cartItem);

        return buildCartResponse(cart);
    }


    // =========================
    // GET CART
    // =========================

    public CartResponseDTO getCart(String email) {

        Cart cart = getOrCreateCart(email);

        return buildCartResponse(cart);
    }


    // =========================
    // UPDATE QUANTITY
    // =========================

    public CartResponseDTO updateQuantity(
            String email,
            Integer itemId,
            UpdateCartItemDTO request) {

        Cart cart = getOrCreateCart(email);

        CartItem cartItem =
                cartItemRepository.findById(itemId)
                        .orElseThrow(() ->
                                new CartItemNotFoundException(
                                        "Cart item not found"));

        if (!cartItem.getCart()
                .getId()
                .equals(cart.getId())) {

            throw new CartItemNotBelongToUserException(
                    "Cart item does not belong to this user");
        }

        Product product = cartItem.getProduct();

        if (request.getQuantity() > product.getStock()) {

            throw new InsufficientStockException(
                    "Insufficient stock for product '" + product.getName() + "'");

        }

        cartItem.setQuantity(request.getQuantity());

        cartItemRepository.save(cartItem);

        return buildCartResponse(cart);
    }


    // =========================
    // REMOVE ITEM
    // =========================

    public CartResponseDTO removeItem(
            String email,
            Integer itemId) {

        Cart cart = getOrCreateCart(email);

        CartItem cartItem =
                cartItemRepository.findById(itemId)
                        .orElseThrow(() ->
                                new CartItemNotFoundException(
                                        "Cart item not found"));

        if (!cartItem.getCart()
                .getId()
                .equals(cart.getId())) {

            throw new CartItemNotBelongToUserException(
                    "Cart item does not belong to this user");

        }

        cartItemRepository.delete(cartItem);

        return buildCartResponse(cart);
    }


    // =========================
    // BUILD CART RESPONSE
    // =========================

    private CartResponseDTO buildCartResponse(Cart cart) {

        List<CartItem> cartItems =
                cartItemRepository.findAll()
                        .stream()
                        .filter(item ->
                                item.getCart()
                                    .getId()
                                    .equals(cart.getId()))
                        .toList();

        List<CartItemResponseDTO> responseItems =
                new ArrayList<>();

        double total = 0;
        int totalItems = 0;

        for (CartItem item : cartItems) {

            Product product = item.getProduct();

            boolean isFlash = Boolean.TRUE.equals(item.getFlashSale());
            double unitPrice = (isFlash && item.getSalePrice() != null)
                    ? item.getSalePrice()
                    : (product.getPrice() != null ? product.getPrice() : 0.0);

            double subtotal = unitPrice * item.getQuantity();

            CartItemResponseDTO response =
                    new CartItemResponseDTO();

            response.setId(item.getId());
            response.setProductId(product.getId());
            response.setProductName(product.getName());
            response.setImage(product.getImage());
            response.setPrice(unitPrice);
            response.setOriginalPrice(isFlash ? item.getOriginalPrice() : null);
            response.setFlashSale(isFlash);
            response.setDiscountPercent(item.getDiscountPercent());
            response.setQuantity(item.getQuantity());
            response.setSubtotal(subtotal);

            responseItems.add(response);

            total += subtotal;

            totalItems += item.getQuantity();
        }

        CartResponseDTO response =
                new CartResponseDTO();

        response.setCartId(cart.getId());
        response.setItems(responseItems);
        response.setTotal(total);
        response.setTotalItems(totalItems);

        return response;
    }
}