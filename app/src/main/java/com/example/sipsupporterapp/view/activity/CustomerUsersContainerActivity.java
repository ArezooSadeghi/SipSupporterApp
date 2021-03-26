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
import com.example.sipsupporterapp.view.fragment.CustomerUsersFragment;

public class CustomerUsersContainerActivity extends AppCompatActivity {
    private FragmentContainerActivityBinding binding;

    private static final String EXTRA_CUSTOMER_ID = "com.example.sipsupporterapp.customerID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.fragment_container_activity);

        int customerID = getIntent().getIntExtra(EXTRA_CUSTOMER_ID, -1);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragmentManager
                    .beginTransaction()
                    .add(R.id.fragment_container, CustomerUsersFragment.newInstance(customerID))
                    .commit();
        }
    }

    public static Intent newIntent(Context context, int customerID) {
        Intent intent = new Intent(context, CustomerUsersContainerActivity.class);
        intent.putExtra(EXTRA_CUSTOMER_ID, customerID);
        return intent;
    }
}