package org.tensorflow.lite.examples.detection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    EditText et_nip,et_password;
    Button btn_login;

    SharedPreferences check_login;
    SharedPreferences.Editor editor_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        et_nip = findViewById(R.id.et_nip);
        et_password = findViewById(R.id.et_password);
        btn_login = findViewById(R.id.btn_login);

        check_login = getSharedPreferences("login",MODE_PRIVATE);
        editor_login = check_login.edit();

        if (check_login.contains("login")) {
            Intent intent= new Intent(LoginActivity.this,HomeActivity.class);
            startActivity(intent);
            finish();
            Toast.makeText(this, check_login.getString("nip",null), Toast.LENGTH_SHORT).show();
        }


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_nip.getText().toString().isEmpty()){
                    Toast.makeText(LoginActivity.this, "Silakan isi nip", Toast.LENGTH_SHORT).show();
                }else if(et_password.getText().toString().isEmpty()){
                    Toast.makeText(LoginActivity.this, "Silakan isi password", Toast.LENGTH_SHORT).show();
                }else {
                    prosesLogin(et_nip.getText().toString(), et_password.getText().toString());
                }
            }
        });



    }

    private void prosesLogin(String nip, String password) {


            String url = "http://192.168.43.161/report/login.php";
            StringRequest respon = new StringRequest(
                    Request.Method.POST,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String status = jsonObject.getString("response");
                                if (status.equals("success")) {
                                    movetoHome(nip);
                                } else if (status.equals("failed")) {
                                    Toast.makeText(getApplicationContext(), "Gagal Melakukan Login", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
            )
            {

                protected Map<String, String> getParams() {
                    Map<String, String> kirim_isi = new HashMap<String, String>();
                    kirim_isi.put("nip", nip);
                    kirim_isi.put("pw", password);

                    return kirim_isi;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(respon);



    }

    private void movetoHome(String nip) {

        editor_login.putString("nip",nip);
        editor_login.putString("login","login");

        editor_login.commit();

        Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
        startActivity(intent);
        Toast.makeText(getApplicationContext(), "Berhasil Login", Toast.LENGTH_SHORT).show();
        finish();

    }


}