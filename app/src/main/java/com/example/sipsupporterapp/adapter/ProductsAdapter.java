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
import com.example.sipsupporterapp.databinding.ProductsAdapeterItemBinding;
import com.example.sipsupporterapp.model.CustomerProducts;
import com.example.sipsupporterapp.utils.Converter;
import com.example.sipsupporterapp.viewmodel.RegisterProductViewModel;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductsHolder> {
    private Context context;
    private List<CustomerProducts> customerProducts;
    private RegisterProductViewModel viewModel;

    public ProductsAdapter(Context context, List<CustomerProducts> customerProducts, RegisterProductViewModel viewModel) {
        this.context = context;
        this.customerProducts = customerProducts;
        this.viewModel = viewModel;
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

        int customerProductID = customerProducts.get(position).getCustomerProductID();

        holder.binding.imgMore.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                PopupMenu menu = new PopupMenu(context, holder.binding.imgMore);
                menu.inflate(R.menu.products_adapter_menu);

                @SuppressLint("RestrictedApi") MenuPopupHelper menuHelper = new MenuPopupHelper(context, (MenuBuilder) menu.getMenu(), holder.binding.imgMore);
                menuHelper.setForceShowIcon(true);

                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.item_edit:
                                viewModel.getEditClickedSingleLiveEvent().setValue(customerProducts.get(position));
                                return true;
                            case R.id.item_delete:
                                viewModel.getDeleteClickedSingleLiveEvent().setValue(customerProductID);
                                return true;
                            case R.id.item_see_documents:
                                viewModel.getProductAdapterSeeDocumentsClickedSingleLiveEvent().setValue(customerProducts.get(position));
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
        return customerProducts == null ? 0 : customerProducts.size();
    }

    public class ProductsHolder extends RecyclerView.ViewHolder {
        private ProductsAdapeterItemBinding binding;

        public ProductsHolder(ProductsAdapeterItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindCustomerProducts(CustomerProducts customerProducts) {
            String productName = Converter.convert(customerProducts.getProductName());
            binding.txtProductName.setText(productName);
            if (!customerProducts.getDescription().isEmpty()) {
                binding.txtDescription.setVisibility(View.VISIBLE);
                String description = Converter.convert(customerProducts.getDescription());
                binding.txtDescription.setText(description);
            }

            String userFullName = Converter.convert(customerProducts.getUserFullName());
            binding.txtUserName.setText(userFullName);

           /* String addTime = String.valueOf(customerProducts.getAddTime());
            String str = addTime.substring(0, 4) + "/" + addTime.substring(4, 6) + "/" + addTime.substring(6, 10) + ":" + addTime.substring(10, 12) + ":" + addTime.substring(12);
            binding.txtAddTime.setText(str);
*/

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
