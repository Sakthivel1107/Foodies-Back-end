package foodies.in.Foodies.repository;

import foodies.in.Foodies.models.CartEntity;
import foodies.in.Foodies.models.OrderEntity;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class OrderRepository {
    @Autowired
    private MongoTemplate mongoTemplate;
    public List<OrderEntity> findByUserId(String userId){
        Query query = Query.query(Criteria.where("userId").is(userId));
        return mongoTemplate.find(query,OrderEntity.class);
    }
    public Optional<OrderEntity> findByRazorpayOrderId(String razorpayOrderId){
        Query query = Query.query(Criteria.where("razorPayOrderId").is(razorpayOrderId));
        return Optional.ofNullable(mongoTemplate.findOne(query,OrderEntity.class));
    }
    public OrderEntity save(OrderEntity order){
        return mongoTemplate.save(order);
    }
    public void deleteOrderById(String orderId){
        Query query = Query.query(Criteria.where("_id").is(orderId));
        mongoTemplate.remove(query, OrderEntity.class);
    }
    public List<OrderEntity> getAllOrders(){
        return mongoTemplate.findAll(OrderEntity.class);
    }
    public Optional<OrderEntity> findById(String orderId){
        return Optional.ofNullable(mongoTemplate.findById(orderId,OrderEntity.class));
    }
}
