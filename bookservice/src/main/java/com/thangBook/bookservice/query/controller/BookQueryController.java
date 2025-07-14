package com.thangBook.bookservice.query.controller;

import java.util.List;

import org.axonframework.messaging.responsetypes.ResponseType;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thangBook.bookservice.query.queries.GetAllBookQuery;
import com.thangBook.commonservice.queries.GetBookDetailQuery;
import com.thangBook.commonservice.service.KafkaService;
import com.thangBook.commonservice.model.BookResponseCommonModel;

@RestController
@RequestMapping("/api/v1/books")
public class BookQueryController {
    @Autowired
    private QueryGateway queryGateway;
    @Autowired
    private KafkaService kafkaService;

    @GetMapping
    public List<BookResponseCommonModel> getAllBooks() {
        GetAllBookQuery query = new GetAllBookQuery();
        List<BookResponseCommonModel> results = queryGateway.query(query, ResponseTypes.multipleInstancesOf(BookResponseCommonModel.class)).join();
        return results;
    }
    @GetMapping("/{bookId}")
    public BookResponseCommonModel getBookDetail(@PathVariable String bookId) {
        GetBookDetailQuery query = new GetBookDetailQuery(bookId);
        BookResponseCommonModel result = queryGateway.query(query, ResponseTypes.instanceOf(BookResponseCommonModel.class)).join();
        
        return result;
    }

    @PostMapping("/sendMessage")
    public void sendMessage(@RequestBody String message) {
        kafkaService.sendMessage("test", message);
    }
}
