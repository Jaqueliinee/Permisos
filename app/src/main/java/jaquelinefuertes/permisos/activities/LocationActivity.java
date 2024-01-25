package jaquelinefuertes.permisos.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;


import java.io.IOException;
import java.util.List;
import java.util.Locale;

import jaquelinefuertes.permisos.R;
import jaquelinefuertes.permisos.databinding.ActivityLocationBinding;

public class LocationActivity extends AppCompatActivity {

    private ActivityLocationBinding binding;
    private final int LOCATION= 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLocationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonnGetLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                    getLocationAction();
                }else{
                String[] permisos = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
                requestPermissions(permisos, LOCATION);
                }
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void getLocationAction() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (location == null){
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        if (location != null){
            binding.lblCoordenadas.setText("Longitud: "+location.getLongitude()+ "Latitud: "+ location.getLatitude());
            if (location.getLatitude() != 0 && location.getLongitude() != 0){
                Geocoder geo = new Geocoder(this, Locale.getDefault());
                try{
                    List<Address> direcciones = geo.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    if (!direcciones.isEmpty()){
                        Address direccion = direcciones.get(0);
                        binding.lblCoordenadas.setText(direccion.getAddressLine(0));
                    }
                }catch (IOException e){
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == RESULT_OK){

        }
    }

}