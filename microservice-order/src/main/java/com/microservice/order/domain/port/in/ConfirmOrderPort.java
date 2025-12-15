package com.microservice.order.domain.port.in;

import java.util.UUID;

/**
 * Inbound port for confirming orders after payment.
 */
public interface ConfirmOrderPort {

    void execute(ConfirmOrderCommand command);

    record ConfirmOrderCommand(
            UUID orderId,
            UUID paymentId,
            String confirmedBy) {
    }
}
