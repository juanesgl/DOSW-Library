package edu.eci.dosw.tdd.service;

import edu.eci.dosw.tdd.model.Book;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class BookService {
    private Map<Book, Integer> bookInventory = new HashMap<>();

    public Book addBook(Book book, int quantity){
        bookInventory.put(book, bookInventory.getOrDefault(book, 0) + quantity);
        return book;
    }

    public Map<Book, Integer> getAllBooks(){
        return bookInventory;
    }

    public Optional<Book> getBookByID(String ID){
        return bookInventory.keySet().stream()
                .filter(b -> b.getID().equals(ID))
                .findFirst();
    }

    public boolean isBookAvailable(Book book){
        return bookInventory.getOrDefault(book, 0) > 0;
    }

    public void updateBookAvailability(Book book, int quantityChange){
        int currentQuantity = bookInventory.getOrDefault(book, 0);
        bookInventory.put(book, currentQuantity + quantityChange);
    }
}
