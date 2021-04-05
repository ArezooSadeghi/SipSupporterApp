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
import com.example.sipsupporterapp.view.fragment.GalleryProductsFragment;

public class GalleryProductsContainerActivity extends AppCompatActivity {
    private FragmentContainerActivityBinding binding;

    private static final String EXTRA_CUSTOMER_ID = "customerID";
    private static final String EXTRA_CUSTOMER_PRODUCT_ID = "customerProductID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.fragment_container_activity);

        int customerID = getIntent().getIntExtra(EXTRA_CUSTOMER_ID, -1);
        int customerProductID = getIntent().getIntExtra(EXTRA_CUSTOMER_PRODUCT_ID, -1);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragmentManager
                    .beginTransaction()
                    .add(R.id.fragment_container, GalleryProductsFragment.newInstance(customerID, customerProductID))
                    .commit();
        }
    }

    public static Intent newIntent(Context context, int customerID, int customerProductID) {
        Intent intent = new Intent(context, GalleryProductsContainerActivity.class);
        intent.putExtra(EXTRA_CUSTOMER_ID, customerID);
        intent.putExtra(EXTRA_CUSTOMER_PRODUCT_ID, customerProductID);
        return intent;
    }
}