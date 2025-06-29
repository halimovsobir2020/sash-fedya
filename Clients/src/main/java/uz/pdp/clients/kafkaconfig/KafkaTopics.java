package uz.pdp.clients.kafkaconfig;

public interface KafkaTopics {

    String PRODUCT_LEFTOVER_CHANGE = "PRODUCT_LEFTOVER_CHANGE";
    String ORDER_SUCCEED = "ORDER_SUCCEED";
    String ORDER_ROLLBACK = "ORDER_ROLLBACK";
    String PAYMENT_ROLLBACK = "PAYMENT_ROLLBACK";
    String PRODUCT_ROLLBACK = "PRODUCT_ROLLBACK";
    String OUTCOME_STORE = "OUTCOME_STORE";
    String CREATE_ORDER_PAYMENT = "CREATE_ORDER_PAYMENT";

}
