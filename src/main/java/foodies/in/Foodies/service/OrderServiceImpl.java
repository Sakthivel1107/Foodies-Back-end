package foodies.in.Foodies.service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import foodies.in.Foodies.io.OrderRequest;
import foodies.in.Foodies.io.OrderResponse;
import foodies.in.Foodies.models.OrderEntity;
import foodies.in.Foodies.repository.CartRepository;
import foodies.in.Foodies.repository.OrderRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private UserService userService;
    @Value("${razorpay_key}")
    private String RAZORPAY_KEY;
    @Value("${razorpay_secret}")
    private String RAZORPAY_SECRET;

    @Override
    public OrderResponse CreateOrderWithPayment(OrderRequest orderRequest) throws RazorpayException {
        OrderEntity newOrder = convertToEntity(orderRequest);
        newOrder = orderRepository.save(newOrder);

        //create Razorpay payment order
        RazorpayClient razorpayClient = new RazorpayClient(RAZORPAY_KEY, RAZORPAY_SECRET);
        JSONObject request = new JSONObject();
        request.put("amount", newOrder.getAmount() * 100);
        request.put("currency", "INR");
        request.put("payment_capture", 1);
        Order razorpayOrder = razorpayClient.orders.create(request);
        newOrder.setRazorPayOrderId(razorpayOrder.get("id"));
        String loggedInUserId = userService.findByUserId();
        newOrder.setUserId(loggedInUserId);
        newOrder = orderRepository.save(newOrder);
        return convertToResponse(newOrder);
    }

    @Override
    public void verifyPayment(Map<String, String> paymentData) {
        String razorpayOrderId = paymentData.get("razorpay_order_id");
        OrderEntity existingOrder = orderRepository.findByRazorpayOrderId(razorpayOrderId).orElseThrow(() -> new RuntimeException("Order Not Found"));
        existingOrder.setPaymentStatus(paymentData.get("order_status"));
        existingOrder.setRazorPaySignature(paymentData.get("razorpay_signature"));
        existingOrder.setRazorPayPaymentId(paymentData.get("razorpay_payment_id"));
        orderRepository.save(existingOrder);
        if ("paid".equalsIgnoreCase(paymentData.get("order_status"))) {
            cartRepository.deleteById(existingOrder.getUserId());
        }
    }

    @Override
    public List<OrderResponse> getUserOrders() {
        String loggedInUserId = userService.findByUserId();
        List<OrderEntity> list = orderRepository.findByUserId(loggedInUserId);
        return list.stream().map(entity -> convertToResponse(entity)).collect(Collectors.toList());
    }

    @Override
    public void removeOrder(String orderId) {
        orderRepository.deleteOrderById(orderId);
    }

    @Override
    public List<OrderResponse> getOrdersOfAllUsers() {
        List<OrderEntity> orders = orderRepository.getAllOrders();
        return orders.stream().map(entity -> convertToResponse(entity)).collect(Collectors.toList());
    }

    @Override
    public void updateOrderStatus(String orderId,String status) {
        OrderEntity entity =orderRepository.findById(orderId).orElseThrow(()-> new RuntimeException("Order not found"));
        entity.setOrderStatus(status);
        orderRepository.save(entity);
    }

    public OrderEntity convertToEntity(OrderRequest orderRequest) {
        return OrderEntity.builder()
                .userAddress(orderRequest.getUserAddress())
                .amount(orderRequest.getAmount())
                .orderedItems(orderRequest.getOrderedItems())
                .email(orderRequest.getEmail())
                .phoneNumber(orderRequest.getPhoneNumber())
                .orderStatus(orderRequest.getOrderStatus())
                .paymentStatus(orderRequest.getPaymentStatus())
                .build();
    }

    public OrderResponse convertToResponse(OrderEntity orderEntity) {
        return OrderResponse.builder()
                .id(orderEntity.getId())
                .amount(orderEntity.getAmount())
                .userAddress(orderEntity.getUserAddress())
                .userId(orderEntity.getUserId())
                .razorPayOrderId(orderEntity.getRazorPayOrderId())
                .paymentStatus(orderEntity.getPaymentStatus())
                .orderStatus(orderEntity.getOrderStatus())
                .email(orderEntity.getEmail())
                .phoneNumber(orderEntity.getPhoneNumber())
                .orderedItems(orderEntity.getOrderedItems())
                .build();
    }

}
