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
import com.example.sipsupporterapp.databinding.CustomerSupportInfoAdapterItemBinding;
import com.example.sipsupporterapp.model.CustomerSupportInfo;
import com.example.sipsupporterapp.utils.Converter;
import com.example.sipsupporterapp.viewmodel.SupportHistoryViewModel;

import java.util.List;

public class CustomerSupportInfoAdapter extends RecyclerView.Adapter<CustomerSupportInfoAdapter.CustomerSupportInfoHolder> {
    private Context context;
    private List<CustomerSupportInfo> customerSupportInfoList;
    private SupportHistoryViewModel viewModel;

    public CustomerSupportInfoAdapter(Context context, List<CustomerSupportInfo> customerSupportInfoList, SupportHistoryViewModel viewModel) {
        this.context = context;
        this.customerSupportInfoList = customerSupportInfoList;
        this.viewModel = viewModel;
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

    @SuppressLint("RestrictedApi")
    @Override
    public void onBindViewHolder(@NonNull CustomerSupportInfoHolder holder, int position) {
        holder.bindCustomerSupportInfo(customerSupportInfoList.get(position));


        holder.binding.imgBtnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu menu = new PopupMenu(context, holder.binding.imgBtnMore);
                menu.inflate(R.menu.customer_support_info_adapter_menu);

                @SuppressLint("RestrictedApi") MenuPopupHelper menuHelper = new MenuPopupHelper(context, (MenuBuilder) menu.getMenu(), holder.binding.imgBtnMore);
                menuHelper.setForceShowIcon(true);


                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.item_see_documents:
                                /*viewModel.getAttachFileClicked().setValue(customerSupportInfoList.get(position));*/
                                viewModel.getCustomerSupportInfoAdapterSeeDocumentClickedSingleLiveEvent().setValue(customerSupportInfoList.get(position));
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
        return customerSupportInfoList.size();
    }

    public class CustomerSupportInfoHolder extends RecyclerView.ViewHolder {
        private CustomerSupportInfoAdapterItemBinding binding;

        public CustomerSupportInfoHolder(CustomerSupportInfoAdapterItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindCustomerSupportInfo(CustomerSupportInfo customerSupportInfo) {
            String question = Converter.convert(customerSupportInfo.getQuestion());
            binding.txtQuestion.setText(question);
            String answer = Converter.convert(customerSupportInfo.getAnswer());
            binding.txtAnswer.setText(answer);

            String customerSupportID = String.valueOf(customerSupportInfo.getCustomerSupportID());
            binding.txtCustomerSupportID.setText(customerSupportID);

            binding.txtRegTime.setText(customerSupportInfo.getRegTime());

        }
    }
}
