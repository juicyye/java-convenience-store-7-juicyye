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
            sb.append("==============W 편의점================\n");
            sb.append("상품명\t\t수량\t금액\n");

            // 주문 상품 정보
            List<OrderProduct> orderProducts = order.getOrderProducts();

            for (OrderProduct orderProduct : orderProducts) {
                Product product = orderProduct.getProduct();
                int count = orderProduct.getCount();
                int price = orderProduct.getOrderPrice() * count;

                sb.append(product.getItem().getName()).append("\t\t")
                        .append(count).append("\t")
                        .append(price).append("\n");
            }

            // 증정 상품 정보
            Discount discount = order.getDiscount();
            List<ItemCount> itemCounts = discount.getItemCounts();
            if (!itemCounts.isEmpty()) {
                sb.append("=============증\t정===============\n");
                for (ItemCount itemCount : itemCounts) {
                    sb.append(itemCount.getItem().getName()).append("\t\t")
                            .append(itemCount.getCount()).append("\n");
                }
            }

            int i = order.getTotalPrice() - discount.getTotalDiscount();

            // 합계 및 할인 정보
            sb.append("====================================\n");
            sb.append("총구매액\t\t").append(order.getTotalOrderCount()).append("\t").append(order.getTotalOrderCount()).append("\n");
            sb.append("행사할인\t\t\t").append(discount.getPromotionDiscount()).append("\n");
            sb.append("멤버십할인\t\t\t").append(discount.getMembershipDiscount()).append("\n");
            sb.append("내실돈\t\t\t").append(i).append("\n");
        }

        return sb.toString();
    }
}
