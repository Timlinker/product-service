package dev.petproject.productservice.configuration;

import dev.petproject.productservice.mapper.ModelDtoMapper;
import dev.petproject.productservice.repository.ProductRepository;
import dev.petproject.productservice.service.ProductService;
import org.modelmapper.ModelMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;


@Configuration
public class ProductServiceConfiguration {

    @Bean
    @Lazy
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

    @Bean
    @Lazy
    public ModelDtoMapper mapper(@Autowired ModelMapper modelMapper){

        return new ModelDtoMapper(modelMapper);
    }

    @Bean
    @Lazy
    public ProductService productService(@Autowired ProductRepository repository,
                                         @Autowired ModelDtoMapper modelDtoMapper){
        return new ProductService(repository, modelDtoMapper);
    }
}
