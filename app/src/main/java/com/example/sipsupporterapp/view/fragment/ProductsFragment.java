package com.example.sipsupporterapp.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.sipsupporterapp.R;
import com.example.sipsupporterapp.adapter.ProductsAdapter;
import com.example.sipsupporterapp.databinding.FragmentProductsBinding;
import com.example.sipsupporterapp.model.CustomerProductResult;
import com.example.sipsupporterapp.model.CustomerProducts;
import com.example.sipsupporterapp.model.ServerData;
import com.example.sipsupporterapp.utils.SipSupportSharedPreferences;
import com.example.sipsupporterapp.viewmodel.ProductsViewModel;

import java.util.ArrayList;
import java.util.List;

public class ProductsFragment extends Fragment {
    private FragmentProductsBinding binding;
    private ProductsViewModel viewModel;

    private static final String ARGS_CUSTOMER_ID = "customerID";

    public static ProductsFragment newInstance(int customerID) {
        ProductsFragment fragment = new ProductsFragment();
        Bundle args = new Bundle();
        args.putInt(ARGS_CUSTOMER_ID, customerID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int customerID = getArguments().getInt(ARGS_CUSTOMER_ID);

        viewModel = new ViewModelProvider(this).get(ProductsViewModel.class);

        ServerData serverData = viewModel.getServerData(SipSupportSharedPreferences.getLastValueSpinner(getContext()));
        viewModel.getSipSupportServiceGetCustomerProductResult(serverData.getIpAddress() + ":" + serverData.getPort());
        viewModel.fetchProductResult(SipSupportSharedPreferences.getUserLoginKey(getContext()), customerID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_products,
                container,
                false);

        initToolbar();
        initRecyclerView();

        binding.txtCustomerName.setText(SipSupportSharedPreferences.getCustomerName(getContext()));

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setObserver();
    }

    private void initToolbar() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(binding.toolBarProducts);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(null);
    }

    private void initRecyclerView() {
        binding.recyclerViewProducts.setLayoutManager(new LinearLayoutManager(getContext()));

        binding.recyclerViewProducts.addItemDecoration(new DividerItemDecoration(
                binding.recyclerViewProducts.getContext(),
                DividerItemDecoration.VERTICAL));
    }

    private void setObserver() {
        viewModel.getCustomerProductResultSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<CustomerProductResult>() {
            @Override
            public void onChanged(CustomerProductResult productResult) {
                binding.progressBar.setVisibility(View.GONE);
                binding.recyclerViewProducts.setVisibility(View.VISIBLE);

                StringBuilder stringBuilder = new StringBuilder();
                String listSize = String.valueOf(productResult.getCustomerProducts().length);

                for (int i = 0; i < listSize.length(); i++) {
                    stringBuilder.append((char) ((int) listSize.charAt(i) - 48 + 1632));
                }


                binding.txtCountProducts.setText("تعداد محصولات: " + stringBuilder.toString());
                setupAdapter(productResult.getCustomerProducts());
            }
        });

        viewModel.getErrorCustomerProductResultSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String error) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getTimeoutExceptionHappenSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                ErrorDialogFragment fragment = ErrorDialogFragment.newInstance("اتصال به اینترنت با خطا مواجه شد");
                fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
            }
        });
    }

    private void setupAdapter(CustomerProducts[] customerProducts) {
        List<CustomerProducts> customerProductsList = new ArrayList<>();
        for (CustomerProducts products1 : customerProducts) {
            customerProductsList.add(products1);
        }

        ProductsAdapter adapter = new ProductsAdapter(getContext(), customerProductsList);
        binding.recyclerViewProducts.setAdapter(adapter);
    }
}