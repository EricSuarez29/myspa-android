package net.kraken.myspa_android.ui.clients;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ClientsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ClientsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Modulo de Clientes");
    }

    public LiveData<String> getText() {
        return mText;
    }
}