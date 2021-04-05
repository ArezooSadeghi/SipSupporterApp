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
import com.example.sipsupporterapp.view.fragment.ImageListFragment;

public class ImageListContainerActivity extends AppCompatActivity {
    private FragmentContainerActivityBinding binding;

    private int customerID, customerSupportID, customerProductID, customerPaymentID;

    private static final String EXTRA_CUSTOMER_ID = "customerID";
    private static final String EXTRA_CUSTOMER_SUPPORT_ID = "customerSupportID";
    private static final String EXTRA_CUSTOMER_PRODUCT_ID = "customerProductID";
    private static final String EXTRA_CUSTOMER_PAYMENT_ID = "customerPaymentID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.fragment_container_activity);

        customerID = getIntent().getIntExtra(EXTRA_CUSTOMER_ID, 0);
        customerSupportID = getIntent().getIntExtra(EXTRA_CUSTOMER_SUPPORT_ID, 0);
        customerProductID = getIntent().getIntExtra(EXTRA_CUSTOMER_PRODUCT_ID, 0);
        customerPaymentID = getIntent().getIntExtra(EXTRA_CUSTOMER_PAYMENT_ID, 0);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragmentManager
                    .beginTransaction()
                    .add(R.id.fragment_container, ImageListFragment.newInstance(customerID, customerSupportID, customerProductID, customerPaymentID))
                    .commit();
        }
    }

    public static Intent newIntent(Context context, int customerID, int customerSupportID, int customerProductID, int customerPaymentID) {
        Intent intent = new Intent(context, ImageListContainerActivity.class);
        intent.putExtra(EXTRA_CUSTOMER_ID, customerID);
        intent.putExtra(EXTRA_CUSTOMER_SUPPORT_ID, customerSupportID);
        intent.putExtra(EXTRA_CUSTOMER_PRODUCT_ID, customerProductID);
        intent.putExtra(EXTRA_CUSTOMER_PAYMENT_ID, customerPaymentID);
        return intent;
    }
}