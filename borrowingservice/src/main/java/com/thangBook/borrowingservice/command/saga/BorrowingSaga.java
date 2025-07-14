package com.thangBook.borrowingservice.command.saga;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseType;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.SagaLifecycle;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;
import com.thangBook.borrowingservice.command.aggregate.BorrowingAggregate;
import com.thangBook.borrowingservice.command.command.DeleteBorrowingCommand;
import com.thangBook.borrowingservice.command.event.BorrowingCreatedEvent;
import com.thangBook.borrowingservice.command.event.BorrowingDeletedEvent;
import com.thangBook.commonservice.command.RollBackStatusBookCommand;
import com.thangBook.commonservice.command.UpdateBookStatusCommand;
import com.thangBook.commonservice.event.BookRollBackStatusEvent;
import com.thangBook.commonservice.event.BookUpdateStatusEvent;
import com.thangBook.commonservice.model.BookResponseCommonModel;
import com.thangBook.commonservice.model.EmployeeResponseCommonModel;
import com.thangBook.commonservice.queries.GetBookDetailQuery;

import lombok.extern.slf4j.Slf4j;

@Saga
@Slf4j
public class BorrowingSaga {

    private final BorrowingAggregate borrowingAggregate;
    @Autowired
    private transient CommandGateway commandGateway;

    @Autowired
    private transient QueryGateway queryGateway;

    BorrowingSaga(BorrowingAggregate borrowingAggregate) {
        this.borrowingAggregate = borrowingAggregate;
    }
    
    @StartSaga
    @SagaEventHandler(associationProperty = "id")
    private void handle(BorrowingCreatedEvent event) {
        log.info("BorrowingCreatedEvent in saga for BookId: " + event.getBookId() + event.getEmployeeId());

        try {
            GetBookDetailQuery getBookDetailQuery = new GetBookDetailQuery(event.getBookId());
            BookResponseCommonModel bookResponseCommonModel = queryGateway.query(getBookDetailQuery,
                ResponseTypes.instanceOf(BookResponseCommonModel.class)).join();

            if(!bookResponseCommonModel.getIsReady()) {
                throw new Error("Sách đã có người mượn");
            }
            SagaLifecycle.associateWith("bookId", event.getBookId());
            UpdateBookStatusCommand command = new UpdateBookStatusCommand(event.getBookId(), false, event.getEmployeeId(), event.getId());
            commandGateway.sendAndWait(command);
            
        }catch (Exception e){
            rollbackBorrowingRecord(event.getId());
            log.error(e.getMessage());
        }
    }

    @SagaEventHandler(associationProperty = "bookId")
    private void handler(BookUpdateStatusEvent event) {
        log.info("BookUpdateStatusEvent in Saga for BookId : "+event.getBookId());
        try {
            GetBookDetailQuery query = new GetBookDetailQuery(event.getEmployeeId());
            EmployeeResponseCommonModel employeeModel = queryGateway.query(query, ResponseTypes.instanceOf(EmployeeResponseCommonModel.class)).join();
            if(employeeModel.getIsDisciplined()) {
                throw new Exception("Nhân viên bị kỉ luật");
            } else {
                log.info("Đã mượn sách thành công");
                SagaLifecycle.end();
            }
            
            SagaLifecycle.end();
        } catch(Exception ex) {
            rollBackBookStatus(event.getBookId(), event.getEmployeeId(), event.getBorrowingId());
            log.error(ex.getMessage());
        }
    }

    private void rollbackBorrowingRecord(String id) {
        DeleteBorrowingCommand command = new DeleteBorrowingCommand();
        commandGateway.sendAndWait(command);
        SagaLifecycle.end();
    }

    private void rollBackBookStatus(String bookId, String employeeId, String borrowingId) {
        SagaLifecycle.associateWith("bookId", bookId);
        RollBackStatusBookCommand command = new RollBackStatusBookCommand(bookId, true, employeeId, borrowingId);
        commandGateway.sendAndWait(command);
        rollbackBorrowingRecord(bookId);
    }

    @SagaEventHandler(associationProperty = "id")
    @EndSaga
    public void handle(BorrowingDeletedEvent event) {
        log.info("BorrowDeletedEvent in Saga for Borrowing Id : {} " +
                event.getId());
        SagaLifecycle.end();
    }


    @SagaEventHandler(associationProperty = "bookId")
    public void handleRollBackBookStatus(BookRollBackStatusEvent event) {
        System.out.println("BookRollBackStatusEvent in Saga for book Id : {} " + event.getBookId());
        rollbackBorrowingRecord(event.getBorrowId());
    }
}
