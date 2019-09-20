package fitflex.com.fitflex.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import fitflex.com.fitflex.R;
import android.widget.*;

public class VisualizarTreinoA extends AppCompatActivity {

    private TextView nomeVideo ;
    private VideoView vv;
    private MediaController mc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_treino);

        nomeVideo = findViewById(R.id.txtNomeVideo);
        vv = findViewById(R.id.visualizarVideo);

        Intent intent = getIntent();

        String nomeExer = intent.getStringExtra("nome");
        String stringUri = intent.getStringExtra("uri");

        nomeVideo.setText(nomeExer);
        Uri uri = Uri.parse(stringUri);
        vv.setVideoURI(uri);
        vv.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mediaPlayer, int i, int i1) {
                        mc = new MediaController(VisualizarTreinoA.this);
                        vv.setMediaController(mc);
                        mc.setAnchorView(vv);
                    }
                });
            }
        });

        vv.start();


    }
}
