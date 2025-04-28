package aba3.lucid.domain.product.dto;

import lombok.*;

import java.math.BigInteger;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductItem {

    private Long id;
    private String name;
    private BigInteger price;
    private int taskTime;
    private List<OptionItem> optionItemList;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class OptionItem {
        private Long id;
        private String name;
        private List<OptionDetailItem> optionDetailItemList;

        @Getter
        @Setter
        @AllArgsConstructor
        @NoArgsConstructor
        @Builder
        public static class OptionDetailItem {
            private Long id;
            private String name;
            private BigInteger price;
            private int taskTime;
        }
    }
}