package fitflex.com.fitflex.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import fitflex.com.fitflex.R;
import fitflex.com.fitflex.config.ConfiguracaoFirebase;
import fitflex.com.fitflex.helper.Preferencias;
import fitflex.com.fitflex.ui.main.SectionsPagerAdapter;

public class MainActivity extends AppCompatActivity   {
    private Toolbar toolbar;
    private FirebaseAuth usuarioAutenticacao;
    private DatabaseReference firebase;
    private String identificadorContato;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar_principal);
        toolbar.setTitle("AcademiaVirtual");
        setSupportActionBar( toolbar );


        SectionsPagerAdapter sectionsPagerAdapter =
                new SectionsPagerAdapter(this, getSupportFragmentManager());

        ViewPager viewPager = findViewById(R.id.page);
        viewPager.setAdapter(sectionsPagerAdapter);



        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);





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
                abrirContatosMensagens();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }


    private void deslogarUsuario() {
        usuarioAutenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        usuarioAutenticacao.signOut();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
    private void abrirContatosMensagens(){
        Intent intent = new Intent(MainActivity.this, MensagemGerencia.class );
        startActivity(intent);
    }
}