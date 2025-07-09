package com.thangBook.bookservice.query.controller;

import java.util.List;

import org.axonframework.messaging.responsetypes.ResponseType;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thangBook.bookservice.query.model.BookResponseModel;
import com.thangBook.bookservice.query.queries.GetAllBookQuery;
import com.thangBook.bookservice.query.queries.GetBookDetailQuery;

@RestController
@RequestMapping("/api/v1/books")
public class BookQueryController {
    @Autowired
    private QueryGateway queryGateway;

    @GetMapping
    public List<BookResponseModel> getAllBooks() {
        GetAllBookQuery query = new GetAllBookQuery();
        List<BookResponseModel> results = queryGateway.query(query, ResponseTypes.multipleInstancesOf(BookResponseModel.class)).join();
        return results;
    }
    @GetMapping("/{bookId}")
    public BookResponseModel getBookDetail(@PathVariable String bookId) {
        GetBookDetailQuery query = new GetBookDetailQuery(bookId);
        BookResponseModel result = queryGateway.query(query, ResponseTypes.instanceOf(BookResponseModel.class)).join();
        
        return result;
    }
}
