package cl.transbank.onepay.pos.activities;

import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;

import cl.transbank.onepay.pos.R;
import cl.transbank.onepay.pos.databinding.ActivityMainBinding;
import cl.transbank.onepay.pos.fragments.PaymentDialogFragment;
import cl.transbank.onepay.pos.utils.HTTPClient;

public class MainActivity extends AppCompatActivity implements PaymentDialogFragment.OnFragmentInteractionListener {

    boolean[] buttonPressed = {false, false, false, false};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseApp.initializeApp(this);

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        HTTPClient.sendRegistrationToServer(refreshedToken, this, null);

        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setCafePrice(1500);
        binding.setMedialunaPrice(500);
        binding.setSandwichPrice(3000);
        binding.setMuffinPrice(1000);

        initializeUI(binding);

        binding.pagarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                PaymentDialogFragment paymentDialogFragment = PaymentDialogFragment.newInstance(null);
                paymentDialogFragment.show(fm, "fragment_edit_name");
            }
        });
    }

    private void initializeUI(ActivityMainBinding binding) {
        final ActivityMainBinding finalBinding = binding;

        final Drawable normalButtonDrawable = getResources().getDrawable(R.color.normalButton);
        final Drawable pressedButtonDrawable = getResources().getDrawable(R.drawable.pressed_button_drawable);

        binding.cafeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalBinding.cafeButton.setBackground(buttonPressed[0] ? normalButtonDrawable : pressedButtonDrawable);

                finalBinding.setAmount(finalBinding.getAmount() + (finalBinding.getCafePrice() * (buttonPressed[0] ? -1 : 1)));
                buttonPressed[0] = !buttonPressed[0];
            }
        });

        binding.sandwichButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalBinding.sandwichButton.setBackground(buttonPressed[1] ? normalButtonDrawable : pressedButtonDrawable);

                finalBinding.setAmount(finalBinding.getAmount() + (finalBinding.getSandwichPrice() * (buttonPressed[1] ? -1 : 1)));
                buttonPressed[1] = !buttonPressed[1];
            }
        });

        binding.muffinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalBinding.muffinButton.setBackground(buttonPressed[2] ? normalButtonDrawable : pressedButtonDrawable);

                finalBinding.setAmount(finalBinding.getAmount() + (finalBinding.getMuffinPrice() * (buttonPressed[2] ? -1 : 1)));
                buttonPressed[2] = !buttonPressed[2];
            }
        });

        binding.medialunaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalBinding.medialunaButton.setBackground(buttonPressed[3] ? normalButtonDrawable : pressedButtonDrawable);

                finalBinding.setAmount(finalBinding.getAmount() + (finalBinding.getMedialunaPrice() * (buttonPressed[3] ? -1 : 1)));
                buttonPressed[3] = !buttonPressed[3];
            }
        });
    }

    @Override
    public void onPaymentDone(Integer result) {

    }
}
