package net.kraken.myspa_android.ui.salas;

import android.hardware.lights.LightState;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.uttrat.myspa.model.Sala;
import com.uttrat.myspa.model.Sucursal;

import net.kraken.myspa_android.R;
import net.kraken.myspa_android.ui.ActivityMain;
import net.kraken.myspa_android.ui.commons.MySPACommons;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SalasFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SalasFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ActivityMain activityMain;

    Spinner cmbSucursales;
    RecyclerView rclvSalas;

    List<Sucursal> sucursales;
    List<Sala> salas;

    public SalasFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentSalas.
     */
    // TODO: Rename and change types and number of parameters
    public static SalasFragment newInstance(String param1, String param2) {
        SalasFragment fragment = new SalasFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_salas, container, false);

        activityMain = (ActivityMain) getActivity();

        cmbSucursales = v.findViewById(R.id.cmbSucursales);
        rclvSalas = v.findViewById(R.id.rclvSalas);

        rclvSalas.setLayoutManager(new LinearLayoutManager(activityMain.getApplicationContext()));

        cmbSucursales.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                consultarSalas(cmbSucursales.getSelectedItemPosition());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        consultarSucursales();
        return v;
    }

    private void consultarSucursales()
    {
        String url = MySPACommons.MYSPA_SERVER + "/api/sucursal?token=" + activityMain.getEmpleado().getUsuario().getToken();
        RequestQueue rq = Volley.newRequestQueue(getActivity().getApplicationContext());

        Response.Listener<String> rl = new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                JsonObject jso = null;
                try
                {
                    jso = JsonParser.parseString(response).getAsJsonObject();
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                    Log.i("info", "No pasa nada, excepción controlada");
                }

                if (jso != null && jso.get("error") != null)
                {
                    activityMain.mostrarMensaje("", jso.get("error").getAsString());
                    return;
                }
                else
                {
                    ArrayAdapter<String> adapter = null;
                    List<String> nombres = new ArrayList<>(); // Aquí guardamos los nombres de las sucursales.
                    TypeToken<List<Sucursal>> tt = new TypeToken<List<Sucursal>>(){};

                    sucursales = new Gson().fromJson(response, tt.getType());

                    // Llenamos la lista de nombres de sucursales:
                    for (Sucursal s : sucursales)
                        nombres.add(s.getNombre());

                    adapter = new ArrayAdapter<String>(activityMain,
                            android.R.layout.simple_spinner_item,
                            nombres);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    cmbSucursales.setAdapter(adapter);
                }
            }
        };

        Response.ErrorListener rel = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Imprimimos el error en la consola de Android Studio:
                error.printStackTrace();
                activityMain.mostrarMensaje("", "No se pudo obtener información de las sucursales en este momento. Intente nuevamente.");
            }
        };

        StringRequest sr = new StringRequest(Request.Method.GET, url, rl, rel);

        rq.add(sr);
    }

    private void consultarSalas(int posicion)
    {
        String url = MySPACommons.MYSPA_SERVER + "/api/salas?token="+ activityMain.getEmpleado().getUsuario().getToken() +"&idSucursal=" + sucursales.get(posicion).getId();

        RequestQueue rq = Volley.newRequestQueue(getActivity().getApplicationContext());

        Response.Listener<String> rl = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("info", response);
                JsonObject json = null;
                try {
                    json = JsonParser.parseString(response).getAsJsonObject();
                }catch (Exception e){
                    e.printStackTrace();
                    Log.i("info","Exception controlada");
                }

                if(json != null && json.get("error") != null){
                    activityMain.mostrarMensaje("", json.get("error").getAsString());
                    return;
                } else {
                    AdapterSala adapter = null;
                    TypeToken<List<Sala>> typeToken = new TypeToken<List<Sala>>(){};
                    salas = new Gson().fromJson(response, typeToken.getType());

                    adapter = new AdapterSala(activityMain, salas);
                    rclvSalas.setAdapter(adapter);
                }
            }
        };

        Response.ErrorListener rel = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Imprimimos el error en la consola de Android Studio:
                error.printStackTrace();
                activityMain.mostrarMensaje("", "Ah error en el modulo de salas.");
            }
        };

        StringRequest sr = new StringRequest(Request.Method.GET, url, rl, rel);

        rq.add(sr);
    }
}