package event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static event.GatewayEvents.getGatewayEventName;

@Data
@NoArgsConstructor
//@AllArgsConstructor
public class PaymentEvents {

    public static String getPaymentEventName() {
        return "PAYMENT";
    }

    public static String getPaymentRequestTopics() {
        return getPaymentEventName() + ".PAYMENT_REQUEST";
    }

    public static String getTargetPaymentRequestTopics() {
        return getPaymentEventName() + ".TARGET_PAYMENT_REQUEST";
    }

    public static String getHistoryRequestTopics() {
        return getPaymentEventName() + ".GET_HISTORY_REQUEST";
    }

    public static String getAllHistoryRequestTopics() {
        return getPaymentEventName() + ".GET_ALL_HISTORY_REQUEST";
    }

    public static String getPaymentRequestGatewayTopics() {
        return getGatewayEventName() + ".CREATE_PAYMENT_REQUEST";
    }

    public static String getHistoryRequestGatewayTopics() {
        return getGatewayEventName() + ".GET_HISTORY_REQUEST";
    }

    public static String getAllHistoryRequestGatewayTopics() {
        return getGatewayEventName() + ".GET_ALL_HISTORY_REQUEST";
    }

    public static String getTargetPaymentRequestGatewayTopics() {
        return getGatewayEventName() + ".TARGET_PAYMENT_REQUEST";
    }

}
