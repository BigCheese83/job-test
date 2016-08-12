package ru.ibs.task.library.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.ibs.task.library.model.Document;
import ru.ibs.task.library.repo.DocumentRepository;

import java.util.List;

@RestController
@RequestMapping("/docs")
public class DocumentsController {

    @Autowired
    private DocumentRepository documentRepository;

    @RequestMapping(path = "/all", method = RequestMethod.GET)
    public List<Document> getAllDocuments() {
        return documentRepository.findAll();
    }

    @RequestMapping(path = "/new", method = RequestMethod.POST)
    public Document createDocument(@RequestBody Document document) {
        List<Document> find = documentRepository.findByTitle(document.getTitle());
        if (!find.isEmpty()) {
            document.setId(find.get(0).getId());
        }
        return documentRepository.save(document);
    }

    @RequestMapping(path = "/doc/{id}", method = RequestMethod.DELETE)
    public void deleteDocument(@PathVariable("id") Long id) {
        documentRepository.delete(id);
    }
}
