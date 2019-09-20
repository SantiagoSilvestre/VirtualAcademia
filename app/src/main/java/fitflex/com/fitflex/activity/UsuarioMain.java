package fitflex.com.fitflex.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import fitflex.com.fitflex.R;
import fitflex.com.fitflex.config.ConfiguracaoFirebase;
import fitflex.com.fitflex.helper.Preferencias;
import fitflex.com.fitflex.model.Usuario;
import fitflex.com.fitflex.ui.main.SectionsPagerAdapter;
import fitflex.com.fitflex.ui.main.UsuarioSectionsAdapter;

import android.provider.OpenableColumns;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

public class UsuarioMain extends AppCompatActivity {

    private VideoView videoR;
    private FirebaseAuth usuarioAutenticacao;
    private Toolbar toolbar;
    private StorageReference mStorageRef;
    private Uri videouri;
    private MediaController mc;
    private String videoName;
    private TextView localparaacao;

    private DatabaseReference firebase;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_main);

        toolbar = findViewById(R.id.toolbar_principal);
        toolbar.setTitle("AcademiaVirtual");
        setSupportActionBar(toolbar);

        UsuarioSectionsAdapter sectionsPagerAdapter =
                new UsuarioSectionsAdapter(this, getSupportFragmentManager());

        ViewPager viewPager = findViewById(R.id.page);
        viewPager.setAdapter(sectionsPagerAdapter);



        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);


    }

    public void deslogarUsuario() {
        usuarioAutenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        usuarioAutenticacao.signOut();
        Intent intent = new Intent( UsuarioMain.this, LoginActivity.class);
        //Preferencias preferencias = new Preferencias(this);
        //preferencias.salvarDados("", "");
        startActivity(intent);
        finish();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch ( item.getItemId() ) {
            case R.id.item_sair:
                deslogarUsuario();
                return true;

            case R.id.item_mensagens:
                abrirTreinoOutros();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }
    @Override
    public void onBackPressed(){ //Botão BACK padrão do android         //startActivity(new Intent(this, MainActivity.class)); //O efeito ao ser pressionado do botão (no caso abre a activity)
        finish(); //Método para matar a activity e não deixa-lá indexada na pilhagem
        return;
    }


    public void abrirTreinoOutros(){
        Intent intent = new Intent(UsuarioMain.this, ConversaUsuario.class);
        startActivity(intent);
    }



}
