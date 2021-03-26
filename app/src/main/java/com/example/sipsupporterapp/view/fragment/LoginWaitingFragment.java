package com.example.sipsupporterapp.view.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.example.sipsupporterapp.R;
import com.example.sipsupporterapp.databinding.FragmentLoginWaitingBinding;

public class LoginWaitingFragment extends DialogFragment {
    private FragmentLoginWaitingBinding binding;

    public static final String TAG = LoginWaitingFragment.class.getSimpleName();


    public static LoginWaitingFragment newInstance() {
        LoginWaitingFragment fragment = new LoginWaitingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                LayoutInflater.from(getContext()),
                R.layout.fragment_login_waiting,
                null,
                false);

        AlertDialog dialog = new AlertDialog.Builder(getContext()).setView(binding.getRoot()).create();

        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        return dialog;
    }
}