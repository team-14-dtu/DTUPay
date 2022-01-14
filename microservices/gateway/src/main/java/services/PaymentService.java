package services;

import messaging.Event;
import messaging.MessageQueue;
import messaging.implementations.RabbitMqQueue;
import rest.Payment;
import sharedMisc.QueueUtils;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static event.payment.PaymentEvents.*;

@ApplicationScoped
public class PaymentService {

    private final MessageQueue queue = new RabbitMqQueue(QueueUtils.getQueueName());

    public CompletableFuture<Payment> createPayment = new CompletableFuture<>();
    public CompletableFuture<List<Payment>> getPaymentsForUser = new CompletableFuture<>();
    public CompletableFuture<Payment> getTargetPayment = new CompletableFuture<>();

    public PaymentService() {
        queue.addHandler(getPaymentRequestGatewayTopics(), this::createPaymentConsumer);
        queue.addHandler(getHistoryRequestGatewayTopics(), this::getHistoryPaymentConsumer);
        queue.addHandler(getTargetPaymentRequestGatewayTopics(), this::getTargetPaymentConsumer);
    }

    private void createPaymentConsumer(Event event) {
        var payment = event.getArgument(0, Payment.class);
        createPayment.complete(payment);
    }

    private void getHistoryPaymentConsumer(Event event) {
        List<Payment> payments = (List<Payment>) event.getArgument(0, List.class);
        getPaymentsForUser.complete(payments);
    }

    private void getTargetPaymentConsumer(Event event) {
        Payment payment = event.getArgument(0, Payment.class);
        getTargetPayment.complete(payment);
    }

    public void publishEvent(Event event) {
        queue.publish(event);
    }
}