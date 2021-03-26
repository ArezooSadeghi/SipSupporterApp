package com.example.sipsupporterapp.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.sipsupporterapp.R;
import com.example.sipsupporterapp.databinding.FragmentContainerActivityBinding;
import com.example.sipsupporterapp.view.fragment.CustomerFragment;

public class CustomerContainerActivity extends AppCompatActivity {
    private FragmentContainerActivityBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.fragment_container_activity);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragmentManager
                    .beginTransaction()
                    .add(R.id.fragment_container, CustomerFragment.newInstance())
                    .commit();
        }
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, CustomerContainerActivity.class);
    }
}