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

public class MassageActivity extends AppCompatActivity {

    private Button goToCart;
    private TextView ticketQty;

    final Cart cart = CartHelper.getCart();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_massage);
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
        goToCart = findViewById(R.id.open_cart_btn);
        ticketQty = findViewById(R.id.massage_cantidad);

        checkGoToCartBtn();
        setQtyLabel();

        ImageView addBtn = findViewById(R.id.massage_add_qty);
        assert addBtn != null;
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qtyAdded = Integer.parseInt(ticketQty.getText().toString());
                qtyAdded++;
                ticketQty.setText(String.valueOf(qtyAdded));
                addProducts(ProductKey.MASSAGE);

            }
        });

        ImageView minusBtn = findViewById(R.id.massage_minus_qty);
        assert minusBtn != null;
        minusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qtyMinus = Integer.parseInt(ticketQty.getText().toString());
                if(qtyMinus>0) {
                    qtyMinus--;
                    ticketQty.setText(String.valueOf(qtyMinus));
                    subtractProduct(ProductKey.MASSAGE);
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
        Item item = new ItemImpl(Constants.MASSAGE_DESC, 1, new BigDecimal(Constants.MASSAGE_PRICE), Constants.MASSAGE, 1,key.getKey());
        cart.add(item, key.getKey());
        Toast.makeText(MassageActivity.this, Constants.PRODUCT_ADDED, Toast.LENGTH_LONG).show();

        goToCart.setEnabled(Boolean.TRUE);
        invalidateCart();
    }

    private void subtractProduct(ProductKey key){
        cart.removeItem(key.getKey());
        Toast.makeText(MassageActivity.this, Constants.PRODUCT_SUBTRACT, Toast.LENGTH_LONG).show();
        checkGoToCartBtn();
        invalidateCart();
    }

    private void setQtyLabel(){
        ticketQty.setText(String.valueOf(0));
        if(cart.getTotalQuantity()>0){
            for(Item item: cart.getProducts()){
                if(item.getKey().equals(ProductKey.MASSAGE.getKey())){
                    ticketQty.setText(String.valueOf(item.getQuantity()));
                }
            }
        }
    }
}
