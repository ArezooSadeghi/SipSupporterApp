package com.example.sipsupporterapp.view.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.sipsupporterapp.R;
import com.example.sipsupporterapp.adapter.IPAddressAdapter;
import com.example.sipsupporterapp.databinding.FragmentIPAddressListDialogBinding;
import com.example.sipsupporterapp.model.ServerData;
import com.example.sipsupporterapp.utils.SipSupportSharedPreferences;
import com.example.sipsupporterapp.view.activity.LoginContainerActivity;
import com.example.sipsupporterapp.viewmodel.SharedLoginAndAddAndEditIPAddressDialogAndIPAddressListDialogViewModel;

import java.util.List;

public class IPAddressListDialogFragment extends DialogFragment {
    private FragmentIPAddressListDialogBinding binding;
    private SharedLoginAndAddAndEditIPAddressDialogAndIPAddressListDialogViewModel viewModel;

    public static final String TAG = IPAddressListDialogFragment.class.getSimpleName();


    public static IPAddressListDialogFragment newInstance() {
        IPAddressListDialogFragment fragment = new IPAddressListDialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(SharedLoginAndAddAndEditIPAddressDialogAndIPAddressListDialogViewModel.class);
        setObserver();
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                LayoutInflater.from(getContext()),
                R.layout.fragment_i_p_address_list_dialog,
                null,
                false);


        AlertDialog dialog = new AlertDialog
                .Builder(getContext())
                .setView(binding.getRoot())
                .create();

        initRecyclerView();

        if (viewModel.getServerDataList().size() != 0) {
            binding.txtNoAddress.setVisibility(View.GONE);
            binding.recyclerViewIpAddress.setVisibility(View.VISIBLE);
            setupAdapter();
        }

        setListener();

        return dialog;
    }


    private void initRecyclerView() {
        binding.recyclerViewIpAddress.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewIpAddress.addItemDecoration(new DividerItemDecoration(
                binding.recyclerViewIpAddress.getContext(),
                DividerItemDecoration.VERTICAL));
    }


    private void setupAdapter() {
        List<ServerData> serverDataList = viewModel.getServerDataList();
        if (serverDataList.size() == 0) {
            binding.txtNoAddress.setVisibility(View.VISIBLE);
            binding.recyclerViewIpAddress.setVisibility(View.GONE);
        } else {
            IPAddressAdapter adapter = new IPAddressAdapter(getContext(), serverDataList, viewModel);
            binding.recyclerViewIpAddress.setAdapter(adapter);
        }
    }


    private void setListener() {
        binding.fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddAndEditIPAddressDialogFragment fragment = AddAndEditIPAddressDialogFragment.newInstance("", "", "");
                fragment.show(getParentFragmentManager(), AddAndEditIPAddressDialogFragment.TAG);
                dismiss();
            }
        });
    }


    private void setObserver() {
        viewModel.getInsertIPAddressListSingleLiveEvent().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isInsert) {
                setupAdapter();
            }
        });

        viewModel.getDeleteIPAddressListSingleLiveEvent().observe(this, new Observer<ServerData>() {
            @Override
            public void onChanged(ServerData serverData) {
                viewModel.deleteServerData(serverData);
                setupAdapter();
            }
        });

        viewModel.getUpdateIPAddressListSingleLiveEvent().observe(this, new Observer<ServerData>() {
            @Override
            public void onChanged(ServerData serverData) {
                String centerName = serverData.getCenterName();
                String ipAddress = serverData.getIpAddress();
                String port = serverData.getPort();

                AddAndEditIPAddressDialogFragment fragment = AddAndEditIPAddressDialogFragment.newInstance(centerName, ipAddress, port);
                fragment.show(getParentFragmentManager(), AddAndEditIPAddressDialogFragment.TAG);

                viewModel.deleteServerData(serverData);
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