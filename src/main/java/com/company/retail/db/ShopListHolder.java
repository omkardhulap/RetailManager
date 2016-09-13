package com.company.retail.db;
/**
 * @author omkar
 * @Description in-memory placeholder to holds an array of shops
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.company.retail.models.Shop;

public class ShopListHolder {
    
	@NotNull
    private List<Shop> shopList 
    = Collections.synchronizedList(new ArrayList<Shop>());

    public Shop get(int index) {
        return shopList.get(index);
    }

    public void add(Shop shop) {
        shopList.add(shop);
    }

    public void remove(Shop shop) {
        shopList.remove(shop);
    }

    public void remove(int index) {
        shopList.remove(index);
    }

    public List<Shop> getAll() {
        return Collections.unmodifiableList(shopList);
    }
}
