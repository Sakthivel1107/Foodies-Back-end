package foodies.in.Foodies.repository;

import foodies.in.Foodies.models.CartEntity;
import foodies.in.Foodies.models.FoodEntity;
import foodies.in.Foodies.models.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class CartRepository {
    @Autowired
    private MongoTemplate mongoTemplate;
    public CartEntity save(CartEntity cart){
        return mongoTemplate.save(cart);
    }
    public Optional<CartEntity> findByUserId(String userId){
        Query query = new Query(Criteria.where("userId").is(userId));
        return Optional.ofNullable(mongoTemplate.findOne(query, CartEntity.class));
    }
    public void deleteById(String userId){
        Query query = Query.query(Criteria.where("userId").is(userId));
        mongoTemplate.remove(query, CartEntity.class);
    }
}
