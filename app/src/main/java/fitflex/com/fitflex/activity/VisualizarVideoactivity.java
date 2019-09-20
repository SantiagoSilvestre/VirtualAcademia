package fitflex.com.fitflex.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import fitflex.com.fitflex.R;
import fitflex.com.fitflex.config.ConfiguracaoFirebase;

import android.util.Log;
import android.view.View;
import android.widget.*;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class VisualizarVideoactivity extends AppCompatActivity {


    private EditText nomeVideo;
    private VideoView vv;
    private MediaController mc;
    private StorageReference storage;
    private String nome;
    private String filename;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_videoactivity);


        nomeVideo = findViewById(R.id.visNomeV);
        vv = findViewById(R.id.assistirTreino);
        reference = ConfiguracaoFirebase.getFirebase();

        Intent intent = getIntent();

        nome = intent.getStringExtra("nome");
        String nomeUri = intent.getStringExtra("uri");
        filename = intent.getStringExtra("filename");
        Log.i("testefile", filename);

        Uri uri = Uri.parse(nomeUri);
        nomeVideo.setText(nome);

        vv.setVideoURI(uri);
        vv.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mediaPlayer, int i, int i1) {
                        mc = new MediaController(VisualizarVideoactivity.this);
                        vv.setMediaController(mc);
                        mc.setAnchorView(vv);
                    }
                });
            }
        });

        vv.start();










    }



}
