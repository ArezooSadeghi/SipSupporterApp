package com.example.sipsupporterapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sipsupporterapp.R;
import com.example.sipsupporterapp.databinding.CustomerAdapterItemBinding;
import com.example.sipsupporterapp.model.CustomerInfo;
import com.example.sipsupporterapp.utils.SipSupportSharedPreferences;
import com.example.sipsupporterapp.viewmodel.SharedCenterNameDialogAndCustomerViewModel;

import java.util.List;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.CustomerHolder> {
    private Context context;
    private List<CustomerInfo> customerInfoList;
    private SharedCenterNameDialogAndCustomerViewModel viewModel;
    private String date;

    public CustomerAdapter(Context context, List<CustomerInfo> customerInfoList, SharedCenterNameDialogAndCustomerViewModel viewModel, String date) {
        this.context = context;
        this.customerInfoList = customerInfoList;
        this.viewModel = viewModel;
        this.date = date;
    }

    @NonNull
    @Override
    public CustomerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CustomerHolder(DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.customer_adapter_item,
                parent,
                false));
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerHolder holder, int position) {
        CustomerInfo customerInfo = customerInfoList.get(position);
        holder.bindCustomInfo(customerInfo);
        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.getItemClickedSingleLiveEvent().setValue(customerInfo.getCustomerID());
                SipSupportSharedPreferences.setCustomerName(context, customerInfo.getCustomerName());
                SipSupportSharedPreferences.setCustomerTel(context, customerInfo.getTel());
            }
        });

        String date = customerInfo.getLastSeen().substring(0, 10);

        if (date != null && this.date != null && this.date.equals(date)) {
            holder.binding.frameLayout.setBackgroundColor(Color.parseColor("#FAFD0F"));
            holder.binding.txtLastSeen.setTextColor(Color.parseColor("#000000"));
        } else {
            holder.binding.frameLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
            holder.binding.txtCustomerName.setTextColor(Color.parseColor("#696969"));
            holder.binding.txtLastSeen.setTextColor(Color.parseColor("#808080"));
        }
    }

    @Override
    public int getItemCount() {
        return customerInfoList.size();
    }


    public class CustomerHolder extends RecyclerView.ViewHolder {
        private CustomerAdapterItemBinding binding;

        public CustomerHolder(CustomerAdapterItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindCustomInfo(CustomerInfo customerInfo) {
            binding.txtCity.setText(customerInfo.getCity());
            binding.txtLastSeen.setText(customerInfo.getLastSeen());

            String output = replace(customerInfo);
            binding.txtCustomerName.setText(output);
            binding.txtCustomerID.setText(String.valueOf(customerInfo.getCustomerID()));
        }

        private String replace(CustomerInfo customerInfo) {
            String input = customerInfo.getCustomerName();
            String output = "";
            for (int i = 0; i < input.length(); i++) {
                if (input.charAt(i) == 'ي') {
                    output += 'ی';
                } else {
                    output += input.charAt(i);
                }
            }
            return output;
        }
    }
}

