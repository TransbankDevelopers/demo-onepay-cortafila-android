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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.text.ParseException;

import cl.transbank.onepay.pos.R;
import cl.transbank.onepay.pos.model.Cart;
import cl.transbank.onepay.pos.model.Item;
import cl.transbank.onepay.pos.model.ItemImpl;
import cl.transbank.onepay.pos.utils.CartHelper;
import cl.transbank.onepay.pos.utils.Constants;
import cl.transbank.onepay.pos.utils.CurrencyFormat;
import cl.transbank.onepay.pos.utils.ProductKey;

public class GymActivity extends AppCompatActivity {

    boolean[] buttonPressed = {false, false, false};
    ImageButton gymSmartfit;
    ImageButton gymEnergy;
    ImageButton gymSportlife;
    TextView price;
    TextView planStr;
    private Button goToCart;
    ProductKey key;
    TextView planQty;
    final Cart cart = CartHelper.getCart();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym);
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

        gymSmartfit = findViewById(R.id.smartfit_logo);
        gymEnergy = findViewById(R.id.energy_logo);
        gymSportlife = findViewById(R.id.sportlife_logo);
        goToCart = findViewById(R.id.open_cart_btn);
        planStr = findViewById(R.id.gym_plan);
        price = findViewById(R.id.gym_price);
        planQty = findViewById(R.id.gym_cantidad);

        checkGoToCartBtn();
        setQtyLabel();

        ImageView addBtn = (ImageView)findViewById(R.id.gym_add_qty);
        assert addBtn != null;
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView ticketQty = findViewById(R.id.gym_cantidad);
                int qty = Integer.parseInt(ticketQty.getText().toString());
                int priceGym = 0;
                try {
                    priceGym = CurrencyFormat.parse(price.getText().toString()).intValue();
                }catch(ParseException e){
                    Toast.makeText(GymActivity.this, Constants.ERROR_PARSING, Toast.LENGTH_LONG).show();
                }
                if(priceGym == 0){
                    Toast.makeText(GymActivity.this, Constants.GYM_PLAN_VALIDATION, Toast.LENGTH_LONG).show();
                }else {
                    int qtyAdded = Integer.parseInt(planQty.getText().toString());
                    qtyAdded++;

                    planQty.setText(String.valueOf(qtyAdded));
                    addProducts(key,priceGym);
                }

            }
        });

        ImageView minusBtn = findViewById(R.id.gym_minus_qty);
        assert minusBtn != null;
        minusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qtyMinus = Integer.parseInt(planQty.getText().toString());
                if(qtyMinus>1) {
                    qtyMinus--;
                    planQty.setText(String.valueOf(qtyMinus));
                    subtractProduct(key);
                }

            }
        });


        assert gymSmartfit != null;
        gymSmartfit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setImagesBackgroundOff();
                gymSmartfit.setImageResource(R.drawable.smartfif_logo);
                price.setText(CurrencyFormat.formatBigDecimalToCurrency(new BigDecimal(Constants.SMARTFIT_PRICE)));
                planStr.setText(Constants.SMARTFIT_PLAN);
                buttonPressed[0] = !buttonPressed[0];
                key = ProductKey.GYM_SMARTFIT;
                setQtyLabel();

            }
        });


        assert gymSportlife != null;
        gymSportlife.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setImagesBackgroundOff();
                gymSportlife.setImageResource(R.drawable.sportlife_logo);
                price.setText(CurrencyFormat.formatBigDecimalToCurrency(new BigDecimal(Constants.SPORTLIFE_PRICE)));
                planStr.setText(Constants.SPORTLIFE_PLAN);
                buttonPressed[1] = !buttonPressed[1];
                key = ProductKey.GYM_SPORTLIFE;
                setQtyLabel();

            }
        });


        assert gymEnergy != null;
        gymEnergy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setImagesBackgroundOff();
                gymEnergy.setImageResource(R.drawable.energy_logo);
                price.setText(CurrencyFormat.formatBigDecimalToCurrency(new BigDecimal(Constants.ENERGY_PRICE)));
                planStr.setText(Constants.ENERGY_PLAN);
                buttonPressed[2] = !buttonPressed[2];
                key = ProductKey.GYM_ENERGY;
                setQtyLabel();
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


    private void  setImagesBackgroundOff(){
        gymSmartfit.setImageResource(R.drawable.smartfif_logo_off);
        gymEnergy.setImageResource(R.drawable.energy_logo_off);
        gymSportlife.setImageResource(R.drawable.sportlife_logo_off);
        buttonPressed[0] = false;
        buttonPressed[1] = false;
        buttonPressed[2] = false;

    }


    private Drawable createDrawableFromView(int count, int backgroundImageId) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.shopping_layout, null);
        view.setBackgroundResource(backgroundImageId);

        if (count == 0) {
            View counterTextPanel = view.findViewById(R.id.counterValuePanel);
            counterTextPanel.setVisibility(View.GONE);
        } else {
            TextView textView = (TextView) view.findViewById(R.id.count);
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

    private void addProducts(ProductKey key, Integer priceGym) {
        Item item = new ItemImpl(Constants.GYM_DESCRIPTION+" "+key, 1, new BigDecimal(priceGym), Constants.GYM, 1,key.getKey());
        cart.add(item, key.getKey());
        Toast.makeText(GymActivity.this, Constants.PRODUCT_ADDED, Toast.LENGTH_LONG).show();
        goToCart.setEnabled(Boolean.TRUE);
        invalidateCart();
    }

    private void subtractProduct(ProductKey key){
        cart.removeItem(key.getKey());
        Toast.makeText(GymActivity.this, Constants.PRODUCT_SUBTRACT, Toast.LENGTH_LONG).show();
        checkGoToCartBtn();
        invalidateCart();
    }

    private void setQtyLabel(){
        planQty.setText(String.valueOf(0));
        if(cart.getTotalQuantity()>0 && key !=null){
            for(Item item: cart.getProducts()){
                if(item.getKey().equals(key.getKey())){
                    planQty.setText(String.valueOf(item.getQuantity()));
                }
            }
        }
    }

}
