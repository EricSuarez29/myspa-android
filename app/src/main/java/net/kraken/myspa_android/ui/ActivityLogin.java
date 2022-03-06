package net.kraken.myspa_android.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.uttrat.myspa.model.Empleado;

import net.kraken.myspa_android.R;
import net.kraken.myspa_android.ui.commons.MySPACommons;
import java.util.HashMap;
import java.util.Map;

public class ActivityLogin extends AppCompatActivity {

    private EditText txtUsuario;
    private EditText txtContrasenia;
    private Button btnIngresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (Build.VERSION.SDK_INT >= 23)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET},1);
        else
            initComponents();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int [] grantResults){
        // Invocar al método de la super-clase
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        //Revisamos qye los resultados de los permisos no sean null:
        if (grantResults != null && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Este sería el equivalente alSystem.out.println
            Log.i("info", "Permisos Concedidos");

            initComponents();
        }
        else {
            mostrarMensaje("No ha concedido los permisos necesarios a la aplicación. Esta se cerrará");
            System.exit(0);
        }

    }

    private void initComponents()
    {
        txtUsuario = findViewById(R.id.txtUsuario);
        txtContrasenia = findViewById(R.id.txtContrasenia);
        btnIngresar = findViewById(R.id.btnIngresar);

        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validarUsuario();
            }
        });
    }

    private void validarUsuario()
    {
        final ActivityLogin activityLogin = this;
        String url = MySPACommons.MYSPA_SERVER + "/api/auth/login";
        RequestQueue rq = Volley.newRequestQueue(this);

        Response.Listener<String> rl = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JsonObject jso = JsonParser.parseString(response).getAsJsonObject();
                if (jso.get("error") != null){
                    mostrarMensaje(jso.get("error").getAsString());
                    return;
                }else{
                    // Generamos un Intent para navegar a la siguiente Activity:
                    Intent intent = new Intent(activityLogin,ActivityMain.class);

                    // Establecemos los datos que va a recibir el ActivityMain:
                    intent.putExtra("datosEmpleado", response);

                    // Cargamos el ActivityMain:
                    startActivity(intent);
                }
            }
        };

        Response.ErrorListener rel = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Imprimimos el error en la consola de Android Studio:
                error.printStackTrace();
                mostrarMensaje("Por el momento no se pudo validar su identidad. Intente más tarde");
            }
        };

        StringRequest sr = new StringRequest(Request.Method.POST, url, rl, rel){
            @Override
            public Map<String, String> getParams() throws AuthFailureError{
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("username", txtUsuario.getText().toString());
                params.put("password", txtContrasenia.getText().toString());
                return params;
            }

            @Override
            public String getBodyContentType(){
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }
        };
        rq.add(sr);
    }

    private void mostrarMensaje(String mensaje)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(mensaje)
                .setTitle("");
        AlertDialog alert = builder.create();
        alert.show();
    }

}