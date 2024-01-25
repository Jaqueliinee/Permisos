package jaquelinefuertes.permisos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import jaquelinefuertes.permisos.activities.CamaraActivity;
import jaquelinefuertes.permisos.activities.LocationActivity;
import jaquelinefuertes.permisos.activities.PhoneActivity;
import jaquelinefuertes.permisos.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnPhoneMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, PhoneActivity.class));
            }
        });

        binding.btnCamaraMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CamaraActivity.class));
            }
        });
        binding.btnGPSMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LocationActivity.class));
            }
        });
    }
}