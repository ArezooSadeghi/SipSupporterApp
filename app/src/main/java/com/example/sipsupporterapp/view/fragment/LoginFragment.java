package com.example.sipsupporterapp.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.sipsupporterapp.R;
import com.example.sipsupporterapp.databinding.FragmentLoginBinding;
import com.example.sipsupporterapp.model.ServerData;
import com.example.sipsupporterapp.model.UserInfo;
import com.example.sipsupporterapp.model.UserLoginParameter;
import com.example.sipsupporterapp.model.UserResult;
import com.example.sipsupporterapp.utils.SipSupportSharedPreferences;
import com.example.sipsupporterapp.view.activity.CustomerContainerActivity;
import com.example.sipsupporterapp.view.activity.LoginContainerActivity;
import com.example.sipsupporterapp.viewmodel.SharedLoginAndAddAndEditIPAddressDialogAndIPAddressListDialogViewModel;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.List;

public class LoginFragment extends Fragment {
    private FragmentLoginBinding binding;
    private String value;
    private IPAddressListDialogFragment fragment;
    private LoginWaitingFragment loginWaitingFragment;
    private SharedLoginAndAddAndEditIPAddressDialogAndIPAddressListDialogViewModel viewModel;

    private static final String TAG = LoginFragment.class.getSimpleName();

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(SharedLoginAndAddAndEditIPAddressDialogAndIPAddressListDialogViewModel.class);

        if (viewModel.getServerDataList().size() == 0) {
            EnterIPAddressDialogFragment fragment = EnterIPAddressDialogFragment.newInstance();
            fragment.show(getChildFragmentManager(), EnterIPAddressDialogFragment.TAG);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_login,
                container,
                false);


        if (SipSupportSharedPreferences.getUserName(getContext()) != null) {
            binding.edTextUserName.setText(SipSupportSharedPreferences.getUserName(getContext()));
            binding.edTextUserName.setSelection(binding.edTextUserName.getText().toString().length());
        }


        if (viewModel.getServerDataList().size() != 0) {
            setupSpinner();
        }

        setListener();
        setItemSelected();

        return binding.getRoot();
    }


    private void setItemSelected() {
        binding.spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                value = (String) item;
                SipSupportSharedPreferences.setLastValueSpinner(getContext(), value);
            }
        });
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setObserver();
    }


    private void setListener() {
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = binding.edTextUserName.getText().toString();
                String password = binding.edTextPassword.getText().toString();

                UserLoginParameter userLoginParameter = new UserLoginParameter(userName, password);

                if (value != null) {
                    ServerData serverData = viewModel.getServerData(value);
                    viewModel.getSipSupportServicePostUserLoginParameter(serverData.getIpAddress() + ":" + serverData.getPort());
                    viewModel.fetchUserResult(userLoginParameter);
                    binding.progressBar.setVisibility(View.VISIBLE);
                } else {
                    EnterIPAddressDialogFragment fragment = EnterIPAddressDialogFragment.newInstance();
                    fragment.show(getParentFragmentManager(), EnterIPAddressDialogFragment.TAG);
                }
            }
        });

        binding.imgMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = IPAddressListDialogFragment.newInstance();
                fragment.show(getParentFragmentManager(), IPAddressListDialogFragment.TAG);
            }
        });
    }


    private void setObserver() {
        viewModel.getUserResultSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<UserResult>() {
            @Override
            public void onChanged(UserResult userResult) {
                SipSupportSharedPreferences.setUserName(getContext(), binding.edTextUserName.getText().toString());
                binding.progressBar.setVisibility(View.GONE);
                UserInfo[] userInfoArray = userResult.getUsers();
                if (userInfoArray.length != 0) {
                    SipSupportSharedPreferences.setUserLoginKey(getContext(), userInfoArray[0].getUserLoginKey());
                    SipSupportSharedPreferences.setUserFullName(getContext(), userInfoArray[0].getUserFullName());

                    Intent intent = CustomerContainerActivity.newIntent(getContext());
                    startActivity(intent);
                    getActivity().finish();
                }
            }
        });

        viewModel.getInsertSpinnerSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isInsert) {
                setupSpinner();
            }
        });

        viewModel.getDeleteSpinnerSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<ServerData>() {
            @Override
            public void onChanged(ServerData serverData) {
                setupSpinner();
                if (viewModel.getServerDataList().size() == 0) {
                    fragment.dismiss();
                    EnterIPAddressDialogFragment fragment = EnterIPAddressDialogFragment.newInstance();
                    fragment.show(getChildFragmentManager(), EnterIPAddressDialogFragment.TAG);
                }
            }
        });

        viewModel.getUpdateSpinnerSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isUpdate) {
                setupSpinner();
            }
        });

        viewModel.getWrongIpAddressSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String error) {
                binding.progressBar.setVisibility(View.GONE);
                ErrorDialogFragment fragment = ErrorDialogFragment.newInstance(error);
                fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
            }
        });

        viewModel.getErrorUserResult().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String errorUserResult) {
                binding.progressBar.setVisibility(View.GONE);
                ErrorDialogFragment fragment = ErrorDialogFragment.newInstance(errorUserResult);
                fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
            }
        });

        viewModel.getNoConnection().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String noConnectionMessage) {
                binding.progressBar.setVisibility(View.GONE);
                ErrorDialogFragment fragment = ErrorDialogFragment.newInstance(noConnectionMessage);
                fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
            }
        });

        viewModel.getDangerousUserSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                SipSupportSharedPreferences.setUserLoginKey(getContext(), null);
                SipSupportSharedPreferences.setUserFullName(getContext(), null);
                SipSupportSharedPreferences.setLastValueSpinner(getContext(), null);
                SipSupportSharedPreferences.setLastSearchQuery(getContext(), null);
                SipSupportSharedPreferences.setCustomerUserId(getContext(), -1);
                SipSupportSharedPreferences.setCustomerName(getContext(), null);
                Intent intent = LoginContainerActivity.newIntent(getContext());
                startActivity(intent);
                getActivity().finish();
            }
        });

        viewModel.getTimeoutExceptionHappenSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                ErrorDialogFragment fragment = ErrorDialogFragment.newInstance("اتصال به اینترنت با خطا مواجه شد");
                fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
            }
        });

    }


    private void setupSpinner() {
        String valueNew = SipSupportSharedPreferences.getLastValueSpinner(getContext());
        List<ServerData> serverDataList = viewModel.getServerDataList();
        List<String> centerNames = new ArrayList<>();
        for (ServerData serverData : serverDataList) {
            centerNames.add(serverData.getCenterName());
        }

        List<String> newCenterNames = new ArrayList<>();

        if (valueNew != null) {
            for (String str : centerNames) {
                if (!str.equals(valueNew)) {
                    newCenterNames.add(str);
                }
            }
            newCenterNames.add(0, valueNew);
            binding.spinner.setItems(newCenterNames);
        } else {
            binding.spinner.setItems(centerNames);
        }


        if (binding.spinner.getItems().size() > 0) {
            value = (String) binding.spinner.getItems().get(0);
            SipSupportSharedPreferences.setLastValueSpinner(getContext(), value);
        } else {
            value = null;
            SipSupportSharedPreferences.setLastValueSpinner(getContext(), null);
        }
    }
}