package pl.edu.agh.mwo.invoice;

import java.math.BigDecimal;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pl.edu.agh.mwo.invoice.product.*;

public class InvoiceTest {
    private Invoice invoice;

    @Before
    public void createEmptyInvoiceForTheTest() {
        invoice = new Invoice();
    }

    @Test
    public void testInvoiceHasProperTaxValueForExciseProducts() {
        Product wine = new BottleOfWine("WineTest", new BigDecimal("100"));
        Assert.assertThat(new BigDecimal("128.56"), Matchers.comparesEqualTo(wine.getPriceWithTax()));

        Product fuel1withTax = new FuelCanister("fuel", new BigDecimal("100"));
        Assert.assertThat(new BigDecimal("128.56"), Matchers.comparesEqualTo(fuel1withTax.getPriceWithTax()));

        Product fuel2withoutTax = new FuelCanister("fuel witout tax", new BigDecimal("100"), true);
        Assert.assertThat(new BigDecimal("123"), Matchers.comparesEqualTo(fuel2withoutTax.getPriceWithTax()));
    }

    @Test
    public void testInvoiceNumberOnInvoicePrint() {
        String testString = "Invoice No. " + invoice.getNumber();
        Assert.assertTrue(invoice.print().contains(testString));
    }

    @Test
    public void testAddSameTypeProduct() {
        Product testProduct = new TaxFreeProduct("Type1Product", new BigDecimal("10"));
        invoice.addProduct(testProduct);
        invoice.addProduct(testProduct);
        invoice.addProduct(testProduct, 2);
        Assert.assertEquals("Liczba pozycji: 1", invoice.printProducts().split(System.lineSeparator())[2]);
        Assert.assertEquals("Type1Product 4", invoice.printProducts().split(System.lineSeparator())[1]);
    }

    @Test
    public void testInvoicePrintProductsCorrectProducts() {
        invoice.addProduct(new TaxFreeProduct("TaxFreeProduct01", new BigDecimal("10")));
        invoice.addProduct(new TaxFreeProduct("TaxFreeProduct02", new BigDecimal("10")), 2);
        invoice.addProduct(new TaxFreeProduct("TaxFreeProduct03", new BigDecimal("10")), 3);
        invoice.addProduct(new TaxFreeProduct("TaxFreeProduct04", new BigDecimal("10")), 4);
        invoice.addProduct(new TaxFreeProduct("TaxFreeProduct05", new BigDecimal("10")), 5);

        System.out.println(invoice.printProducts());

        String[] printedLines = invoice.printProducts().split(System.lineSeparator());
        Assert.assertEquals("Invoice No. " + invoice.getNumber(), printedLines[0]);
        Assert.assertEquals("TaxFreeProduct01 1", printedLines[1]);
        Assert.assertEquals("TaxFreeProduct02 2", printedLines[2]);
        Assert.assertEquals("TaxFreeProduct03 3", printedLines[3]);
        Assert.assertEquals("TaxFreeProduct04 4", printedLines[4]);
        Assert.assertEquals("TaxFreeProduct05 5", printedLines[5]);

        Assert.assertEquals("Liczba pozycji: 5", printedLines[6]);
    }

    @Test
    public void testInvoicePrintProductsCorrectProductsNo() {
        String[] printedLines = invoice.printProducts().split(System.lineSeparator());
        Assert.assertEquals("Liczba pozycji: 0", printedLines[printedLines.length - 1]);

        invoice.addProduct(new TaxFreeProduct("TaxFreeProduct01", new BigDecimal("10")));

        printedLines = invoice.printProducts().split(System.lineSeparator());
        Assert.assertEquals("Liczba pozycji: 1", printedLines[printedLines.length - 1]);

        invoice.addProduct(new TaxFreeProduct("TaxFreeProduct02", new BigDecimal("10")));
        invoice.addProduct(new TaxFreeProduct("TaxFreeProduct03", new BigDecimal("10")));
        invoice.addProduct(new TaxFreeProduct("TaxFreeProduct04", new BigDecimal("10")));
        invoice.addProduct(new TaxFreeProduct("TaxFreeProduct05", new BigDecimal("10")));

        printedLines = invoice.printProducts().split(System.lineSeparator());
        Assert.assertEquals("Liczba pozycji: 5", printedLines[printedLines.length - 1]);
    }

    @Test
    public void testEmptyInvoicePrintProducts() {
        Assert.assertEquals("Invoice No. " + invoice.getNumber() + System.lineSeparator() +
                "Liczba pozycji: 0", invoice.printProducts());
    }

    @Test
    public void testNextInvoiceHasNextNumber() {
        int number1 = new Invoice().getNumber();
        int number2 = new Invoice().getNumber();
        Assert.assertEquals(number2, number1 + 1);
    }

    @Test
    public void testInvoiceNumberGraterThenZero() {
        Assert.assertThat(new Invoice().getNumber(), Matchers.greaterThan(0));
    }

    @Test
    public void testEmptyInvoiceHasEmptySubtotal() {
        Assert.assertThat(BigDecimal.ZERO, Matchers.comparesEqualTo(invoice.getNetTotal()));
    }

    @Test
    public void testEmptyInvoiceHasEmptyTaxAmount() {
        Assert.assertThat(BigDecimal.ZERO, Matchers.comparesEqualTo(invoice.getTaxTotal()));
    }

    @Test
    public void testEmptyInvoiceHasEmptyTotal() {
        Assert.assertThat(BigDecimal.ZERO, Matchers.comparesEqualTo(invoice.getGrossTotal()));
    }

    @Test
    public void testInvoiceSubtotalWithTwoDifferentProducts() {
        Product onions = new TaxFreeProduct("Warzywa", new BigDecimal("10"));
        Product apples = new TaxFreeProduct("Owoce", new BigDecimal("10"));
        invoice.addProduct(onions);
        invoice.addProduct(apples);
        Assert.assertThat(new BigDecimal("20"), Matchers.comparesEqualTo(invoice.getNetTotal()));
    }

    @Test
    public void testInvoiceSubtotalWithManySameProducts() {
        Product onions = new TaxFreeProduct("Warzywa", BigDecimal.valueOf(10));
        invoice.addProduct(onions, 100);
        Assert.assertThat(new BigDecimal("1000"), Matchers.comparesEqualTo(invoice.getNetTotal()));
    }

    @Test
    public void testInvoiceHasTheSameSubtotalAndTotalIfTaxIsZero() {
        Product taxFreeProduct = new TaxFreeProduct("Warzywa", new BigDecimal("199.99"));
        invoice.addProduct(taxFreeProduct);
        Assert.assertThat(invoice.getNetTotal(), Matchers.comparesEqualTo(invoice.getGrossTotal()));
    }

    @Test
    public void testInvoiceHasProperSubtotalForManyProducts() {
        invoice.addProduct(new TaxFreeProduct("Owoce", new BigDecimal("200")));
        invoice.addProduct(new DairyProduct("Maslanka", new BigDecimal("100")));
        invoice.addProduct(new OtherProduct("Wino", new BigDecimal("10")));
        Assert.assertThat(new BigDecimal("310"), Matchers.comparesEqualTo(invoice.getNetTotal()));
    }

    @Test
    public void testInvoiceHasProperTaxValueForManyProduct() {
        // tax: 0
        invoice.addProduct(new TaxFreeProduct("Pampersy", new BigDecimal("200")));
        // tax: 8
        invoice.addProduct(new DairyProduct("Kefir", new BigDecimal("100")));
        // tax: 2.30
        invoice.addProduct(new OtherProduct("Piwko", new BigDecimal("10")));
        Assert.assertThat(new BigDecimal("10.30"), Matchers.comparesEqualTo(invoice.getTaxTotal()));
    }

    @Test
    public void testInvoiceHasProperTotalValueForManyProduct() {
        // price with tax: 200
        invoice.addProduct(new TaxFreeProduct("Maskotki", new BigDecimal("200")));
        // price with tax: 108
        invoice.addProduct(new DairyProduct("Maslo", new BigDecimal("100")));
        // price with tax: 12.30
        invoice.addProduct(new OtherProduct("Chipsy", new BigDecimal("10")));
        Assert.assertThat(new BigDecimal("320.30"), Matchers.comparesEqualTo(invoice.getGrossTotal()));
    }

    @Test
    public void testInvoiceHasPropoerSubtotalWithQuantityMoreThanOne() {
        // 2x kubek - price: 10
        invoice.addProduct(new TaxFreeProduct("Kubek", new BigDecimal("5")), 2);
        // 3x kozi serek - price: 30
        invoice.addProduct(new DairyProduct("Kozi Serek", new BigDecimal("10")), 3);
        // 1000x pinezka - price: 10
        invoice.addProduct(new OtherProduct("Pinezka", new BigDecimal("0.01")), 1000);
        Assert.assertThat(new BigDecimal("50"), Matchers.comparesEqualTo(invoice.getNetTotal()));
    }

    @Test
    public void testInvoiceHasPropoerTotalWithQuantityMoreThanOne() {
        // 2x chleb - price with tax: 10
        invoice.addProduct(new TaxFreeProduct("Chleb", new BigDecimal("5")), 2);
        // 3x chedar - price with tax: 32.40
        invoice.addProduct(new DairyProduct("Chedar", new BigDecimal("10")), 3);
        // 1000x pinezka - price with tax: 12.30
        invoice.addProduct(new OtherProduct("Pinezka", new BigDecimal("0.01")), 1000);
        Assert.assertThat(new BigDecimal("54.70"), Matchers.comparesEqualTo(invoice.getGrossTotal()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvoiceWithZeroQuantity() {
        invoice.addProduct(new TaxFreeProduct("Tablet", new BigDecimal("1678")), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvoiceWithNegativeQuantity() {
        invoice.addProduct(new DairyProduct("Zsiadle mleko", new BigDecimal("5.55")), -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddingNullProduct() {
        invoice.addProduct(null);
    }
}
