package com.example.sipsupporterapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sipsupporterapp.R;
import com.example.sipsupporterapp.databinding.ProductsAdapeterItemBinding;
import com.example.sipsupporterapp.model.CustomerProducts;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductsHolder> {
    private Context context;
    private List<CustomerProducts> customerProducts;

    public ProductsAdapter(Context context, List<CustomerProducts> customerProducts) {
        this.context = context;
        this.customerProducts = customerProducts;
    }

    @NonNull
    @Override
    public ProductsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductsHolder(DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.products_adapeter_item,
                parent,
                false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsHolder holder, int position) {
        holder.bindCustomerProducts(customerProducts.get(position));
    }

    @Override
    public int getItemCount() {
        return customerProducts.size();
    }

    public class ProductsHolder extends RecyclerView.ViewHolder {
        private ProductsAdapeterItemBinding binding;

        public ProductsHolder(ProductsAdapeterItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindCustomerProducts(CustomerProducts customerProducts) {
            binding.txtProductName.setText(customerProducts.getProductName());
            if (!customerProducts.getDescription().isEmpty()) {
                binding.txtDescription.setVisibility(View.VISIBLE);
                binding.txtDescription.setText(customerProducts.getDescription());
            }

            binding.txtUserName.setText(customerProducts.getUserFullName());

            if (customerProducts.isInvoicePayment()) {
                binding.imgIsPay.setVisibility(View.VISIBLE);
            } else {
                binding.imgIsPay.setVisibility(View.GONE);
            }

            String addTime = String.valueOf(customerProducts.getAddTime());
            String str = addTime.substring(0, 4) + "/" + addTime.substring(4, 6) + "/" + addTime.substring(6, 10) + ":" + addTime.substring(10, 12) + ":" + addTime.substring(12);
            binding.txtAddTime.setText(str);


            NumberFormat formatter = new DecimalFormat("#,###");
            String formattedNumber = formatter.format(customerProducts.getInvoicePrice());

            binding.txtInvoicePrice.setText(formattedNumber + "تومان");

            if (customerProducts.isFinish()) {
                binding.checkBoxFinish.setChecked(true);
            } else {
                binding.checkBoxFinish.setChecked(false);
            }

            if (customerProducts.isInvoicePayment()) {
                binding.checkBoxInvoicePayment.setChecked(true);
            } else {
                binding.checkBoxInvoicePayment.setChecked(false);
            }
        }
    }
}
