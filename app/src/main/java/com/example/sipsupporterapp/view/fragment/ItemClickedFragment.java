package com.example.sipsupporterapp.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.sipsupporterapp.R;
import com.example.sipsupporterapp.databinding.FragmentItemClickedBinding;
import com.example.sipsupporterapp.utils.SipSupportSharedPreferences;
import com.example.sipsupporterapp.view.activity.CustomerUsersContainerActivity;
import com.example.sipsupporterapp.view.activity.ProductsContainerActivity;
import com.example.sipsupporterapp.view.activity.SupportHistoryContainerActivity;
import com.example.sipsupporterapp.viewmodel.CustomerUsersViewModel;

public class ItemClickedFragment extends Fragment {

    private FragmentItemClickedBinding binding;
    private int customerID;
    private CustomerUsersViewModel viewModel;
    private static final String ARGS_CUSTOMER_ID = "customerID";

    public static ItemClickedFragment newInstance(int customerID) {
        ItemClickedFragment fragment = new ItemClickedFragment();
        Bundle args = new Bundle();
        args.putInt(ARGS_CUSTOMER_ID, customerID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        customerID = getArguments().getInt(ARGS_CUSTOMER_ID);

        viewModel = new ViewModelProvider(requireActivity()).get(CustomerUsersViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_item_clicked,
                container,
                false);

        initToolbar();

        binding.txtUserName.setText(SipSupportSharedPreferences.getUserFullName(getContext()));
        binding.txtCustomerName.setText(SipSupportSharedPreferences.getCustomerName(getContext()));

        setListener();

        return binding.getRoot();
    }

    private void setListener() {
        binding.btnHistorySupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = SupportHistoryContainerActivity.newIntent(getContext(), customerID);
                startActivity(intent);
            }
        });

        binding.btnRegisterSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = CustomerUsersContainerActivity.newIntent(getContext(), customerID);
                startActivity(intent);
            }
        });

        binding.btnProductList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ProductsContainerActivity.newIntent(getContext(), customerID);
                startActivity(intent);
            }
        });

        binding.btnSeeCallInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowInformationCallDialogFragment fragment = ShowInformationCallDialogFragment.newInstance();
                fragment.show(getParentFragmentManager(), ShowInformationCallDialogFragment.TAG);
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel.getSuccessfulRegisterItemClickedSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isSuccessfulRegister) {
                getActivity().finish();
            }
        });
    }

    private void initToolbar() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(binding.toolBar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(null);
    }
}