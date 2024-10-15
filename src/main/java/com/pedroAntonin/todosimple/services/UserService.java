package com.pedroAntonin.todosimple.services;

import java.util.Optional;

import javax.transaction.Transactional;

import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pedroAntonin.todosimple.Models.User;
import com.pedroAntonin.todosimple.repositories.UserRepository;
import com.pedroAntonin.todosimple.services.exceptions.DataBindingViolationException;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User findById(Long id) {
        Optional<User> user = this.userRepository.findById(id);
        return user.orElseThrow(() -> new ObjectNotFoundException(
            "Usuario não encontrado! Id: " + id + ", Tipo: " + User.class.getName()));
    }

    @Transactional
    public User create(User obj) {
        obj.setId( null);
        obj = this.userRepository.save(obj);
        return obj;
    }

    @Transactional
    public User update(User obj) {
        User newObj = findById(obj.getId());
        newObj.setPassword(obj.getPassword());
        return this.userRepository.save(newObj);
    }

    public void delete(Long id) {
        findById(id);
        try {
            this.userRepository.deleteById(id);
        } catch (Exception e) {
            throw  new DataBindingViolationException("Não é possivelexcluir pois há entidades relacionadas!");
        }
    }
}




