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

import java.util.ArrayList;

import cl.transbank.onepay.pos.R;
import cl.transbank.onepay.pos.databinding.ActivityMainBinding;
import cl.transbank.onepay.pos.fragments.PaymentDialogFragment;
import cl.transbank.onepay.pos.model.Item;
import cl.transbank.onepay.pos.utils.HTTPClient;

public class MainActivity extends AppCompatActivity implements PaymentDialogFragment.OnFragmentInteractionListener {

    boolean[] buttonPressed = {false, false, false, false};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseApp.initializeApp(this);

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        HTTPClient.sendRegistrationToServer(refreshedToken, this, null);

        final ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setCafePrice(3);
        binding.setMedialunaPrice(3);
        binding.setSandwichPrice(3);
        binding.setMuffinPrice(3);

        initializeUI(binding);

        binding.pagarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();

                ArrayList<Item> items = getSelectedItems(binding);

                PaymentDialogFragment paymentDialogFragment = PaymentDialogFragment.newInstance(items);

                paymentDialogFragment.show(fm, "fragment_edit_name");
            }
        });
    }



    public ArrayList<Item> getSelectedItems(ActivityMainBinding binding) {
        ArrayList<Item> items = new ArrayList<>();

        for (int i = 0; i < buttonPressed.length; i++) {
            if (buttonPressed[i] == true) {
                Item item = null;
                switch (i) {
                    case 0:
                        item = new Item("Cafe", 1, binding.getCafePrice(), null, 0);
                        break;
                    case 1:
                        item = new Item("Sandwich", 1, binding.getSandwichPrice(), null, 0);
                        break;
                    case 2:
                        item = new Item("Muffin", 1, binding.getMuffinPrice(), null, 0);
                        break;
                    case 3:
                        item = new Item("Medialuna", 1, binding.getMedialunaPrice(), null, 0);
                        break;
                }
                items.add(item);
            }
        }
        return items;
    }

    private void setPayButtonStatus(ActivityMainBinding binding) {
        for (int i = 0; i < buttonPressed.length; i++) {
            if (buttonPressed[i] == true) {
                binding.pagarButton.setEnabled(true);
                return;
            }

        }

        binding.pagarButton.setEnabled(false);
    }

    private void initializeUI(final ActivityMainBinding binding) {
        final ActivityMainBinding finalBinding = binding;

        final Drawable normalButtonDrawable = getResources().getDrawable(R.color.normalButton);
        final Drawable pressedButtonDrawable = getResources().getDrawable(R.drawable.pressed_button_drawable);

        binding.cafeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalBinding.cafeButton.setBackground(buttonPressed[0] ? normalButtonDrawable : pressedButtonDrawable);

                finalBinding.setAmount(finalBinding.getAmount() + (finalBinding.getCafePrice() * (buttonPressed[0] ? -1 : 1)));
                buttonPressed[0] = !buttonPressed[0];
                setPayButtonStatus(binding);
            }
        });

        binding.sandwichButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalBinding.sandwichButton.setBackground(buttonPressed[1] ? normalButtonDrawable : pressedButtonDrawable);

                finalBinding.setAmount(finalBinding.getAmount() + (finalBinding.getSandwichPrice() * (buttonPressed[1] ? -1 : 1)));
                buttonPressed[1] = !buttonPressed[1];
                setPayButtonStatus(binding);
            }
        });

        binding.muffinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalBinding.muffinButton.setBackground(buttonPressed[2] ? normalButtonDrawable : pressedButtonDrawable);

                finalBinding.setAmount(finalBinding.getAmount() + (finalBinding.getMuffinPrice() * (buttonPressed[2] ? -1 : 1)));
                buttonPressed[2] = !buttonPressed[2];
                setPayButtonStatus(binding);
            }
        });

        binding.medialunaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalBinding.medialunaButton.setBackground(buttonPressed[3] ? normalButtonDrawable : pressedButtonDrawable);

                finalBinding.setAmount(finalBinding.getAmount() + (finalBinding.getMedialunaPrice() * (buttonPressed[3] ? -1 : 1)));
                buttonPressed[3] = !buttonPressed[3];
                setPayButtonStatus(binding);
            }
        });
    }

    @Override
    public void onPaymentDone(Integer result) {

    }
}
