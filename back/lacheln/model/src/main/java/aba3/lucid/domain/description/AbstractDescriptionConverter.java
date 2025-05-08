package aba3.lucid.domain.description;

import aba3.lucid.common.annotation.Converter;
import aba3.lucid.domain.product.dto.DescriptionRequest;
import aba3.lucid.domain.product.dto.DescriptionResponse;

import java.util.ArrayList;
import java.util.List;

@Converter
public abstract class AbstractDescriptionConverter<T, DES extends Description> implements DescriptionConverterIfs<T, DES> {

    @Override
    public List<DES> toDescriptionEntityList(T entity, List<DescriptionRequest> requestDescriptionList) {
        if (requestDescriptionList == null) return List.of();

        List<DES> result = new ArrayList<>();
        for (int i = 0; i < requestDescriptionList.size(); i++) {
            result.add(toDescriptionEntity(requestDescriptionList.get(i), entity, i));
        }

        return result;
    }

    @Override
    public List<DescriptionResponse> toDescriptionResponseList(List<DES> descriptionEntityList) {
        if (descriptionEntityList == null) return List.of();

        return descriptionEntityList.stream()
                .map(this::toDescriptionResponse)
                .toList();
    }

    public abstract DES toDescriptionEntity(DescriptionRequest request, T entity,  int order);

    @Override
    public DescriptionResponse toDescriptionResponse(DES descriptionEntity){
        return DescriptionResponse.builder()
                .value(descriptionEntity.getValue())
                .type(descriptionEntity.getType())
                .order(descriptionEntity.getOrder())
                .build()
                ;
    }
}
