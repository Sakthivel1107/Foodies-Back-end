package foodies.in.Foodies.service;

import foodies.in.Foodies.io.CartRequest;
import foodies.in.Foodies.io.CartResponse;

public interface CartService {
    CartResponse addToCart(CartRequest request);
    CartResponse getCart();
    void deleteCart();
    CartResponse decreaseItemFromCart(CartRequest request);
    CartResponse removeItemFromCart(CartRequest request);
}
