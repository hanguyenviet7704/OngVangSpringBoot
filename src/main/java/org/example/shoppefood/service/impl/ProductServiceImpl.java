package org.example.shoppefood.service.impl;
import org.example.shoppefood.dto.ProductDTO;
import org.example.shoppefood.dto.output.PurchasedOrderDTO;
import org.example.shoppefood.dto.responsePage.ResponsePage;
import org.example.shoppefood.entity.ProductEntity;
import org.example.shoppefood.mapper.ProductMapper;
import org.example.shoppefood.repository.ProductRepository;
import org.example.shoppefood.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductMapper productMapper;
    public ResponsePage<List<ProductDTO>> getAllProducts(Pageable pageable) {
        Page<ProductEntity> productPage = productRepository.findAll(pageable);
        List<ProductEntity> productEntities = productPage.getContent();
        List<ProductDTO> productDTOS = productMapper.entityToDtoList(productEntities);
        ResponsePage<List<ProductDTO>> responsePage = new ResponsePage<>();
        responsePage.setContent(productDTOS);
        responsePage.setPageNumber(productPage.getNumber());
        responsePage.setPageSize(productPage.getSize());
        responsePage.setTotalElements(productPage.getTotalElements());
        responsePage.setTotalPages(productPage.getTotalPages());
        return responsePage;
    }
    public ProductDTO getProductById(int id) {
        ProductEntity productEntity = productRepository.findByProductId(id);
        ProductDTO productDTO = productMapper.entityToDto(productEntity);
        return productDTO;
    }
    public ResponsePage<List<ProductDTO>> getProductsByCategoryId(Long categoryId, Pageable pageable) {
        Page<ProductEntity> productPage = productRepository.findByCategoryId(categoryId, pageable);
        List<ProductEntity> productEntities = productPage.getContent();
        List<ProductDTO> productDTOS = productMapper.entityToDtoList(productEntities);
        ResponsePage<List<ProductDTO>> responsePage = new ResponsePage<>();
        responsePage.setContent(productDTOS);
        responsePage.setPageNumber(productPage.getNumber());
        responsePage.setPageSize(productPage.getSize());
        responsePage.setTotalElements(productPage.getTotalElements());
        responsePage.setTotalPages(productPage.getTotalPages());
        return responsePage;
    }
    public ResponsePage<List<ProductDTO>> searchProducts(String keyword, Pageable pageable){
        Page<ProductEntity> productPage = productRepository.searchByNameLike(pageable, keyword);
        List<ProductEntity> productEntities = productPage.getContent();
        List<ProductDTO> productDTOS = productMapper.entityToDtoList(productEntities);
        ResponsePage<List<ProductDTO>> responsePage = new ResponsePage<>();
        responsePage.setContent(productDTOS);
        responsePage.setPageNumber(productPage.getNumber());
        responsePage.setPageSize(productPage.getSize());
        responsePage.setTotalElements(productPage.getTotalElements());
        responsePage.setTotalPages(productPage.getTotalPages());
        return responsePage;
    }
    public ResponsePage<List<ProductDTO>> searchProductsByCategoryId(Long categoryId, Pageable pageable){
        Page<ProductEntity> productPage = productRepository.searchByCategoryId(pageable,categoryId);
        List<ProductEntity> productEntities = productPage.getContent();
        List<ProductDTO> productDTOS = productMapper.entityToDtoList(productEntities);
        ResponsePage<List<ProductDTO>> responsePage = new ResponsePage<>();
        responsePage.setContent(productDTOS);
        responsePage.setPageNumber(productPage.getNumber());
        responsePage.setPageSize(productPage.getSize());
        responsePage.setTotalElements(productPage.getTotalElements());
        responsePage.setTotalPages(productPage.getTotalPages());
        return responsePage;
    }
    public List<Object[]> getCategoryAndCountProduct(){
        List<Object[]> objects = productRepository.countProductsByCategory();
        return objects;
    }

    public List<PurchasedOrderDTO> findPurchasedProductsByOrderId(Long orderId){
        List<Object[]> purchasedOrderObjects = productRepository.findPurchasedProductsByOrderId(orderId);
        List<PurchasedOrderDTO> purchasedOrderDTOS = new ArrayList<>();
        for(Object[] objects : purchasedOrderObjects){
            PurchasedOrderDTO purchasedOrderDTO = new PurchasedOrderDTO();
            purchasedOrderDTO.setProductImage(objects[0].toString());
            purchasedOrderDTO.setProductName(objects[1].toString());
            purchasedOrderDTO.setProductPrice(((Number) objects[2]).doubleValue());
            purchasedOrderDTO.setProductQuantity(((Number) objects[3]).intValue());
            purchasedOrderDTO.setTotalAmount(((Number) objects[4]).doubleValue());
            purchasedOrderDTOS.add(purchasedOrderDTO);
        }
        return purchasedOrderDTOS;
    }

    public List<ProductEntity> findProductsByName(String name) {
        return productRepository.findProductByNameLike(name);
    }


}
