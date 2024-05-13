package pl.edu.agh.mwo.invoice.product;

import java.math.BigDecimal;

public class FuelCanister extends ExciseProduct {

    public FuelCanister(String name, BigDecimal price, boolean isTaxFreeDay) {
        super(name, price, new BigDecimal("5.56"));
        if (isTaxFreeDay)
            super.setFixedPromotion(super.getExciseTaxValue());
    }

    public FuelCanister(String name, BigDecimal price) {
        this(name, price, false);
    }
}
