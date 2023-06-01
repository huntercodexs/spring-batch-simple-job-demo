package com.huntercodexs.simplejob;

import lombok.*;

import javax.persistence.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "PRODUCTS")
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private String price;
}
