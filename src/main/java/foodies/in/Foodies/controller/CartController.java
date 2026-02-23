package foodies.in.Foodies.controller;

import foodies.in.Foodies.io.CartRequest;
import foodies.in.Foodies.io.CartResponse;
import foodies.in.Foodies.service.CartService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("api/cart")
@AllArgsConstructor
@CrossOrigin("*")
public class CartController {
    private final CartService cartService;

    @PostMapping
    public CartResponse addToCart(@RequestBody CartRequest request) {
        String foodId = request.getFoodId();
        if(foodId == null || foodId.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"FoodId Not Found");
        }
        return cartService.addToCart(request);
    }
    @GetMapping
    public CartResponse getCart(){
        return cartService.getCart();
    }
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCart(){
        cartService.deleteCart();
    }
    @PostMapping("/decItem")
    public CartResponse decreaseItemFromCart(@RequestBody CartRequest request){
        String foodId = request.getFoodId();
        if(foodId == null || foodId.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"FoodId Nof Found");
        }
        return cartService.decreaseItemFromCart(request);
    }
    @PostMapping("/remove")
    public CartResponse removeItemFromCart(@RequestBody CartRequest request){
        String foodId = request.getFoodId();
        if(foodId == null || foodId.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"FoodId Nof Found");
        }
        return cartService.removeItemFromCart(request);
    }
}