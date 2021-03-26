package com.example.sipsupporterapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.sipsupporterapp.model.CustomerSupportResult;
import com.example.sipsupporterapp.model.ServerData;
import com.example.sipsupporterapp.repository.SipSupportRepository;

import java.util.List;

public class SupportHistoryViewModel extends AndroidViewModel {
    private SipSupportRepository repository;
    private SingleLiveEvent<CustomerSupportResult> customerSupportResult;
    private SingleLiveEvent<String> errorCustomerSupportResult;
    private SingleLiveEvent<Boolean> dangerousUserSingleLiveEvent;
    private SingleLiveEvent<String> noConnection;
    private SingleLiveEvent<Boolean> timeoutExceptionHappenSingleLiveEvent;

    public SupportHistoryViewModel(@NonNull Application application) {
        super(application);
        repository = SipSupportRepository.getInstance(getApplication());
        customerSupportResult = repository.getCustomerSupportResult();
        errorCustomerSupportResult = repository.getErrorCustomerSupportResult();
        dangerousUserSingleLiveEvent = repository.getDangerousUserSingleLiveEvent();
        noConnection = repository.getNoConnection();
        timeoutExceptionHappenSingleLiveEvent = repository.getTimeoutExceptionHappenSingleLiveEvent();
    }

    public SingleLiveEvent<CustomerSupportResult> getCustomerSupportResult() {
        return customerSupportResult;
    }

    public SingleLiveEvent<String> getErrorCustomerSupportResult() {
        return errorCustomerSupportResult;
    }

    public void getCustomerSupportResult(String userLoginKey, int customerID) {
        repository.getCustomerSupportResult(userLoginKey, customerID);
    }

    public ServerData getServerData(String centerName) {
        return repository.getServerData(centerName);
    }

    public void getSipSupportServiceCustomerSupportResult(String baseUrl) {
        repository.getSipSupportServiceCustomerSupportResult(baseUrl);
    }

    public SingleLiveEvent<Boolean> getDangerousUserSingleLiveEvent() {
        return dangerousUserSingleLiveEvent;
    }

    public SingleLiveEvent<String> getNoConnection() {
        return noConnection;
    }

    public SingleLiveEvent<Boolean> getTimeoutExceptionHappenSingleLiveEvent() {
        return timeoutExceptionHappenSingleLiveEvent;
    }

    public void deleteServerData(ServerData serverData) {
        repository.deleteServerData(serverData);
    }

    public List<ServerData> getServerDataList() {
        return repository.getServerDataList();
    }
}
