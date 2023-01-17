package shop.mtcoding.orange.model;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Product {
    private Integer id;
    private String name;
    private Integer price;
    private String qty;
    private Timestamp createdAt;
}
