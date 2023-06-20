package org.tensorflow.lite.examples.detection;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {

    CardView presensi,laporan;
    TextView nip;

    ImageView logout,detail;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        presensi = findViewById(R.id.absensi);
        laporan = findViewById(R.id.laporan);
        nip = findViewById(R.id.nip);

        logout = findViewById(R.id.logout_btn);
        detail = findViewById(R.id.edituser);


        SharedPreferences sharedPreferences = getSharedPreferences("absensi", Context.MODE_PRIVATE);
        String chekabsensi = sharedPreferences.getString("presensi", "");

        SharedPreferences login_check = getSharedPreferences("login", Context.MODE_PRIVATE);
        String checklogin  = login_check.getString("nip", "");

        SharedPreferences.Editor editor = login_check.edit();


        if (checklogin.isEmpty()){
            Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(intent);
            finish();
        }else {
            nip.setText(checklogin);
        }

        presensi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (chekabsensi.isEmpty()){
                    Intent intent = new Intent(getApplicationContext(),DetectorActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(HomeActivity.this, "Sudah Melakukan Absensi", Toast.LENGTH_SHORT).show();
                }


            }
        });



        laporan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (chekabsensi.isEmpty()){
                    Toast.makeText(HomeActivity.this, "Silakan Melakukan Absensi Terlebih Dahulu", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(getApplicationContext(),ReportActivity.class);
                    startActivity(intent);
                }

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.clear();
                editor.apply();
                finish();
                Toast.makeText(HomeActivity.this, "Berhasil Keluar", Toast.LENGTH_SHORT).show();
            }
        });

    }
}