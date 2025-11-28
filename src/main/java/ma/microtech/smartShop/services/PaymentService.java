package ma.microtech.smartShop.services;

import java.util.List;

import ma.microtech.smartShop.dto.request.PaymentRequest;
import ma.microtech.smartShop.dto.response.PaymentResponse;

public interface PaymentService {

    PaymentResponse recordPayment(PaymentRequest request);

    PaymentResponse encashPayment(Long paymentId);

    PaymentResponse rejectPayment(Long paymentId);

    List<PaymentResponse> findByOrderId(Long orderId);

    PaymentResponse findById(Long paymentId);
}
