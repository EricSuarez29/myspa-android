package net.kraken.myspa_android.ui.salas;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.uttrat.myspa.model.Sala;

import net.kraken.myspa_android.R;
import net.kraken.myspa_android.ui.ActivityMain;
import net.kraken.myspa_android.ui.commons.MySPACommons;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AdapterSala extends RecyclerView.Adapter<ViewHolderSala> {
    ActivityMain activityMain;
    List<Sala> salas;

    public AdapterSala(ActivityMain activityMain, List<Sala> salas){
        this.activityMain = activityMain;
        this.salas = salas;
    }

    @NonNull
    @Override
    public ViewHolderSala onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_sala, parent, false);
        ViewHolderSala viewHolderSala = new ViewHolderSala(view);
        return viewHolderSala;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderSala holder, int position) {
        Sala sala = salas.get(position);

        holder.lblNombre.setText(sala.getNombre());
        holder.lblDescripcion.setText(sala.getDescripcion());

        try {
            if(sala.getFoto() != null && sala.getFoto().length() > 64)
                holder.imgFoto.setImageBitmap(MySPACommons.fromBase64(sala.getFoto()));
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return salas == null ? 0: salas.size();
    }
}
