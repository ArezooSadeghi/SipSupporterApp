package com.example.sipsupporterapp.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.sipsupporterapp.database.SipSupporterDBHelper;
import com.example.sipsupporterapp.database.SipSupporterSchema;
import com.example.sipsupporterapp.model.CustomerProductResult;
import com.example.sipsupporterapp.model.CustomerProducts;
import com.example.sipsupporterapp.model.CustomerResult;
import com.example.sipsupporterapp.model.CustomerSupportInfo;
import com.example.sipsupporterapp.model.CustomerSupportResult;
import com.example.sipsupporterapp.model.CustomerUserResult;
import com.example.sipsupporterapp.model.DateResult;
import com.example.sipsupporterapp.model.ProductInfo;
import com.example.sipsupporterapp.model.ProductResult;
import com.example.sipsupporterapp.model.ServerData;
import com.example.sipsupporterapp.model.SupportEventResult;
import com.example.sipsupporterapp.model.UserLoginParameter;
import com.example.sipsupporterapp.model.UserResult;
import com.example.sipsupporterapp.retrofit.AddProductResultDeserializer;
import com.example.sipsupporterapp.retrofit.CustomerProductResultDeserializer;
import com.example.sipsupporterapp.retrofit.CustomerResultDeserializer;
import com.example.sipsupporterapp.retrofit.CustomerSupportResultDeserializer;
import com.example.sipsupporterapp.retrofit.CustomerUserResultDeserializer;
import com.example.sipsupporterapp.retrofit.DateResultDeserializer;
import com.example.sipsupporterapp.retrofit.NoConnectivityException;
import com.example.sipsupporterapp.retrofit.ProductResultDeserializer;
import com.example.sipsupporterapp.retrofit.RetrofitInstance;
import com.example.sipsupporterapp.retrofit.SipSupportService;
import com.example.sipsupporterapp.retrofit.SupportEventResultDeserializer;
import com.example.sipsupporterapp.retrofit.UserResultDeserializer;
import com.example.sipsupporterapp.viewmodel.SingleLiveEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SipSupportRepository {

    public static SipSupportRepository instance;
    private SQLiteDatabase database;
    private Context context;
    private SipSupportService sipSupportServicePostUserLoginParameter, sipSupportServicePostCustomerParameter, sipSupportServiceChangePassword,
            sipSupportServiceCustomerSupportResult, sipSupportServiceCustomerUserResult, sipSupportServiceSupportEventResult, sipSupportServicePostCustomerSupportInfo,
            sipSupportServiceGetDateResult, sipSupportServiceGetCustomerProductResult, sipSupportServicePostProductInfo, sipSupportServiceGetProductResult, sipSupportServicePostCustomerProducts;
    private static final String TAG = SipSupportRepository.class.getSimpleName();

    private SingleLiveEvent<CustomerResult> customerResultSingleLiveEvent = new SingleLiveEvent<>();
    private MutableLiveData<String> wrongUserLoginKeyMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<String> notValueUserLoginKeyMutableLiveData = new MutableLiveData<>();
    private SingleLiveEvent<String> wrongIpAddressSingleLiveEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<UserResult> userResultSingleLiveEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<Boolean> timeoutExceptionHappenSingleLiveEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<Boolean> noConnectivityExceptionSingleLiveEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<String> errorUserResult = new SingleLiveEvent<>();
    private SingleLiveEvent<String> noConnection = new SingleLiveEvent<>();
    private SingleLiveEvent<UserResult> changedPassword = new SingleLiveEvent<>();
    private SingleLiveEvent<String> errorChangedPassword = new SingleLiveEvent<>();
    private SingleLiveEvent<CustomerSupportResult> customerSupportResult = new SingleLiveEvent<>();
    private SingleLiveEvent<String> errorCustomerSupportResult = new SingleLiveEvent<>();
    private SingleLiveEvent<CustomerUserResult> customerUserResultSingleLiveEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<String> errorCustomerUserResultSingleLiveEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<SupportEventResult> supportEventResultSingleLiveEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<String> errorSupportEventResultSingleLiveEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<CustomerSupportResult> customerSupportResultSingleLiveEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<String> errorCustomerSupportResultSingleLiveEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<Boolean> dangerousUserSingleLiveEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<String> errorSingleLiveEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<DateResult> dateResultSingleLiveEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<CustomerProductResult> customerProductResultSingleLiveEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<String> errorCustomerProductResultSingleLiveEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<ProductResult> productResultSingleLiveEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<String> errorProductResultSingleLiveEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<ProductResult> getProductResultSingleLiveEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<String> getErrorProductResultSingleLiveEvent = new SingleLiveEvent<>();

    private SingleLiveEvent<CustomerProductResult> PostCustomerProductsSingleLiveEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<String> errorPostCustomerProductsSingleLiveEvent = new SingleLiveEvent<>();


    private SipSupportRepository(Context context) {
        this.context = context.getApplicationContext();
        SipSupporterDBHelper helper = new SipSupporterDBHelper(context);
        database = helper.getWritableDatabase();
    }

    public static SipSupportRepository getInstance(Context context) {
        if (instance == null) {
            instance = new SipSupportRepository(context.getApplicationContext());
        }
        return instance;
    }

    public void getSipSupportServicePostUserLoginParameter(String baseUrl) {
        RetrofitInstance.getNewBaseUrl(baseUrl);
        sipSupportServicePostUserLoginParameter = RetrofitInstance
                .getRetrofitInstanceForPostUserLoginParameter(new TypeToken<UserResult>() {
                }.getType(), new UserResultDeserializer(), context).create(SipSupportService.class);
    }

    public void getSupportServicePostCustomerParameter(String baseUrl) {
        RetrofitInstance.getNewBaseUrl(baseUrl);
        sipSupportServicePostCustomerParameter = RetrofitInstance
                .getRetrofitInstanceForPostCustomerParameter(new TypeToken<CustomerResult>() {
                }.getType(), new CustomerResultDeserializer(), context).create(SipSupportService.class);
    }

    public void getSipSupportServiceChangePassword(String baseUrl) {
        RetrofitInstance.getNewBaseUrl(baseUrl);
        sipSupportServiceChangePassword = RetrofitInstance
                .getRetrofitInstanceChangePassword(new TypeToken<UserResult>() {
                }.getType(), new UserResultDeserializer(), context).create(SipSupportService.class);
    }

    public void getSipSupportServiceCustomerSupportResult(String baseUrl) {
        RetrofitInstance.getNewBaseUrl(baseUrl);
        sipSupportServiceCustomerSupportResult = RetrofitInstance
                .getRetrofitInstanceCustomerSupportResult(new TypeToken<CustomerSupportResult>() {
                }.getType(), new CustomerSupportResultDeserializer(), context).create(SipSupportService.class);
    }

    public void getSipSupportServiceCustomerUserResult(String baseUrl) {
        RetrofitInstance.getNewBaseUrl(baseUrl);
        sipSupportServiceCustomerUserResult = RetrofitInstance
                .getRetrofitInstanceCustomerUserResult(new TypeToken<CustomerUserResult>() {
                }.getType(), new CustomerUserResultDeserializer(), context).create(SipSupportService.class);
    }

    public void getSipSupportServiceSupportEventResult(String baseUrl) {
        RetrofitInstance.getNewBaseUrl(baseUrl);
        sipSupportServiceSupportEventResult = RetrofitInstance
                .getRetrofitInstanceSupportEventResult(new TypeToken<SupportEventResult>() {
                }.getType(), new SupportEventResultDeserializer(), context).create(SipSupportService.class);
    }

    public void getSipSupportServicePostCustomerSupportResult(String baseUrl) {
        RetrofitInstance.getNewBaseUrl(baseUrl);
        sipSupportServiceCustomerSupportResult = RetrofitInstance
                .getRetrofitInstancePostCustomerSupportInfo(new TypeToken<CustomerSupportResult>() {
                }.getType(), new CustomerSupportResultDeserializer(), context).create(SipSupportService.class);
    }

    public void getSipSupportServiceGetDateResult(String baseUrl) {
        RetrofitInstance.getNewBaseUrl(baseUrl);
        sipSupportServiceGetDateResult = RetrofitInstance
                .getRetrofitInstanceGetDateResult(new TypeToken<DateResult>() {
                }.getType(), new DateResultDeserializer(), context).create(SipSupportService.class);
    }

    public void getSipSupportServiceGetCustomerProductResult(String baseUrl) {
        RetrofitInstance.getNewBaseUrl(baseUrl);
        sipSupportServiceGetCustomerProductResult = RetrofitInstance
                .getRetrofitInstanceGetCustomerProductResult(new TypeToken<CustomerProductResult>() {
                }.getType(), new ProductResultDeserializer(), context).create(SipSupportService.class);
    }

    public void getSipSupportServicePostProductInfo(String baseUrl) {
        RetrofitInstance.getNewBaseUrl(baseUrl);
        sipSupportServicePostProductInfo = RetrofitInstance
                .getRetrofitInstancePostProductInfo(new TypeToken<ProductResult>() {
                }.getType(), new AddProductResultDeserializer(), context).create(SipSupportService.class);
    }

    public void getSipSupportServiceGetProductResult(String baseUrl) {
        RetrofitInstance.getNewBaseUrl(baseUrl);
        sipSupportServiceGetProductResult = RetrofitInstance
                .getRetrofitInstanceGetProductResult(new TypeToken<ProductResult>() {
                }.getType(), new AddProductResultDeserializer(), context).create(SipSupportService.class);
    }

    public void getSipSupportServicePostCustomerProducts(String baseUrl) {
        RetrofitInstance.getNewBaseUrl(baseUrl);
        sipSupportServicePostCustomerProducts = RetrofitInstance
                .getRetrofitInstancePostCustomerProducts(new TypeToken<CustomerProductResult>() {
                }.getType(), new CustomerProductResultDeserializer(), context).create(SipSupportService.class);
    }

    public SingleLiveEvent<CustomerResult> getCustomerResultSingleLiveEvent() {
        return customerResultSingleLiveEvent;
    }

    public MutableLiveData<String> getWrongUserLoginKeyMutableLiveData() {
        return wrongUserLoginKeyMutableLiveData;
    }

    public MutableLiveData<String> getNotValueUserLoginKeyMutableLiveData() {
        return notValueUserLoginKeyMutableLiveData;
    }

    public SingleLiveEvent<String> getWrongIpAddressSingleLiveEvent() {
        return wrongIpAddressSingleLiveEvent;
    }

    public SingleLiveEvent<UserResult> getUserResultSingleLiveEvent() {
        return userResultSingleLiveEvent;
    }

    public SingleLiveEvent<Boolean> getTimeoutExceptionHappenSingleLiveEvent() {
        return timeoutExceptionHappenSingleLiveEvent;
    }

    public SingleLiveEvent<Boolean> getNoConnectivityExceptionSingleLiveEvent() {
        return noConnectivityExceptionSingleLiveEvent;
    }

    public SingleLiveEvent<String> getErrorUserResult() {
        return errorUserResult;
    }

    public SingleLiveEvent<String> getNoConnection() {
        return noConnection;
    }

    public SingleLiveEvent<UserResult> getChangedPassword() {
        return changedPassword;
    }

    public SingleLiveEvent<String> getErrorChangedPassword() {
        return errorChangedPassword;
    }

    public SingleLiveEvent<CustomerSupportResult> getCustomerSupportResult() {
        return customerSupportResult;
    }

    public SingleLiveEvent<String> getErrorCustomerSupportResult() {
        return errorCustomerSupportResult;
    }

    public SingleLiveEvent<CustomerUserResult> getCustomerUserResultSingleLiveEvent() {
        return customerUserResultSingleLiveEvent;
    }

    public SingleLiveEvent<String> getErrorCustomerUserResultSingleLiveEvent() {
        return errorCustomerUserResultSingleLiveEvent;
    }

    public SingleLiveEvent<SupportEventResult> getSupportEventResultSingleLiveEvent() {
        return supportEventResultSingleLiveEvent;
    }

    public SingleLiveEvent<String> getErrorSupportEventResultSingleLiveEvent() {
        return errorSupportEventResultSingleLiveEvent;
    }

    public SingleLiveEvent<CustomerSupportResult> getCustomerSupportResultSingleLiveEvent() {
        return customerSupportResultSingleLiveEvent;
    }

    public SingleLiveEvent<String> getErrorCustomerSupportResultSingleLiveEvent() {
        return errorCustomerSupportResultSingleLiveEvent;
    }

    public SingleLiveEvent<Boolean> getDangerousUserSingleLiveEvent() {
        return dangerousUserSingleLiveEvent;
    }

    public SingleLiveEvent<String> getErrorSingleLiveEvent() {
        return errorSingleLiveEvent;
    }

    public SingleLiveEvent<DateResult> getDateResultSingleLiveEvent() {
        return dateResultSingleLiveEvent;
    }

    public SingleLiveEvent<CustomerProductResult> getCustomerProductResultSingleLiveEvent() {
        return customerProductResultSingleLiveEvent;
    }

    public SingleLiveEvent<String> getErrorCustomerProductResultSingleLiveEvent() {
        return errorCustomerProductResultSingleLiveEvent;
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

    public SingleLiveEvent<CustomerProductResult> getPostCustomerProductsSingleLiveEvent() {
        return PostCustomerProductsSingleLiveEvent;
    }

    public SingleLiveEvent<String> getErrorPostCustomerProductsSingleLiveEvent() {
        return errorPostCustomerProductsSingleLiveEvent;
    }

    public void insertServerData(ServerData serverData) {
        ContentValues values = new ContentValues();

        values.put(SipSupporterSchema.ServerDataTable.Cols.CENTER_NAME, serverData.getCenterName());
        values.put(SipSupporterSchema.ServerDataTable.Cols.IP_ADDRESS, serverData.getIpAddress());
        values.put(SipSupporterSchema.ServerDataTable.Cols.PORT, serverData.getPort());

        database.insert(SipSupporterSchema.ServerDataTable.NAME, null, values);
    }

    public List<ServerData> getServerDataList() {
        List<ServerData> serverDataList = new ArrayList<>();
        Cursor cursor = database.query(
                SipSupporterSchema.ServerDataTable.NAME,
                null,
                null,
                null,
                null,
                null,
                null);

        if (cursor == null || cursor.getCount() == 0) {
            return serverDataList;
        }

        try {

            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {

                String centerName = cursor.getString(cursor.getColumnIndex(SipSupporterSchema.ServerDataTable.Cols.CENTER_NAME));
                String ipAddress = cursor.getString(cursor.getColumnIndex(SipSupporterSchema.ServerDataTable.Cols.IP_ADDRESS));
                String port = cursor.getString(cursor.getColumnIndex(SipSupporterSchema.ServerDataTable.Cols.PORT));

                ServerData serverData = new ServerData(centerName, ipAddress, port);

                serverDataList.add(serverData);

                cursor.moveToNext();
            }

        } finally {

            cursor.close();
        }
        return serverDataList;
    }

    public ServerData getServerData(String centerName) {
        ServerData serverData = new ServerData();
        String selection = "centerName=?";
        String[] selectionArgs = {centerName};
        Cursor cursor = database.query(
                SipSupporterSchema.ServerDataTable.NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null);

        if (cursor == null || cursor.getCount() == 0) {
            return serverData;
        }

        try {

            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {

                String ipAddress = cursor.getString(cursor.getColumnIndex(SipSupporterSchema.ServerDataTable.Cols.IP_ADDRESS));
                String port = cursor.getString(cursor.getColumnIndex(SipSupporterSchema.ServerDataTable.Cols.PORT));

                serverData.setCenterName(centerName);
                serverData.setIpAddress(ipAddress);
                serverData.setPort(port);

                cursor.moveToNext();
            }

        } finally {

            cursor.close();
        }
        return serverData;
    }

    public void deleteServerData(ServerData serverData) {
        String whereClause = "centerName=?";
        String whereArgs[] = {serverData.getCenterName()};
        database.delete(SipSupporterSchema.ServerDataTable.NAME, whereClause, whereArgs);
    }

    public void fetchUserResult(UserLoginParameter userLoginParameter) {
        sipSupportServicePostUserLoginParameter.postUserLoginParameter(userLoginParameter)
                .enqueue(new Callback<UserResult>() {
                    @Override
                    public void onResponse(Call<UserResult> call, Response<UserResult> response) {
                        if (response.code() == 200) {
                            try {
                                if (Integer.valueOf(response.body().getErrorCode()) <= -9001) {
                                    dangerousUserSingleLiveEvent.setValue(true);
                                } else {
                                    userResultSingleLiveEvent.setValue(response.body());
                                }
                            } catch (NumberFormatException e) {
                                errorUserResult.setValue(response.body().getError());
                            }

                        } else if (response.code() == 400) {
                            Gson gson = new GsonBuilder().create();
                            UserResult userResult = new UserResult();
                            try {
                                userResult = gson.fromJson(response.errorBody().string(), UserResult.class);
                                try {
                                    if (Integer.valueOf(userResult.getErrorCode()) <= -9001) {
                                        dangerousUserSingleLiveEvent.setValue(true);
                                    } else {
                                        errorUserResult.setValue(userResult.getError());
                                    }
                                } catch (NumberFormatException e) {
                                    errorUserResult.setValue(userResult.getError());
                                }

                            } catch (IOException e) {
                                Log.e(TAG, e.getMessage(), e);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserResult> call, Throwable t) {
                        if (t instanceof NoConnectivityException) {
                            noConnection.setValue(t.getMessage());
                        } else if (t instanceof SocketTimeoutException) {
                            timeoutExceptionHappenSingleLiveEvent.setValue(true);
                        } else {
                            wrongIpAddressSingleLiveEvent.setValue("سرور موجود نمی باشد");
                        }
                    }
                });
    }

    public void fetchCustomerResult(String userLoginKey, String customerName) {
        sipSupportServicePostCustomerParameter.getCustomers(userLoginKey, customerName).enqueue(new Callback<CustomerResult>() {
            @Override
            public void onResponse(Call<CustomerResult> call, Response<CustomerResult> response) {
                if (response.code() == 200) {
                    if (Integer.valueOf(response.body().getErrorCode()) <= -9001) {
                        dangerousUserSingleLiveEvent.setValue(true);
                    } else {
                        customerResultSingleLiveEvent.setValue(response.body());
                    }
                } else if (response.code() == 400) {
                    Gson gson = new GsonBuilder().create();
                    CustomerResult customerResult = new CustomerResult();
                    try {
                        customerResult = gson.fromJson(response.errorBody().string(), CustomerResult.class);
                        if (Integer.valueOf(customerResult.getErrorCode()) <= -9001) {
                            dangerousUserSingleLiveEvent.setValue(true);
                        } else {
                            errorSingleLiveEvent.setValue(customerResult.getError());
                        }

                    } catch (IOException e) {
                        Log.e(TAG, e.getMessage(), e);
                    }
                }
            }

            @Override
            public void onFailure(Call<CustomerResult> call, Throwable t) {
                if (t instanceof NoConnectivityException) {
                    noConnectivityExceptionSingleLiveEvent.setValue(true);
                } else if (t instanceof SocketTimeoutException) {
                    timeoutExceptionHappenSingleLiveEvent.setValue(true);
                } else {
                    Log.e(TAG, t.getMessage(), t);
                }
            }
        });
    }

    public void changePassword(String userLoginKey, String newPassword) {
        sipSupportServiceChangePassword.changePassword(userLoginKey, newPassword).enqueue(new Callback<UserResult>() {
            @Override
            public void onResponse(Call<UserResult> call, Response<UserResult> response) {
                if (response.code() == 200) {
                    if (Integer.valueOf(response.body().getErrorCode()) <= -9001) {
                        dangerousUserSingleLiveEvent.setValue(true);
                    } else {
                        changedPassword.setValue(response.body());
                    }
                } else if (response.code() == 400) {
                    Gson gson = new GsonBuilder().create();
                    UserResult userResult = new UserResult();
                    try {
                        userResult = gson.fromJson(response.errorBody().string(), UserResult.class);
                        if (Integer.valueOf(userResult.getErrorCode()) <= -9001) {
                            dangerousUserSingleLiveEvent.setValue(true);
                        } else {
                            errorChangedPassword.setValue(userResult.getError());
                        }

                    } catch (IOException e) {
                        Log.e(TAG, e.getMessage(), e);
                    }
                }
            }

            @Override
            public void onFailure(Call<UserResult> call, Throwable t) {
                if (t instanceof NoConnectivityException) {
                    noConnection.setValue(t.getMessage());
                } else if (t instanceof SocketTimeoutException) {
                    timeoutExceptionHappenSingleLiveEvent.setValue(true);
                } else {
                    Log.e(TAG, t.getMessage(), t);
                }
            }
        });
    }

    public void getCustomerSupportResult(String userLoginKey, int customerID) {
        sipSupportServiceCustomerSupportResult.getCustomerSupportResult(userLoginKey, customerID).enqueue(new Callback<CustomerSupportResult>() {
            @Override
            public void onResponse(Call<CustomerSupportResult> call, Response<CustomerSupportResult> response) {
                if (response.code() == 200) {
                    if (Integer.valueOf(response.body().getErrorCode()) <= -9001) {
                        dangerousUserSingleLiveEvent.setValue(true);
                    } else {
                        customerSupportResult.setValue(response.body());
                    }
                } else if (response.code() == 400) {
                    Gson gson = new GsonBuilder().create();
                    CustomerSupportResult customerSupportResult = new CustomerSupportResult();
                    try {
                        customerSupportResult = gson.fromJson(response.errorBody().string(), CustomerSupportResult.class);
                        if (Integer.valueOf(customerSupportResult.getErrorCode()) <= -9001) {
                            dangerousUserSingleLiveEvent.setValue(true);
                        } else {
                            errorCustomerSupportResult.setValue(customerSupportResult.getError());
                        }


                    } catch (IOException e) {
                        Log.e(TAG, e.getMessage(), e);
                    }

                }
            }

            @Override
            public void onFailure(Call<CustomerSupportResult> call, Throwable t) {
                if (t instanceof NoConnectivityException) {
                    noConnection.setValue(t.getMessage());
                } else if (t instanceof SocketTimeoutException) {
                    timeoutExceptionHappenSingleLiveEvent.setValue(true);
                } else {
                    Log.e(TAG, t.getMessage(), t);
                }
            }
        });
    }


    public void fetchCustomerUserResult(String userLoginKey, int customerID) {
        sipSupportServiceCustomerUserResult.getCustomerUserResult(userLoginKey, customerID).enqueue(new Callback<CustomerUserResult>() {
            @Override
            public void onResponse(Call<CustomerUserResult> call, Response<CustomerUserResult> response) {
                if (response.code() == 200) {
                    if (Integer.valueOf(response.body().getErrorCode()) <= -9001) {
                        dangerousUserSingleLiveEvent.setValue(true);
                    } else {
                        customerUserResultSingleLiveEvent.setValue(response.body());
                    }
                } else if (response.code() == 400) {
                    Gson gson = new GsonBuilder().create();
                    CustomerUserResult customerUserResult = new CustomerUserResult();
                    try {
                        customerUserResult = gson.fromJson(response.errorBody().string(), CustomerUserResult.class);
                        if (Integer.valueOf(customerUserResult.getErrorCode()) <= -9001) {
                            dangerousUserSingleLiveEvent.setValue(true);
                        } else {
                            errorCustomerUserResultSingleLiveEvent.setValue(customerUserResult.getError());
                        }

                    } catch (IOException e) {
                        Log.e(TAG, e.getMessage(), e);
                    }
                }
            }

            @Override
            public void onFailure(Call<CustomerUserResult> call, Throwable t) {
                if (t instanceof NoConnectivityException) {
                    noConnection.setValue(t.getMessage());
                } else if (t instanceof SocketTimeoutException) {
                    timeoutExceptionHappenSingleLiveEvent.setValue(true);
                } else {
                    Log.e(TAG, t.getMessage(), t);
                }
            }
        });
    }


    public void fetchSupportEventResult(String userLoginKey) {
        sipSupportServiceSupportEventResult.getSupportEventResult(userLoginKey).enqueue(new Callback<SupportEventResult>() {
            @Override
            public void onResponse(Call<SupportEventResult> call, Response<SupportEventResult> response) {
                if (response.code() == 200) {
                    if (Integer.valueOf(response.body().getErrorCode()) <= -9001) {
                        dangerousUserSingleLiveEvent.setValue(true);
                    } else {
                        supportEventResultSingleLiveEvent.setValue(response.body());
                    }
                } else if (response.code() == 400) {
                    Gson gson = new GsonBuilder().create();
                    SupportEventResult supportEventResult = new SupportEventResult();
                    try {
                        supportEventResult = gson.fromJson(response.errorBody().string(), SupportEventResult.class);
                        if (Integer.valueOf(supportEventResult.getErrorCode()) <= -9001) {
                            dangerousUserSingleLiveEvent.setValue(true);
                        } else {
                            errorSupportEventResultSingleLiveEvent.setValue(supportEventResult.getError());
                        }

                    } catch (IOException e) {
                        Log.e(TAG, e.getMessage(), e);
                    }

                }
            }

            @Override
            public void onFailure(Call<SupportEventResult> call, Throwable t) {
                if (t instanceof NoConnectivityException) {
                    noConnection.setValue(t.getMessage());
                } else if (t instanceof SocketTimeoutException) {
                    timeoutExceptionHappenSingleLiveEvent.setValue(true);
                } else {
                    Log.e(TAG, t.getMessage(), t);
                }
            }
        });
    }

    public void postCustomerSupportInfo(String userLoginKey, CustomerSupportInfo customerSupportInfo) {
        sipSupportServiceCustomerSupportResult.postCustomerSupportInfo(userLoginKey, customerSupportInfo).enqueue(new Callback<CustomerSupportResult>() {
            @Override
            public void onResponse(Call<CustomerSupportResult> call, Response<CustomerSupportResult> response) {
                if (response.code() == 200) {
                    if (Integer.valueOf(response.body().getErrorCode()) <= -9001) {
                        dangerousUserSingleLiveEvent.setValue(true);
                    } else {
                        customerSupportResultSingleLiveEvent.setValue(response.body());
                    }

                } else if (response.code() == 400) {
                    Gson gson = new GsonBuilder().create();
                    CustomerSupportResult customerSupportResult = new CustomerSupportResult();
                    try {
                        customerSupportResult = gson.fromJson(response.errorBody().string(), CustomerSupportResult.class);
                        if (Integer.valueOf(customerSupportResult.getErrorCode()) <= -9001) {
                            dangerousUserSingleLiveEvent.setValue(true);
                        } else {
                            errorCustomerSupportResultSingleLiveEvent.setValue(customerSupportResult.getError());
                        }

                    } catch (IOException e) {
                        Log.e(TAG, e.getMessage(), e);
                    }
                }
            }

            @Override
            public void onFailure(Call<CustomerSupportResult> call, Throwable t) {
                if (t instanceof NoConnectivityException) {
                    noConnection.setValue(t.getMessage());
                } else if (t instanceof SocketTimeoutException) {
                    timeoutExceptionHappenSingleLiveEvent.setValue(true);
                } else {
                    Log.e(TAG, t.getMessage(), t);
                }
            }
        });
    }

    public void fetchDateResult(String userLoginKey) {
        sipSupportServiceGetDateResult.getDateResult(userLoginKey).enqueue(new Callback<DateResult>() {
            @Override
            public void onResponse(Call<DateResult> call, Response<DateResult> response) {
                if (response.code() == 200) {
                    dateResultSingleLiveEvent.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<DateResult> call, Throwable t) {
                if (t instanceof NoConnectivityException) {
                    noConnection.setValue(t.getMessage());
                } else if (t instanceof SocketTimeoutException) {
                    timeoutExceptionHappenSingleLiveEvent.setValue(true);
                } else {
                    Log.e(TAG, t.getMessage(), t);
                }
            }
        });
    }

    public void fetchProductResult(String userLoginKey, int customerID) {
        sipSupportServiceGetCustomerProductResult.getProductResult(userLoginKey, customerID).enqueue(new Callback<CustomerProductResult>() {
            @Override
            public void onResponse(Call<CustomerProductResult> call, Response<CustomerProductResult> response) {
                if (response.code() == 200) {
                    if (Integer.valueOf(response.body().getErrorCode()) <= -9001) {
                        dangerousUserSingleLiveEvent.setValue(true);
                    } else {
                        customerProductResultSingleLiveEvent.setValue(response.body());
                    }

                } else if (response.code() == 400) {
                    Gson gson = new GsonBuilder().create();
                    CustomerProductResult productResult = new CustomerProductResult();
                    try {
                        productResult = gson.fromJson(response.errorBody().string(), CustomerProductResult.class);
                        if (Integer.valueOf(productResult.getErrorCode()) <= -9001) {
                            dangerousUserSingleLiveEvent.setValue(true);
                        } else {
                            errorCustomerProductResultSingleLiveEvent.setValue(productResult.getError());
                        }

                    } catch (IOException e) {
                        Log.e(TAG, e.getMessage(), e);
                    }
                }
            }

            @Override
            public void onFailure(Call<CustomerProductResult> call, Throwable t) {
                if (t instanceof NoConnectivityException) {
                    noConnection.setValue(t.getMessage());
                } else if (t instanceof SocketTimeoutException) {
                    timeoutExceptionHappenSingleLiveEvent.setValue(true);
                } else {
                    Log.e(TAG, t.getMessage(), t);
                }
            }
        });
    }

    public void postProductInfo(String userLoginKey, ProductInfo productInfo) {
        sipSupportServicePostProductInfo.postProductInfo(userLoginKey, productInfo).enqueue(new Callback<ProductResult>() {
            @Override
            public void onResponse(Call<ProductResult> call, Response<ProductResult> response) {
                if (response.code() == 200) {
                    if (Integer.valueOf(response.body().getErrorCode()) <= -9001) {
                        dangerousUserSingleLiveEvent.setValue(true);
                    } else {
                        productResultSingleLiveEvent.setValue(response.body());
                    }

                } else if (response.code() == 400) {
                    Gson gson = new GsonBuilder().create();
                    ProductResult productResult = new ProductResult();
                    try {
                        productResult = gson.fromJson(response.errorBody().string(), ProductResult.class);
                        if (Integer.valueOf(productResult.getErrorCode()) <= -9001) {
                            dangerousUserSingleLiveEvent.setValue(true);
                        } else {
                            errorProductResultSingleLiveEvent.setValue(productResult.getError());
                        }

                    } catch (IOException e) {
                        Log.e(TAG, e.getMessage(), e);
                    }
                }
            }

            @Override
            public void onFailure(Call<ProductResult> call, Throwable t) {
                if (t instanceof NoConnectivityException) {
                    noConnection.setValue(t.getMessage());
                } else if (t instanceof SocketTimeoutException) {
                    timeoutExceptionHappenSingleLiveEvent.setValue(true);
                } else {
                    Log.e(TAG, t.getMessage(), t);
                }
            }
        });
    }

    public void fetchProductResult(String userLoginKey) {
        sipSupportServiceGetProductResult.getProductResult(userLoginKey).enqueue(new Callback<ProductResult>() {
            @Override
            public void onResponse(Call<ProductResult> call, Response<ProductResult> response) {
                if (response.code() == 200) {
                    if (Integer.valueOf(response.body().getErrorCode()) <= -9001) {
                        dangerousUserSingleLiveEvent.setValue(true);
                    } else {
                        getProductResultSingleLiveEvent.setValue(response.body());
                    }

                } else if (response.code() == 400) {
                    Gson gson = new GsonBuilder().create();
                    ProductResult productResult = new ProductResult();
                    try {
                        productResult = gson.fromJson(response.errorBody().string(), ProductResult.class);
                        if (Integer.valueOf(productResult.getErrorCode()) <= -9001) {
                            dangerousUserSingleLiveEvent.setValue(true);
                        } else {
                            getErrorProductResultSingleLiveEvent.setValue(productResult.getError());
                        }

                    } catch (IOException e) {
                        Log.e(TAG, e.getMessage(), e);
                    }
                }
            }

            @Override
            public void onFailure(Call<ProductResult> call, Throwable t) {
                if (t instanceof NoConnectivityException) {
                    noConnection.setValue(t.getMessage());
                } else if (t instanceof SocketTimeoutException) {
                    timeoutExceptionHappenSingleLiveEvent.setValue(true);
                } else {
                    Log.e(TAG, t.getMessage(), t);
                }
            }
        });
    }

    public void postCustomerProducts(String userLoginKey, CustomerProducts customerProducts) {
        sipSupportServicePostCustomerProducts.postCustomerProducts(userLoginKey, customerProducts).enqueue(new Callback<CustomerProductResult>() {
            @Override
            public void onResponse(Call<CustomerProductResult> call, Response<CustomerProductResult> response) {
                if (response.code() == 200) {
                    if (Integer.valueOf(response.body().getErrorCode()) <= -9001) {
                        dangerousUserSingleLiveEvent.setValue(true);
                    } else {
                        PostCustomerProductsSingleLiveEvent.setValue(response.body());
                    }

                } else if (response.code() == 400) {
                    Log.d("Arezoo", "400");
                    Gson gson = new GsonBuilder().create();
                    CustomerProductResult customerProductResult = new CustomerProductResult();
                    try {
                        customerProductResult = gson.fromJson(response.errorBody().string(), CustomerProductResult.class);
                        if (Integer.valueOf(customerProductResult.getErrorCode()) <= -9001) {
                            dangerousUserSingleLiveEvent.setValue(true);
                        } else {
                            errorPostCustomerProductsSingleLiveEvent.setValue(customerProductResult.getError());
                        }

                    } catch (IOException e) {
                        Log.e(TAG, e.getMessage(), e);
                    }
                }
            }

            @Override
            public void onFailure(Call<CustomerProductResult> call, Throwable t) {
                if (t instanceof NoConnectivityException) {
                    noConnection.setValue(t.getMessage());
                } else if (t instanceof SocketTimeoutException) {
                    timeoutExceptionHappenSingleLiveEvent.setValue(true);
                } else {
                    Log.e(TAG, t.getMessage(), t);
                }
            }
        });
    }
}
