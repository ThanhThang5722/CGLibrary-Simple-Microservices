package com.thangBook.bookservice.query.projection;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.thangBook.bookservice.command.data.Book;
import com.thangBook.bookservice.command.data.BookRepository;
import com.thangBook.bookservice.query.model.BookResponseModel;
import com.thangBook.bookservice.query.queries.GetAllBookQuery;
import com.thangBook.bookservice.query.queries.GetBookDetailQuery;

@Component
public class BookProjection {
    @Autowired
    private BookRepository bookRepository;

    @QueryHandler
    public List<BookResponseModel> handle(GetAllBookQuery query) {
        List<Book> list = bookRepository.findAll();

        List<BookResponseModel> results = list.stream().map(book -> {
            BookResponseModel model = new BookResponseModel();
            BeanUtils.copyProperties(book, model);
            return model;
        }).toList();
        return results;
    }

    @QueryHandler
    public BookResponseModel handle(GetBookDetailQuery query) throws Exception {
        BookResponseModel model = new BookResponseModel();
        Book book = bookRepository.findById(query.getId()).orElseThrow(() -> new Exception("Not found Book with BookId: "+ query.getId()));
        BeanUtils.copyProperties(book, model);
        return model;
    }
}
