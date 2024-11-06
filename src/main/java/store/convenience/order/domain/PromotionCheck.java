package store.convenience.order.domain;

import store.convenience.product.domain.Product;

public class PromotionCheck {

    private Product product;
    private int count;
    private boolean isPromotion;
    private boolean bonusAvailable;
    private boolean isExceeded;
    private int bonusItemCount;
    private int exceededItemCount;

    public PromotionCheck(Product product, int count, boolean isPromotion,
                          boolean bonusAvailable, boolean isExceeded, int bonusItemCount,
                          int exceededItemCount) {
        this.product = product;
        this.count = count;
        this.bonusAvailable = bonusAvailable;
        this.isExceeded = isExceeded;
        this.bonusItemCount = bonusItemCount;
        this.exceededItemCount = exceededItemCount;
    }

    public void addBonus() {
        this.count += bonusItemCount;
    }

    public void removeCount() {
        this.count -= bonusItemCount;
    }

    public Product getProduct() {
        return product;
    }

    public int getCount() {
        return count;
    }

    public boolean isPromotion() {
        return isPromotion;
    }

    public boolean isBonusAvailable() {
        return bonusAvailable;
    }

    public boolean isExceeded() {
        return isExceeded;
    }

    public int getBonusItemCount() {
        return bonusItemCount;
    }

    public int getExceededItemCount() {
        return exceededItemCount;
    }
}
