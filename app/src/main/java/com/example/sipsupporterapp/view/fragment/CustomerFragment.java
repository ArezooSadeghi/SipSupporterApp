package com.example.sipsupporterapp.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.sipsupporterapp.R;
import com.example.sipsupporterapp.adapter.CustomerAdapter;
import com.example.sipsupporterapp.databinding.FragmentCustomerBinding;
import com.example.sipsupporterapp.model.CustomerInfo;
import com.example.sipsupporterapp.model.CustomerResult;
import com.example.sipsupporterapp.model.DateResult;
import com.example.sipsupporterapp.model.ServerData;
import com.example.sipsupporterapp.utils.SipSupportSharedPreferences;
import com.example.sipsupporterapp.view.activity.ItemClickedContainerActivity;
import com.example.sipsupporterapp.view.activity.LoginContainerActivity;
import com.example.sipsupporterapp.viewmodel.SharedCenterNameDialogAndCustomerViewModel;

import java.util.ArrayList;
import java.util.List;

public class CustomerFragment extends Fragment {
    private FragmentCustomerBinding binding;
    private SharedCenterNameDialogAndCustomerViewModel viewModel;
    private String date;


    public static CustomerFragment newInstance() {
        CustomerFragment fragment = new CustomerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        viewModel = new ViewModelProvider(requireActivity())
                .get(SharedCenterNameDialogAndCustomerViewModel.class);

        ServerData serverData = viewModel.getServerData(SipSupportSharedPreferences.getLastValueSpinner(getContext()));
        viewModel.getSipSupportServiceGetDateResult(serverData.getIpAddress() + ":" + serverData.getPort());
        viewModel.fetchDateResult(SipSupportSharedPreferences.getUserLoginKey(getContext()));

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_customer,
                container,
                false);

        if (SipSupportSharedPreferences.getLastSearchQuery(getContext()) == null) {
            String customerName = "";
            ServerData serverData = viewModel.getServerData(SipSupportSharedPreferences.getLastValueSpinner(getContext()));
            viewModel.getSupportServicePostCustomerParameter(serverData.getIpAddress() + ":" + serverData.getPort());
            viewModel.fetchCustomerResult(SipSupportSharedPreferences.getUserLoginKey(getContext()), customerName);

        } else {
            String customerName = SipSupportSharedPreferences.getLastSearchQuery(getContext());

            ServerData serverData = viewModel.getServerData(SipSupportSharedPreferences.getLastValueSpinner(getContext()));
            viewModel.getSupportServicePostCustomerParameter(serverData.getIpAddress() + ":" + serverData.getPort());
            viewModel.fetchCustomerResult(SipSupportSharedPreferences.getUserLoginKey(getContext()), customerName);
        }


        binding.txtUserName.setText(SipSupportSharedPreferences.getUserFullName(getContext()));

        initToolbar();

        binding.imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CenterNameDialogFragment fragment = CenterNameDialogFragment.newInstance();
                fragment.show(getChildFragmentManager(), CenterNameDialogFragment.TAG);
            }
        });

        initRecyclerView();

        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setObserver();
    }

    private void setObserver() {
        viewModel.getCustomerResultSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<CustomerResult>() {
            @Override
            public void onChanged(CustomerResult customerResult) {

                StringBuilder stringBuilder = new StringBuilder();
                String listSize = String.valueOf(customerResult.getCustomers().length);

                for (int i = 0; i < listSize.length(); i++) {
                    stringBuilder.append((char) ((int) listSize.charAt(i) - 48 + 1632));
                }


                binding.txtCount.setText("تعداد مراکز: " + stringBuilder.toString());
                binding.progressBar.setVisibility(View.GONE);
                binding.recyclerViewCustomerList.setVisibility(View.VISIBLE);
                List<CustomerInfo> customerInfoList = new ArrayList<>();
                for (CustomerInfo customerInfo : customerResult.getCustomers()) {
                    customerInfoList.add(customerInfo);
                }
                setupAdapter(customerInfoList);
            }
        });

        viewModel.getShowProgressBarSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isShowProgressBar) {
                binding.progressBar.setVisibility(View.VISIBLE);
            }
        });

        viewModel.getWrongUserLoginKeyLiveData().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String error) {
                ErrorDialogFragment fragment = ErrorDialogFragment.newInstance(error);
                fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
            }
        });

        viewModel.getNotValueUserLoginKeyLiveData().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String error) {
                ErrorDialogFragment fragment = ErrorDialogFragment.newInstance(error);
                fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
            }
        });

        viewModel.getItemClickedSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer customerID) {
                Intent intent = ItemClickedContainerActivity.newIntent(getContext(), customerID);
                startActivity(intent);
            }
        });


        viewModel.getTimeoutExceptionHappenSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean timeoutExceptionHappen) {
                binding.progressBar.setVisibility(View.GONE);
                ErrorDialogFragment fragment = ErrorDialogFragment.newInstance("اتصال به اینترنت با خطا مواجه شد");
                fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
            }
        });

        viewModel.getNoConnectivityExceptionSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean noConnectivity) {
                binding.progressBar.setVisibility(View.GONE);
                ErrorDialogFragment fragment = ErrorDialogFragment.newInstance("عدم اتصال به اینترنت");
                fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
            }
        });

        viewModel.getDangerousUserSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
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
        viewModel.getErrorSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String error) {
                ErrorDialogFragment fragment = ErrorDialogFragment.newInstance(error);
                fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
            }
        });

        viewModel.getDateResultSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<DateResult>() {
            @Override
            public void onChanged(DateResult dateResult) {
                date = dateResult.getDate();
                SipSupportSharedPreferences.setDate(getContext(), date);
            }
        });
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_customer, menu);
        MenuCompat.setGroupDividerEnabled(menu, true);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_exit:
                SipSupportSharedPreferences.setUserLoginKey(getContext(), null);
                SipSupportSharedPreferences.setLastSearchQuery(getContext(), null);
                Intent intent = LoginContainerActivity.newIntent(getContext());
                startActivity(intent);
                getActivity().finish();
                return true;
            case R.id.item_change_password:
                ChangePasswordDialogFragment changePasswordDialogFragment = ChangePasswordDialogFragment.newInstance();
                changePasswordDialogFragment.show(getActivity().getSupportFragmentManager(), ChangePasswordDialogFragment.TAG);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initToolbar() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(binding.toolBarCustomer);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(null);
    }

    private void initRecyclerView() {
        binding.recyclerViewCustomerList.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewCustomerList.addItemDecoration(new DividerItemDecoration(
                binding.recyclerViewCustomerList.getContext(),
                DividerItemDecoration.VERTICAL));
    }

    private void setupAdapter(List<CustomerInfo> customerInfoList) {
        if (date == null) {
            CustomerAdapter adapter = new CustomerAdapter(getContext(), customerInfoList, viewModel, SipSupportSharedPreferences.getDate(getContext()));
            binding.recyclerViewCustomerList.setAdapter(adapter);
        } else {
            CustomerAdapter adapter = new CustomerAdapter(getContext(), customerInfoList, viewModel, date);
            binding.recyclerViewCustomerList.setAdapter(adapter);
        }
    }
}