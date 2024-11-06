package store.convenience.order.domain;

import store.convenience.product.domain.Item;

public class PromotionCheck {

    private Item item;
    private int count;
    private boolean bonusAvailable;
    private boolean isExceeded;
    private int bonusItemCount;
    private int exceededItemCount;

    public PromotionCheck(Item item, int count, boolean bonusAvailable, boolean isExceeded, int bonusItemCount, int exceededItemCount) {
        this.item = item;
        this.count = count;
        this.bonusAvailable = bonusAvailable;
        this.isExceeded = isExceeded;
        this.bonusItemCount = bonusItemCount;
        this.exceededItemCount = exceededItemCount;
    }

    public void addBonus(){
        this.count += bonusItemCount;
    }

    public void removeCount(){
        this.count -= bonusItemCount;
    }

    public Item getItem() {
        return item;
    }

    public int getCount() {
        return count;
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
