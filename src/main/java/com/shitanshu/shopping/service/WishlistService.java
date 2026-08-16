package com.shitanshu.shopping.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shitanshu.shopping.dto.WishlistItemResponseDTO;
import com.shitanshu.shopping.dto.WishlistResponseDTO;
import com.shitanshu.shopping.exception.ProductNotFoundException;
import com.shitanshu.shopping.exception.WishlistItemNotBelongToUserException;
import com.shitanshu.shopping.exception.WishlistItemNotFoundException;
import com.shitanshu.shopping.model.Product;
import com.shitanshu.shopping.model.User;
import com.shitanshu.shopping.model.Wishlist;
import com.shitanshu.shopping.model.WishlistItem;
import com.shitanshu.shopping.repository.ProductRepository;
import com.shitanshu.shopping.repository.UserRepository;
import com.shitanshu.shopping.repository.WishlistItemRepository;
import com.shitanshu.shopping.repository.WishlistRepository;
import com.shitanshu.shopping.exception.WishlistAlreadyExistsException;
@Service
public class WishlistService {

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private WishlistItemRepository wishlistItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;


    // =========================
    // GET OR CREATE WISHLIST
    // =========================

    private Wishlist getOrCreateWishlist(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        return wishlistRepository.findByUser(user)
                .orElseGet(() -> {

                    Wishlist wishlist = new Wishlist();

                    wishlist.setUser(user);

                    return wishlistRepository.save(wishlist);
                });
    }


    // =========================
    // ADD TO WISHLIST
    // =========================

    public WishlistResponseDTO addToWishlist(
            String email,
            Integer productId) {

        Wishlist wishlist =
                getOrCreateWishlist(email);

        Product product =
                productRepository.findById(productId)
                        .orElseThrow(() ->
                                new ProductNotFoundException(
                                        "Product with ID "
                                        + productId
                                        + " not found"));


        // =========================
        // CHECK DUPLICATE
        // =========================

        WishlistItem existingItem =
                wishlistItemRepository
                        .findByWishlistAndProduct(
                                wishlist,
                                product)
                        .orElse(null);

        if (existingItem != null) {

        	throw new WishlistAlreadyExistsException(
        	        "Product already exists in wishlist");
        }


        // =========================
        // CREATE WISHLIST ITEM
        // =========================

        WishlistItem wishlistItem =
                new WishlistItem();

        wishlistItem.setWishlist(wishlist);

        wishlistItem.setProduct(product);

        wishlistItemRepository.save(wishlistItem);


        return buildWishlistResponse(wishlist);
    }


    // =========================
    // VIEW WISHLIST
    // =========================

    public WishlistResponseDTO getWishlist(
            String email) {

        Wishlist wishlist =
                getOrCreateWishlist(email);

        return buildWishlistResponse(wishlist);
    }


    // =========================
    // REMOVE FROM WISHLIST
    // =========================

    public WishlistResponseDTO removeFromWishlist(
            String email,
            Integer itemId) {

        Wishlist wishlist =
                getOrCreateWishlist(email);

        WishlistItem wishlistItem =
                wishlistItemRepository.findById(itemId)
                        .orElseThrow(() ->
                                new WishlistItemNotFoundException(
                                        "Wishlist item not found"));


        // =========================
        // OWNERSHIP CHECK
        // =========================

        if (!wishlistItem.getWishlist()
                .getId()
                .equals(wishlist.getId())) {

            throw new WishlistItemNotBelongToUserException(
                    "Wishlist item does not belong to this user");
        }


        // =========================
        // DELETE ITEM
        // =========================

        wishlistItemRepository.delete(wishlistItem);


        return buildWishlistResponse(wishlist);
    }


    // =========================
    // BUILD WISHLIST RESPONSE
    // =========================

    private WishlistResponseDTO buildWishlistResponse(
            Wishlist wishlist) {

        List<WishlistItem> wishlistItems =
                wishlistItemRepository.findAll()
                        .stream()
                        .filter(item ->
                                item.getWishlist()
                                        .getId()
                                        .equals(wishlist.getId()))
                        .toList();


        List<WishlistItemResponseDTO> responseItems =
                new ArrayList<>();


        for (WishlistItem item : wishlistItems) {

            Product product =
                    item.getProduct();


            WishlistItemResponseDTO response =
                    new WishlistItemResponseDTO();


            response.setId(item.getId());

            response.setProductId(
                    product.getId());

            response.setProductName(
                    product.getName());

            response.setImage(
                    product.getImage());

            response.setPrice(
                    product.getPrice());


            responseItems.add(response);
        }


        WishlistResponseDTO response =
                new WishlistResponseDTO();


        response.setWishlistId(
                wishlist.getId());

        response.setItems(
                responseItems);

        response.setTotalItems(
                responseItems.size());


        return response;
    }
}