package dev.petproject.productservice.mapper;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ModelDtoMapper {
    private final ModelMapper modelMapper;

    public <M, D> D toDto(M model, Class<D> dto){
        return modelMapper.map(model, dto);
    }

    public <M, D> M toModel(D dto, Class<M> model){
        return modelMapper.map(dto, model);
    }
}
