package com.example.sipsupporterapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.sipsupporterapp.model.CustomerProductResult;
import com.example.sipsupporterapp.model.ServerData;
import com.example.sipsupporterapp.repository.SipSupportRepository;

public class ProductsViewModel extends AndroidViewModel {
    private SipSupportRepository repository;
    private SingleLiveEvent<CustomerProductResult> customerProductResultSingleLiveEvent;
    private SingleLiveEvent<String> errorCustomerProductResultSingleLiveEvent;
    private SingleLiveEvent<Boolean> timeoutExceptionHappenSingleLiveEvent;

    public ProductsViewModel(@NonNull Application application) {
        super(application);
        repository = SipSupportRepository.getInstance(getApplication());
        customerProductResultSingleLiveEvent = repository.getCustomerProductResultSingleLiveEvent();
        errorCustomerProductResultSingleLiveEvent = repository.getErrorCustomerProductResultSingleLiveEvent();
        timeoutExceptionHappenSingleLiveEvent = repository.getTimeoutExceptionHappenSingleLiveEvent();
    }

    public SingleLiveEvent<CustomerProductResult> getCustomerProductResultSingleLiveEvent() {
        return customerProductResultSingleLiveEvent;
    }

    public SingleLiveEvent<String> getErrorCustomerProductResultSingleLiveEvent() {
        return errorCustomerProductResultSingleLiveEvent;
    }

    public void getSipSupportServiceGetCustomerProductResult(String baseUrl) {
        repository.getSipSupportServiceGetCustomerProductResult(baseUrl);
    }

    public SingleLiveEvent<Boolean> getTimeoutExceptionHappenSingleLiveEvent() {
        return timeoutExceptionHappenSingleLiveEvent;
    }

    public void fetchProductResult(String userLoginKey, int customerID) {
        repository.fetchProductResult(userLoginKey, customerID);
    }

    public ServerData getServerData(String centerName) {
        return repository.getServerData(centerName);
    }
}
