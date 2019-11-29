package cl.transbank.onepay.pos.exceptions;

public class ItemNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 43L;

    private static final String DEFAULT_MESSAGE = "Product is not found in the shopping cart.";

    public ItemNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public ItemNotFoundException(String message) {
        super(message);
    }
}
