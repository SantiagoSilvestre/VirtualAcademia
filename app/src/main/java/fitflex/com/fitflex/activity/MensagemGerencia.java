package fitflex.com.fitflex.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import fitflex.com.fitflex.R;
import fitflex.com.fitflex.adapter.ContatosAdapter;
import fitflex.com.fitflex.adapter.GerenciaMensagensAdapter;
import fitflex.com.fitflex.config.ConfiguracaoFirebase;
import fitflex.com.fitflex.helper.Base64Custom;
import fitflex.com.fitflex.helper.Preferencias;
import fitflex.com.fitflex.model.Contato;
import fitflex.com.fitflex.model.Conversa;
import fitflex.com.fitflex.model.Mensagem;

public class MensagemGerencia extends AppCompatActivity {

    private Toolbar toolbar;
    private ListView listView;
    private ArrayAdapter adapter;
    private ArrayList<Conversa> conversas;
    private DatabaseReference firebase;
    private ValueEventListener valueEventListener;
    private FirebaseAuth usuarioAutenticacao;
    private Conversa cvs;

    public MensagemGerencia(){
        //Required
    }

    @Override
    public void onStart() {
        super.onStart();
        firebase.addValueEventListener( valueEventListener );

    }

    @Override
    public void onStop() {
        super.onStop();

        firebase.removeEventListener( valueEventListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensagem_gerencia);

        toolbar = findViewById(R.id.tb_menu);
        toolbar.setTitle("AcademiaVirtual");
        setSupportActionBar( toolbar );

        final String identificadorUsuarioLogado;
        final ArrayList<Contato> cont = new ArrayList<Contato>();

        conversas = new ArrayList<>();

        listView = findViewById(R.id.lv_mensagens_contatos);

        adapter = new GerenciaMensagensAdapter(MensagemGerencia.this, conversas);
        listView.setAdapter( adapter );

        //Recuperar contatos do firebase
        Preferencias preferencias = new Preferencias(MensagemGerencia.this);
        identificadorUsuarioLogado = preferencias.getIdentificador();
        //Recuperar conversas do Firebase
        firebase = ConfiguracaoFirebase.getFirebase().child("conversas")
                    .child("c2FuQGdtYWlsLmNvbQ==");

        //Listener para recuperar contatos
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {

                //Limpar a lista
                conversas.clear();

                //Listar Contatos
                for ( DataSnapshot dados: dataSnapshot.getChildren() ) {

                    Conversa cvs = dados.getValue( Conversa.class );
                    String email = cvs.getIdUsuario();
                    String nome = cvs.getNome();
                    conversas.add( cvs );

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {



                            if (MensagemGerencia.this != null) {
                                Conversa conversa = conversas.get((i));
                                Intent intent = new Intent(MensagemGerencia.this,ConversaGerencia.class);
                                intent.putExtra("nome", conversa.getNome());
                                intent.putExtra("email", conversa.getIdUsuario());

                                startActivity(intent);
                            }
                        }
                    });




                }
                adapter.notifyDataSetChanged();



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

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
        Intent intent = new Intent(MensagemGerencia.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void abrirContatosMensagens(){
        Intent intent = new Intent(MensagemGerencia.this, MensagemGerencia.class );
        startActivity(intent);
    }
}
