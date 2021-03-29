package com.example.sipsupporterapp.retrofit;

import com.example.sipsupporterapp.model.AttachInfo;
import com.example.sipsupporterapp.model.AttachResult;
import com.example.sipsupporterapp.model.CustomerProductResult;
import com.example.sipsupporterapp.model.CustomerProducts;
import com.example.sipsupporterapp.model.CustomerResult;
import com.example.sipsupporterapp.model.CustomerSupportInfo;
import com.example.sipsupporterapp.model.CustomerSupportResult;
import com.example.sipsupporterapp.model.CustomerUserResult;
import com.example.sipsupporterapp.model.DateResult;
import com.example.sipsupporterapp.model.ProductInfo;
import com.example.sipsupporterapp.model.ProductResult;
import com.example.sipsupporterapp.model.SupportEventResult;
import com.example.sipsupporterapp.model.UserLoginParameter;
import com.example.sipsupporterapp.model.UserResult;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface SipSupportService {

    @POST(".")
    Call<UserResult> postUserLoginParameter(@Body UserLoginParameter userLoginParameter);

    @GET(".")
    Call<CustomerResult> getCustomers(@Header("userLoginKey") String userLoginKey, @Query("customerName") String customerName);

    @GET(".")
    Call<CustomerResult> getCustomerInfo(@Header("userLoginKey") String userLoginKey, int customerID);

    @POST(".")
    Call<UserResult> changePassword(@Header("userLoginKey") String userLoginKey, @Body String newPassword);

    @GET(".")
    Call<CustomerSupportResult> getCustomerSupportResult(@Header("userLoginKey") String userLoginKey, @Query("customerID") int customerID);

    @GET(".")
    Call<CustomerUserResult> getCustomerUserResult(@Header("userLoginKey") String userLoginKey, @Query("customerID") int customerID);

    @GET(".")
    Call<SupportEventResult> getSupportEventResult(@Header("userLoginKey") String userLoginKey);

    @POST(".")
    Call<CustomerSupportResult> postCustomerSupportInfo(@Header("userLoginKey") String userLoginKey, @Body CustomerSupportInfo customerSupportInfo);

    @GET(".")
    Call<DateResult> getDateResult(@Header("userLoginKey") String userLoginKey);

    @GET(".")
    Call<CustomerProductResult> getProductResult(@Header("userLoginKey") String userLoginKey, @Query("customerID") int customerID);

    @POST(".")
    Call<ProductResult> postProductInfo(@Header("userLoginKey") String userLoginKey, @Body ProductInfo productInfo);

    @GET(".")
    Call<ProductResult> getProductResult(@Header("userLoginKey") String userLoginKey);

    @POST(".")
    Call<CustomerProductResult> postCustomerProducts(@Header("userLoginKey") String userLoginKey, @Body CustomerProducts customerProducts);

    @GET(".")
    Call<ProductResult> getProductInfo(@Header("userLoginKey") String userLoginKey, @Query("productID") int productID);

    @DELETE(".")
    Call<CustomerProductResult> deleteCustomerProduct(@Header("userLoginKey") String userLoginKey, @Query("customerProductID") int customerProductID);

    @PUT(".")
    Call<CustomerProductResult> editCustomerProduct(@Header("userLoginKey") String userLoginKey, @Body CustomerProducts customerProducts);

    @POST(".")
    Call<AttachResult> attach(@Header("userLoginKey") String userLoginKey, @Body AttachInfo attachInfo);
}
