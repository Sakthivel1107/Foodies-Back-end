package foodies.in.Foodies.models;

import lombok.Data;

@Data
public class ContactRequest {
    private String name;
    private String email;
    private String message;
}
