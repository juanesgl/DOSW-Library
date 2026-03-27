package edu.eci.dosw.tdd.persistence.mapper;

import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.persistence.entity.BookEntity;
import org.springframework.stereotype.Component;

@Component
public class BookEntityMapper {

    public Book toModel(BookEntity entity) {
        if (entity == null)
            return null;
        return Book.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .author(entity.getAuthor())
                .totalQuantity(entity.getTotalQuantity())
                .availableQuantity(entity.getAvailableQuantity())
                .build();
    }

    public BookEntity toEntity(Book model) {
        if (model == null)
            return null;
        return BookEntity.builder()
                .id(model.getId())
                .title(model.getTitle())
                .author(model.getAuthor())
                .totalQuantity(model.getTotalQuantity())
                .availableQuantity(model.getAvailableQuantity())
                .build();
    }
}
