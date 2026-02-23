package foodies.in.Foodies.repository;

import foodies.in.Foodies.models.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public class UserRepository {
    @Autowired
    private MongoTemplate mongoTemplate;

    public Optional<UserEntity> findByEmail(String email){
        Query query = new Query(Criteria.where("email").is(email));
        return Optional.ofNullable(mongoTemplate.findOne(query, UserEntity.class));
    }
    public UserEntity save(UserEntity user){
        return mongoTemplate.save(user);
    }
}

