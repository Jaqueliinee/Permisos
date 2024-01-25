package jaquelinefuertes.permisos.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import jaquelinefuertes.permisos.R;
import jaquelinefuertes.permisos.databinding.ActivityCamaraBinding;

public class CamaraActivity extends AppCompatActivity {
    private final int CAMARA_MINIATURA =1;
    private final int CAMARA_GALERIA =2;
    private final int CAMARA_FULL =3;
    private ActivityCamaraBinding binding;
    private ActivityResultLauncher<Intent> launcherMiniatura;
    private ActivityResultLauncher<Intent> launcherGaleria;
    private ActivityResultLauncher<Intent> launcherFull;
    private String imgPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCamaraBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnMiniaturaCamara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                    cameraMiniaturaAction();
                }else{
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMARA_MINIATURA);
                }
            }
        });

        binding.btnGaleriaCamara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                    galeriaAction();
                }else{
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CAMARA_GALERIA);
                }
            }
        });
        
        binding.btnFicheroCamara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                    camaraFullAction();
                }else{
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA}, 
                            CAMARA_FULL);
                }
            }
        });
        
        launcherMiniatura = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult o) {
                if (o.getResultCode() == RESULT_OK && o.getData()!= null){
                    Bitmap imagenBMP = (Bitmap) o.getData().getExtras().get("data");
                    binding.imgFotoCamara.setImageBitmap(imagenBMP);
                }
            }
        });
        launcherGaleria = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult o) {
                if (o.getResultCode() == RESULT_OK){
                    Uri imagenSeleccionada = o.getData().getData();
                    binding.imgFotoCamara.setImageURI(imagenSeleccionada);
                }
            }
        });
        launcherFull = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult o) {
                if (o.getResultCode() == RESULT_OK){
                    Uri uriImagen = Uri.parse(imgPath);
                    binding.imgFotoCamara.setImageURI(uriImagen);
                }
            }
        });
    }

    private void camaraFullAction() {
        try {
            File ficheroImg = crearFichero();
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Uri uriImagen = FileProvider.getUriForFile(this, "jaquelinefuertes.permisos", ficheroImg);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uriImagen);
            launcherFull.launch(intent);
        }catch (IOException e){
            e.printStackTrace();
            Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show();
        }
    }

    private File crearFichero() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd.HHmmss").format(new Date());
        String fileName = "IMG_" + timeStamp + "_";
        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imagen = File.createTempFile(fileName, ".jpg", dir);
        imgPath = imagen.getAbsolutePath();
        return imagen;
    }

    private void galeriaAction() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        launcherGaleria.launch(intent);
    }

    private void cameraMiniaturaAction() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        launcherMiniatura.launch(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMARA_MINIATURA){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                cameraMiniaturaAction();
            }else{
                finish();
            }
        }

        if (requestCode == CAMARA_GALERIA){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                galeriaAction();
            }else{
                finish();
            }
        }

        if (requestCode == CAMARA_FULL){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                camaraFullAction();
            }else{
                finish();
            }
        }
    }
}