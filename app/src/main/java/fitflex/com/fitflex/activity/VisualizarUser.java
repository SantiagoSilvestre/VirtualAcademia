package fitflex.com.fitflex.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.inputmethodservice.ExtractEditText;
import android.os.Bundle;

import fitflex.com.fitflex.R;
import fitflex.com.fitflex.adapter.ContatosAdapter;
import fitflex.com.fitflex.config.ConfiguracaoFirebase;
import fitflex.com.fitflex.helper.Base64Custom;
import fitflex.com.fitflex.helper.Preferencias;
import fitflex.com.fitflex.model.Contato;
import fitflex.com.fitflex.model.Usuario;

import android.view.View;
import android.widget.*;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class VisualizarUser extends AppCompatActivity {

    private DatabaseReference firebase;
    private DatabaseReference reference;
    private ValueEventListener valueEventListenerContatos;


    private EditText aluno;
    private EditText email;
    private Switch status;

    private Button btnVoltar;
    private Button btnConfirmar;
    private String statusAluno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_user);

        btnVoltar = findViewById(R.id.btn_cancelar);
        aluno = findViewById(R.id.nome_aluno);
        email = findViewById(R.id.email);
        status = findViewById(R.id.switch_ativo);
        btnConfirmar = findViewById(R.id.btn_confirmar);

        Intent intent = getIntent();

        String nomeAluno = intent.getStringExtra("nome");
        String emailAluno = intent.getStringExtra("email");
        statusAluno = intent.getStringExtra("status");

        aluno.setText(nomeAluno);
        email.setText(emailAluno);

        if ( statusAluno.equals("true")) {
            status.setChecked(true);
        }



        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VisualizarUser.this, MainActivity.class);
                startActivity(intent);
            }
        });



        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add();
                Intent intent = new Intent(VisualizarUser.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    public String mudarStatus() {

        String statusrecebido;

        if ( statusAluno.equals("false") ) {
            statusrecebido = "true";
            return statusrecebido;
        } else {
            statusrecebido = "false";
            return statusrecebido;
        }



    }


    public void add() {
        String nomeM = aluno.getText().toString();
        String emailM = email.getText().toString();
        String identificadorContato = Base64Custom.codificarBase64(emailM);
        String statusmudado = mudarStatus();
        String nivel_acesso = "usuario";

        Contato contato = new Contato();
        contato.setNome(nomeM);
        contato.setEmail(emailM);
        contato.setIdentificadorUsuario(identificadorContato);
        contato.setStatus(statusmudado);
        contato.setNivel_acesso(nivel_acesso);

        contato.salvar();


    }


}

