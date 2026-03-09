package dev.petproject.productservice.service;

import dev.petproject.productservice.dto.ProductRequest;
import dev.petproject.productservice.dto.ProductResponse;
import dev.petproject.productservice.mapper.ModelDtoMapper;
import dev.petproject.productservice.model.Product;
import dev.petproject.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final ModelDtoMapper modelDtoMapper;

    public void createProduct(ProductRequest request){

        Product product = modelDtoMapper.toModel(request, Product.class);

        productRepository.save(product);

        log.info("Продукт с id={} сохранён", product.getId());

    }

    public List<ProductResponse> getAllProducts(){
        List<Product> products = productRepository.findAll();

        return products.stream()
                .map(source -> modelDtoMapper
                        .toDto(source, ProductResponse.class)).toList();


    }
}
