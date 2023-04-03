package lk.ijse.thogakade.dto;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Customer {
    private String id;
    private String name;
    private String address;
    private double salary;


}
