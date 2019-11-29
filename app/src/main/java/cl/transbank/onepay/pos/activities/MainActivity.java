package cl.transbank.onepay.pos.activities;


import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import cl.transbank.onepay.pos.R;
import cl.transbank.onepay.pos.adapters.CheckRecyclerViewAdapter;
import cl.transbank.onepay.pos.fragments.PaymentDialogFragment;
import cl.transbank.onepay.pos.model.Cart;
import cl.transbank.onepay.pos.utils.CartHelper;
import cl.transbank.onepay.pos.utils.Constants;
import cl.transbank.onepay.pos.utils.CurrencyFormat;
import cl.transbank.onepay.pos.utils.HTTPClient;
import cl.transbank.onepay.pos.utils.SimpleDividerItemDecoration;

public class MainActivity extends AppCompatActivity implements PaymentDialogFragment.OnFragmentInteractionListener, CheckRecyclerViewAdapter.OnUpdateClickedListener {

    private RecyclerView checkRecyclerView;
    private CheckRecyclerViewAdapter mAdapter;
    private TextView subTotal;
    private Button pay;
    // get content of cart
    final Cart cart = CartHelper.getCart();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFirebase();
        initializeUI();
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void initializeUI() {

        subTotal = findViewById(R.id.amount_textView);
        pay = findViewById(R.id.pagar_button);

        checkRecyclerView = findViewById(R.id.checkout_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        checkRecyclerView.setLayoutManager(linearLayoutManager);
        checkRecyclerView.setHasFixedSize(true);
        checkRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(MainActivity.this));

        mAdapter = new CheckRecyclerViewAdapter(MainActivity.this, cart);
        mAdapter.setOnUpdateClickedListener(this);
        checkRecyclerView.setAdapter(mAdapter);

        subTotal.setText(String.valueOf(cart.getTotalPrice()));

        assert pay != null;
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (HTTPClient.isOnline(MainActivity.this)) {
                    FragmentManager fm = getSupportFragmentManager();

                    PaymentDialogFragment paymentDialogFragment = PaymentDialogFragment.newInstance(cart.getProducts());
                    paymentDialogFragment.show(fm, "fragment_edit_name");
                } else {
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("¡Error!")
                            .setMessage("No hay conexión a internet disponible, reintenta nuevamente.")
                            .setNegativeButton("Cerrar", null)
                            .show();
                }

            }
        });
        setPayButtonStatus();
    }

    private void initFirebase() {
        FirebaseApp.initializeApp(this);

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        HTTPClient.sendRegistrationToServer(refreshedToken, this, null);
    }



    @Override
    public void onPaymentDone(String result, String externalUniqueNumber) {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Resultado de compra")
                .setMessage(getAlertMessageFromResult(result, externalUniqueNumber))
                .setNegativeButton("Cerrar", null)
                .show();
    }

    private String getAlertMessageFromResult(String result, String externalUniqueNumber) {
        String paymentStatus;
        String mExternalUniqueNumberMessage = "Número de compra: ";

        String items = "Items comprados:\n";

        String alertMessage;

        if (result == null) {
            paymentStatus = "El pago no ha finalizado o no fue exitoso. Reintenta nuevamente";
            alertMessage = paymentStatus;
        } else if(result.equals("OK")) {
            paymentStatus = "¡El pago fue exitoso!";
            mExternalUniqueNumberMessage += externalUniqueNumber;
            items += cart.toString();

            alertMessage = paymentStatus + "\n" + mExternalUniqueNumberMessage + "\n\n"+ items;
            clearCartAndRedirectHome();

        } else {
            paymentStatus = "Hubo un error al intentar realizar el cobro. Reintenta nuevamente";
            alertMessage = paymentStatus;
        }

        return alertMessage;
    }


    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }

    @Override
    public void updateView(String msg) {
        subTotal.setText(CurrencyFormat.formatBigDecimalToCurrency(CartHelper.getCart().getTotalPrice()));
        setPayButtonStatus();
    }

    private void setPayButtonStatus() {
        if(CartHelper.getCart().getTotalPrice().intValue()>0){
            pay.setEnabled(true);
            return;
        }
        pay.setEnabled(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
                // app icon in action bar clicked; go home
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void clearCartAndRedirectHome(){
        cart.clear();
        Toast.makeText(MainActivity.this, Constants.MASSAGE_REDIRECT, Toast.LENGTH_LONG).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent i=new Intent(MainActivity.this,CategoryActivity.class);
                startActivity(i);
            }
        }, 10000);
    }

    @Override
    public void onResume(){
        super.onResume();
        mAdapter.updateCartItems();
        this.updateView("");

    }

}
