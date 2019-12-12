package cl.transbank.onepay.pos.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.math.BigDecimal;

import cl.transbank.onepay.pos.R;
import cl.transbank.onepay.pos.model.Cart;
import cl.transbank.onepay.pos.model.Item;
import cl.transbank.onepay.pos.model.ItemImpl;
import cl.transbank.onepay.pos.utils.CurrencyFormat;

public class CheckRecyclerViewAdapter extends RecyclerView.Adapter<CheckRecyclerViewHolder> implements View.OnClickListener {

    private Context context;
    private final Cart cart;
    private OnUpdateClickedListener mCallback;

    public interface OnUpdateClickedListener {
         void updateView(String url);
    }

    public void setOnUpdateClickedListener(OnUpdateClickedListener mCallback) {
        this.mCallback = mCallback;
    }

    public CheckRecyclerViewAdapter(Context context, final Cart cart) {
        this.context = context;
        this.cart = cart;
    }

    @Override
    public CheckRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.check_layout, parent, false);
        CheckRecyclerViewHolder productHolder = new CheckRecyclerViewHolder(layoutView);
        return productHolder;
    }

    @Override
    public void onBindViewHolder(CheckRecyclerViewHolder holder, final int position) {
        //get product quantity
        holder.quantity.setText(String.valueOf(cart.getProducts().get(position).getQuantity()));
        holder.productName.setText(cart.getProducts().get(position).getDescription());
        holder.productPrice.setText(CurrencyFormat.formatBigDecimalToCurrency(cart.getProducts().get(position).getAmount().multiply(new BigDecimal(cart.getProducts().get(position).getQuantity()))));

        switch (cart.getProducts().get(position).getKey()){
            case 1:
                holder.productLogo.setImageResource(R.drawable.item_ticket);
                break;
            case 2:
                holder.productLogo.setImageResource(R.drawable.item_popcorn);
                break;
            case 7:
                holder.productLogo.setImageResource(R.drawable.massage_icon);
                break;
            case 6:
                holder.productLogo.setImageResource(R.drawable.product_icon);
                break;
            default:
              holder.productLogo.setImageResource(R.drawable.gym_icon);
              break;

        }

        holder.removeProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Producto eliminado", Toast.LENGTH_LONG).show();

                //remove from cart
                cart.remove(cart.getProducts().get(position).getKey());
                updateCartItems();
                mCallback.updateView("update total");


            }
        });

        holder.btMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //minus
                int qty = cart.getProducts().get(position).getQuantity();
                if(qty>1) {
                    cart.removeItem(cart.getProducts().get(position).getKey());
                }else if(qty==1){
                    cart.remove(cart.getProducts().get(position).getKey());
                }

                updateCartItems();
                mCallback.updateView("update total");

            }
        });

        holder.btPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //plus
                Item item = cart.getProducts().get(position);
                Item aux = new ItemImpl(item.getDescription(),1,item.getAmount()," ",1,item.getKey());
                cart.add(aux,item.getKey());
                updateCartItems();
                mCallback.updateView("update total");

            }
        });

    }

    @Override
    public int getItemCount() {
        return cart.getProducts().size();
    }

    public void updateCartItems() {
        notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        mCallback.updateView("update total");
    }
}
