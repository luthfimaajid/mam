package com.etspplbo50.ticketservice.controller;

import com.etspplbo50.ticketservice.dto.TicketRequest;
import com.etspplbo50.ticketservice.dto.TicketResponse;
import com.etspplbo50.ticketservice.event.PaymentSettledEvent;
import com.etspplbo50.ticketservice.service.TicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ticket")
@RequiredArgsConstructor
@Slf4j
public class TicketController {
    private final TicketService ticketService;

    @KafkaListener(
            topics = "paymentSettledTopic",
            groupId = "ticketId",
            containerFactory = "paymentSettledEventListenerFactory"
    )
    public void paymentSettledTopicHandler(PaymentSettledEvent paymentSettledEvent) {
        ticketService.paymentSettledTopicHandler(paymentSettledEvent.getOrderId());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TicketResponse getTicket(@PathVariable("id") String id) {
        return ticketService.getTicket(id);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<TicketResponse> getAllTicket() {
        return ticketService.getAllTicket();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TicketResponse updateTicket(@PathVariable("id") String id, @RequestBody TicketRequest ticketRequest) {
        return ticketService.updateTicket(id, ticketRequest);
    }

    @PatchMapping("/{id}/status")
    @ResponseStatus(HttpStatus.OK)
    public TicketResponse updateTicketStatus(@PathVariable("id") String id) {
        return ticketService.updateTicketStatus(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String deleteTicket(@PathVariable("id") String id) {
        return ticketService.deleteTicket(id);
    }
}
