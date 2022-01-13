package dk.dtu.team14.services;

import event.QueueNames;
import messaging.Event;
import messaging.MessageQueue;
import messaging.implementations.RabbitMqQueue;
import rest.Payment;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static event.PaymentEvents.*;

@ApplicationScoped
public class PaymentService {

    private final MessageQueue queue = new RabbitMqQueue(QueueNames.getQueueName());

    public CompletableFuture<Payment> createPayment = new CompletableFuture<>();
    public CompletableFuture<List<Payment>> getPaymentsForUser = new CompletableFuture<>();
    public CompletableFuture<List<Payment>> getAllPayments = new CompletableFuture<>();
    public CompletableFuture<List<Payment>> getTargetPayment = new CompletableFuture<>();

    public PaymentService() {
        queue.addHandler(getPaymentRequestGatewayTopics(), this::createPaymentConsumer);
        queue.addHandler(getHistoryRequestGatewayTopics(), this::getPaymentConsumer);
        queue.addHandler(getAllHistoryRequestGatewayTopics(), this::getAllPaymentsConsumer);
        queue.addHandler(getTargetPaymentRequestGatewayTopics(), this::getTargetPaymentConsumer);
    }

    private void createPaymentConsumer(Event event) {
        var payment = event.getArgument(0, Payment.class);
        createPayment.complete(payment);
    }

    private void getPaymentConsumer(Event event) {
        List<Payment> payments = (List<Payment>) event.getArgument(0, List.class);
        getPaymentsForUser.complete(payments);
    }

    private void getAllPaymentsConsumer(Event event) {
        List<Payment> payments = (List<Payment>) event.getArgument(0, List.class);
        getPaymentsForUser.complete(payments);
    }

    private void getTargetPaymentConsumer(Event event) {
        List<Payment> payments = (List<Payment>) event.getArgument(0, List.class);
        getPaymentsForUser.complete(payments);
    }

    public void publishEvent(Event event) {
        queue.publish(event);
    }
}