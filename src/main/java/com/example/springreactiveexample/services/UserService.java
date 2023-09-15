package com.example.springreactiveexample.services;

import com.example.springreactiveexample.model.User;
import com.example.springreactiveexample.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;


@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final ReactiveMongoTemplate reactiveMongoTemplate;
    private final UserRepository userRepository;

    public Mono<User> createUser(User user){
        return userRepository.save(user);
    }

    public Flux<User> getAllUsers(){
        return userRepository.findAll();
    }

    public Mono<User> findById(String id){
        return userRepository.findById(id);
    }

    public Mono<User> updateUser(String id,  User user){
        return userRepository.findById(id)
                .flatMap(dbUser -> {
                    dbUser.setName(user.getName());
                    dbUser.setAge(user.getAge());
                    dbUser.setSalary(user.getSalary());
                    dbUser.setDepartment(user.getDepartment());
                    return userRepository.save(dbUser);
                });
    }

    public Mono<User> deleteUser(String id){
        return userRepository.findById(id)
                .flatMap(existingUser -> userRepository.delete(existingUser)
                        .then(Mono.just(existingUser)));
    }

}
