package com.example.sipsupporterapp.view.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.sipsupporterapp.R;
import com.example.sipsupporterapp.databinding.FragmentAddAndEditIPAddressDialogBinding;
import com.example.sipsupporterapp.model.ServerData;
import com.example.sipsupporterapp.utils.SipSupportSharedPreferences;
import com.example.sipsupporterapp.view.activity.LoginContainerActivity;
import com.example.sipsupporterapp.viewmodel.SharedLoginAndAddAndEditIPAddressDialogAndIPAddressListDialogViewModel;

import java.util.List;

public class AddAndEditIPAddressDialogFragment extends DialogFragment {
    private FragmentAddAndEditIPAddressDialogBinding binding;
    private SharedLoginAndAddAndEditIPAddressDialogAndIPAddressListDialogViewModel viewModel;
    private String centerName, ipAddress, port;
    private static final String ARGS_CENTER_NAME = "centerName";
    private static final String ARGS_IP_ADDRESS = "ipAddress";
    private static final String ARGS_PORT = "port";
    private boolean flag;
    public static final String TAG = AddAndEditIPAddressDialogFragment.class.getSimpleName();


    public static AddAndEditIPAddressDialogFragment newInstance(String centerName, String ipAddress, String port) {
        AddAndEditIPAddressDialogFragment fragment = new AddAndEditIPAddressDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARGS_CENTER_NAME, centerName);
        args.putString(ARGS_IP_ADDRESS, ipAddress);
        args.putString(ARGS_PORT, port);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        centerName = getArguments().getString(ARGS_CENTER_NAME);
        ipAddress = getArguments().getString(ARGS_IP_ADDRESS);
        port = getArguments().getString(ARGS_PORT);

        viewModel = new ViewModelProvider(requireActivity()).get(SharedLoginAndAddAndEditIPAddressDialogAndIPAddressListDialogViewModel.class);

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


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(
                getContext()),
                R.layout.fragment_add_and_edit_i_p_address_dialog,
                null,
                false);

        binding.edTextCenterName.setText(centerName);
        binding.edTextIpAddress.setText(ipAddress);
        binding.edTextPort.setText(port);

        binding.edTextCenterName.setSelection(binding.edTextCenterName.getText().length());
        binding.edTextIpAddress.setSelection(binding.edTextIpAddress.getText().length());
        binding.edTextPort.setSelection(binding.edTextPort.getText().length());

        setListener();

        AlertDialog dialog = new AlertDialog
                .Builder(getContext())
                .setView(binding.getRoot())
                .create();

        return dialog;
    }

    private void setListener() {
        binding.fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = false;
                String centerName = binding.edTextCenterName.getText().toString();
                String ipAddress = binding.edTextIpAddress.getText().toString();
                String port = binding.edTextPort.getText().toString();

                if (centerName.isEmpty() || ipAddress.isEmpty() || port.isEmpty()) {
                    ErrorDialogFragment fragment = ErrorDialogFragment.newInstance("لطفا موارد خواسته شده را پر کنید");
                    fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
                    /*Toast.makeText(getContext(), "لطفا موارد خواسته شده را پر کنید", Toast.LENGTH_SHORT).show();*/
                } else if (ipAddress.length() < 7 || !checkDot(ipAddress) || hasEnglishLetter(ipAddress) || hasEnglishLetter(port)) {
                    ErrorDialogFragment fragment = ErrorDialogFragment.newInstance("فرمت آدرس ip نادرست است");
                    fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
                    /* Toast.makeText(getContext(), "فرمت آدرس ip نادرست است", Toast.LENGTH_SHORT).show();*/
                } else {
                    List<ServerData> serverDataList = viewModel.getServerDataList();
                    if (serverDataList.size() > 0) {
                        for (ServerData serverData1 : serverDataList) {
                            if (serverData1.getCenterName().equals(centerName)) {
                                ErrorDialogFragment fragment = ErrorDialogFragment.newInstance("نام مرکز تکراری می باشد");
                                fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
                                /*  Toast.makeText(getContext(), "نام مرکز تکراری می باشد", Toast.LENGTH_SHORT).show();*/
                                flag = true;
                            }
                        }
                    }
                    if (!flag) {
                        String output = "";
                        output = replace(ipAddress, output);

                        String newPort = "";
                        newPort = replace(port, newPort);

                        ServerData serverData = new ServerData(centerName, output, newPort);

                        viewModel.insertServerData(serverData);

                        viewModel.getInsertSpinnerSingleLiveEvent().setValue(true);
                        viewModel.getInsertIPAddressListSingleLiveEvent().setValue(true);
                        dismiss();
                    }
                }
            }
        });
    }

    private String replace(String ipAddress, String output) {
        for (int i = 0; i < ipAddress.length(); i++) {
            if (ipAddress.charAt(i) == '۱') {
                output += '1';
            } else if (ipAddress.charAt(i) == '۲') {
                output += '2';

            } else if (ipAddress.charAt(i) == '۳') {
                output += '3';

            } else if (ipAddress.charAt(i) == '۴') {
                output += '4';

            } else if (ipAddress.charAt(i) == '۵') {
                output += '5';

            } else if (ipAddress.charAt(i) == '۶') {
                output += '6';

            } else if (ipAddress.charAt(i) == '۷') {
                output += '7';

            } else if (ipAddress.charAt(i) == '۸') {
                output += '8';

            } else if (ipAddress.charAt(i) == '۹') {
                output += '9';

            } else if (ipAddress.charAt(i) == '۰') {
                output += '0';

            } else {
                output += ipAddress.charAt(i);
            }
        }
        return output;
    }

    private boolean checkDot(String ipAddress) {
        int dotNumber = 0;
        char[] chars = ipAddress.toCharArray();
        for (Character character : chars) {
            if (String.valueOf(character).equals(".")) {
                dotNumber++;
            }
        }
        if (dotNumber == 3) {
            return true;
        } else {
            return false;
        }
    }

    private boolean hasEnglishLetter(String ipAddress) {
        String str = "";
        char[] chars = ipAddress.toCharArray();
        for (Character character : chars) {
            if (!String.valueOf(character).equals(".")) {
                str += String.valueOf(character);
            }
        }

        if (str.matches(".*[a-zA-Z]+.*")) {
            return true;
        } else {
            return false;
        }
    }
}