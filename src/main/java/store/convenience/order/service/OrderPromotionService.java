package store.convenience.order.service;

import static store.global.util.StoreConstant.MAX_MEMBERSHIP_DISCOUNT;
import static store.global.util.StoreConstant.MEMBERSHIP_RATE;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import store.convenience.order.controller.req.OrderCreateReqDto;
import store.convenience.order.domain.Discount;
import store.convenience.order.domain.ItemCount;
import store.convenience.product.domain.Item;
import store.convenience.product.domain.Product;
import store.convenience.product.service.port.ProductRepository;
import store.convenience.promotion.domain.Promotion;
import store.global.exception.NotFoundException;
import store.global.util.ErrorMessage;

public class OrderPromotionService {

    private final ProductRepository productRepository;

    public OrderPromotionService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Discount calculateOrderDiscount(List<OrderCreateReqDto> createReqDtos, boolean hasMembership) {
        List<ItemCount> items = new ArrayList<>();
        int totalPrice = 0;
        int promotionAmount = 0;

        for (OrderCreateReqDto createReqDto : createReqDtos) {
            Product product = getProduct(createReqDto.itemName());
            totalPrice += calculateOrderAmounts(product, createReqDto);
            promotionAmount += calculatePromotionAmounts(product, createReqDto, items);
        }

        int membershipDiscount = calculateMembershipDiscount(totalPrice - promotionAmount, hasMembership);

        return new Discount(items, promotionAmount, membershipDiscount);
    }

    private int calculateOrderAmounts(Product product, OrderCreateReqDto createReqDto) {
        return product.getItem().getPrice() * createReqDto.count();
    }

    private int calculatePromotionAmounts(Product product, OrderCreateReqDto createReqDto, List<ItemCount> items) {
        if (!isPromotionActive(product.getPromotion(), createReqDto.currentDate())) {
            return 0;
        }
        int availableCount = Math.min(product.getQuantity(), createReqDto.count());
        int purChaseQuantity = product.getPromotion().getDetails().purchaseQuantity();
        int bonusQuantity = product.getPromotion().getDetails().bonusQuantity();

        if (purChaseQuantity == 1 && bonusQuantity == 1) {
            return calculateOneGetOneFree(availableCount, product.getItem(), items);
        }
        int bonusItemCount = calculateTwoGetOneFree(availableCount, purChaseQuantity, bonusQuantity);
        items.add(new ItemCount(product.getItem(), bonusItemCount));
        return product.getItem().getPrice() * bonusItemCount;
    }

    private int calculateOneGetOneFree(int availableCount, Item item, List<ItemCount> items) {
        int bonusItemCount = availableCount / 2;
        items.add(new ItemCount(item, bonusItemCount));
        return item.getPrice() * bonusItemCount;
    }

    private int calculateTwoGetOneFree(int availableCount, int purchaseQuantity, int bonusQuantity) {
        int promotionSets = availableCount / purchaseQuantity;
        return promotionSets * bonusQuantity;
    }

    private boolean isPromotionActive(Promotion promotion, LocalDate currentDate) {
        return promotion != null && promotion.isActivePromotion(currentDate);
    }

    private int calculateMembershipDiscount(int priceAfterPromotion, boolean hasMembership) {
        if (!hasMembership) {
            return 0;
        }
        double membershipDiscount =  Math.min(MAX_MEMBERSHIP_DISCOUNT, priceAfterPromotion * MEMBERSHIP_RATE);
        return (int) Math.floor(membershipDiscount / 100) * 100;
    }

    private Product getProduct(String itemName) {
        return productRepository.findByName(itemName)
                .orElseThrow(() -> new NotFoundException(
                        ErrorMessage.NOT_FOUND_PRODUCT.getMessage()));
    }

    public OrderCreateReqDto applyBonus(OrderCreateReqDto createReqDto, int bonusCount) {
        return applyQuantityAdjustment(createReqDto, bonusCount);
    }

    private OrderCreateReqDto applyQuantityAdjustment(OrderCreateReqDto createReqDto, int adjustmentCount) {
        return new OrderCreateReqDto(
                createReqDto.itemName(),
                createReqDto.count() + adjustmentCount,
                createReqDto.currentDate()
        );
    }

}
