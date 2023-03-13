package com.etspplbo50.paymentservice.service;

import com.etspplbo50.paymentservice.dto.PaymentRequest;
import com.etspplbo50.paymentservice.dto.PaymentResponse;
import com.etspplbo50.paymentservice.event.OrderPlacedEvent;
import com.etspplbo50.paymentservice.event.PaymentSettledEvent;
import com.etspplbo50.paymentservice.model.Payment;
import com.etspplbo50.paymentservice.repository.PaymentRepository;
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
public class PaymentService {
    private final PaymentRepository paymentRepository;

    private final KafkaTemplate<String, PaymentSettledEvent> paymentSettledEventKafkaTemplate;

    public void createPayment(String orderId) {
        Payment payment = Payment.builder()
                .orderId(orderId)
                .status("PENDING")
                .build();

        paymentRepository.save(payment);

        log.info("Received new order, waiting for payment {}", orderId);
    }

    public List<PaymentResponse> getAllPayment() {
        List<Payment> paymentList = paymentRepository.findAll();

        return paymentList.stream().map(payment -> mapPaymentToPaymentResponse(payment)).toList();
    }

    public PaymentResponse getPayment(String id) {
        Optional<Payment> payment = paymentRepository.findById(id);

        if (payment.isPresent()) {
            return mapPaymentToPaymentResponse(payment.get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment not found");
        }
    }

    public PaymentResponse updatePayment(String id, PaymentRequest paymentRequest) {
        Optional<Payment> payment = paymentRepository.findById(id);

        if (payment.isPresent()) {
            Payment newPayment = payment.get();

            newPayment.setStatus("SETTLED");

            paymentRepository.save(newPayment);
            return mapPaymentToPaymentResponse(newPayment);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment not found");
        }
    }

    public PaymentResponse updatePaymentStatus(String id, PaymentRequest paymentRequest) {
        Optional<Payment> payment = paymentRepository.findById(id);

        if (payment.isPresent()) {
            Payment newPayment = payment.get();

            newPayment.setStatus("SETTLED");

            paymentRepository.save(newPayment);

            paymentSettledEventKafkaTemplate.send("paymentSettledTopic", new PaymentSettledEvent(newPayment.getOrderId()));
            return mapPaymentToPaymentResponse(newPayment);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment not found");
        }
    }

    public String deletePayment(String id) {
        Optional<Payment> payment = paymentRepository.findById(id);

        if (payment.isPresent()) {
            paymentRepository.deleteById(id);
            return "Payment deleted";
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment not found");
        }
    }

    private PaymentResponse mapPaymentToPaymentResponse(Payment payment) {
        return PaymentResponse.builder()
                .id(payment.getId())
                .orderId(payment.getOrderId())
                .status(payment.getStatus())
                .build();
    }
}
