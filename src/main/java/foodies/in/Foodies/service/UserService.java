package foodies.in.Foodies.service;

import foodies.in.Foodies.io.UserRequest;
import foodies.in.Foodies.io.UserResponse;
import foodies.in.Foodies.models.UserEntity;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    UserResponse registerUser(UserRequest request);
    String findByUserId();
}
