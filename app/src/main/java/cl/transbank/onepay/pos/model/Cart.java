package cl.transbank.onepay.pos.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import cl.transbank.onepay.pos.exceptions.ItemNotFoundException;

public class Cart implements Serializable {
    private static final long serialVersionUID = 42L;

    private Map<Integer, Item> cartItemMap = new LinkedHashMap<>();
    private BigDecimal totalPrice = BigDecimal.ZERO;
    private int totalQuantity = 0;


    public void add(final Item item,Integer key) {


        if (cartItemMap.containsKey(key)) {
            Item i = cartItemMap.get(key);
            int qty = i.getQuantity();
            i.setQuantity(qty+ item.getQuantity());
            cartItemMap.put(key, i);
        } else {
            cartItemMap.put(key, item);
        }

        totalPrice = totalPrice.add(item.getAmount());
        totalQuantity += item.getQuantity();
    }


    public void remove(final Integer key) throws ItemNotFoundException {
        if (!cartItemMap.containsKey(key)) throw new ItemNotFoundException();

        Item item = cartItemMap.get(key);
        if(item==null) throw new ItemNotFoundException();

        cartItemMap.remove(key);

        totalPrice = totalPrice.subtract(item.getAmount().multiply(new BigDecimal(item.getQuantity())));
        totalQuantity -= item.getQuantity();
    }

    public void removeItem(final Integer key) throws ItemNotFoundException {
        if (!cartItemMap.containsKey(key)) throw new ItemNotFoundException();

        Item item = cartItemMap.get(key);
        if(item==null) throw new ItemNotFoundException();

        int qty = item.getQuantity();
        item.setQuantity(qty - 1);
        cartItemMap.put(key,item);

        totalPrice = totalPrice.subtract(item.getAmount());
        totalQuantity -= 1;
    }


    public void clear() {
        cartItemMap.clear();
        totalPrice = BigDecimal.ZERO;
        totalQuantity = 0;
    }

    public int getQuantity(final Integer key) throws ItemNotFoundException {
        if (!cartItemMap.containsKey(key)) throw new ItemNotFoundException();
        Item item = cartItemMap.get(key);
        if(item==null) throw new ItemNotFoundException();
        return item.getQuantity();
    }


    public BigDecimal getCost(final Integer key) throws ItemNotFoundException {
        if (!cartItemMap.containsKey(key)) throw new ItemNotFoundException();

        Item item = cartItemMap.get(key);
        if(item==null) throw new ItemNotFoundException();
        return item.getAmount().multiply(new BigDecimal(item.getQuantity()));
    }


    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void updateTotalPrice(BigDecimal amount){
        this.totalPrice =this.totalPrice.add(amount);
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }


    public ArrayList<Item> getProducts() {
        return new ArrayList<>(cartItemMap.values());
    }


    @Override
    public String toString() {
        StringBuilder strBuilder = new StringBuilder();
        for (Item entry : cartItemMap.values()) {
            strBuilder.append(String.format(Locale.getDefault(),"Productos: %s, Precio: $%s, Cantidad: %d%n", entry.getDescription(), formatAmount(entry.getAmount()), entry.getQuantity()));
        }
        strBuilder.append(String.format(Locale.getDefault(),"Cantidad total: %d, Precio Total: $%s", totalQuantity, formatAmount(totalPrice)));

        return strBuilder.toString();
    }

    private String formatAmount(BigDecimal amount){
        return amount.setScale(0,BigDecimal.ROUND_HALF_UP).toPlainString();
    }
}
