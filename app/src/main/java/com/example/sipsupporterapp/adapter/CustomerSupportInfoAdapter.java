package com.example.sipsupporterapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sipsupporterapp.R;
import com.example.sipsupporterapp.databinding.CustomerSupportInfoAdapterItemBinding;
import com.example.sipsupporterapp.model.CustomerSupportInfo;

import java.util.List;

public class CustomerSupportInfoAdapter extends RecyclerView.Adapter<CustomerSupportInfoAdapter.CustomerSupportInfoHolder> {
    private Context context;
    private List<CustomerSupportInfo> customerSupportInfoList;

    public CustomerSupportInfoAdapter(Context context, List<CustomerSupportInfo> customerSupportInfoList) {
        this.context = context;
        this.customerSupportInfoList = customerSupportInfoList;
    }

    @NonNull
    @Override
    public CustomerSupportInfoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CustomerSupportInfoHolder(DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.customer_support_info_adapter_item,
                parent,
                false));
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerSupportInfoHolder holder, int position) {
        holder.bindCustomerSupportInfo(customerSupportInfoList.get(position));
    }

    @Override
    public int getItemCount() {
        return customerSupportInfoList.size();
    }

    public class CustomerSupportInfoHolder extends RecyclerView.ViewHolder {
        private CustomerSupportInfoAdapterItemBinding binding;

        public CustomerSupportInfoHolder(CustomerSupportInfoAdapterItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindCustomerSupportInfo(CustomerSupportInfo customerSupportInfo) {
            binding.txtQuestion.setText(customerSupportInfo.getQuestion());
            binding.txtAnswer.setText(customerSupportInfo.getAnswer());

            String customerSupportID = String.valueOf(customerSupportInfo.getCustomerSupportID());
            binding.txtCustomerSupportID.setText(customerSupportID);

            binding.txtCustomerUserName.setText(customerSupportInfo.getCustomerUserName());
            binding.txtRegTime.setText(customerSupportInfo.getRegTime());
            binding.txtUserFullName.setText(customerSupportInfo.getUserFullName());
        }
    }
}
