package aba3.lucid.domain.description;

import aba3.lucid.domain.product.dto.DescriptionRequest;
import aba3.lucid.domain.product.dto.DescriptionResponse;

import java.util.List;

public interface DescriptionConverterIfs<T, DES>{

    public DES toDescriptionEntity(DescriptionRequest request, T entity, int order);

    public DescriptionResponse toDescriptionResponse(DES descriptionEntity);

    public List<DES> toDescriptionEntityList(T entity, List<DescriptionRequest> request);

    public List<DescriptionResponse> toDescriptionResponseList(List<DES> descriptionEntityList);

}
