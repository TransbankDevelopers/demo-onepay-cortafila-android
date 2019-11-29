package cl.transbank.onepay.pos.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import cl.transbank.onepay.pos.R;
import cl.transbank.onepay.pos.model.Cart;
import cl.transbank.onepay.pos.utils.CartHelper;

public class CategoryActivity extends AppCompatActivity {
    final Cart cart = CartHelper.getCart();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
    }

    public void openMovieView(View view) {
        Intent intent = new Intent(this, MovieActivity.class);
        startActivity(intent);

    }

    public void openGymView(View view) {
        Intent intent = new Intent(this, GymActivity.class);
        startActivity(intent);

    }

    public void openOtherView(View view) {
        Intent intent = new Intent(this, OtherActivity.class);
        startActivity(intent);

    }

    public void openMassageView(View view) {
        Intent intent = new Intent(this, MassageActivity.class);
        startActivity(intent);

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
    public void onResume(){
        super.onResume();
        invalidateCart();
    }

}
