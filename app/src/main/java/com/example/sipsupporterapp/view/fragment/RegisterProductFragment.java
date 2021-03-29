package com.example.sipsupporterapp.view.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
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

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterProductFragment extends DialogFragment {
    private FragmentRegisterProductBinding binding;
    private RegisterProductViewModel viewModel;
    private String value;
    private int customerID, customerProductID;
    private boolean finish, invoicePayment, isAdd;
    private long invoicePrice;
    private String description, productName;
    private Map<String, Integer> map = new HashMap<>();

    private static final String ARGS_FINISH = "finish";
    private static final String ARGS_IS_ADD = "isAdd";
    private static final String ARGS_CUSTOMER_ID = "customerID";
    private static final String ARGS_PRODUCT_NAME = "productName";
    private static final String ARGS_DESCRIPTION = "description";
    private static final String ARGS_INVOICE_PRICE = "invoicePrice";
    private static final String ARGS_INVOICE_PAYMENT = "invoicePayment";
    private static final String ARGS_CUSTOMER_PRODUCT_ID = "customerProductID";

    public static final String TAG = RegisterProductFragment.class.getSimpleName();

    public static RegisterProductFragment newInstance(int customerID, String productName, String description, long invoicePrice, boolean invoicePayment, boolean finish, boolean isAdd, int customerProductID) {
        RegisterProductFragment fragment = new RegisterProductFragment();
        Bundle args = new Bundle();
        args.putInt(ARGS_CUSTOMER_ID, customerID);
        args.putString(ARGS_PRODUCT_NAME, productName);
        args.putString(ARGS_DESCRIPTION, description);
        args.putLong(ARGS_INVOICE_PRICE, invoicePrice);
        args.putBoolean(ARGS_INVOICE_PAYMENT, invoicePayment);
        args.putBoolean(ARGS_FINISH, finish);
        args.putBoolean(ARGS_IS_ADD, isAdd);
        args.putInt(ARGS_CUSTOMER_PRODUCT_ID, customerProductID);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        customerID = getArguments().getInt(ARGS_CUSTOMER_ID);
        invoicePrice = getArguments().getLong(ARGS_INVOICE_PRICE);
        finish = getArguments().getBoolean(ARGS_FINISH);
        invoicePayment = getArguments().getBoolean(ARGS_INVOICE_PAYMENT);
        productName = getArguments().getString(ARGS_PRODUCT_NAME);
        description = getArguments().getString(ARGS_DESCRIPTION);
        isAdd = getArguments().getBoolean(ARGS_IS_ADD);
        customerProductID = getArguments().getInt(ARGS_CUSTOMER_PRODUCT_ID);

        viewModel = new ViewModelProvider(requireActivity()).get(RegisterProductViewModel.class);

        ServerData serverData = viewModel.getServerData(SipSupportSharedPreferences.getLastValueSpinner(getContext()));
        viewModel.getSipSupportServiceGetProductResult(serverData.getIpAddress() + ":" + serverData.getPort());
        viewModel.fetchProductResult(SipSupportSharedPreferences.getUserLoginKey(getContext()));

        setObserver();
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(
                getContext()),
                R.layout.fragment_register_product,
                null,
                false);

        binding.txtCustomerName.setText(SipSupportSharedPreferences.getCustomerName(getContext()));


        binding.edTextDescription.setText(description);
        binding.edTextDescription.setSelection(binding.edTextDescription.getText().toString().length());


        if (finish) {
            binding.checkBoxFinish.setChecked(true);
        } else {
            binding.checkBoxFinish.setChecked(false);
        }

        if (invoicePayment) {
            binding.checkBoxInvoicePrice.setChecked(true);
        } else {
            binding.checkBoxInvoicePrice.setChecked(false);
        }

        setListener();
        setItemSelectedListener();

        AlertDialog dialog = new AlertDialog.Builder(getContext()).setView(binding.getRoot()).create();

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }


    private void setListener() {
        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int productID = map.get(value);

                String text = binding.edTextInvoicePrice.getText().toString();
                String newText = text.replace(",", "");

                long invoicePrice = Long.valueOf(newText);
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

                if (isAdd) {
                    ServerData serverData = viewModel.getServerData(SipSupportSharedPreferences.getLastValueSpinner(getContext()));
                    viewModel.getSipSupportServicePostCustomerProducts(serverData.getIpAddress() + ":" + serverData.getPort());
                    viewModel.postCustomerProducts(SipSupportSharedPreferences.getUserLoginKey(getContext()), customerProducts);
                } else {
                    ServerData serverData = viewModel.getServerData(SipSupportSharedPreferences.getLastValueSpinner(getContext()));
                    viewModel.getSipSupportServiceForEditCustomerProduct(serverData.getIpAddress() + ":" + serverData.getPort());
                    customerProducts.setCustomerProductID(customerProductID);
                    viewModel.editCustomerProduct(SipSupportSharedPreferences.getUserLoginKey(getContext()), customerProducts);
                }
            }
        });

        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }


    private void setItemSelectedListener() {
        binding.spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                value = (String) item;

                int productID = map.get(value);

                ServerData serverData = viewModel.getServerData(SipSupportSharedPreferences.getLastValueSpinner(getContext()));
                viewModel.getSipSupportServiceForGetCustomerProductInfo(serverData.getIpAddress() + ":" + serverData.getPort());
                viewModel.fetchProductInfo(SipSupportSharedPreferences.getUserLoginKey(getContext()), productID);

            }
        });
    }


    private void setObserver() {
        viewModel.getGetProductResultSingleLiveEvent().observe(this, new Observer<ProductResult>() {
            @Override
            public void onChanged(ProductResult productResult) {
                for (ProductInfo productInfo : productResult.getProducts()) {
                    map.put(productInfo.getProductName(), productInfo.getProductID());
                }
                setupSpinner(productResult.getProducts());

                int productID = map.get(value);

                ServerData serverData = viewModel.getServerData(SipSupportSharedPreferences.getLastValueSpinner(getContext()));
                viewModel.getSipSupportServiceForGetCustomerProductInfo(serverData.getIpAddress() + ":" + serverData.getPort());
                viewModel.fetchProductInfo(SipSupportSharedPreferences.getUserLoginKey(getContext()), productID);

            }
        });

        viewModel.getPostCustomerProductsSingleLiveEvent().observe(this, new Observer<CustomerProductResult>() {
            @Override
            public void onChanged(CustomerProductResult customerProductResult) {
                RegisterProductSuccessfulDialogFragment fragment = RegisterProductSuccessfulDialogFragment.newInstance();
                fragment.show(getActivity().getSupportFragmentManager(), RegisterProductSuccessfulDialogFragment.TAG);
            }
        });

        viewModel.getErrorPostCustomerProductsSingleLiveEvent().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                ErrorDialogFragment fragment = ErrorDialogFragment.newInstance(s);
                fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
            }
        });

        viewModel.getProductInfoSingleLiveEvent().observe(this, new Observer<ProductResult>() {
            @Override
            public void onChanged(ProductResult productResult) {
                if (invoicePrice == 0) {
                    NumberFormat formatter = new DecimalFormat("#,###");
                    String formattedNumber = formatter.format(productResult.getProducts()[0].getCost());
                    binding.edTextInvoicePrice.setText(formattedNumber);
                } else {
                    NumberFormat formatter = new DecimalFormat("#,###");
                    String formattedNumber = formatter.format(invoicePrice);
                    binding.edTextInvoicePrice.setText(formattedNumber);
                }
            }
        });

        viewModel.getErrorProductInfoSingleLiveEvent().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String error) {
                //TODO
            }
        });

        viewModel.getDialogDismissSingleLiveEvent().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean dialogDismissed) {
                dismiss();
            }
        });

        viewModel.getEditCustomerProductSingleLiveEvent().observe(this, new Observer<CustomerProductResult>() {
            @Override
            public void onChanged(CustomerProductResult customerProductResult) {
                RegisterProductSuccessfulDialogFragment fragment = RegisterProductSuccessfulDialogFragment.newInstance();
                fragment.show(getActivity().getSupportFragmentManager(), RegisterProductSuccessfulDialogFragment.TAG);
            }
        });

        viewModel.getErrorEditCustomerProductSingleLiveEvent().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String error) {
                ErrorDialogFragment fragment = ErrorDialogFragment.newInstance(error);
                fragment.show(getActivity().getSupportFragmentManager(), ErrorDialogFragment.TAG);
            }
        });
    }

    private void setupSpinner(ProductInfo[] productInfoArray) {
        List<String> productNameList = new ArrayList<>();
        for (ProductInfo productInfo : productInfoArray) {
            productNameList.add(productInfo.getProductName());
        }

        if (!productName.isEmpty()) {
            value = productName;
            productNameList.remove(productName);
            productNameList.add(0, productName);
            binding.spinner.setItems(productNameList);
        } else {
            value = productNameList.get(0);
            binding.spinner.setItems(productNameList);
        }
    }
}