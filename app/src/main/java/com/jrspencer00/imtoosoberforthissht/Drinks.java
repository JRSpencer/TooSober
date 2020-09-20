package com.jrspencer00.imtoosoberforthissht;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Drinks {
    private double drinkSize;
    private double drinkPercentage;
}
