package org.example.shoppefood.api;
import com.google.cloud.dialogflow.v2.WebhookRequest;
import org.example.shoppefood.dto.ProductDTO;
import org.example.shoppefood.dto.output.PurchasedOrderDTO;
import org.example.shoppefood.dto.responsePage.ResponsePage;
import org.example.shoppefood.entity.ProductEntity;
import org.example.shoppefood.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api")
public class ProductAPI {
    @Autowired
     private ProductService productService;
    @GetMapping("/products")
       public ResponsePage<List<ProductDTO>> getProducts(
               @RequestParam (defaultValue = "0") int page,
               @RequestParam (defaultValue = "100") int size
    ) {
        Pageable pageable = PageRequest.of(page,size);
        return productService.getAllProducts(pageable);
    }
   @GetMapping ("/product/{id}")
    public ProductDTO getProductById(@PathVariable int id) {
        return productService.getProductById(id);
   }
   @GetMapping("/products/{id}")
    public ResponsePage<List<ProductDTO>> getProductsByCategoryId (
            @PathVariable Long id,
             @RequestParam (defaultValue = "0") int page,
              @RequestParam (defaultValue = "100") int size
   ) {
       Pageable pageable = PageRequest . of (page, size);
        return productService.getProductsByCategoryId(id,pageable);
   }
    @GetMapping("/find/products")
    public ResponsePage<List<ProductDTO>> getProductsByName (
            @RequestParam String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size){
        Pageable pageable = PageRequest.of(page, size);
        return productService.searchProducts(name, pageable);
    }
    @GetMapping("/find/productsByCategory/")
    public ResponsePage<List<ProductDTO>> getProductsByCategory(
            @RequestParam Long categoryID,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productService.searchProductsByCategoryId(categoryID, pageable);
    }
      @GetMapping ("/countProductsByCategory")
       public List<Object[]> countProductsByCategory ()    {
        return productService.getCategoryAndCountProduct();
      }

      @GetMapping("/purchasedOrder/{id}")
      public List<PurchasedOrderDTO> getPurchasedOrder (@PathVariable Long id){
        return productService.findPurchasedProductsByOrderId(id);
      }

    @PostMapping("/webhook")
    public String webhook(@RequestBody WebhookRequest request) {
          String queryName = request.getQueryResult()
                .getParameters()
                .getFieldsMap()
                .get("product-name")
                .getStringValue();
        List<ProductEntity> results = productService.findProductsByName(queryName);

        if (results.isEmpty()) {
            return "{ \"fulfillmentText\": \"Không tìm thấy sản phẩm phù hợp.\" }";
        } else {
            ProductEntity p = results.get(0);
            return String.format(
                    "{ \"fulfillmentText\": \"Sản phẩm %s có giá %.2f VNĐ.\" }",
                    p.getProductName(), p.getPrice()
            );
        }
    }

}
