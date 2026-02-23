package foodies.in.Foodies.controller;

import foodies.in.Foodies.io.UserRequest;
import foodies.in.Foodies.io.UserResponse;
import foodies.in.Foodies.models.UserEntity;
import foodies.in.Foodies.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
@CrossOrigin("*")
public class UserController {

    private final UserService userService;
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(@RequestBody UserRequest request){
        return userService.registerUser(request);
    }
}
