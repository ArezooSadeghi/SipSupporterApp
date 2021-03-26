package com.example.sipsupporterapp.view.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.example.sipsupporterapp.R;
import com.example.sipsupporterapp.databinding.FragmentRegisterProductSuccessfulDialogBinding;

public class RegisterProductSuccessfulDialogFragment extends DialogFragment {
    private FragmentRegisterProductSuccessfulDialogBinding binding;

    public static final String TAG = RegisterProductSuccessfulDialogFragment.class.getSimpleName();

    public static RegisterProductSuccessfulDialogFragment newInstance() {
        RegisterProductSuccessfulDialogFragment fragment = new RegisterProductSuccessfulDialogFragment();
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
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.fragment_register_product_successful_dialog, null, false);
        AlertDialog dialog = new AlertDialog.Builder(getContext()).setView(binding.getRoot()).create();

        binding.imgCloseWindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        return dialog;
    }
}