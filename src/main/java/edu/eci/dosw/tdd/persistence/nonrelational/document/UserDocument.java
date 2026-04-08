package edu.eci.dosw.tdd.persistence.nonrelational.document;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

@Data
@Document(collection = "users")
public class UserDocument {
    @Id
    private String id;
    private String name;
    private String username;
    private String password;
    private String role;
    private String email;
    private String membership;
    private Date createdAt;
}
