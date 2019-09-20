package fitflex.com.fitflex.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import fitflex.com.fitflex.R;
import fitflex.com.fitflex.adapter.MensagemAdapter;
import fitflex.com.fitflex.config.ConfiguracaoFirebase;
import fitflex.com.fitflex.helper.Base64Custom;
import fitflex.com.fitflex.helper.Preferencias;
import fitflex.com.fitflex.model.Mensagem;

import androidx.appcompat.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ConversaGerencia extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText editNomeMessage;
    private ImageView btnEnviarMessage;
    private DatabaseReference firebase;
    private ListView lista_conversa;
    private ArrayList<Mensagem> mensagensLista;
    private ArrayAdapter<Mensagem> adapter;

    private ValueEventListener eventListener;
    private String idUsarioRemetente ;
    private String idDestinatario ;

    private String nomeUsuarioDestinatario;
    private String emailDestinatario;

    public ConversaGerencia() {
        //required
    }


    @Override
    public void onStop() {
        super.onStop();

        firebase.removeEventListener(eventListener);

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversa_gerencia);

        toolbar = findViewById(R.id.tb_conversa);
        Intent intent = getIntent();


        if ( intent != null ) {
            nomeUsuarioDestinatario = intent.getStringExtra("nome");
            emailDestinatario = intent.getStringExtra("email");
            idDestinatario = emailDestinatario;
            Log.i("testeDestinatario", emailDestinatario);
            Log.i("testeNomeDesti", nomeUsuarioDestinatario);
        }
        //Configura a toolbar
        toolbar.setTitle(nomeUsuarioDestinatario);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);


        editNomeMessage = findViewById(R.id.editMensagem);
        btnEnviarMessage = findViewById(R.id.btnEnviar);


        Preferencias preferencias = new Preferencias(ConversaGerencia.this);
        idUsarioRemetente = preferencias.getIdentificador();


        mensagensLista = new ArrayList<>();
        lista_conversa = findViewById(R.id.lv_conversa);
        adapter = new MensagemAdapter(ConversaGerencia.this, mensagensLista);

        lista_conversa.setAdapter( adapter );

        //Recuperar mensagens do Firebase
        firebase = ConfiguracaoFirebase.getFirebase()
                .child("mensagens")
                .child(idUsarioRemetente)
                .child(idDestinatario);

        //Criar o listener para mensagens
        eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mensagensLista.clear();

                for (DataSnapshot dados: dataSnapshot.getChildren()) {

                    Mensagem msg = dados.getValue(Mensagem.class);
                    mensagensLista.add(msg);
                }

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        firebase.addValueEventListener( eventListener );

        btnEnviarMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String mensagem = editNomeMessage.getText().toString();

                if ( mensagem.isEmpty() ){
                    Toast.makeText(ConversaGerencia.this, "Digite uma mensagem para enviar", Toast.LENGTH_SHORT).show();
                } else {

                    Mensagem message =  new Mensagem();
                    message.setIdUsuario(idUsarioRemetente);
                    message.setMensagem( mensagem );

                    salvarMensagem(idUsarioRemetente, idDestinatario , message);
                    salvarMensagem(idDestinatario, idUsarioRemetente , message);


                }
                editNomeMessage.setText("");

            }
        });

    }
    private boolean salvarMensagem( String idUsarioRemetente, String idDestinatario, Mensagem message ) {
        try {

            firebase = ConfiguracaoFirebase.getFirebase().child("mensagens");

            firebase.child(idUsarioRemetente)
                    .child(idDestinatario)
                    .push()
                    .setValue(message);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }


}
