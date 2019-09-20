package fitflex.com.fitflex.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import fitflex.com.fitflex.R;
import fitflex.com.fitflex.adapter.MensagemAdapter;
import fitflex.com.fitflex.config.ConfiguracaoFirebase;
import fitflex.com.fitflex.helper.Preferencias;
import fitflex.com.fitflex.model.Conversa;
import fitflex.com.fitflex.model.Mensagem;

public class ConversaUsuario extends AppCompatActivity {

    private EditText editNomeMessage;
    private ImageView btnEnviarMessage;
    private DatabaseReference firebase;
    private ListView lista_conversa;
    private ArrayList<Mensagem> mensagensLista;
    private ArrayAdapter<Mensagem> adapter;
    private Toolbar toolbar;

    private ValueEventListener eventListener;

    //Dados do remetente
    private String idUsarioRemetente;
    private String idDestinatario = "c2FuQGdtYWlsLmNvbQ==";
    private String nomeRemetente;

    public ConversaUsuario(){
        //Required
    }

    @Override
    public void onStop() {
        super.onStop();

        firebase.removeEventListener(eventListener);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversa_usuario);

        toolbar = findViewById(R.id.tb_conversa);

        //Configurar a toolbar
        toolbar.setTitle("Personal");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);

        editNomeMessage = findViewById(R.id.editMensagem);
        btnEnviarMessage = findViewById(R.id.btnEnviar);
        lista_conversa = findViewById(R.id.list_conversa);

        Preferencias preferencias = new Preferencias(ConversaUsuario.this);
        idUsarioRemetente = preferencias.getIdentificador();
        nomeRemetente = preferencias.getNome();


        mensagensLista = new ArrayList<>();
        adapter = new MensagemAdapter(ConversaUsuario.this, mensagensLista);

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
                    Toast.makeText(ConversaUsuario.this, "Digite uma mensagem para enviar", Toast.LENGTH_SHORT).show();
                } else {

                    Mensagem message =  new Mensagem();
                    message.setIdUsuario(idUsarioRemetente);
                    message.setMensagem( mensagem );

                    //Salvamos mensagem para o remetente
                    Boolean retornoMensagemRementente = salvarMensagem(idUsarioRemetente, idDestinatario , message);

                    if ( !retornoMensagemRementente ) {
                        Toast.makeText(ConversaUsuario.this, "Problema ao salvar mensagem tente novamente", Toast.LENGTH_SHORT).show();
                    } else {
                        //Salvando mensagem para o destinatário
                        Boolean retornoMensagemDestinatario =  salvarMensagem(idDestinatario, idUsarioRemetente , message);
                        if ( !retornoMensagemDestinatario ) {
                            Toast.makeText(ConversaUsuario.this, "Problema ao salvar mensagem tente novamente", Toast.LENGTH_SHORT).show();
                        } else {

                        }

                    }

                    //Salvar conversa para o Destinário

                    Conversa conversa = new Conversa();
                    conversa.setIdUsuario(idUsarioRemetente);
                    conversa.setNome(nomeRemetente);
                    conversa.setMensagem(mensagem);

                    Boolean retornoConversaDestinatario = salvarConversa( idDestinatario, idUsarioRemetente, conversa );
                    if ( !retornoConversaDestinatario ) {
                        Toast.makeText(ConversaUsuario.this, "Erro ao salvar Conversa", Toast.LENGTH_SHORT).show();
                    }

                    editNomeMessage.setText("");
                }


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

    private boolean salvarConversa( String idUsarioRemetente, String idDestinatario, Conversa conversa ) {
        try {
            firebase = ConfiguracaoFirebase.getFirebase().child("conversas");
            firebase.child(idUsarioRemetente)
                    .child(idDestinatario)
                    .setValue(conversa);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
