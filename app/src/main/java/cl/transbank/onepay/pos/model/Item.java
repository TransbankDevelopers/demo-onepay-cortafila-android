package cl.transbank.onepay.pos.model;

import android.os.Parcelable;

import java.math.BigDecimal;

public interface Item extends Parcelable {

    String getDescription();
    BigDecimal getAmount();
    Integer getQuantity();
    Integer getKey();

    void setQuantity(Integer qty);
    void setAmount(BigDecimal qty);
}
