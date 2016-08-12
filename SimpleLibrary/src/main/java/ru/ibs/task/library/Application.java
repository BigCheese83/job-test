package ru.ibs.task.library;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.ibs.task.library.model.Document;
import ru.ibs.task.library.model.User;
import ru.ibs.task.library.repo.DocumentRepository;
import ru.ibs.task.library.repo.UserRepository;

import java.util.Arrays;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    CommandLineRunner initDataBase(final UserRepository userRepo, final DocumentRepository docRepo) {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                Document d1 = new Document("Effective Java", "860-1400000663");
                Document d2 = new Document("Java Concurrency in Practice", "078-5342349603");
                Document d3 = new Document("Java 8 in Action", "978-1617291999");
                Document d4 = new Document("Concurrent Programming in Java", "978-0201310092");
                Document d5 = new Document("Java: The Complete Reference", "978-0071808552");
                Document d6 = new Document("Head First Design Patterns", "000-0596007124");
                Document d7 = new Document("Introduction to Algorithms", "978-8120340077");
                Document d8 = new Document("Thinking in Java", "007-6092039389");
                Document d9 = new Document("JavaScript: The Definitive Guide", "978-0596000486");
                Document d10 = new Document("Core Java Volume I. Fundamentals", "978-0137081899");
                Document d11 = new Document("Core Java, Volume II. Advanced Features", "978-0137081608");
                Document d12 = new Document("Spring in Action", "978-1617291203");
                docRepo.save(Arrays.asList(d1, d2, d3, d4, d5, d6, d7, d8, d9, d10, d11, d12));
                User u1 = new User("petya", Arrays.asList(d1,d2,d7));
                User u2 = new User("alex", Arrays.asList(d1,d3,d6));
                User u3 = new User("ksenia", Arrays.asList(d10,d11));
                User u4 = new User("vasya", Arrays.asList(d12));
                User u5 = new User("lentyay", null);
                userRepo.save(Arrays.asList(u1,u2,u3,u4,u5));
            }
        };
    }
}
