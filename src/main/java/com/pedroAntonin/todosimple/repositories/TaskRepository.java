package com.pedroAntonin.todosimple.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pedroAntonin.todosimple.Models.Task;


@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> FindByUser_Id(Long Id);
    
}
