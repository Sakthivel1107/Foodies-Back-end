package foodies.in.Foodies.repository;

import foodies.in.Foodies.models.FoodEntity;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class FoodRepository {
    @Autowired
    private MongoTemplate mongoTemplate;
    public FoodEntity save(FoodEntity food){
        return mongoTemplate.save(food);
    }
    public List<FoodEntity> findAll(){
        return mongoTemplate.findAll(FoodEntity.class);
    }
    public Optional<FoodEntity> findById(String Id){
            ObjectId objectId = new ObjectId(Id);
            Query query = Query.query(Criteria.where("_id").is(objectId));
            return Optional.ofNullable(mongoTemplate.findOne(query,FoodEntity.class));
    }
    public void deleteById(String Id){
        Query query = Query.query(Criteria.where("_id").is(Id));
        mongoTemplate.remove(query, FoodEntity.class);
    }
}
