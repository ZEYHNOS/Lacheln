package aba3.lucid.domain.cart.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CartDeleteRequest {
    private List<Long> cartIds;
}