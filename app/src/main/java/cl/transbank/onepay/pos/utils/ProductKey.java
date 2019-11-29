package cl.transbank.onepay.pos.utils;

public enum ProductKey {
    CINE_TICKET(1),
    CINE_POPCORN(2),
    GYM_SMARTFIT(3),
    GYM_ENERGY(4),
    GYM_SPORTLIFE(5),
    OTHERS(6),
    MASSAGE(7);

    private Integer key;

    ProductKey(Integer key){
        this.setKey(key);
    }


    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }
}
