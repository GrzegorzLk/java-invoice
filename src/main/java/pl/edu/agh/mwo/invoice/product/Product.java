package pl.edu.agh.mwo.invoice.product;

import org.hamcrest.Matchers;

import java.math.BigDecimal;

public abstract class Product {
    private final String name;

    private final BigDecimal price;

    private final BigDecimal taxPercent;

    protected Product(String name, BigDecimal price, BigDecimal tax) {
        if (name == null || name.isEmpty())
            throw new IllegalArgumentException("Name cannot be null or empty.");
        this.name = name;
        if (price == null || price.compareTo(new BigDecimal(0)) == -1)
            throw new IllegalArgumentException("Price cannot be null or negative.");
        this.price = price;
        if (tax.compareTo(new BigDecimal(0)) == -1)
            throw new IllegalArgumentException("Tax cannot be null or negative.");
        this.taxPercent = tax;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getTaxPercent() {
        return taxPercent;
    }

    public BigDecimal getPriceWithTax() {
        return price.multiply(taxPercent).add(price);
    }
}
