package pl.edu.agh.mwo.invoice.product;

import java.math.BigDecimal;

public class ExciseProduct extends Product {
    BigDecimal exciseTax;
    BigDecimal fixedPromotion;

    public ExciseProduct(String name, BigDecimal price, BigDecimal excise) {
        super(name, price, new BigDecimal("0.23"));
        exciseTax = excise;
        fixedPromotion = new BigDecimal("0");
    }

    @Override
    public BigDecimal getPriceWithTax() {
        return super.getPriceWithTax().add(exciseTax).subtract(fixedPromotion);
    }

    public BigDecimal getExciseTaxValue() {
        return exciseTax;
    }

    public BigDecimal getFixedPromotion() {
        return fixedPromotion;
    }

    public void setFixedPromotion(BigDecimal promotion) {
        fixedPromotion = promotion;
    }

}
