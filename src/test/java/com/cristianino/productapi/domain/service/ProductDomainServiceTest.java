package com.cristianino.productapi.domain.service;package com.cristianino.productapi.domain.service;package com.cristianino.productapi.domain.service;package com.cristianino.productapi.domain.service;



import com.cristianino.productapi.domain.model.Product;

import com.cristianino.productapi.domain.port.ProductRepository;

import org.junit.jupiter.api.BeforeEach;import com.cristianino.productapi.domain.model.Product;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;import com.cristianino.productapi.domain.port.ProductRepository;

import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;import org.junit.jupiter.api.BeforeEach;import com.cristianino.productapi.domain.model.Product;import com.cristianino.productapi.domain.model.Product;



import java.math.BigDecimal;import org.junit.jupiter.api.Test;

import java.util.Arrays;

import java.util.List;import org.junit.jupiter.api.extension.ExtendWith;import com.cristianino.productapi.domain.port.ProductRepository;import com.cristianino.productapi.domain.port.ProductRepository;

import java.util.Optional;

import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.any;import org.mockito.junit.jupiter.MockitoExtension;import org.junit.jupiter.api.BeforeEach;import org.junit.jupiter.api.BeforeEach;

import static org.mockito.ArgumentMatchers.anyLong;

import static org.mockito.Mockito.*;



@ExtendWith(MockitoExtension.class)import java.math.BigDecimal;import org.junit.jupiter.api.Test;import org.junit.jupiter.api.Test;

class ProductDomainServiceTest {

import java.util.Arrays;

    @Mock

    private ProductRepository productRepository;import java.util.List;import org.junit.jupiter.api.extension.ExtendWith;import org.junit.jupiter.api.extension.ExtendWith;



    private ProductDomainService productDomainService;import java.util.Optional;

    private Product product;

import org.mockito.Mock;import org.mockito.Mock;

    @BeforeEach

    void setUp() {import static org.junit.jupiter.api.Assertions.*;

        productDomainService = new ProductDomainService(productRepository);

        import static org.mockito.ArgumentMatchers.any;import org.mockito.junit.jupiter.MockitoExtension;import org.mockito.junit.jupiter.MockitoExtension;

        product = new Product();

        product.setId(1L);import static org.mockito.ArgumentMatchers.anyLong;

        product.setName("Test Product");

        product.setDescription("Test Description");import static org.mockito.Mockito.*;

        product.setPrice(new BigDecimal("99.99"));

        product.setStock(10);

        product.setCategory("Electronics");

        product.setBrand("TestBrand");@ExtendWith(MockitoExtension.class)import java.math.BigDecimal;import java.math.BigDecimal;

        product.setSku("TEST-001");

        product.setActive(true);class ProductDomainServiceTest {

    }

    import java.util.Arrays;import java.util.List;

    @Test

    void createProduct_ValidProduct_Success() {    @Mock

        // Arrange

        when(productRepository.save(any(Product.class))).thenReturn(product);    private ProductRepository productRepository;import java.util.List;import java.util.Optional;



        // Act    

        Product result = productDomainService.createProduct(product);

    private ProductDomainService productDomainService;import java.util.Optional;

        // Assert

        assertNotNull(result);    private Product testProduct;

        assertEquals(product.getName(), result.getName());

        assertEquals(product.getPrice(), result.getPrice());    import static org.junit.jupiter.api.Assertions.*;

        verify(productRepository).save(product);

    }    @BeforeEach



    @Test    void setUp() {import static org.junit.jupiter.api.Assertions.*;import static org.mockito.ArgumentMatchers.any;

    void findProductById_ExistingProduct_ReturnsProduct() {

        // Arrange        productDomainService = new ProductDomainService(productRepository);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        testProduct = new Product();import static org.mockito.ArgumentMatchers.any;import static org.mockito.Mockito.*;

        // Act

        Optional<Product> result = productDomainService.findProductById(1L);        testProduct.setId(1L);



        // Assert        testProduct.setName("Test Product");import static org.mockito.ArgumentMatchers.anyLong;

        assertTrue(result.isPresent());

        assertEquals(product.getName(), result.get().getName());        testProduct.setDescription("Test Description");

        verify(productRepository).findById(1L);

    }        testProduct.setPrice(new BigDecimal("99.99"));import static org.mockito.Mockito.*;@ExtendWith(MockitoExtension.class)



    @Test    }

    void findProductById_NonExistingProduct_ReturnsEmpty() {

        // Arrange    class ProductDomainServiceTest {

        when(productRepository.findById(2L)).thenReturn(Optional.empty());

    @Test

        // Act

        Optional<Product> result = productDomainService.findProductById(2L);    void testCreateProduct() {@ExtendWith(MockitoExtension.class)    



        // Assert        // Given

        assertFalse(result.isPresent());

        verify(productRepository).findById(2L);        Product productToCreate = new Product();class ProductDomainServiceTest {    @Mock

    }

        productToCreate.setName("New Product");

    @Test

    void getAllProducts_ReturnsAllProducts() {        productToCreate.setDescription("New Description");        private ProductRepository productRepository;

        // Arrange

        Product product2 = new Product();        productToCreate.setPrice(new BigDecimal("149.99"));

        product2.setId(2L);

        product2.setName("Another Product");            @Mock    

        product2.setPrice(new BigDecimal("149.99"));

                when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        List<Product> products = Arrays.asList(product, product2);

        when(productRepository.findAll()).thenReturn(products);            private ProductRepository productRepository;    private ProductDomainService productDomainService;



        // Act        // When

        List<Product> result = productDomainService.getAllProducts();

        Product result = productDomainService.createProduct(productToCreate);    

        // Assert

        assertEquals(2, result.size());        

        assertTrue(result.contains(product));

        assertTrue(result.contains(product2));        // Then    private ProductDomainService productDomainService;        

        verify(productRepository).findAll();

    }        assertNotNull(result);



    @Test        assertEquals(testProduct.getId(), result.getId());    private Product testProduct;

    void updateProduct_ExistingProduct_Success() {

        // Arrange        assertEquals(testProduct.getName(), result.getName());

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        when(productRepository.save(any(Product.class))).thenReturn(product);        verify(productRepository).save(productToCreate);        @BeforeEach    @BeforeEach



        product.setName("Updated Product");    }

        product.setPrice(new BigDecimal("129.99"));

        @BeforeEach

        // Act

        Optional<Product> result = productDomainService.updateProduct(1L, product);    @Test



        // Assert    void testGetProductById_Found() {    void setUp() {    void setUp() {    void setUp() {

        assertTrue(result.isPresent());

        assertEquals("Updated Product", result.get().getName());        // Given

        assertEquals(new BigDecimal("129.99"), result.get().getPrice());

        verify(productRepository).findById(1L);        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));        productDomainService = new ProductDomainService(productRepository);

        verify(productRepository).save(any(Product.class));

    }        



    @Test        // When        testProduct = Product.builder()        productDomainService = new ProductDomainService(productRepository);        productDomainService = new ProductDomainService(productRepository);

    void updateProduct_NonExistingProduct_ReturnsEmpty() {

        // Arrange        Optional<Product> result = productDomainService.getProductById(1L);

        when(productRepository.findById(2L)).thenReturn(Optional.empty());

                        .id(1L)

        // Act

        Optional<Product> result = productDomainService.updateProduct(2L, product);        // Then



        // Assert        assertTrue(result.isPresent());                .name("Test Product")    }    }

        assertFalse(result.isPresent());

        verify(productRepository).findById(2L);        assertEquals(testProduct, result.get());

        verify(productRepository, never()).save(any(Product.class));

    }        verify(productRepository).findById(1L);                .description("Test Description")



    @Test    }

    void deleteProduct_ExistingProduct_Success() {

        // Arrange                    .price(new BigDecimal("99.99"))        

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

    @Test

        // Act

        boolean result = productDomainService.deleteProduct(1L);    void testGetProductById_NotFound() {                .build();



        // Assert        // Given

        assertTrue(result);

        verify(productRepository).findById(1L);        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());    }    @Test    @Test

        verify(productRepository).delete(product);

    }        



    @Test        // When    

    void deleteProduct_NonExistingProduct_ReturnsFalse() {

        // Arrange        Optional<Product> result = productDomainService.getProductById(999L);

        when(productRepository.findById(2L)).thenReturn(Optional.empty());

            @Test    void createProduct_ValidProduct_ReturnsProduct() {    void createProduct_ValidProduct_ReturnsProduct() {

        // Act

        boolean result = productDomainService.deleteProduct(2L);        // Then



        // Assert        assertFalse(result.isPresent());    void testCreateProduct() {

        assertFalse(result);

        verify(productRepository).findById(2L);        verify(productRepository).findById(999L);

        verify(productRepository, never()).delete(any(Product.class));

    }    }        // Given        // Given        // Given



    @Test    

    void findByCategory_ExistingCategory_ReturnsProducts() {

        // Arrange    @Test        Product productToCreate = Product.builder()

        List<Product> electronics = Arrays.asList(product);

        when(productRepository.findByCategory("Electronics")).thenReturn(electronics);    void testGetAllProducts() {



        // Act        // Given                .name("New Product")        Product inputProduct = new Product(null, "Test Product", new BigDecimal("99.99"));        Product inputProduct = new Product(null, "Test Product", new BigDecimal("99.99"));

        List<Product> result = productDomainService.findByCategory("Electronics");

        Product product2 = new Product();

        // Assert

        assertEquals(1, result.size());        product2.setId(2L);                .description("New Description")

        assertEquals(product.getCategory(), result.get(0).getCategory());

        verify(productRepository).findByCategory("Electronics");        product2.setName("Product 2");

    }

        product2.setDescription("Description 2");                .price(new BigDecimal("149.99"))        Product savedProduct = new Product(1L, "Test Product", new BigDecimal("99.99"));        Product savedProduct = new Product(1L, "Test Product", new BigDecimal("99.99"));

    @Test

    void findByNameContaining_MatchingName_ReturnsProducts() {        product2.setPrice(new BigDecimal("199.99"));

        // Arrange

        List<Product> products = Arrays.asList(product);                        .build();

        when(productRepository.findByNameContaining("Test")).thenReturn(products);

        List<Product> products = Arrays.asList(testProduct, product2);

        // Act

        List<Product> result = productDomainService.findByNameContaining("Test");        when(productRepository.findAll()).thenReturn(products);                when(productRepository.save(any(Product.class))).thenReturn(savedProduct);        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);



        // Assert        

        assertEquals(1, result.size());

        assertTrue(result.get(0).getName().contains("Test"));        // When        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        verify(productRepository).findByNameContaining("Test");

    }        List<Product> result = productDomainService.getAllProducts();



    @Test                                

    void validateProduct_ValidProduct_NoException() {

        // Act & Assert - Should not throw any exception        // Then

        assertDoesNotThrow(() -> productDomainService.validateProduct(product));

    }        assertNotNull(result);        // When



    @Test        assertEquals(2, result.size());

    void validateProduct_NullName_ThrowsException() {

        // Arrange        assertEquals(products, result);        Product result = productDomainService.createProduct(productToCreate);        // When        // When

        product.setName(null);

        verify(productRepository).findAll();

        // Act & Assert

        assertThrows(IllegalArgumentException.class, () -> productDomainService.validateProduct(product));    }        

    }

    

    @Test

    void validateProduct_EmptyName_ThrowsException() {    @Test        // Then        Product result = productDomainService.createProduct(inputProduct);        Product result = productDomainService.createProduct(inputProduct);

        // Arrange

        product.setName("");    void testUpdateProduct_Found() {



        // Act & Assert        // Given        assertNotNull(result);

        assertThrows(IllegalArgumentException.class, () -> productDomainService.validateProduct(product));

    }        Product updatedProduct = new Product();



    @Test        updatedProduct.setId(1L);        assertEquals(testProduct.getId(), result.getId());                

    void validateProduct_NegativePrice_ThrowsException() {

        // Arrange        updatedProduct.setName("Updated Product");

        product.setPrice(new BigDecimal("-10.00"));

        updatedProduct.setDescription("Updated Description");        assertEquals(testProduct.getName(), result.getName());

        // Act & Assert

        assertThrows(IllegalArgumentException.class, () -> productDomainService.validateProduct(product));        updatedProduct.setPrice(new BigDecimal("199.99"));

    }

}                verify(productRepository).save(productToCreate);        // Then        // Then

        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);    }

        

        // When            assertNotNull(result);        assertNotNull(result);

        Optional<Product> result = productDomainService.updateProduct(1L, updatedProduct);

            @Test

        // Then

        assertTrue(result.isPresent());    void testGetProductById_Found() {        assertEquals(1L, result.getId());        assertEquals(1L, result.getId());

        assertEquals(updatedProduct, result.get());

        verify(productRepository).findById(1L);        // Given

        verify(productRepository).save(any(Product.class));

    }        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));        assertEquals("Test Product", result.getName());        assertEquals("Test Product", result.getName());

    

    @Test        

    void testUpdateProduct_NotFound() {

        // Given        // When        assertEquals(new BigDecimal("99.99"), result.getPrice());        assertEquals(new BigDecimal("99.99"), result.getPrice());

        Product updatedProduct = new Product();

        updatedProduct.setName("Updated Product");        Optional<Product> result = productDomainService.getProductById(1L);

        updatedProduct.setDescription("Updated Description");

        updatedProduct.setPrice(new BigDecimal("199.99"));                verify(productRepository).save(inputProduct);        verify(productRepository).save(inputProduct);

        

        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());        // Then

        

        // When        assertTrue(result.isPresent());    }    }

        Optional<Product> result = productDomainService.updateProduct(999L, updatedProduct);

                assertEquals(testProduct, result.get());

        // Then

        assertFalse(result.isPresent());        verify(productRepository).findById(1L);        

        verify(productRepository).findById(999L);

        verify(productRepository, never()).save(any(Product.class));    }

    }

            @Test    @Test

    @Test

    void testDeleteProduct_Found() {    @Test

        // Given

        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));    void testGetProductById_NotFound() {    void createProduct_NullName_ThrowsException() {    void createProduct_NullName_ThrowsException() {

        

        // When        // Given

        boolean result = productDomainService.deleteProduct(1L);

                when(productRepository.findById(anyLong())).thenReturn(Optional.empty());        // Given        // Given

        // Then

        assertTrue(result);        

        verify(productRepository).findById(1L);

        verify(productRepository).delete(testProduct);        // When        Product product = new Product(null, null, BigDecimal.valueOf(10.0));        Product product = new Product(null, null, BigDecimal.valueOf(10.0));

    }

            Optional<Product> result = productDomainService.getProductById(999L);

    @Test

    void testDeleteProduct_NotFound() {        

        // Given

        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());        // Then

        

        // When        assertFalse(result.isPresent());        // When & Then        // When & Then

        boolean result = productDomainService.deleteProduct(999L);

                verify(productRepository).findById(999L);

        // Then

        assertFalse(result);    }        IllegalArgumentException exception = assertThrows(        IllegalArgumentException exception = assertThrows(

        verify(productRepository).findById(999L);

        verify(productRepository, never()).delete(any(Product.class));    

    }

        @Test            IllegalArgumentException.class,            IllegalArgumentException.class,

    @Test

    void testFindByName() {    void testGetAllProducts() {

        // Given

        List<Product> products = Arrays.asList(testProduct);        // Given            () -> productDomainService.createProduct(product)            () -> productDomainService.createProduct(product)

        when(productRepository.findByNameContainingIgnoreCase("Test")).thenReturn(products);

                Product product2 = Product.builder()

        // When

        List<Product> result = productDomainService.findByName("Test");                .id(2L)        );        );

        

        // Then                .name("Product 2")

        assertNotNull(result);

        assertEquals(1, result.size());                .description("Description 2")        assertEquals("Product name cannot be null or empty", exception.getMessage());        assertEquals("Product name cannot be null or empty", exception.getMessage());

        assertEquals(testProduct, result.get(0));

        verify(productRepository).findByNameContainingIgnoreCase("Test");                .price(new BigDecimal("199.99"))

    }

                    .build();    }    }

    @Test

    void testFindByPriceRange() {        

        // Given

        BigDecimal minPrice = new BigDecimal("50.00");        List<Product> products = Arrays.asList(testProduct, product2);

        BigDecimal maxPrice = new BigDecimal("150.00");

        List<Product> products = Arrays.asList(testProduct);        when(productRepository.findAll()).thenReturn(products);

        when(productRepository.findByPriceBetween(minPrice, maxPrice)).thenReturn(products);

                    @Test    @Test

        // When

        List<Product> result = productDomainService.findByPriceRange(minPrice, maxPrice);        // When

        

        // Then        List<Product> result = productDomainService.getAllProducts();    void createProduct_NegativePrice_ThrowsException() {    void createProduct_NegativePrice_ThrowsException() {

        assertNotNull(result);

        assertEquals(1, result.size());        

        assertEquals(testProduct, result.get(0));

        verify(productRepository).findByPriceBetween(minPrice, maxPrice);        // Then        // Given        // Given

    }

}        assertNotNull(result);

        assertEquals(2, result.size());        Product product = new Product(null, "Test Product", BigDecimal.valueOf(-10.0));        Product product = new Product(null, "Test Product", BigDecimal.valueOf(-10.0));

        assertEquals(products, result);

        verify(productRepository).findAll();

    }

            // When & Then        // When & Then

    @Test

    void testUpdateProduct_Found() {        IllegalArgumentException exception = assertThrows(        IllegalArgumentException exception = assertThrows(

        // Given

        Product updatedProduct = Product.builder()            IllegalArgumentException.class,            IllegalArgumentException.class,

                .id(1L)

                .name("Updated Product")            () -> productDomainService.createProduct(product)            () -> productDomainService.createProduct(product)

                .description("Updated Description")

                .price(new BigDecimal("199.99"))        );        );

                .build();

                assertEquals("Product price cannot be negative", exception.getMessage());        assertEquals("Product price cannot be negative", exception.getMessage());

        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);    }    }

        

        // When

        Optional<Product> result = productDomainService.updateProduct(1L, updatedProduct);

            @Test    @Test

        // Then

        assertTrue(result.isPresent());    void getProductById_ExistingId_ReturnsProduct() {    void getProductById_ExistingId_ReturnsProduct() {

        assertEquals(updatedProduct, result.get());

        verify(productRepository).findById(1L);        // Given        // Given

        verify(productRepository).save(any(Product.class));

    }        Long id = 1L;        Long id = 1L;

    

    @Test        Product product = new Product(id, "Test Product", new BigDecimal("99.99"));        Product product = new Product(id, "Test Product", new BigDecimal("99.99"));

    void testUpdateProduct_NotFound() {

        // Given        when(productRepository.findById(id)).thenReturn(Optional.of(product));        when(productRepository.findById(id)).thenReturn(Optional.of(product));

        Product updatedProduct = Product.builder()

                .name("Updated Product")                

                .description("Updated Description")

                .price(new BigDecimal("199.99"))        // When        // When

                .build();

                Optional<Product> result = productDomainService.getProductById(id);        Optional<Product> result = productDomainService.getProductById(id);

        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

                        

        // When

        Optional<Product> result = productDomainService.updateProduct(999L, updatedProduct);        // Then        // Then

        

        // Then        assertTrue(result.isPresent());        assertTrue(result.isPresent());

        assertFalse(result.isPresent());

        verify(productRepository).findById(999L);        assertEquals(product, result.get());        assertEquals(product, result.get());

        verify(productRepository, never()).save(any(Product.class));

    }        verify(productRepository).findById(id);        verify(productRepository).findById(id);

    

    @Test    }    }

    void testDeleteProduct_Found() {

        // Given        

        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

            @Test    @Test

        // When

        boolean result = productDomainService.deleteProduct(1L);    void getProductById_NonExistingId_ReturnsEmpty() {    void getProductById_NonExistingId_ReturnsEmpty() {

        

        // Then        // Given        // Given

        assertTrue(result);

        verify(productRepository).findById(1L);        Long id = 999L;        Long id = 999L;

        verify(productRepository).delete(testProduct);

    }        when(productRepository.findById(id)).thenReturn(Optional.empty());        when(productRepository.findById(id)).thenReturn(Optional.empty());

    

    @Test                

    void testDeleteProduct_NotFound() {

        // Given        // When        // When

        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

                Optional<Product> result = productDomainService.getProductById(id);        Optional<Product> result = productDomainService.getProductById(id);

        // When

        boolean result = productDomainService.deleteProduct(999L);                

        

        // Then        // Then        // Then

        assertFalse(result);

        verify(productRepository).findById(999L);        assertTrue(result.isEmpty());        assertTrue(result.isEmpty());

        verify(productRepository, never()).delete(any(Product.class));

    }        verify(productRepository).findById(id);        verify(productRepository).findById(id);

    

    @Test    }    }

    void testFindByName() {

        // Given        

        List<Product> products = Arrays.asList(testProduct);

        when(productRepository.findByNameContainingIgnoreCase("Test")).thenReturn(products);    @Test    @Test

        

        // When    void getAllProducts_ReturnsAllProducts() {    void getAllProducts_ReturnsAllProducts() {

        List<Product> result = productDomainService.findByName("Test");

                // Given        // Given

        // Then

        assertNotNull(result);        List<Product> products = List.of(        List<Product> products = List.of(

        assertEquals(1, result.size());

        assertEquals(testProduct, result.get(0));            new Product(1L, "Product 1", new BigDecimal("10.00")),            new Product(1L, "Product 1", new BigDecimal("10.00")),

        verify(productRepository).findByNameContainingIgnoreCase("Test");

    }            new Product(2L, "Product 2", new BigDecimal("20.00"))            new Product(2L, "Product 2", new BigDecimal("20.00"))

    

    @Test        );        );

    void testFindByPriceRange() {

        // Given        when(productRepository.findAll()).thenReturn(products);        when(productRepository.findAll()).thenReturn(products);

        BigDecimal minPrice = new BigDecimal("50.00");

        BigDecimal maxPrice = new BigDecimal("150.00");                

        List<Product> products = Arrays.asList(testProduct);

        when(productRepository.findByPriceBetween(minPrice, maxPrice)).thenReturn(products);        // When        // When

        

        // When        List<Product> result = productDomainService.getAllProducts();        List<Product> result = productDomainService.getAllProducts();

        List<Product> result = productDomainService.findByPriceRange(minPrice, maxPrice);

                        

        // Then

        assertNotNull(result);        // Then        // Then

        assertEquals(1, result.size());

        assertEquals(testProduct, result.get(0));        assertEquals(2, result.size());        assertEquals(2, result.size());

        verify(productRepository).findByPriceBetween(minPrice, maxPrice);

    }        assertEquals(products, result);        assertEquals(products, result);

    

    @Test        verify(productRepository).findAll();        verify(productRepository).findAll();

    void testValidateProduct_ValidProduct() {

        // Given    }    }

        Product validProduct = Product.builder()

                .name("Valid Product")        

                .description("Valid Description")

                .price(new BigDecimal("99.99"))    @Test    @Test

                .build();

            void updateProduct_ExistingProduct_ReturnsUpdatedProduct() {    void updateProduct_ExistingProduct_ReturnsUpdatedProduct() {

        // When & Then

        assertDoesNotThrow(() -> productDomainService.validateProduct(validProduct));        // Given        // Given

    }

            Long id = 1L;        Long id = 1L;

    @Test

    void testValidateProduct_NullProduct() {        Product existingProduct = new Product(id, "Old Name", new BigDecimal("10.00"));        Product existingProduct = new Product(id, "Old Name", new BigDecimal("10.00"));

        // When & Then

        assertThrows(IllegalArgumentException.class,         Product updatedProduct = new Product(id, "New Name", new BigDecimal("20.00"));        Product updatedProduct = new Product(id, "New Name", new BigDecimal("20.00"));

                () -> productDomainService.validateProduct(null));

    }                

    

    @Test        when(productRepository.findById(id)).thenReturn(Optional.of(existingProduct));        when(productRepository.findById(id)).thenReturn(Optional.of(existingProduct));

    void testValidateProduct_EmptyName() {

        // Given        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);

        Product invalidProduct = Product.builder()

                .name("")                

                .description("Valid Description")

                .price(new BigDecimal("99.99"))        // When        // When

                .build();

                Optional<Product> result = productDomainService.updateProduct(id, updatedProduct);        Optional<Product> result = productDomainService.updateProduct(id, updatedProduct);

        // When & Then

        assertThrows(IllegalArgumentException.class,                 

                () -> productDomainService.validateProduct(invalidProduct));

    }        // Then        // Then

    

    @Test        assertTrue(result.isPresent());        assertTrue(result.isPresent());

    void testValidateProduct_NegativePrice() {

        // Given        assertEquals("New Name", result.get().getName());        assertEquals("New Name", result.get().getName());

        Product invalidProduct = Product.builder()

                .name("Valid Product")        assertEquals(new BigDecimal("20.00"), result.get().getPrice());        assertEquals(new BigDecimal("20.00"), result.get().getPrice());

                .description("Valid Description")

                .price(new BigDecimal("-10.00"))        verify(productRepository).findById(id);        verify(productRepository).findById(id);

                .build();

                verify(productRepository).save(any(Product.class));        verify(productRepository).save(any(Product.class));

        // When & Then

        assertThrows(IllegalArgumentException.class,     }    }

                () -> productDomainService.validateProduct(invalidProduct));

    }        

}
    @Test    @Test

    void updateProduct_NonExistingProduct_ReturnsEmpty() {    void updateProduct_NonExistingProduct_ReturnsEmpty() {

        // Given        // Given

        Long id = 999L;        Long id = 999L;

        Product updatedProduct = new Product(id, "New Name", new BigDecimal("20.00"));        Product updatedProduct = new Product(id, "New Name", new BigDecimal("20.00"));

                

        when(productRepository.findById(id)).thenReturn(Optional.empty());        when(productRepository.findById(id)).thenReturn(Optional.empty());

                

        // When        // When

        Optional<Product> result = productDomainService.updateProduct(id, updatedProduct);        Optional<Product> result = productDomainService.updateProduct(id, updatedProduct);

                

        // Then        // Then

        assertTrue(result.isEmpty());        assertTrue(result.isEmpty());

        verify(productRepository).findById(id);        verify(productRepository).findById(id);

        verify(productRepository, never()).save(any(Product.class));        verify(productRepository, never()).save(any(Product.class));

    }    }

        

    @Test    @Test

    void deleteProduct_ExistingProduct_ReturnsTrue() {    void deleteProduct_ExistingProduct_ReturnsTrue() {

        // Given        // Given

        Long id = 1L;        Long id = 1L;

        Product existingProduct = new Product(id, "Product", new BigDecimal("10.00"));        Product existingProduct = new Product(id, "Product", new BigDecimal("10.00"));

        when(productRepository.findById(id)).thenReturn(Optional.of(existingProduct));        when(productRepository.findById(id)).thenReturn(Optional.of(existingProduct));

                

        // When        // When

        boolean result = productDomainService.deleteProduct(id);        boolean result = productDomainService.deleteProduct(id);

                

        // Then        // Then

        assertTrue(result);        assertTrue(result);

        verify(productRepository).findById(id);        verify(productRepository).findById(id);

        verify(productRepository).deleteById(id);        verify(productRepository).deleteById(id);

    }    }

        

    @Test    @Test

    void deleteProduct_NonExistingProduct_ReturnsFalse() {    void deleteProduct_NonExistingProduct_ReturnsFalse() {

        // Given        // Given

        Long id = 999L;        Long id = 999L;

        when(productRepository.findById(id)).thenReturn(Optional.empty());        when(productRepository.findById(id)).thenReturn(Optional.empty());

                

        // When        // When

        boolean result = productDomainService.deleteProduct(id);        boolean result = productDomainService.deleteProduct(id);

                

        // Then        // Then

        assertFalse(result);        assertFalse(result);

        verify(productRepository).findById(id);        verify(productRepository).findById(id);

        verify(productRepository, never()).deleteById(id);        verify(productRepository, never()).deleteById(id);

    }    }

        

    @Test    @Test

    void findByName_ExistingName_ReturnsProducts() {    void findByName_ExistingName_ReturnsProducts() {

        // Given        // Given

        String name = "Test";        String name = "Test";

        List<Product> products = List.of(        List<Product> products = List.of(

            new Product(1L, "Test Product 1", new BigDecimal("10.00")),            new Product(1L, "Test Product 1", new BigDecimal("10.00")),

            new Product(2L, "Test Product 2", new BigDecimal("20.00"))            new Product(2L, "Test Product 2", new BigDecimal("20.00"))

        );        );

        when(productRepository.findByNameContaining(name)).thenReturn(products);        when(productRepository.findByNameContaining(name)).thenReturn(products);

                

        // When        // When

        List<Product> result = productDomainService.findByName(name);        List<Product> result = productDomainService.findByName(name);

                

        // Then        // Then

        assertEquals(2, result.size());        assertEquals(2, result.size());

        assertEquals(products, result);        assertEquals(products, result);

        verify(productRepository).findByNameContaining(name);        verify(productRepository).findByNameContaining(name);

    }    }

        

    @Test    @Test

    void findByPriceRange_ValidRange_ReturnsProducts() {    void findByPriceRange_ValidRange_ReturnsProducts() {

        // Given        // Given

        BigDecimal minPrice = new BigDecimal("10.00");        BigDecimal minPrice = new BigDecimal("10.00");

        BigDecimal maxPrice = new BigDecimal("20.00");        BigDecimal maxPrice = new BigDecimal("20.00");

        List<Product> products = List.of(        List<Product> products = List.of(

            new Product(1L, "Product 1", new BigDecimal("15.00")),            new Product(1L, "Product 1", new BigDecimal("15.00")),

            new Product(2L, "Product 2", new BigDecimal("18.00"))            new Product(2L, "Product 2", new BigDecimal("18.00"))

        );        );

        when(productRepository.findByPriceBetween(minPrice, maxPrice)).thenReturn(products);        when(productRepository.findByPriceBetween(minPrice, maxPrice)).thenReturn(products);

                

        // When        // When

        List<Product> result = productDomainService.findByPriceRange(minPrice, maxPrice);        List<Product> result = productDomainService.findByPriceRange(minPrice, maxPrice);

                

        // Then        // Then

        assertEquals(2, result.size());        assertEquals(2, result.size());

        assertEquals(products, result);        assertEquals(products, result);

        verify(productRepository).findByPriceBetween(minPrice, maxPrice);        verify(productRepository).findByPriceBetween(minPrice, maxPrice);

    }    }

}}

    @Test
    void updateProduct_ExistingProduct_ReturnsUpdatedProduct() {
        // Given
        Long id = 1L;
        Product product = new Product("Updated Product", new BigDecimal("199.99"));
        Product savedProduct = new Product(id, "Updated Product", new BigDecimal("199.99"));
        
        when(productRepository.existsById(id)).thenReturn(true);
        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        // When
        Product result = productDomainService.updateProduct(id, product);

        // Then
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("Updated Product", result.getName());
        assertEquals(new BigDecimal("199.99"), result.getPrice());
        verify(productRepository, times(1)).existsById(id);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void updateProduct_NonExistingProduct_ReturnsNull() {
        // Given
        Long id = 999L;
        Product product = new Product("Updated Product", new BigDecimal("199.99"));
        when(productRepository.existsById(id)).thenReturn(false);

        // When
        Product result = productDomainService.updateProduct(id, product);

        // Then
        assertNull(result);
        verify(productRepository, times(1)).existsById(id);
        verify(productRepository, never()).save(any());
    }

    @Test
    void deleteProduct_ExistingProduct_ReturnsTrue() {
        // Given
        Long id = 1L;
        when(productRepository.existsById(id)).thenReturn(true);

        // When
        boolean result = productDomainService.deleteProduct(id);

        // Then
        assertTrue(result);
        verify(productRepository, times(1)).existsById(id);
        verify(productRepository, times(1)).deleteById(id);
    }

    @Test
    void deleteProduct_NonExistingProduct_ReturnsFalse() {
        // Given
        Long id = 999L;
        when(productRepository.existsById(id)).thenReturn(false);

        // When
        boolean result = productDomainService.deleteProduct(id);

        // Then
        assertFalse(result);
        verify(productRepository, times(1)).existsById(id);
        verify(productRepository, never()).deleteById(any());
    }
}