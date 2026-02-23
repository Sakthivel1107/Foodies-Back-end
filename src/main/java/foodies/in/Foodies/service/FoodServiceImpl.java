package foodies.in.Foodies.service;

import foodies.in.Foodies.config.GitHubConfig;
import foodies.in.Foodies.io.FoodRequest;
import foodies.in.Foodies.io.FoodResponse;
import foodies.in.Foodies.models.FoodEntity;
import foodies.in.Foodies.repository.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FoodServiceImpl implements FoodService{
    @Autowired
    private GitHubConfig config;
    @Autowired
    private FoodRepository foodRepository;
    private PasswordEncoder passwordEncoder;
    @Override
    public String uploadFile(MultipartFile file) {
        try {
            String filenameExtension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
            String key = UUID.randomUUID().toString() + "." + filenameExtension;
            String base64Content = Base64.getEncoder().encodeToString(file.getBytes());
            String apiUrl = "https://api.github.com/repos/"+config.getUsername()+"/"+config.getRepo()+"/contents/images/image"+key;
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization","Bearer "+ config.getToken());
            headers.setContentType(MediaType.APPLICATION_JSON);
            Map<String,String> body  = new HashMap<>();
            body.put("message","upload image");
            body.put("content",base64Content);
            HttpEntity<Map<String,String>> request = new HttpEntity<>(body,headers);
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.put(apiUrl,request);
            return "https://raw.githubusercontent.com/"+config.getUsername()+"/"+config.getRepo()+"/"+config.getBranch()+"/images/image"+key;
        }
        catch (Exception e){
            throw new RuntimeException("Image upload failed",e);
        }
    }

    @Override
    public FoodResponse addFood(FoodRequest request, MultipartFile file) {
        FoodEntity foodEntity = convertToFoodEntity(request);
        String imageUrl = uploadFile(file);
        foodEntity.setImageUrl(imageUrl);
        foodEntity = foodRepository.save(foodEntity);
        return convertToFoodResponse(foodEntity);
    }

    @Override
    public List<FoodResponse> readFoods() {
        List<FoodEntity> foods = foodRepository.findAll();
        return foods.stream().map(this::convertToFoodResponse).collect(Collectors.toList());
    }

    @Override
    public FoodResponse readFood(String id) {
        FoodEntity food = foodRepository.findById(id).orElseThrow(()-> new RuntimeException("Food not found for the id: "+id));
        return convertToFoodResponse(food);
    }

    @Override
    public boolean deleteFileByName(String fileName) {
        try{
            RestTemplate restTemplate = new RestTemplate();
            String filePath = "images/"+fileName;
            String apiUrl = "https://api.github.com/repos/"+config.getUsername()+"/"+config.getRepo()+"/contents/"+filePath;
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Authorization","token "+config.getToken());
            HttpEntity<Void> entity = new HttpEntity<>(httpHeaders);
            ResponseEntity<Map> response = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.GET,
                    entity,
                    Map.class
            );
            String sha = (String)response.getBody().get("sha");
            String deleteBody = """
                    {
                    "message": "delete image %s",
                    "sha": "%s",
                    "branch": "%s"
                    }
                    """.formatted(fileName,sha, config.getBranch());
            HttpHeaders deleteHeaders = new HttpHeaders();
            deleteHeaders.set("Authorization","token "+config.getToken());
            deleteHeaders.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> deleteRequest = new HttpEntity<>(deleteBody, deleteHeaders);
            restTemplate.exchange(
                    apiUrl,
                    HttpMethod.DELETE,
                    deleteRequest,
                    String.class
            );
            return true;
        }
        catch (HttpClientErrorException e){
            return false;
        }
    }

    @Override
    public String deleteFoodById(String id) {
        FoodResponse food = readFood(id);
        String imageUrl = food.getImageUrl();
        String fileName = imageUrl.substring(imageUrl.lastIndexOf('/')+1);
        boolean isDeleted = deleteFileByName(fileName);
        if(isDeleted){
            foodRepository.deleteById(id);
            return "Food Deleted Successfully";
        }
        else
            return "Deletion is unsuccessfull for foodid: "+id;
    }

    private FoodEntity convertToFoodEntity(FoodRequest request){
        return FoodEntity.builder()
                .name(request.getName())
                .description(request.getDescription())
                .category(request.getCategory())
                .price(request.getPrice())
                .build();
    }
    private FoodResponse convertToFoodResponse(FoodEntity foodentity){
        return FoodResponse.builder()
                .name(foodentity.getName())
                .description(foodentity.getDescription())
                .price(foodentity.getPrice())
                .id(foodentity.getId())
                .imageUrl(foodentity.getImageUrl())
                .category(foodentity.getCategory())
                .build();
    }
}
