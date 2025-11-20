package store.dto;

import lombok.Data;

import java.util.Map;

@Data
public class OrderRequest {
    private Long clientId;
    private Map<Long, Integer> products; // Map<ProductId, Quantity>
}