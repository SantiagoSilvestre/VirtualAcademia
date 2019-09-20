package fitflex.com.fitflex.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import fitflex.com.fitflex.R;
import fitflex.com.fitflex.config.ConfiguracaoFirebase;
import fitflex.com.fitflex.model.Video;

import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.*;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class CadastrarExercicio extends AppCompatActivity {

    private FirebaseAuth autenticacao;
    private FirebaseAuth auth;
    private DatabaseReference firebase;
    private Video video;


    private static final int PICK_VIDEO_REQUEST = 3;
    private VideoView vv;
    private Uri videouri;
    private MediaController mc;
    private String videoName;

    private StorageReference mStorageRef;
    private StorageReference riversRef;

    private EditText nomeExercicio;
    private EditText categoria;
    private EditText descricao;
    private EditText dicas;
    private Button btn_foto;
    private Button btn_salvar;
    private Button btn_cancelar;
    private String treino;
    private ProgressBar progressBar;

    private RadioGroup radioGroup;
    private RadioButton radioEscolhido;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_exercicio);

        progressBar = findViewById(R.id.progVideo);
        nomeExercicio = findViewById(R.id.nome_exercicio);
        descricao = findViewById(R.id.nome_descricao);
        dicas = findViewById(R.id.nome_dicas);
        btn_foto = findViewById(R.id.btn_camera);
        btn_salvar = findViewById(R.id.btn_salvarExercicio);
        btn_cancelar = findViewById(R.id.btn_cancelarExercicio);
        vv = findViewById(R.id.video_exercicio);

        mStorageRef = FirebaseStorage.getInstance().getReference("uploads").child("videos");

        vv.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mediaPlayer, int i, int i1) {
                        mc = new MediaController(CadastrarExercicio.this);
                        vv.setMediaController(mc);
                        mc.setAnchorView(vv);
                    }
                });
            }
        });

        vv.start();


        radioGroup = findViewById(R.id.gropTreino);


        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelarEnvio();
            }
        });

        btn_salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                includesForCreateReference();
            }
        });

    }

    public void cancelarEnvio() {
        Intent intent = new Intent(CadastrarExercicio.this, MainActivity.class);
        startActivity(intent);
    }

    public void salvar(String string) {

        //autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        auth = ConfiguracaoFirebase.getFirebaseAutenticacao();
        if (auth.getCurrentUser() != null) {


            try {
                int idRadioEscolhido = radioGroup.getCheckedRadioButtonId();
                if( idRadioEscolhido > 0 ) {
                    radioEscolhido = findViewById( idRadioEscolhido);
                    treino = radioEscolhido.getText().toString();
                    video.setCategoria(treino);
                    video.salvar(string);
                    Toast.makeText(this, "Adicionado com sucesso", Toast.LENGTH_SHORT).show();
                } else {
                    video.setCategoria("outros");
                    video.salvar(string);
                    Toast.makeText(this, "Adicionado com sucesso", Toast.LENGTH_SHORT).show();
                }

            } catch ( Exception e ){
                e.printStackTrace();
            }




        }



    }

    public void includesForCreateReference() {

        video = new Video();
        video.setNome(nomeExercicio.getText().toString());
        video.setDescricao(descricao.getText().toString());
        video.setDicas(dicas.getText().toString());
        videoName = nomeExercicio.getText().toString();
        //

        if (video.getNome().equals("")) {
            Toast.makeText(CadastrarExercicio.this, "O nome do exercício não pode ser vazio", Toast.LENGTH_SHORT).show();
        } else if (video.getDescricao().equals("")) {
            Toast.makeText(CadastrarExercicio.this, "O campo da descrição não pode ser vazio", Toast.LENGTH_SHORT).show();
        } else {

            final StorageReference riversRef = mStorageRef.child(videoName);

            riversRef.putFile(videouri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get a URL to the uploaded content
                            //Uri downloadUrl = taskSnapshot.getDownloadUri();
                            if (riversRef == null ){

                            } else {
                                riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        //Log.i("Tuts+", "uri: " + uri.toString());

                                        String nomeUri = uri.toString();
                                        String filename = UUID.randomUUID().toString();
                                        video.setUri(nomeUri);
                                        salvar(filename);
                                        finish();

                                    }
                                });
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(CadastrarExercicio.this, exception.toString(), Toast.LENGTH_SHORT).show();
                        }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                    progressBar.setProgress((int) progress);

                }
            });
        }

    }

    public void videoUpload(View view){

        Intent i = new Intent();
        i.setType("video/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Select a Video"), PICK_VIDEO_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_VIDEO_REQUEST && resultCode == RESULT_OK && data != null) {
            videouri = data.getData();
            vv.setVideoURI(videouri);
            videoName = getFileName(videouri);
        }

    }

    public String getFileName(Uri uri){

        String result = null;

        if ( uri.getScheme().equals("content") ) {
            Cursor cursor = getContentResolver().query(uri, null, null,null, null, null);
            try {
                if ( cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if ( result == null ) {
            result =  uri.getPath();
            int cut = result.lastIndexOf('/');
            if ( cut != -1 ) {
                result = result.substring(cut + 1);
            }
        }

        return result;

    }

}



