package com.thangBook.borrowingservice.command.aggregate;

import java.util.Date;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

import com.thangBook.borrowingservice.command.command.CreateBorrowingCommand;
import com.thangBook.borrowingservice.command.command.DeleteBorrowingCommand;
import com.thangBook.borrowingservice.command.event.BorrowingCreatedEvent;
import com.thangBook.borrowingservice.command.event.BorrowingDeletedEvent;
import com.thangBook.commonservice.command.UpdateBookStatusCommand;
import com.thangBook.commonservice.event.BookUpdateStatusEvent;


@Aggregate
public class BorrowingAggregate {

    @AggregateIdentifier
    private String id;

    private String bookId;
    private String employeeId;
    private Date borrowingDate;
    private Date returnDate;

    public BorrowingAggregate(){}

    @CommandHandler
    public BorrowingAggregate(CreateBorrowingCommand command){
        BorrowingCreatedEvent event = new BorrowingCreatedEvent();
        BeanUtils.copyProperties(command,event);
        AggregateLifecycle.apply(event);
    }

    @CommandHandler
    public void handle(DeleteBorrowingCommand command) {
        BorrowingDeletedEvent event = new BorrowingDeletedEvent();
        BeanUtils.copyProperties(command, event);
        AggregateLifecycle.apply(event);
    }


    @EventSourcingHandler
    public void on(BorrowingDeletedEvent event) {
        this.id = event.getId();
    }

    @EventSourcingHandler
    public void on(BorrowingCreatedEvent event){
        this.id = event.getId();
        this.bookId = event.getBookId();
        this.employeeId = event.getEmployeeId();
        this.borrowingDate = event.getBorrowingDate();
    }
}