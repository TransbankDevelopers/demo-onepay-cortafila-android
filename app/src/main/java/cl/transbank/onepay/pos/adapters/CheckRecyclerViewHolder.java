package cl.transbank.onepay.pos.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import cl.transbank.onepay.pos.R;


public class CheckRecyclerViewHolder extends RecyclerView.ViewHolder{

    public TextView quantity, productName, productPrice;
    public ImageView removeProduct,productLogo,btMinus,btPlus;

    public CheckRecyclerViewHolder(View itemView) {
        super(itemView);

        quantity = itemView.findViewById(R.id.quantity);
        productName =itemView.findViewById(R.id.product_name);
        productPrice = itemView.findViewById(R.id.product_price);
        removeProduct = itemView.findViewById(R.id.remove_from_cart);
        productLogo = itemView.findViewById(R.id.product_logo);
        btMinus = itemView.findViewById(R.id.checkout_minus);
        btPlus = itemView.findViewById(R.id.checkout_plus);
    }
}
