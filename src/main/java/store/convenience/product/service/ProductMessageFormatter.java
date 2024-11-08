package store.convenience.product.service;

import java.util.List;
import java.util.Optional;
import store.convenience.product.domain.Item;
import store.convenience.product.domain.Product;
import store.convenience.product.domain.ProductInventory;
import store.convenience.promotion.domain.Promotion;

public class ProductMessageFormatter {

    private static final ProductMessageFormatter instance = new ProductMessageFormatter();

    private ProductMessageFormatter() {
    }

    public static ProductMessageFormatter getInstance() {
        return instance;
    }

    public String formatInventory(List<ProductInventory> inventories) {
        StringBuilder sb = new StringBuilder();
        for (ProductInventory inventory : inventories) {
            appendInventoryDetails(sb, inventory);
        }
        return sb.toString();
    }

    private void appendInventoryDetails(StringBuilder sb, ProductInventory inventory) {
        List<Product> products = inventory.getProducts();
        for (Product product : products) {
            appendProductDetails(sb, product);
        }
    }

    private void appendProductDetails(StringBuilder sb, Product product) {
        sb.append(formatItemLine(product.getItem()));
        sb.append(formatProductLine(product));
        appendPromotionDetails(sb, product);
        sb.append("\n");
    }

    private String formatProductLine(Product product) {
        Integer quantity = product.getQuantity();
        if (quantity == 0) {
            return "재고 없음 ";
        }
        return String.format("%s개 ", quantity);
    }

    private String formatItemLine(Item item) {
        return String.format("- %s %s원 ", item.getName(), String.format("%,d", item.getPrice()));
    }

    private void appendPromotionDetails(StringBuilder sb, Product product) {
        Optional<Promotion> applicablePromotion = product.getApplicablePromotion();
        applicablePromotion.ifPresent(i -> sb.append(i.getDetails().name()));
    }

}