package net.kraken.myspa_android.ui.employees;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EmployeesViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private MutableLiveData<String> mText;

    public EmployeesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Modulo de empleados");
    }

    public LiveData<String> getText() {
        return mText;
    }
}