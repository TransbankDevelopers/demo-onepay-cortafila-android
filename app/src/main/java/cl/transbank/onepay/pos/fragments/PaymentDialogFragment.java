package cl.transbank.onepay.pos.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.google.gson.JsonObject;
import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.ArrayList;
import java.util.List;

import cl.transbank.onepay.pos.R;
import cl.transbank.onepay.pos.model.Item;
import cl.transbank.onepay.pos.utils.HTTPClient;

public class PaymentDialogFragment extends DialogFragment {
    private static final String ARG_ITEMS = "items";

    private ArrayList<Item> mItems;

    private OnFragmentInteractionListener mListener;

    BroadcastReceiver br;

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
        setCancelable(false);
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
                showQR(result.getAsJsonPrimitive("ott").getAsString(), inflatedView);
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

    private void showQR(String ott, View inflatedView){
        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.encodeBitmap("onepay=ott:" + ott, BarcodeFormat.QR_CODE, 400, 400);
            ImageView imageViewQrCode = (ImageView) inflatedView.findViewById(R.id.qr_imageView);
            imageViewQrCode.setImageBitmap(bitmap);
        } catch(Exception e) {

        }

        int mShortAnimationDuration = getResources().getInteger(
                android.R.integer.config_longAnimTime);

        View mContentView = getView().findViewById(R.id.payment_constraintLayout);
        final View mLoadingView = getView().findViewById(R.id.loading_constraintLayout);

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
