package jaquelinefuertes.permisos.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import jaquelinefuertes.permisos.MainActivity;
import jaquelinefuertes.permisos.R;
import jaquelinefuertes.permisos.databinding.ActivityMainBinding;
import jaquelinefuertes.permisos.databinding.ActivityPhoneBinding;

public class PhoneActivity extends AppCompatActivity {
    ActivityPhoneBinding binding;
    private final int CALL_REQUEST = 123;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);
        binding = ActivityPhoneBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnActionCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.txtNumeroCall.getText().toString().isEmpty()){
                    Toast.makeText(PhoneActivity.this, "No hay telefono al que llamar", Toast.LENGTH_SHORT).show();
                }else{
                    if (checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED)
                    {
                        callAction();
                    }else{
                        requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, CALL_REQUEST);
                    }
                }
            }
        });
    }

    private void callAction() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:"+binding.txtNumeroCall.getText().toString()));
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CALL_REQUEST){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                callAction();
            }else{
                finish();
            }
        }
    }
}