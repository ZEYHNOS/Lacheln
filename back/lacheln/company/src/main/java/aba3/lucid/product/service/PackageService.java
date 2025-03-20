package aba3.lucid.product.service;

import aba3.lucid.domain.product.repository.PackageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PackageService {

    private final PackageRepository packageRepository;

}
