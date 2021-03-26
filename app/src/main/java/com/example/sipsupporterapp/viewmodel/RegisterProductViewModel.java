package com.example.sipsupporterapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.sipsupporterapp.model.CustomerProductResult;
import com.example.sipsupporterapp.model.CustomerProducts;
import com.example.sipsupporterapp.model.ProductInfo;
import com.example.sipsupporterapp.model.ProductResult;
import com.example.sipsupporterapp.model.ServerData;
import com.example.sipsupporterapp.repository.SipSupportRepository;

public class RegisterProductViewModel extends AndroidViewModel {
    private SipSupportRepository repository;
    private SingleLiveEvent<ProductResult> productResultSingleLiveEvent;
    private SingleLiveEvent<String> errorProductResultSingleLiveEvent;
    private SingleLiveEvent<ProductResult> getProductResultSingleLiveEvent;
    private SingleLiveEvent<String> getErrorProductResultSingleLiveEvent;

    private SingleLiveEvent<CustomerProductResult> PostCustomerProductsSingleLiveEvent;
    private SingleLiveEvent<String> errorPostCustomerProductsSingleLiveEvent;

    public RegisterProductViewModel(@NonNull Application application) {
        super(application);
        repository = SipSupportRepository.getInstance(getApplication());
        productResultSingleLiveEvent = repository.getProductResultSingleLiveEvent();
        errorProductResultSingleLiveEvent = repository.getErrorProductResultSingleLiveEvent();
        getProductResultSingleLiveEvent = repository.getGetProductResultSingleLiveEvent();
        getErrorProductResultSingleLiveEvent = repository.getGetErrorProductResultSingleLiveEvent();
        PostCustomerProductsSingleLiveEvent = repository.getPostCustomerProductsSingleLiveEvent();
        errorPostCustomerProductsSingleLiveEvent = repository.getErrorPostCustomerProductsSingleLiveEvent();
    }

    public SingleLiveEvent<ProductResult> getProductResultSingleLiveEvent() {
        return productResultSingleLiveEvent;
    }

    public SingleLiveEvent<String> getErrorProductResultSingleLiveEvent() {
        return errorProductResultSingleLiveEvent;
    }

    public SingleLiveEvent<ProductResult> getGetProductResultSingleLiveEvent() {
        return getProductResultSingleLiveEvent;
    }

    public SingleLiveEvent<String> getGetErrorProductResultSingleLiveEvent() {
        return getErrorProductResultSingleLiveEvent;
    }

    public ServerData getServerData(String centerName) {
        return repository.getServerData(centerName);
    }

    public void postProductInfo(String userLoginKey, ProductInfo productInfo) {
        repository.postProductInfo(userLoginKey, productInfo);
    }

    public void fetchProductResult(String userLoginKey) {
        repository.fetchProductResult(userLoginKey);
    }

    public void getSipSupportServicePostProductInfo(String baseUrl) {
        repository.getSipSupportServicePostProductInfo(baseUrl);
    }

    public void getSipSupportServiceGetProductResult(String baseUrl) {
        repository.getSipSupportServiceGetProductResult(baseUrl);
    }

    public SingleLiveEvent<CustomerProductResult> getPostCustomerProductsSingleLiveEvent() {
        return PostCustomerProductsSingleLiveEvent;
    }

    public SingleLiveEvent<String> getErrorPostCustomerProductsSingleLiveEvent() {
        return errorPostCustomerProductsSingleLiveEvent;
    }

    public void getSipSupportServicePostCustomerProducts(String baseUrl) {
        repository.getSipSupportServicePostCustomerProducts(baseUrl);
    }

    public void postCustomerProducts(String userLoginKey, CustomerProducts customerProducts) {
        repository.postCustomerProducts(userLoginKey, customerProducts);
    }
}
