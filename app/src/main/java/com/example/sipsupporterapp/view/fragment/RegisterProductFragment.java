package com.example.sipsupporterapp.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.sipsupporterapp.R;
import com.example.sipsupporterapp.databinding.FragmentRegisterProductBinding;
import com.example.sipsupporterapp.model.CustomerProductResult;
import com.example.sipsupporterapp.model.CustomerProducts;
import com.example.sipsupporterapp.model.ProductInfo;
import com.example.sipsupporterapp.model.ProductResult;
import com.example.sipsupporterapp.model.ServerData;
import com.example.sipsupporterapp.utils.SipSupportSharedPreferences;
import com.example.sipsupporterapp.viewmodel.RegisterProductViewModel;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterProductFragment extends Fragment {
    public static final String ARGS_CUSTOMER_ID = "customerID";
    private FragmentRegisterProductBinding binding;
    private RegisterProductViewModel viewModel;
    private String value;
    private int customerID;
    private Map<String, Integer> map = new HashMap<>();

    public static RegisterProductFragment newInstance(int customerID) {
        RegisterProductFragment fragment = new RegisterProductFragment();
        Bundle args = new Bundle();
        args.putInt(ARGS_CUSTOMER_ID, customerID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        customerID = getArguments().getInt(ARGS_CUSTOMER_ID);
        viewModel = new ViewModelProvider(this).get(RegisterProductViewModel.class);

        ServerData serverData = viewModel.getServerData(SipSupportSharedPreferences.getLastValueSpinner(getContext()));
        viewModel.getSipSupportServiceGetProductResult(serverData.getIpAddress() + ":" + serverData.getPort());
        viewModel.fetchProductResult(SipSupportSharedPreferences.getUserLoginKey(getContext()));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register_product, container, false);

        binding.edTextCustomerName.setText(SipSupportSharedPreferences.getCustomerName(getContext()));

        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int productID = map.get(value);
                long invoicePrice = Long.valueOf(binding.edTextInvoicePrice.getText().toString());
                boolean paymentPrice;
                if (binding.checkBoxInvoicePrice.isChecked()) {
                    paymentPrice = true;
                } else {
                    paymentPrice = false;
                }

                boolean finish;
                if (binding.checkBoxFinish.isChecked()) {
                    finish = true;
                } else {
                    finish = false;
                }

                String description = binding.edTextDescription.getText().toString();

                CustomerProducts customerProducts = new CustomerProducts();
                customerProducts.setCustomerID(customerID);
                customerProducts.setProductID(productID);
                customerProducts.setInvoicePrice(invoicePrice);
                customerProducts.setInvoicePayment(paymentPrice);
                customerProducts.setFinish(finish);
                customerProducts.setDescription(description);

                ServerData serverData = viewModel.getServerData(SipSupportSharedPreferences.getLastValueSpinner(getContext()));
                viewModel.getSipSupportServicePostCustomerProducts(serverData.getIpAddress() + ":" + serverData.getPort());
                viewModel.postCustomerProducts(SipSupportSharedPreferences.getUserLoginKey(getContext()), customerProducts);
            }

        });

        binding.spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                value = (String) item;
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel.getGetProductResultSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<ProductResult>() {
            @Override
            public void onChanged(ProductResult productResult) {
                for (ProductInfo productInfo : productResult.getProducts()) {
                    map.put(productInfo.getProductName(), productInfo.getProductID());
                }
                setupSpinner(productResult.getProducts());
            }
        });

        viewModel.getPostCustomerProductsSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<CustomerProductResult>() {
            @Override
            public void onChanged(CustomerProductResult customerProductResult) {
                RegisterProductSuccessfulDialogFragment fragment = RegisterProductSuccessfulDialogFragment.newInstance();
                fragment.show(getActivity().getSupportFragmentManager(), RegisterProductSuccessfulDialogFragment.TAG);
            }
        });

        viewModel.getErrorPostCustomerProductsSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                ErrorDialogFragment fragment = ErrorDialogFragment.newInstance(s);
                fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
            }
        });
    }

    private void setupSpinner(ProductInfo[] productInfoArray) {
        List<String> productNameList = new ArrayList<>();
        for (ProductInfo productInfo : productInfoArray) {
            productNameList.add(productInfo.getProductName());
        }

        value = productNameList.get(0);
        binding.spinner.setItems(productNameList);
        /* value = "Sip(رادیولوژي و سونوگرافي)";*/
    }
}