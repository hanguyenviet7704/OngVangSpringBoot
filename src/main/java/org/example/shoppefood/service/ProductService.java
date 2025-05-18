package org.example.shoppefood.service;

import org.example.shoppefood.dto.ProductDTO;
import org.example.shoppefood.dto.output.PurchasedOrderDTO;
import org.example.shoppefood.dto.responsePage.ResponsePage;
import org.example.shoppefood.entity.ProductEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface ProductService {
   public ResponsePage<List<ProductDTO>> getAllProducts(Pageable pageable);

   public ProductDTO getProductById(int id);

   public ResponsePage<List<ProductDTO>> getProductsByCategoryId(Long categoryId, Pageable pageable);

   public ResponsePage<List<ProductDTO>> searchProducts(String keyword, Pageable pageable);

   public ResponsePage<List<ProductDTO>> searchProductsByCategoryId(Long categoryId, Pageable pageable);

   List<Object[]> getCategoryAndCountProduct();

   List<PurchasedOrderDTO> findPurchasedProductsByOrderId(Long orderId);

   public List<ProductEntity> findProductsByName(String name) ;

}