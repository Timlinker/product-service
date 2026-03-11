package dev.petproject.productservice;

import dev.petproject.productservice.dto.ProductRequest;
import dev.petproject.productservice.dto.ProductResponse;
import dev.petproject.productservice.mapper.ModelDtoMapper;
import dev.petproject.productservice.model.Product;
import dev.petproject.productservice.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.mongodb.MongoDBContainer;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class ProductServiceApplicationTests {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.2");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper odjectMapper;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelDtoMapper modelDtoMapper;


    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry){
        dynamicPropertyRegistry.add("spring.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
    }

    @Test
    void shouldCreateProduct() throws Exception {
        ProductRequest productRequest = getProductRequest();
        String productRequestString = odjectMapper.writeValueAsString(productRequest);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productRequestString))
                .andExpect(status().isCreated());

        Assertions.assertEquals(1, productRepository.findAll().size());

    }


    @Test
    void getAllProducts() throws Exception {

        ProductRequest request1 = ProductRequest.builder()
                .name("iPhone 13")
                .description("About iPhone 13")
                .price(BigDecimal.valueOf(1200))
                .build();

        ProductRequest request2 = ProductRequest.builder()
                .name("Samsung Galaxy M55")
                .description("About Samsung Galaxy M55")
                .price(BigDecimal.valueOf(1000))
                .build();

        productRepository.save(modelDtoMapper.toModel(request1, Product.class));
        productRepository.save(modelDtoMapper.toModel(request2, Product.class));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/product")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                        .andExpect(jsonPath("$[0].id", notNullValue()))
                        .andExpect(jsonPath("$[0].name").value("iPhone 13"))
                        .andExpect(jsonPath("$[0].description").value("About iPhone 13"))
                        .andExpect(jsonPath("$[0].price").value(1200))
                        .andExpect(jsonPath("$[1].id", notNullValue()))
                        .andExpect(jsonPath("$[1].name").value("Samsung Galaxy M55"))
                        .andExpect(jsonPath("$[1].description").value("About Samsung Galaxy M55"))
                        .andExpect(jsonPath("$[1].price").value(1000));


        Assertions.assertEquals(2, productRepository.findAll().size());
    }

    private ProductRequest getProductRequest() {
        return ProductRequest.builder()
                .name("iPhone 13")
                .description("About iPhone 13")
                .price(BigDecimal.valueOf(1200))
                .build();
    }


}
