package store.convenience.order.service;

import java.util.List;
import store.convenience.order.domain.Discount;
import store.convenience.order.domain.ItemCount;
import store.convenience.order.domain.Order;
import store.convenience.order.domain.OrderProduct;
import store.convenience.product.domain.Product;

public class OrderMessageService {

    public String showReceipt(List<Order> orders) {
        StringBuilder sb = new StringBuilder();
        for (Order order : orders) {
            printReceiptHeader(sb);
            printOrderProducts(order, sb);
            printPromotionProducts(order, sb);
            printSummary(order, sb);
        }
        return sb.toString();
    }

    private void printReceiptHeader(StringBuilder sb) {
        sb.append("\n==============W 편의점================\n");
        sb.append(addNamePadding("상품명"))
                .append(addPadding("수량"))
                .append(addPadding("금액"))
                .append("\n");
    }

    private void printOrderProducts(Order order, StringBuilder sb) {
        for (OrderProduct orderProduct : order.getOrderProducts()) {
            printOrderProduct(orderProduct, sb);
        }
    }

    private void printOrderProduct(OrderProduct orderProduct, StringBuilder sb) {
        Product product = orderProduct.getProduct();
        int count = orderProduct.getCount();
        int price = orderProduct.getOrderPrice() * count;

        sb.append(addNamePadding(product.getItem().getName()))
                .append(formatWithCommas(count))
                .append(formatWithCommas(price))
                .append("\n");
    }

    private void printPromotionProducts(Order order, StringBuilder sb) {
        Discount discount = order.getDiscount();
        List<ItemCount> itemCounts = discount.getItemCounts();

        sb.append("=============증\t정===============\n");
        if (!itemCounts.isEmpty()) {
            for (ItemCount itemCount : itemCounts) {
                sb.append(addNamePadding(itemCount.getItem().getName()))
                        .append(formatWithCommas(itemCount.getCount()))
                        .append("\n");
            }
        }
    }

    private void printSummary(Order order, StringBuilder sb) {
        Discount discount = order.getDiscount();
        int finalAmount = order.getTotalPrice() - discount.getTotalDiscount();

        sb.append("====================================\n");
        sb.append(addNamePadding("총구매액"))
                .append(formatWithCommas(order.getTotalOrderCount()))
                .append(formatWithCommas(order.getTotalPrice()))
                .append("\n");
        sb.append(addTotalPadding("행사할인"))
                .append(formatWithMinus(discount.getPromotionDiscount()))
                .append("\n");
        sb.append(addTotalPadding("멤버십할인"))
                .append(formatWithMinus(discount.getMembershipDiscount()))
                .append("\n");
        sb.append(addTotalPadding("내실돈"))
                .append(formatWithCommas(finalAmount));
    }

    private String addNamePadding(String input) {
        return String.format("%-5s\t\t\t", input);
    }

    private String addPadding(String input) {
        return String.format("%-5s\t\t", input);
    }

    private String addTotalPadding(String input) {
        return String.format("%-5s\t\t\t\t\t\t", input);
    }

    private String formatWithMinus(int number) {
        return String.format("%-5s", "-" + String.format("%,-5d", number));
    }

    private String formatWithCommas(int number) {
        return String.format("%-5s\t\t", String.format("%,d", number));
    }

}