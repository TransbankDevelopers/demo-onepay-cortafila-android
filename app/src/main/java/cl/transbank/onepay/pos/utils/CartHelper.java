package cl.transbank.onepay.pos.utils;

import cl.transbank.onepay.pos.model.Cart;

public class CartHelper {

    /**
     * create instance of cart
     */
    private static Cart cart = new Cart();

    /**
     * singleton pattern
     * @return
     */
    public static Cart getCart() {
        if (cart == null) {
            cart = new Cart();
        }

        return cart;
    }
}
