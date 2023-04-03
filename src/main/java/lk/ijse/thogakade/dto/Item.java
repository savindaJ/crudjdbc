package lk.ijse.thogakade.dto;

import lombok.*;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Item {
    private String code;
    private String description;
    private Double nitPrice;
    private Integer qtyONHand;


}
