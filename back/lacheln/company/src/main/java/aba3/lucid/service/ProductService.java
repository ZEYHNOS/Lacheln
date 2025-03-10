package aba3.lucid.service;

import aba3.lucid.domain.product.DressEntity;
import aba3.lucid.repository.product.DressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final DressRepository dressRepository;

    public DressEntity dressRegister(DressEntity entity) {
        return dressRepository.save(entity);
    }
}
