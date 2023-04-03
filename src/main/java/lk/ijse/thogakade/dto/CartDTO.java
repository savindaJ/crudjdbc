package lk.ijse.thogakade.dto;

import lombok.Data;

@Data
public class CartDTO {
    private String code;
    private Integer qty;
    private Double unitPrice;

    public CartDTO(String code, Integer qty, Double unitPrice) {
        this.code = code;
        this.qty = qty;
        this.unitPrice = unitPrice;
    }
}
