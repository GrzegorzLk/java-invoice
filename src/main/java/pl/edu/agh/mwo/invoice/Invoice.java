package pl.edu.agh.mwo.invoice;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.edu.agh.mwo.invoice.product.Product;

public class Invoice {
    static int InvoiceCount;
    final int invoiceNumber;

    public Invoice() {
        invoiceNumber = ++InvoiceCount;
    }

    private Map<Product, Integer> products = new HashMap<>();

    public void addProduct(Product product) {
        addProduct(product, 1);
    }

    public void addProduct(Product product, Integer quantity) {
        if (product == null || quantity <= 0) {
            throw new IllegalArgumentException();
        }
        products.put(product, quantity);
    }

    public BigDecimal getNetTotal() {
        BigDecimal totalNet = BigDecimal.ZERO;
        for (Product product : products.keySet()) {
            BigDecimal quantity = new BigDecimal(products.get(product));
            totalNet = totalNet.add(product.getPrice().multiply(quantity));
        }
        return totalNet;
    }

    public BigDecimal getTaxTotal() {
        return getGrossTotal().subtract(getNetTotal());
    }

    public BigDecimal getGrossTotal() {
        BigDecimal totalGross = BigDecimal.ZERO;
        for (Product product : products.keySet()) {
            BigDecimal quantity = new BigDecimal(products.get(product));
            totalGross = totalGross.add(product.getPriceWithTax().multiply(quantity));
        }
        return totalGross;
    }

    public int getNumber() {
        return invoiceNumber;
    }

    public String print() {
        return "Invoice No. " + invoiceNumber;
    }

    public String printProducts() {
        StringBuilder exitString = new StringBuilder();
        exitString.append(print()).append(System.lineSeparator());
        List<Product> sortedProducts = products.keySet().stream()
                .sorted((a, b) -> a.getName().compareToIgnoreCase(b.getName())).toList();
        for (Product product : sortedProducts) {
            exitString.append(product.getName()).append(" ").append(products.get(product))
                    .append(System.lineSeparator());
        }
        exitString.append("Liczba pozycji: ").append(products.size());
        return exitString.toString();
    }
}
