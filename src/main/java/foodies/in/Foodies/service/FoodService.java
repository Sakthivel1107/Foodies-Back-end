package foodies.in.Foodies.service;

import foodies.in.Foodies.io.FoodRequest;
import foodies.in.Foodies.io.FoodResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface FoodService {
    String uploadFile(MultipartFile file);
    FoodResponse addFood(FoodRequest request,MultipartFile file);
    List<FoodResponse> readFoods();
    FoodResponse readFood(String id);
    boolean deleteFileByName(String fileName);
    String deleteFoodById(String id);
}
