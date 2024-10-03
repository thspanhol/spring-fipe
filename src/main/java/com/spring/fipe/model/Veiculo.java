package com.spring.fipe.model;

public record Veiculo(
        Integer vehicleType,
        String price,
        String brand,
        String model,
        Integer modelYear,
        String fuel,
        String codeFipe,
        String referenceMonth,
        String fuelAcronym
) {
}
