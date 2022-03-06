package net.kraken.myspa_android.ui.employees;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.kraken.myspa_android.R;
import net.kraken.myspa_android.databinding.ClientsFragmentBinding;
import net.kraken.myspa_android.databinding.EmployeesFragmentBinding;
import net.kraken.myspa_android.ui.clients.ClientsViewModel;

public class EmployeesFragment extends Fragment {

    private EmployeesViewModel clientsViewModel;
    private EmployeesFragmentBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        clientsViewModel =
                new ViewModelProvider(this).get(EmployeesViewModel.class);

        binding = EmployeesFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textEmployees;
        clientsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}