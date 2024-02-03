package pl.edu.agh.mwo.invoice;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import pl.edu.agh.mwo.invoice.product.Product;

public class Invoice {
    public  Invoice()
    {
        products = new ArrayList<Product>() ;
    }
    private Collection<Product> products;

    public void addProduct(Product product) {
       if (product == null)
           throw  new IllegalArgumentException("Product can't be null.");
        products.add(product);
    }

    public void addProduct(Product product, Integer quantity) {
       if (quantity<=0)
           throw new IllegalArgumentException("Quantity must be grater than zero.");
        if (product == null)
            throw  new IllegalArgumentException("Product can't be null.");
        for (int i = 0; i < quantity; i++) {
            products.add(product);
        }
    }

    public BigDecimal getSubtotal() {

        BigDecimal subtotal = BigDecimal.ZERO;
        for (Product product: products
        ) {
            subtotal = subtotal.add(product.getPrice());
        }
        return subtotal;
    }

    public BigDecimal getTax() {

        BigDecimal tax = BigDecimal.ZERO;
        for (Product product: products
        ) {
           tax = tax.add(product.getPrice().multiply(product.getTaxPercent()));
        }
        return tax;
    }

    public BigDecimal getTotal()
    {
        BigDecimal total = BigDecimal.ZERO;
        for (Product product: products
             ) {
            total  = total.add(product.getPriceWithTax());
        }
        return total;
    }
}
