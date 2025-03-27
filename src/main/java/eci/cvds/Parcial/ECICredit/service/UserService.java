package eci.cvds.Parcial.ECICredit.service;

import eci.cvds.Parcial.ECICredit.model.User;
import eci.cvds.Parcial.ECICredit.mongoConnection.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    /**
     * @param name
     * @param email
     * @return el usuario registrado
     */

    @Autowired
    private UserRepository userRepository;

    public User registrarUsuario(String nombre, String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("El email ya esta registrado.");
        }
        User user = new User(nombre, email);
        return userRepository.save(user);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
