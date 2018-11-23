package cl.transbank.onepay.pos.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Item implements Parcelable {
    private String description;
    private Integer quantity;
    private Integer amount;
    private String additionalData;

    public Item(String description, Integer quantity, Integer amount, String additionalData, Integer expire) {
        this.description = description;
        this.quantity = quantity;
        this.amount = amount;
        this.additionalData = additionalData;
        this.expire = expire;
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

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
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
            dest.writeInt(amount);
        }
        dest.writeString(additionalData);
    }

    protected Item(Parcel in) {
        description = in.readString();
        quantity = in.readByte() == 0x00 ? null : in.readInt();
        amount = in.readByte() == 0x00 ? null : in.readInt();
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
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };
}
