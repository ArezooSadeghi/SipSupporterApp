package com.example.sipsupporterapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.sipsupporterapp.model.AttachInfo;
import com.example.sipsupporterapp.model.AttachResult;
import com.example.sipsupporterapp.model.ServerData;
import com.example.sipsupporterapp.repository.SipSupportRepository;

public class AttachmentViewModel extends AndroidViewModel {
    private SipSupportRepository mRepository;

    private SingleLiveEvent<Boolean> mRequestPermissionSingleLiveEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<Boolean> mAllowPermissionSingleLiveEvent = new SingleLiveEvent<>();

    private SingleLiveEvent<AttachResult> mAttachResultSingleLiveEvent;
    private SingleLiveEvent<String> mErrorAttachResultSingleLiveEvent;
    private SingleLiveEvent<String> mNoConnectionSingleLiveEvent;
    private SingleLiveEvent<Boolean> mTimeOutExceptionHappenSingleLiveEvent;
    private SingleLiveEvent<Boolean> mDangerousUserSingleLiveEvent;

    private SingleLiveEvent<String> mBitmapAsStringSingleLiveEvent = new SingleLiveEvent<>();

    private SingleLiveEvent<Boolean> mIsAttachAgainSingleLiveEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<Boolean> mYesAgainSingleLiveEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<Boolean> mNoAgainSingleLiveEvent = new SingleLiveEvent<>();

    public AttachmentViewModel(@NonNull Application application) {
        super(application);

        mRepository = SipSupportRepository.getInstance(getApplication());

        mAttachResultSingleLiveEvent = mRepository.getAttachResultSingleLiveEvent();
        mErrorAttachResultSingleLiveEvent = mRepository.getErrorAttachResultSingleLiveEvent();
        mNoConnectionSingleLiveEvent = mRepository.getNoConnection();
        mTimeOutExceptionHappenSingleLiveEvent = mRepository.getTimeoutExceptionHappenSingleLiveEvent();
        mDangerousUserSingleLiveEvent = mRepository.getDangerousUserSingleLiveEvent();
    }

    public SingleLiveEvent<Boolean> getRequestPermissionSingleLiveEvent() {
        return mRequestPermissionSingleLiveEvent;
    }

    public SingleLiveEvent<Boolean> getAllowPermissionSingleLiveEvent() {
        return mAllowPermissionSingleLiveEvent;
    }

    public SingleLiveEvent<String> getBitmapAsStringSingleLiveEvent() {
        return mBitmapAsStringSingleLiveEvent;
    }

    public SingleLiveEvent<AttachResult> getAttachResultSingleLiveEvent() {
        return mAttachResultSingleLiveEvent;
    }

    public SingleLiveEvent<String> getErrorAttachResultSingleLiveEvent() {
        return mErrorAttachResultSingleLiveEvent;
    }

    public SingleLiveEvent<String> getNoConnectionSingleLiveEvent() {
        return mNoConnectionSingleLiveEvent;
    }

    public SingleLiveEvent<Boolean> getTimeOutExceptionHappenSingleLiveEvent() {
        return mTimeOutExceptionHappenSingleLiveEvent;
    }

    public SingleLiveEvent<Boolean> getDangerousUserSingleLiveEvent() {
        return mDangerousUserSingleLiveEvent;
    }

    public SingleLiveEvent<Boolean> getIsAttachAgainSingleLiveEvent() {
        return mIsAttachAgainSingleLiveEvent;
    }

    public SingleLiveEvent<Boolean> getYesAgainSingleLiveEvent() {
        return mYesAgainSingleLiveEvent;
    }

    public SingleLiveEvent<Boolean> getNoAgainSingleLiveEvent() {
        return mNoAgainSingleLiveEvent;
    }

    public ServerData getServerData(String centerName) {
        return mRepository.getServerData(centerName);
    }

    public void getSipSupportServiceAttach(String baseUrl) {
        mRepository.getSipSupportServiceAttach(baseUrl);
    }

    public void attach(String userLoginKey, AttachInfo attachInfo) {
        mRepository.attach(userLoginKey, attachInfo);
    }
}
