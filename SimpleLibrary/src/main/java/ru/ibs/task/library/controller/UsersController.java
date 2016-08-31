package ru.ibs.task.library.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ru.ibs.task.library.model.User;
import ru.ibs.task.library.repo.UserRepository;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(path = "/user/{name}", method = RequestMethod.GET)
    public User getUserByName(@PathVariable("name") String name) {
        List<User> find = userRepository.findByName(name);
        return find.isEmpty() ? null : find.get(0);
    }

    @RequestMapping(path = "/all", method = RequestMethod.GET)
    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @RequestMapping(path = "/new", method = RequestMethod.POST)
    public User createUser(@RequestBody User user) {
        List<User> find = userRepository.findByName(user.getName());
        if (!find.isEmpty()) {
            user.setId(find.get(0).getId());
        }
        return userRepository.save(user);
    }

    @RequestMapping(path = "/user/{id}", method = RequestMethod.DELETE)
    public void deleteUser(@PathVariable("id") Long id) {
        userRepository.delete(id);
    }
}
