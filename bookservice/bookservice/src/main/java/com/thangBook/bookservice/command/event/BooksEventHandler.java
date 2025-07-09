package com.thangBook.bookservice.command.event;

import java.util.Optional;

import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.thangBook.bookservice.command.data.Book;
import com.thangBook.bookservice.command.data.BookRepository;

@Component
public class BooksEventHandler {

    @Autowired
    private BookRepository bookRepository;
    
    @EventHandler
    public void on(BookCreatedEvent event) {
        Book book = new Book();
        BeanUtils.copyProperties(event, book);
        bookRepository.save(book);
    }

    @EventHandler
    public void on(BookUpdatedEvent event) {
        Optional<Book> oldBook = bookRepository.findById(event.getId());
        if(oldBook.isEmpty()) {
            return;
        }
        Book book = oldBook.get();
        book.setAuthor(event.getAuthor());
        book.setName(event.getName());
        book.setIsReady(event.getIsReady());

        bookRepository.save(book);
    }

    @EventHandler
    public void on(BookDeletedEvent event) {
        Optional<Book> oldBook = bookRepository.findById(event.getId());
        if(oldBook.isEmpty()) {
            return;
        }
        Book book = oldBook.get();
        bookRepository.delete(book);
    }
}
