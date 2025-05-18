package org.example.shoppefood.api;
import org.example.shoppefood.dto.OrderDTO;
import org.example.shoppefood.dto.OrderDetailDTO;
import org.example.shoppefood.dto.ProductDTO;
import org.example.shoppefood.dto.cart.CartItemDTO;
import org.example.shoppefood.dto.request.CheckoutRequestDTO;
import org.example.shoppefood.entity.OrderDetailEntity;
import org.example.shoppefood.entity.OrderEntity;
import org.example.shoppefood.entity.ProductEntity;
import org.example.shoppefood.mapper.OrderDetailMapper;
import org.example.shoppefood.mapper.OrderMapper;
import org.example.shoppefood.mapper.ProductMapper;
import org.example.shoppefood.repository.OrderDetailRepository;
import org.example.shoppefood.repository.OrderRepository;
import org.example.shoppefood.repository.ProductRepository;
import org.example.shoppefood.service.Cart.CartService;
import org.example.shoppefood.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.util.List;
@RestController
@RequestMapping("api/cart")
@SessionAttributes("cart")
public class CastRestAPI {
    @Autowired
    ProductService productService;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    ProductMapper productMapper;
    @Autowired
    private CartService cartService;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @GetMapping
    public List<CartItemDTO> getCartItem(){
        return cartService.getAllItems();
    }
    @PostMapping
    public ResponseEntity<String> addCartItem(
            @RequestParam int productId,
            @RequestParam int quantity
    ){
         ProductEntity productEntity = productRepository.findByProductId(productId);
         if(productEntity == null){
             return ResponseEntity.notFound().build();
         }
         ProductDTO productDTO = productMapper.entityToDto(productEntity);
         cartService.addToCart(productDTO, quantity);
         return ResponseEntity.ok("Đã thêm sản phẩm vào giỏ ");
    }
   @DeleteMapping("/remove/{productId}")
   public ResponseEntity<String> removeFromCart(@PathVariable Long productId) {
       cartService.removeFromCart(productId);
       return ResponseEntity.ok("Đã xóa sản phẩm khỏi giỏ");
   }
    @PostMapping("/checkout")
    public ResponseEntity<String> checkout(@RequestBody CheckoutRequestDTO request) {
        List<CartItemDTO> cartItems = cartService.getAllItems();
        if (cartItems.size() == 0) {
            return ResponseEntity.notFound().build();
        }
        long i = 9; // Chỉ số người dùng, thay thế bằng ID thực tế
        int j = 1;  // Trạng thái đơn hàng (ví dụ: 1 - Đang chờ xử lý)
        // Tạo OrderDTO
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setAddress(request.getAddress());
        orderDTO.setPhone(request.getPhone());
        orderDTO.setOrderDate(new Date());
        orderDTO.setUserId(i);
        orderDTO.setStatus(j);
        orderDTO.setAmount(cartService.getTotal());
        // Chuyển OrderDTO thành OrderEntity và lưu
        OrderEntity orderEntity = orderMapper.toEntity(orderDTO);
        orderRepository.save(orderEntity);
        // Tạo OrderDetailDTO và liên kết với OrderEntity đã lưu
        for (CartItemDTO cartItemDTO : cartItems) {
            OrderDetailDTO orderDetailDTO = new OrderDetailDTO();
            orderDetailDTO.setOrderId(orderEntity.getOrderId()); // Thiết lập orderId từ OrderEntity đã lưu
            orderDetailDTO.setProductId(cartItemDTO.getProduct().getProductId());
            orderDetailDTO.setPrice(cartService.getTotal()); // Giá trị đơn hàng
            orderDetailDTO.setQuantity(cartItemDTO.getQuantity()); // Số lượng sản phẩm
            // Chuyển OrderDetailDTO thành OrderDetailEntity và lưu
            OrderDetailEntity orderDetailEntity = orderDetailMapper.toEntity(orderDetailDTO);
            orderDetailRepository.save(orderDetailEntity);
        }
        // Xóa giỏ hàng sau khi hoàn thành đặt hàng
        cartService.clearCart();
        return ResponseEntity.ok("Đã đặt thành công đơn hàng");
    }
}
