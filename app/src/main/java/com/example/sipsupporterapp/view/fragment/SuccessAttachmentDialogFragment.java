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
import androidx.lifecycle.ViewModelProvider;

import com.example.sipsupporterapp.R;
import com.example.sipsupporterapp.databinding.FragmentSuccessAttachmentDialogBinding;
import com.example.sipsupporterapp.viewmodel.AttachmentViewModel;

public class SuccessAttachmentDialogFragment extends DialogFragment {
    private FragmentSuccessAttachmentDialogBinding mBinding;
    private AttachmentViewModel mViewModel;

    public static final String TAG = SuccessAttachmentDialogFragment.class.getSimpleName();

    public static SuccessAttachmentDialogFragment newInstance() {
        SuccessAttachmentDialogFragment fragment = new SuccessAttachmentDialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = new ViewModelProvider(requireActivity()).get(AttachmentViewModel.class);

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(
                LayoutInflater.from(getContext()),
                R.layout.fragment_success_attachment_dialog, null, false);

        setListener();

        AlertDialog dialog =
                new AlertDialog.Builder(getContext()).setView(mBinding.getRoot()).create();

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }

    private void setListener() {
        mBinding.imgCloseWindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewModel.getIsAttachAgainSingleLiveEvent().setValue(true);
                dismiss();
            }
        });
    }
}