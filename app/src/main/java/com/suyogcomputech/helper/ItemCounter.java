package com.suyogcomputech.helper;

/**
 * Created by Suyog on 9/12/2016.
 */
public class ItemCounter {
    private static ItemCounter ourInstance = new ItemCounter();

    public static ItemCounter getInstance() {
        return ourInstance;
    }

    private ItemCounter() {
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public static ItemCounter getOurInstance() {
        return ourInstance;
    }

    public static void setOurInstance(ItemCounter ourInstance) {
        ItemCounter.ourInstance = ourInstance;
    }

    int itemCount =0;
}
