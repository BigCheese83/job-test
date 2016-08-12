package ru.ibs.task.library.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ibs.task.library.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByName(String name);
}
