package foodies.in.Foodies.service;

import foodies.in.Foodies.io.CartRequest;
import foodies.in.Foodies.io.CartResponse;
import foodies.in.Foodies.models.CartEntity;
import foodies.in.Foodies.models.FoodEntity;
import foodies.in.Foodies.repository.CartRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.print.DocFlavor;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService{
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private UserService userService;
    @Override
    public CartResponse addToCart(CartRequest request) {
        String loggedInUserId = userService.findByUserId();
        String foodId = request.getFoodId();
        Optional<CartEntity> cartOptional = cartRepository.findByUserId(loggedInUserId);
        CartEntity cart = cartOptional.orElseGet(()-> new CartEntity(loggedInUserId, new HashMap<>()));
        Map<String,Integer> cartItems = cart.getItems();
        cartItems.put(foodId,cartItems.getOrDefault(foodId,0)+1);
        cart.setItems(cartItems);
        cart = cartRepository.save(cart);
        return convertToResponse(cart);
    }

    @Override
    public CartResponse getCart() {
        String loggedInUserId = userService.findByUserId();
        Optional<CartEntity> cartOptional = cartRepository.findByUserId(loggedInUserId);
        CartEntity cart = cartOptional.orElseGet(() -> new CartEntity(loggedInUserId,new HashMap<>()));
        return convertToResponse(cart);
    }

    @Override
    public void deleteCart() {
        String loggedInUserId = userService.findByUserId();
        cartRepository.deleteById(loggedInUserId);
    }

    @Override
    public CartResponse decreaseItemFromCart(CartRequest request) {
        String loggedInUserId = userService.findByUserId();
        CartEntity cart = cartRepository.findByUserId(loggedInUserId).orElseThrow(()-> new RuntimeException("Cart is not found"));
        Map<String,Integer> cartItems = cart.getItems();
        if(cartItems.containsKey(request.getFoodId())){
            int currentQty = cartItems.get(request.getFoodId());
            if (currentQty > 0){
                cartItems.put(request.getFoodId(),currentQty-1);
                --currentQty;
            }
            if(currentQty <= 0){
                cartItems.remove(request.getFoodId());
            }
        }
        cart.setItems(cartItems);
        cart = cartRepository.save(cart);
        return convertToResponse(cart);
    }

    @Override
    public CartResponse removeItemFromCart(CartRequest request) {
        String loggedInUserId = userService.findByUserId();
        CartEntity cart = cartRepository.findByUserId(loggedInUserId).orElseThrow(()-> new RuntimeException("Cart is not found"));
        Map<String,Integer> cartItems = cart.getItems();
        if(cartItems.containsKey(request.getFoodId())){
            cartItems.remove(request.getFoodId());
        }
        else{
            throw new RuntimeException("Food Id Not Found");
        }
        cart.setItems(cartItems);
        cart = cartRepository.save(cart);
        return convertToResponse(cart);
    }

    private CartResponse convertToResponse(CartEntity cart){
        return CartResponse.builder()
                .id(cart.getId())
                .userId(cart.getUserId())
                .items(cart.getItems())
                .build();
    }
}
