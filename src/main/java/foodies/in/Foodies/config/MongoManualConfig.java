package foodies.in.Foodies.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class MongoManualConfig {

    @Bean
    public MongoClient mongoClient(){
        return MongoClients.create("mongodb+srv://sakthivel62628_db_user:pppKzJFHzxGuqVz2@cluster0.1ohftgg.mongodb.net/?appName=Cluster0");
    }

    @Bean
    public MongoTemplate mongoTemplate(){
        return new MongoTemplate(mongoClient(), "foodies");
    }
}
