package com.pedroAntonin.todosimple.services;

import java.util.List;
import java.util.Objects;

import javax.transaction.Transactional;

import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pedroAntonin.todosimple.Models.Task;
import com.pedroAntonin.todosimple.Models.User;
import com.pedroAntonin.todosimple.Models.enums.ProfileEnum;
import com.pedroAntonin.todosimple.Security.UserSpringSecurity;
import com.pedroAntonin.todosimple.repositories.TaskRepository;
import com.pedroAntonin.todosimple.services.exceptions.AuthorizationException;
import com.pedroAntonin.todosimple.services.exceptions.DataBindingViolationException;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserService userService;

    public Task findById(Long id) {
        Task task = this.taskRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException(
                "Tarefa não encontrada! id: " + id + ", Tipo: " + Task.class.getName()));

        UserSpringSecurity userSpringSecurity = UserService.authenticated();
        if(Objects.isNull(userSpringSecurity)
                || !userSpringSecurity.hasRole(ProfileEnum.ADMIN) && !userHasTask(userSpringSecurity, task))
            throw new AuthorizationException("Acesso negado!");
        
        return task;
    }

    public List<TaskProjection> findAllByUser() {
        UserSpringSecurity userSpringSecurity = UserService.authenticated();
        if(Objects.isNull(userSpringSecurity))
            throw new AuthhorizationException("Acesso negado!");

        List<TaskProjection> tasks = this.taskRepository.findByUser_Id(userSpringSecurity.getId());
        return tasks;
    }

    @Transactional
    public Task create(Task obj) {
        UserSpringSecurity userSpringSecurity = UserService.authenticated();
        if(Objects.isNull(userSpringSecurity))
            throw new AuthorizationException("Acesso negado");
        
        User user = this.userService.findById(userSpringSecurity.getId());
        obj.setId(null);
        obj.setUser(user);
        obj = this.taskRepository.save(obj);
        return obj;
    }

    @Transactional
    public Task update(Task obj) {
        Task newObj = findById(obj.getId());
        newObj.setDescripition(obj.getDescripition());
        return this.taskRepository.save(newObj);
    }

    public void delete(Long id) {
        findById(id);
        try {
            this.taskRepository.deleteById(id);
        } catch (Exception e) {
            throw new DataBindingViolationException("Não é possivelexcluir pois há entidades relacionadas!");
        }
    }

    private boolean userHasTask(UserSpringSecurity userSpringSecurity, Task task,) {
        return task.getUser().getId().equals(userSpringSecurity.getId());
    }


}