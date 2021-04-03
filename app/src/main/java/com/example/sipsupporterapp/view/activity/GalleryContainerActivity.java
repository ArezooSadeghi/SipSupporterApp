package com.example.sipsupporterapp.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.sipsupporterapp.R;
import com.example.sipsupporterapp.databinding.FragmentContainerActivityBinding;
import com.example.sipsupporterapp.view.fragment.GalleryFragment;

public class GalleryContainerActivity extends AppCompatActivity {
    private FragmentContainerActivityBinding binding;

    private static final String EXTRA_CUSTOMER_PAYMENT_ID = "customerPaymentID";
    private static final String EXTRA_CUSTOMER_ID = "customerID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.fragment_container_activity);

        int customerID = getIntent().getIntExtra(EXTRA_CUSTOMER_ID, -1);
        int customerPaymentID = getIntent().getIntExtra(EXTRA_CUSTOMER_PAYMENT_ID, -1);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_container, GalleryFragment.newInstance(customerID, customerPaymentID))
                    .commit();
        }
    }

    public static Intent newIntent(Context context, int customerID, int customerPaymentID) {
        Intent intent = new Intent(context, GalleryContainerActivity.class);
        intent.putExtra(EXTRA_CUSTOMER_ID, customerID);
        intent.putExtra(EXTRA_CUSTOMER_PAYMENT_ID, customerPaymentID);
        return intent;
    }
}