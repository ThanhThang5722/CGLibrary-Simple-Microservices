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
import com.thangBook.bookservice.query.queries.GetAllBookQuery;
import com.thangBook.commonservice.queries.GetBookDetailQuery;
import com.thangBook.commonservice.model.BookResponseCommonModel;
@Component
public class BookProjection {
    @Autowired
    private BookRepository bookRepository;

    @QueryHandler
    public List<BookResponseCommonModel> handle(GetAllBookQuery query) {
        List<Book> list = bookRepository.findAll();

        List<BookResponseCommonModel> results = list.stream().map(book -> {
            BookResponseCommonModel model = new BookResponseCommonModel();
            BeanUtils.copyProperties(book, model);
            return model;
        }).toList();
        return results;
    }

    @QueryHandler
    public BookResponseCommonModel handle(GetBookDetailQuery query) throws Exception {
        BookResponseCommonModel model = new BookResponseCommonModel();
        Book book = bookRepository.findById(query.getId()).orElseThrow(() -> new Exception("Not found Book with BookId: "+ query.getId()));
        BeanUtils.copyProperties(book, model);
        return model;
    }
}
