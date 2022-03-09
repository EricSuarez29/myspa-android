package net.kraken.myspa_android.ui.salas;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.kraken.myspa_android.R;

public class ViewHolderSala extends RecyclerView.ViewHolder {

    protected ImageView imgFoto;
    protected TextView lblNombre, lblDescripcion;

    public ViewHolderSala(@NonNull View itemView) {
        super(itemView);
        imgFoto = itemView.findViewById(R.id.imgFotoSala);
        lblNombre = itemView.findViewById(R.id.txtTitleSala);
        lblDescripcion = itemView.findViewById(R.id.txtDescripcionSala);

    }
}
