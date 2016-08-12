package ru.ibs.task.library.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ibs.task.library.model.Document;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findByTitle(String title);
}
