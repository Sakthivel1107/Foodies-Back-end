package foodies.in.Foodies.service;


import com.razorpay.RazorpayException;
import foodies.in.Foodies.io.OrderRequest;
import foodies.in.Foodies.io.OrderResponse;

import java.util.List;
import java.util.Map;

public interface OrderService {
    OrderResponse CreateOrderWithPayment(OrderRequest orderRequest) throws RazorpayException;
    void verifyPayment(Map<String,String> paymentData);

    List<OrderResponse> getUserOrders();

    void removeOrder(String orderId);

    List<OrderResponse> getOrdersOfAllUsers();

    void updateOrderStatus(String orderId,String status);
}
