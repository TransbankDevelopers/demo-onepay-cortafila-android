package cl.transbank.onepay.pos.model;


import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;

public class ItemImpl implements Item {
    private String description;
    private Integer quantity;
    private BigDecimal amount;
    private String additionalData;
    private Integer key;

    public ItemImpl(String description, Integer quantity, BigDecimal amount, String additionalData, Integer expire,Integer key) {
        this.description = description;
        this.quantity = quantity;
        this.amount = amount;
        this.additionalData = additionalData;
        this.expire = expire;
        this.key = key;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getQuantity() {
        return quantity;
    }

    @Override
    public Integer getKey() {
        return key;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getAdditionalData() {
        return additionalData;
    }

    public void setAdditionalData(String additionalData) {
        this.additionalData = additionalData;
    }

    public Integer getExpire() {
        return expire;
    }

    public void setExpire(Integer expire) {
        this.expire = expire;
    }

    private Integer expire;

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(description);
        if (quantity == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(quantity);
        }
        if (amount == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(amount.intValue());
        }
        dest.writeString(additionalData);
    }

    protected ItemImpl(Parcel in) {
        description = in.readString();
        quantity = in.readByte() == 0x00 ? null : in.readInt();
        amount =  in.readByte() == 0x00 ? null : new BigDecimal(in.readInt());
        additionalData = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Item> CREATOR = new Parcelable.Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new ItemImpl(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

}
