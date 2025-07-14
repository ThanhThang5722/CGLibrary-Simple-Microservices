package com.thangBook.bookservice.command.aggregate;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

import com.thangBook.bookservice.command.command.CreateBookCommand;
import com.thangBook.bookservice.command.command.DeleteBookCommand;
import com.thangBook.bookservice.command.command.UpdateBookCommand;
import com.thangBook.bookservice.command.event.BookCreatedEvent;
import com.thangBook.bookservice.command.event.BookDeletedEvent;
import com.thangBook.bookservice.command.event.BookUpdatedEvent;
import com.thangBook.commonservice.command.RollBackStatusBookCommand;
import com.thangBook.commonservice.command.UpdateBookStatusCommand;
import com.thangBook.commonservice.event.BookRollBackStatusEvent;
import com.thangBook.commonservice.event.BookUpdateStatusEvent;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Aggregate
@NoArgsConstructor
@Getter
@Setter
public class BookAggregate {

    @AggregateIdentifier
    private String id;

    private String name;

    private String author;

    private Boolean isReady;

    @CommandHandler
    public BookAggregate(CreateBookCommand command) {
        BookCreatedEvent bookCreatedEvent = new BookCreatedEvent();
        BeanUtils.copyProperties(command, bookCreatedEvent);
        
        AggregateLifecycle.apply(bookCreatedEvent);
    }

    @CommandHandler
    public void handle(UpdateBookCommand command) {
        BookUpdatedEvent bookUpdatedEvent = new BookUpdatedEvent();
        BeanUtils.copyProperties(command, bookUpdatedEvent);
        
        AggregateLifecycle.apply(bookUpdatedEvent);
    }

    @CommandHandler
    public void handle(DeleteBookCommand command) {
        DeleteBookCommand deleteBookCommand = new DeleteBookCommand();
        BeanUtils.copyProperties(command, deleteBookCommand);
        
        AggregateLifecycle.apply(deleteBookCommand);
    }

    @CommandHandler
    public void handle(UpdateBookStatusCommand comman) {
        BookUpdateStatusEvent event = new BookUpdateStatusEvent();
        BeanUtils.copyProperties(comman, event);
        AggregateLifecycle.apply(event);
    }

    @CommandHandler
    public void handle(RollBackStatusBookCommand command) {
        BookRollBackStatusEvent event = new BookRollBackStatusEvent();
        BeanUtils.copyProperties(command, event);
        AggregateLifecycle.apply(event);
    }

    @CommandHandler
    public void on(BookRollBackStatusEvent event) {
        this.id = event.getBookId();
    }

    @EventSourcingHandler
    public void on(BookUpdateStatusEvent event) {
        this.id = event.getBookId();
        this.isReady = event.getIsReady();
    }

    @EventSourcingHandler
    public void on(BookCreatedEvent event) {
        BeanUtils.copyProperties(event, this);
    }

    @EventSourcingHandler
    public void on(BookUpdatedEvent event) {
        BeanUtils.copyProperties(event, this);
    }

    @EventSourcingHandler
    public void on(BookDeletedEvent event) {
        this.id = event.getId();
    }
}

