package com.etspplbo50.ticketservice.service;

import com.etspplbo50.ticketservice.dto.TicketRequest;
import com.etspplbo50.ticketservice.dto.TicketResponse;
import com.etspplbo50.ticketservice.event.OrderReadyEvent;
import com.etspplbo50.ticketservice.model.Ticket;
import com.etspplbo50.ticketservice.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TicketService {
    private final TicketRepository ticketRepository;

    private final KafkaTemplate<String, OrderReadyEvent> orderReadyEventKafkaTemplate;

    public void paymentSettledTopicHandler(String orderId) {
        Ticket ticket = Ticket.builder()
                .orderId(orderId)
                .status("PREPARING")
                .build();

        ticketRepository.save(ticket);

        log.info("Preparing order for {}", orderId);
    }

    public List<TicketResponse> getAllTicket() {
        List<Ticket> ticketList = ticketRepository.findAll();

        return ticketList.stream().map(ticket -> mapTicketToKitchenResponse(ticket)).toList();
    }

    public TicketResponse getTicket(String id) {
        Optional<Ticket> ticket = ticketRepository.findById(id);

        if (ticket.isPresent()) {
            return mapTicketToKitchenResponse(ticket.get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket not found");
        }
    }

    public TicketResponse updateTicket(String id, TicketRequest ticketRequest) {
        Optional<Ticket> ticket = ticketRepository.findById(id);

        if (ticket.isPresent()) {
            Ticket newTicket = ticket.get();

            newTicket.setStatus("SETTLED");

            ticketRepository.save(newTicket);
            return mapTicketToKitchenResponse(newTicket);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket not found");
        }
    }

    public TicketResponse updateTicketStatus(String id) {
        Optional<Ticket> ticket = ticketRepository.findById(id);

        if (ticket.isPresent()) {
            Ticket newTicket = ticket.get();

            newTicket.setStatus("READY TO DELIVER");

            ticketRepository.save(newTicket);

            orderReadyEventKafkaTemplate.send("orderReadyTopic", new OrderReadyEvent(newTicket.getOrderId(), newTicket.getStartTime(), newTicket.getEndTime()));

            return mapTicketToKitchenResponse(newTicket);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket not found");
        }
    }

    public String deleteTicket(String id) {
        Optional<Ticket> ticket = ticketRepository.findById(id);

        if (ticket.isPresent()) {
            ticketRepository.deleteById(id);
            return "Ticket deleted";
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket not found");
        }
    }

    private TicketResponse mapTicketToKitchenResponse(Ticket ticket) {
        return TicketResponse.builder()
                .id(ticket.getId())
                .orderId(ticket.getOrderId())
                .status(ticket.getStatus())
                .build();
    }
}
