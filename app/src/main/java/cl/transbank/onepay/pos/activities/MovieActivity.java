package cl.transbank.onepay.pos.activities;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.math.BigDecimal;
import cl.transbank.onepay.pos.R;
import cl.transbank.onepay.pos.model.Cart;
import cl.transbank.onepay.pos.model.Item;
import cl.transbank.onepay.pos.model.ItemImpl;
import cl.transbank.onepay.pos.utils.CartHelper;
import cl.transbank.onepay.pos.utils.Constants;
import cl.transbank.onepay.pos.utils.ProductKey;


public class MovieActivity extends AppCompatActivity {

    private Button goToCart;
    private TextView ticketQty,ticketQty2;


    final Cart cart = CartHelper.getCart();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        initializeUI();
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
    }


    public void openMainView(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void initializeUI() {
        ImageView qtyTicketBtn,qtyPopCornBtn;
         goToCart = findViewById(R.id.check_btn);
         ticketQty = findViewById(R.id.cine_cantidad);
         ticketQty2 = findViewById(R.id.cine_cantidad_2);
         qtyTicketBtn = findViewById(R.id.cine_add_qty);
         qtyPopCornBtn = findViewById(R.id.cine_plus_qty_2);

         checkGoToCartBtn();
         setQtyLabel();

        assert qtyTicketBtn != null;
        qtyTicketBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int qtyAdded = Integer.parseInt(ticketQty.getText().toString());
                qtyAdded++;

                ticketQty.setText(String.valueOf(qtyAdded));
                addProducts(ProductKey.CINE_TICKET);

            }
        });

        ImageView minusBtn = findViewById(R.id.cine_minus_qty);
        assert minusBtn != null;
        minusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qtyMinus = Integer.parseInt(ticketQty.getText().toString());
                if(qtyMinus>0) {
                    qtyMinus--;
                    ticketQty.setText(String.valueOf(qtyMinus));
                    subtractProduct(ProductKey.CINE_TICKET);
                }

            }
        });

        assert qtyPopCornBtn != null;
        qtyPopCornBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int qtyAdded = Integer.parseInt(ticketQty2.getText().toString());
                qtyAdded++;

                ticketQty2.setText(String.valueOf(qtyAdded));
                addProducts(ProductKey.CINE_POPCORN);

            }
        });

        ImageView minusBtn2 = findViewById(R.id.cine_minus_qty_2);
        assert minusBtn2 != null;
        minusBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qtyMinus = Integer.parseInt(ticketQty2.getText().toString());
                if(qtyMinus>0) {
                    qtyMinus--;
                    ticketQty2.setText(String.valueOf(qtyMinus));
                    subtractProduct(ProductKey.CINE_POPCORN);
                }

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem menuItem = menu.findItem(R.id.action_shop);
        int mCount = cart.getTotalQuantity();
        menuItem.setIcon(createDrawableFromView(mCount, R.drawable.cart));
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                onBackPressed();
                return true;
            case R.id.action_shop:
                // start main activity
                Intent checkoutIntent = new Intent(this, MainActivity.class);
                startActivity(checkoutIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private Drawable createDrawableFromView(int count, int backgroundImageId) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.shopping_layout, null);
        view.setBackgroundResource(backgroundImageId);

        if (count == 0) {
            View counterTextPanel = view.findViewById(R.id.counterValuePanel);
            counterTextPanel.setVisibility(View.GONE);
        } else {
            TextView textView =  view.findViewById(R.id.count);
            textView.setText("" + count);
        }

        view.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(),
                view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Drawable background = view.getBackground();

        if (background != null) {
            background.draw(canvas);
        }
        view.draw(canvas);

        return new BitmapDrawable(getResources(), bitmap);
    }

    private void invalidateCart() {
        invalidateOptionsMenu();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        if(Build.VERSION.SDK_INT > 11) {
            invalidateOptionsMenu();
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onResume(){
        super.onResume();
        invalidateOptionsMenu();
        checkGoToCartBtn();
        setQtyLabel();
    }

    private void checkGoToCartBtn(){
        if(cart.getTotalQuantity() > 0){
            goToCart.setEnabled(Boolean.TRUE);
            return;
        }

        goToCart.setEnabled(Boolean.FALSE);
    }

    private void addProducts(ProductKey key) {
        switch (key) {
            case CINE_TICKET:
                Item item = new ItemImpl(Constants.CINE_DESCRIPTION_TICKETS, 1, new BigDecimal(Constants.CINE_PRICE), Constants.CINE, 1, key.getKey());
                cart.add(item, key.getKey());
                Toast.makeText(MovieActivity.this, Constants.PRODUCT_ADDED, Toast.LENGTH_LONG).show();
                break;

            case CINE_POPCORN:
                Item item2 = new ItemImpl(Constants.CINE_DESCRIPTION_COMBO, 1, new BigDecimal(Constants.CINE_PRICE), Constants.CINE, 1, key.getKey());
                cart.add(item2, key.getKey());
                Toast.makeText(MovieActivity.this, Constants.PRODUCT_ADDED, Toast.LENGTH_LONG).show();
                break;

        }
        goToCart.setEnabled(Boolean.TRUE);
        invalidateCart();
    }

    private void subtractProduct(ProductKey key){
        cart.removeItem(key.getKey());
        Toast.makeText(MovieActivity.this, Constants.PRODUCT_SUBTRACT, Toast.LENGTH_LONG).show();
        checkGoToCartBtn();
        invalidateCart();
    }

    private void setQtyLabel(){
        ticketQty.setText(String.valueOf(0));
        ticketQty2.setText(String.valueOf(0));
        if(cart.getTotalQuantity()>0){
           for(Item item: cart.getProducts()){
               if(item.getKey().equals(ProductKey.CINE_TICKET.getKey())){
                   ticketQty.setText(String.valueOf(item.getQuantity()));
               }else if(item.getKey().equals(ProductKey.CINE_POPCORN.getKey())){
                   ticketQty2.setText(String.valueOf(item.getQuantity()));
               }
           }
        }
    }

}
