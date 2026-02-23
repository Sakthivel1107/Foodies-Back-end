package foodies.in.Foodies.controller;

import foodies.in.Foodies.io.FoodRequest;
import foodies.in.Foodies.io.FoodResponse;
import foodies.in.Foodies.service.FoodService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@RestController
@RequestMapping("/api/foods")
@CrossOrigin("*")
@AllArgsConstructor
public class FoodController {
    private final FoodService foodService;
    @PostMapping
    public FoodResponse addFood(@RequestPart("food") String foodString, @RequestPart("file")MultipartFile file){
        ObjectMapper objectMapper = new ObjectMapper();
        FoodRequest request = null;
        try{
            request = objectMapper.readValue(foodString,FoodRequest.class);
        }
        catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid JSON format");
        }
         return foodService.addFood(request,file);
    }
    @GetMapping
    public List<FoodResponse> getFoods(){
        return foodService.readFoods();
    }
    @GetMapping("/{id}")
    public FoodResponse getFoodById(@PathVariable String id){
        return foodService.readFood(id);
    }
    @DeleteMapping("/{id}")
    public String deleteFoodById(@PathVariable String id){
        return foodService.deleteFoodById(id);
    }

}
