package store.dto;

import lombok.Data;

@Data
public class CreationRequest {
    private String name;
    private String email;
    private Double price;
}