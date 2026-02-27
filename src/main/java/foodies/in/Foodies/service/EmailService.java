package foodies.in.Foodies.service;

import foodies.in.Foodies.models.ContactRequest;

public interface EmailService {
    void sendEmail(ContactRequest request) throws Exception;
}
