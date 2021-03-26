package com.example.sipsupporterapp.view.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.sipsupporterapp.R;
import com.example.sipsupporterapp.databinding.FragmentChangePasswordDialogBinding;
import com.example.sipsupporterapp.model.ServerData;
import com.example.sipsupporterapp.model.UserResult;
import com.example.sipsupporterapp.utils.SipSupportSharedPreferences;
import com.example.sipsupporterapp.view.activity.LoginContainerActivity;
import com.example.sipsupporterapp.viewmodel.ChangePasswordDialogViewModel;

import java.util.List;

public class ChangePasswordDialogFragment extends DialogFragment {
    private FragmentChangePasswordDialogBinding binding;
    private ChangePasswordDialogViewModel viewModel;

    public static final String TAG = ChangePasswordDialogFragment.class.getSimpleName();


    public static ChangePasswordDialogFragment newInstance() {
        ChangePasswordDialogFragment fragment = new ChangePasswordDialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(ChangePasswordDialogViewModel.class);
        setObserver();
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                LayoutInflater.from(getContext()),
                R.layout.fragment_change_password_dialog,
                null,
                false);

        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        binding.btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.edTextNewPassword.getText().toString().length() > 12 || binding.edTextRepeatNewPassword.getText().toString().length() > 12) {
                    ErrorDialogFragment fragment = ErrorDialogFragment.newInstance("حداکثر 12 کاراکتر مجاز می باشد");
                    fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
                    /*Toast toast = Toast.makeText(getContext(), "حداکثر 12 کاراکتر مجاز می باشد", Toast.LENGTH_SHORT);

                    View view = toast.getView();

                    view.getBackground().setColorFilter(Color.parseColor("#FF0000"), PorterDuff.Mode.SRC_IN);

                    TextView text = view.findViewById(android.R.id.message);
                    text.setTextColor(Color.parseColor("#FFFFFF"));

                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();*/
                } else {
                    if (!binding.edTextNewPassword.getText().toString().equals(binding.edTextRepeatNewPassword.getText().toString())) {
                        ErrorDialogFragment fragment = ErrorDialogFragment.newInstance("عدم تطابق رمز ها");
                        fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
                        /*Toast toast = Toast.makeText(getContext(), "عدم تطابق رمز ها", Toast.LENGTH_SHORT);

                        View view = toast.getView();

                        view.getBackground().setColorFilter(Color.parseColor("#FF0000"), PorterDuff.Mode.SRC_IN);

                        TextView text = view.findViewById(android.R.id.message);
                        text.setTextColor(Color.parseColor("#FFFFFF"));


                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();*/
                    } else {
                        ServerData serverData = viewModel.getServerData(SipSupportSharedPreferences.getLastValueSpinner(getContext()));
                        viewModel.getSipSupportServiceChangePassword(serverData.getIpAddress() + ":" + serverData.getPort());
                        viewModel.changePassword(SipSupportSharedPreferences.getUserLoginKey(getContext()), binding.edTextNewPassword.getText().toString());
                    }
                }
            }
        });

        AlertDialog dialog = new AlertDialog.Builder(getContext()).setView(binding.getRoot()).create();

        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        return dialog;
    }


    private void setObserver() {
        viewModel.getChangedPassword().observe(this, new Observer<UserResult>() {
            @Override
            public void onChanged(UserResult userResult) {
                SipSupportSharedPreferences.setUserLoginKey(getContext(), userResult.getUsers()[0].getUserLoginKey());
                Toast toast = Toast.makeText(getContext(), "رمز عبور با موفقیت تغییر کرد", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                dismiss();
            }
        });

        viewModel.getErrorChangedPassword().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String errorChangedPassword) {
                ErrorDialogFragment fragment = ErrorDialogFragment.newInstance(errorChangedPassword);
                fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
               /* Toast toast = Toast.makeText(getContext(), errorChangedPassword, Toast.LENGTH_SHORT);

                View view = toast.getView();

                view.getBackground().setColorFilter(Color.parseColor("#FF0000"), PorterDuff.Mode.SRC_IN);

                TextView text = view.findViewById(android.R.id.message);
                text.setTextColor(Color.parseColor("#FFFFFF"));

                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();*/
            }
        });

        viewModel.getDangerousUserSingleLiveEvent().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                SipSupportSharedPreferences.setUserLoginKey(getContext(), null);
                SipSupportSharedPreferences.setUserFullName(getContext(), null);
                SipSupportSharedPreferences.setLastValueSpinner(getContext(), null);
                SipSupportSharedPreferences.setLastSearchQuery(getContext(), null);
                SipSupportSharedPreferences.setCustomerUserId(getContext(), -1);
                SipSupportSharedPreferences.setCustomerName(getContext(), null);
                Intent intent = LoginContainerActivity.newIntent(getContext());
                startActivity(intent);
                getActivity().finish();
            }
        });

        viewModel.getTimeoutExceptionHappenSingleLiveEvent().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                ErrorDialogFragment fragment = ErrorDialogFragment.newInstance("اتصال به اینترنت با خطا مواجه شد");
                fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
            }
        });
    }
}