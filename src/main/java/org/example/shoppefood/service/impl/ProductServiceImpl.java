package org.example.shoppefood.service.impl;
import org.example.shoppefood.dto.ProductDTO;
import org.example.shoppefood.dto.output.PurchasedOrderDTO;
import org.example.shoppefood.dto.responsePage.ResponsePage;
import org.example.shoppefood.entity.ProductEntity;
import org.example.shoppefood.exception.ProductDeletionException;
import org.example.shoppefood.mapper.ProductMapper;
import org.example.shoppefood.repository.ProductRepository;
import org.example.shoppefood.repository.CategoryRepository;
import org.example.shoppefood.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;
import javax.annotation.PostConstruct;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private CategoryRepository categoryRepository;

    @Value("${upload.path}")
    private String uploadPathString;

    private Path rootLocation;

    @PostConstruct
    public void init() {
        this.rootLocation = Paths.get(uploadPathString);
        try {
            Files.createDirectories(rootLocation);
            System.out.println("Initialized upload directory: " + rootLocation.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("Could not initialize storage location: " + uploadPathString + ": " + e.getMessage());
            throw new RuntimeException("Could not initialize storage location", e);
        }
    }

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
    public ProductDTO getProductById(Long id) {
        ProductEntity productEntity = productRepository.findByProductId( id);
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

    @Override
    public ProductDTO save(ProductDTO productDTO, MultipartFile file) {
        ProductEntity productEntity;
        
        // Handle fetching existing product for update
        if (productDTO.getProductId() != null) {
            Optional<ProductEntity> existingProduct = productRepository.findById(productDTO.getProductId());
            if (existingProduct.isPresent()) {
                productEntity = existingProduct.get();
            } else {
                 // Product not found for update - log and throw specific exception
                 System.err.println("Product with ID " + productDTO.getProductId() + " not found for update.");
                 throw new RuntimeException("Product with ID " + productDTO.getProductId() + " not found for update.");
            }
        } else {
            // Create new product
            productEntity = new ProductEntity(); // Create a new instance
        }

        // Update basic fields from DTO
        productEntity.setProductName(productDTO.getProductName());
        productEntity.setQuantity(productDTO.getQuantity());
        productEntity.setPrice(productDTO.getPrice());
        productEntity.setDiscount(productDTO.getDiscount());
        productEntity.setDescription(productDTO.getDescription());
        // Cập nhật ngày nhập hàng là ngày hiện tại
        productEntity.setEnteredDate(new Date());
        // productEntity.setStatus(productDTO.getStatus()); // Handle status if needed

        // Fetch and set Category
        if (productDTO.getCategoryId() != null) {
            Optional<org.example.shoppefood.entity.CategoryEntity> categoryEntityOptional = categoryRepository.findById(productDTO.getCategoryId());
            if (categoryEntityOptional.isPresent()) {
                productEntity.setCategory(categoryEntityOptional.get());
            } else {
                // Category not found - log and handle (e.g., set category to null or throw error)
                 System.err.println("Category with ID " + productDTO.getCategoryId() + " not found.");
                 productEntity.setCategory(null); // Or throw new RuntimeException("Category not found");
            }
        } else {
             productEntity.setCategory(null); // Handle case where categoryId is null in DTO
        }

        // Handle file upload
        if (file != null && !file.isEmpty()) {
            try {
                String originalFilename = file.getOriginalFilename();
                 if (originalFilename == null || originalFilename.isEmpty()) { // Handle empty original filename
                     System.err.println("Original filename is null or empty for file upload.");
                      // Optionally skip saving file or throw error
                 } else {
                    String fileExtension = "";
                    int dotIndex = originalFilename.lastIndexOf(".");
                    if (dotIndex >= 0) {
                        fileExtension = originalFilename.substring(dotIndex);
                    }
                    String newFilename = UUID.randomUUID().toString() + fileExtension;
                    Path filePath = this.rootLocation.resolve(newFilename);
                     System.out.println("Saving file to: " + filePath.toAbsolutePath());
                    Files.copy(file.getInputStream(), filePath);
                    productEntity.setProductImage(newFilename); // Lưu tên file vào database
                 }
            } catch (IOException e) {
                 System.err.println("Failed to store file: " + e.getMessage());
                 throw new RuntimeException("Failed to store file", e); // Re-throw exception
            } catch (Exception e) {
                 System.err.println("Failed to process file: " + e.getMessage());
                throw new RuntimeException("Failed to process file", e); // Re-throw exception
            }
        } else if (productDTO.getProductId() == null) {
             // If creating a new product and no file is provided
             productEntity.setProductImage(null); // Or set a default image name if required
        }
        // If updating an existing product and no new file is provided, the existing productImage name is retained.

        try {
            productEntity = productRepository.save(productEntity);
             // Convert saved entity back to DTO for response, ensuring categoryName is populated
            return productMapper.entityToDto(productEntity);
        } catch (Exception e) {
            // Log exception and re-throw
             System.err.println("Failed to save product to repository: " + e.getMessage());
            throw new RuntimeException("Failed to save product", e); // Re-throw exception
        }
    }

    @Override
    public void deleteById(Long id) {
         try {
             productRepository.deleteById(id);
         } catch (Exception e) {
             System.err.println("Sản phẩm có trong giỏ hàng của khách hàng hoặc đang trên đường giao  " + id + ": " + e.getMessage());
             throw new ProductDeletionException("Sản phẩm có trong giỏ hàng của khách hàng hoặc đang trên đường giao", e);
         }
    }
}
