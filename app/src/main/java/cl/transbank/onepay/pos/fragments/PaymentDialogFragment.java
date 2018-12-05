package cl.transbank.onepay.pos.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.google.gson.JsonObject;
import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import cl.transbank.onepay.pos.R;
import cl.transbank.onepay.pos.model.Item;
import cl.transbank.onepay.pos.utils.HTTPClient;
import cl.transbank.onepay.pos.utils.StringFormatter;

public class PaymentDialogFragment extends DialogFragment {
    private static final String ARG_ITEMS = "items";

    private ArrayList<Item> mItems;

    private OnFragmentInteractionListener mListener;

    BroadcastReceiver br;

    private String mOcc;
    private String mExternalUniqueNumber;
    private CountDownTimer mCountDownTimer;
    private Animator smoothAnimation;

    public PaymentDialogFragment() {
        // Required empty public constructor
    }

    public static PaymentDialogFragment newInstance(ArrayList<Item> items) {
        PaymentDialogFragment fragment = new PaymentDialogFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_ITEMS, items);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mItems = getArguments().getParcelableArrayList(ARG_ITEMS);
        }

        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Material_Light_Dialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        final View inflatedView = inflater.inflate(R.layout.fragment_payment_dialog, container, false);
        Button cancelButton = inflatedView.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        HTTPClient.createTransaction(mItems, getContext(), new HTTPClient.HTTPClientListener() {
            @Override
            public void onCompleted(JsonObject result) {
                mOcc = result.getAsJsonPrimitive("occ").getAsString();
                mExternalUniqueNumber = result.getAsJsonPrimitive("externalUniqueNumber").getAsString();
                showQR(result.getAsJsonPrimitive("ott").getAsString(), inflatedView);

                final ProgressBar waitingProgressBar = inflatedView.findViewById(R.id.waiting_progress_bar);

                final int totalTime = 90000;
                waitingProgressBar.setMax(totalTime);
                waitingProgressBar.setProgress(totalTime);

                smoothAnimation = ObjectAnimator.ofInt(waitingProgressBar, "progress", totalTime, 0);

                smoothAnimation.setDuration(totalTime);
                smoothAnimation.setInterpolator(new LinearInterpolator());

                mCountDownTimer = new CountDownTimer(totalTime, 300) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        waitingProgressBar.setProgress(0);
                        dismiss();
                        new AlertDialog.Builder(getActivity())
                                .setTitle("¡Ups!")
                                .setMessage("Ha transcurrido el tiempo límite para hacer el pago, inténtalo nuevamente.")
                                .setNegativeButton("Okey", null)
                                .show();

                        smoothAnimation.end();

                    }
                }.start();
                smoothAnimation.start();

            }
        });

        return inflatedView;
    }

    public void onPaymentDone(Integer result) {
        if (mListener != null) {
            mListener.onPaymentDone(result);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }

    public interface OnFragmentInteractionListener {
        void onPaymentDone(Integer result);
    }

    @Override
    public void onResume() {
        br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("POS", "transaction_result");
                HashMap data = (HashMap) intent.getSerializableExtra("data");
                String paymentDescription = (String) data.get("description");

                if (paymentDescription != null && paymentDescription.equals("OK")){
                    showPaymentInfo();
                } else {
                    showError();
                }

                Log.d("POS", data.toString());
            }
        };
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(br, new IntentFilter(("transaction_result")));
        super.onResume();

    }

    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(br);
        super.onPause();
    }


    private void showError() {
        if (smoothAnimation != null)
            smoothAnimation.cancel();
        if (mCountDownTimer != null)
            mCountDownTimer.cancel();

        View currentView = getView();
        View mPaidView = currentView.findViewById(R.id.error_constraintLayout);
        final View mPaymentView = currentView.findViewById(R.id.payment_constraintLayout);

        mPaidView.setAlpha(0f);
        mPaidView.setVisibility(View.VISIBLE);

        int mShortAnimationDuration = getResources().getInteger(
                android.R.integer.config_longAnimTime);

        mPaidView.animate()
                .alpha(1f)
                .setDuration(mShortAnimationDuration)
                .setListener(null);

        mPaymentView.animate()
                .alpha(0f)
                .setDuration(mShortAnimationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mPaymentView.setVisibility(View.INVISIBLE);
                    }
                });
    }

    private void showPaymentInfo() {
        if (smoothAnimation != null)
            smoothAnimation.cancel();
        if (mCountDownTimer != null)
            mCountDownTimer.cancel();

        View currentView = getView();
        View mPaidView = currentView.findViewById(R.id.paid_constraintLayout);
        final View mPaymentView = currentView.findViewById(R.id.payment_constraintLayout);

        mPaidView.setAlpha(0f);
        mPaidView.setVisibility(View.VISIBLE);

        int mShortAnimationDuration = getResources().getInteger(
                android.R.integer.config_longAnimTime);

        mPaidView.animate()
                .alpha(1f)
                .setDuration(mShortAnimationDuration)
                .setListener(null);

        mPaymentView.animate()
                .alpha(0f)
                .setDuration(mShortAnimationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mPaymentView.setVisibility(View.INVISIBLE);
                    }
                });
    }

    private void showQR(String ott, View inflatedView){
        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.encodeBitmap("onepay=ott:" + ott, BarcodeFormat.QR_CODE, 400, 400);
            ImageView imageViewQrCode = inflatedView.findViewById(R.id.qr_imageView);
            imageViewQrCode.setImageBitmap(bitmap);
        } catch(Exception e) {

        }

        int mShortAnimationDuration = getResources().getInteger(
                android.R.integer.config_longAnimTime);

        View currentView = getView();

        TextView ottTextView = currentView.findViewById(R.id.buycode_textView);

        ottTextView.setText(StringFormatter.formatOtt(ott));
        View mContentView = currentView.findViewById(R.id.payment_constraintLayout);
        final View mLoadingView = currentView.findViewById(R.id.loading_constraintLayout);

        mContentView.setAlpha(0f);
        mContentView.setVisibility(View.VISIBLE);

        mContentView.animate()
                .alpha(1f)
                .setDuration(mShortAnimationDuration)
                .setListener(null);

        mLoadingView.animate()
                .alpha(0f)
                .setDuration(mShortAnimationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mLoadingView.setVisibility(View.GONE);
                    }
                });
    }
}
