package edu.eci.dosw.tdd.service;
import edu.eci.dosw.tdd.model.User;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private List<User> users = new ArrayList<>();

    public User registerUser(User user){
        users.add(user);
        return user;
    }

    public List<User> getAllUsers(){
        return users;
    }

    public Optional<User> getUserByID(String ID){
        return users.stream()
                .filter(u -> u.getID().equals(ID))
                .findFirst();
    }
}
