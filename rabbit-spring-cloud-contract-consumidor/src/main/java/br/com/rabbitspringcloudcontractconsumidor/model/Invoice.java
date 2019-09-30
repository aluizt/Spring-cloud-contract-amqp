package br.com.rabbitspringcloudcontractconsumidor.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Invoice {
    private Integer number;
    private Double price;
}
