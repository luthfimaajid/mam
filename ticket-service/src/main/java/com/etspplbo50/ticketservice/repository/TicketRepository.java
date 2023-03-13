package com.etspplbo50.ticketservice.repository;

import com.etspplbo50.ticketservice.model.Ticket;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TicketRepository extends MongoRepository<Ticket, String> {

}
