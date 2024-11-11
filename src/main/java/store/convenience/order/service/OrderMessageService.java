package store.convenience.order.service;

import java.util.List;
import store.convenience.order.domain.Discount;
import store.convenience.order.domain.ItemCount;
import store.convenience.order.domain.Order;
import store.convenience.order.domain.OrderProduct;
import store.convenience.product.domain.Product;

public class OrderMessageService {
    private static final String NEWLINE = "\n";
    private static final String STORE_NAME = "\n==============W 편의점================\n";
    private static final String NAME_HEADER = "상품명";
    private static final String COUNT_HEADER = "수량";
    private static final String PRICE_HEADER = "금액";
    private static final String GIFT_SECTION_HEADER = "=============증\t정===================\n";
    private static final String DIVIDER = "=====================================\n";
    private static final String TOTAL_PRICE_HEADER = "총구매액";
    private static final String MEMBERSHIP_DISCOUNT_HEADER = "멤버십할인";
    private static final String PROMOTION_DISCOUNT_HEADER = "행사할인";
    private static final String PAYMENT_PRICE_HEADER = "내실돈";

    public String showReceipt(Order order) {
        StringBuilder sb = new StringBuilder();
        printReceiptHeader(sb);
        printOrderProducts(order, sb);
        printPromotionProducts(order, sb);
        printSummary(order, sb);
        return sb.toString();
    }

    private void printReceiptHeader(StringBuilder sb) {
        sb.append(STORE_NAME);
        sb.append(addNamePadding(NAME_HEADER))
                .append(addPadding(COUNT_HEADER))
                .append(addPadding(PRICE_HEADER))
                .append(NEWLINE);
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
                .append(NEWLINE);
    }

    private void printPromotionProducts(Order order, StringBuilder sb) {
        Discount discount = order.getDiscount();
        List<ItemCount> itemCounts = discount.getItemCounts();

        sb.append(GIFT_SECTION_HEADER);
        if (!itemCounts.isEmpty()) {
            for (ItemCount itemCount : itemCounts) {
                sb.append(addNamePadding(itemCount.getItem().getName()))
                        .append(formatWithCommas(itemCount.getCount()))
                        .append(NEWLINE);
            }
        }
    }

    private void printSummary(Order order, StringBuilder sb) {
        Discount discount = order.getDiscount();
        int finalAmount = order.getTotalPrice() - discount.getTotalDiscount();

        sb.append(DIVIDER);
        printTotalPrice(order, sb);
        printEventDiscount(discount, sb);
        printMembershipDiscount(discount, sb);
        printFinalPrice(finalAmount, sb);
    }

    private void printTotalPrice(Order order, StringBuilder sb) {
        sb.append(addNamePadding(TOTAL_PRICE_HEADER))
                .append(formatWithCommas(order.getTotalOrderCount()))
                .append(formatWithCommas(order.getTotalPrice()))
                .append(NEWLINE);
    }

    private void printEventDiscount(Discount discount, StringBuilder sb) {
        sb.append(addPaddingEventDiscount(PROMOTION_DISCOUNT_HEADER))
                .append(formatWithMinus(discount.getPromotionDiscount()))
                .append(NEWLINE);
    }

    private void printMembershipDiscount(Discount discount, StringBuilder sb) {
        sb.append(addPaddingMembershipDiscount(MEMBERSHIP_DISCOUNT_HEADER))
                .append(formatWithMinus(discount.getMembershipDiscount()))
                .append(NEWLINE);
    }

    private void printFinalPrice(int finalAmount, StringBuilder sb) {
        sb.append(addTotalPadding(PAYMENT_PRICE_HEADER))
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

    private String addPaddingEventDiscount(String input) {
        return String.format("%-25s", input);
    }

    private String addPaddingMembershipDiscount(String input) {
        return String.format("%-24s", input);
    }

    private String formatWithMinus(int number) {
        return String.format("%-5s", "-" + String.format("%,-5d", number));
    }

    private String formatWithCommas(int number) {
        return String.format("%-5s\t\t", String.format("%,d", number));
    }

}