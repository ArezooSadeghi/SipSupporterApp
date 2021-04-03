package com.example.sipsupporterapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.appcompat.widget.PopupMenu;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sipsupporterapp.R;
import com.example.sipsupporterapp.databinding.CustomerPaymentInfoAdapterItemBinding;
import com.example.sipsupporterapp.model.CustomerPaymentInfo;
import com.example.sipsupporterapp.viewmodel.DepositAmountsViewModel;

import java.util.List;

public class CustomerPaymentInfoAdapter extends RecyclerView.Adapter<CustomerPaymentInfoAdapter.CustomerPaymentInfoHolder> {
    private Context context;
    private List<CustomerPaymentInfo> customerPaymentInfoList;
    private DepositAmountsViewModel viewModel;

    public CustomerPaymentInfoAdapter(Context context, List<CustomerPaymentInfo> customerPaymentInfoList, DepositAmountsViewModel viewModel) {
        this.context = context;
        this.customerPaymentInfoList = customerPaymentInfoList;
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public CustomerPaymentInfoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CustomerPaymentInfoHolder(DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.customer_payment_info_adapter_item,
                parent,
                false));
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerPaymentInfoHolder holder, int position) {
        holder.bindCustomerPaymentInfo(customerPaymentInfoList.get(position));
        holder.binding.imgBtnMore.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {
                PopupMenu menu = new PopupMenu(context, holder.binding.imgBtnMore);
                menu.inflate(R.menu.customer_payment_info_adapter_menu);

                @SuppressLint("RestrictedApi") MenuPopupHelper menuHelper = new MenuPopupHelper(context, (MenuBuilder) menu.getMenu(), holder.binding.imgBtnMore);
                menuHelper.setForceShowIcon(true);


                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.item_see_documents:
                                viewModel.getSeeDocumentsClickedSingleLiveEvent().setValue(customerPaymentInfoList.get(position));
                                return true;
                            case R.id.item_delete:
                                viewModel.getDeleteCustomerPaymentsClickedSingleLiveEvent().setValue(customerPaymentInfoList.get(position));
                                return true;
                            case R.id.item_edit:
                                viewModel.getEditCustomerPaymentsClickedSingleLiveEvent().setValue(customerPaymentInfoList.get(position));
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                menuHelper.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return customerPaymentInfoList == null ? 0 : customerPaymentInfoList.size();
    }

    public class CustomerPaymentInfoHolder extends RecyclerView.ViewHolder {
        private CustomerPaymentInfoAdapterItemBinding binding;

        public CustomerPaymentInfoHolder(CustomerPaymentInfoAdapterItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindCustomerPaymentInfo(CustomerPaymentInfo customerPaymentInfo) {
            binding.txtBankAccountName.setText(customerPaymentInfo.getBankAccountName());
            binding.txtBankAccountNo.setText(customerPaymentInfo.getBankAccountNO());
            binding.txtBankName.setText(customerPaymentInfo.getBankName());
        }
    }
}
