package fitflex.com.fitflex.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import fitflex.com.fitflex.R;
import fitflex.com.fitflex.config.ConfiguracaoFirebase;
import fitflex.com.fitflex.helper.Base64Custom;
import fitflex.com.fitflex.helper.Preferencias;
import fitflex.com.fitflex.model.Contato;
import fitflex.com.fitflex.model.Usuario;

import android.view.View;
import android.widget.*;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWebException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;

public class CadastroUsuario extends AppCompatActivity {

    private EditText nome;
    private EditText email;
    private EditText senha;
    private EditText area;
    private EditText ddd;
    private EditText telefone;
    private Button btn_cadastrar;
    private Usuario usuario;
    private String identificadorContato;

    private FirebaseAuth autenticacao;
    private DatabaseReference firebase;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);

        nome = findViewById(R.id.edit_nome_user);
        email = findViewById(R.id.edit_email);
        senha = findViewById(R.id.edit_senha);
        area = findViewById(R.id.edit_codArea);
        ddd = findViewById(R.id.edit_ddd);
        telefone = findViewById(R.id.edit_telefone);

        SimpleMaskFormatter simpleMaskTelefone = new SimpleMaskFormatter("NNNNN-NNNN");
        SimpleMaskFormatter simpleMaskArea = new SimpleMaskFormatter("+NN");
        SimpleMaskFormatter simpleMaskDdd = new SimpleMaskFormatter("NN");

        MaskTextWatcher maskTelefone = new MaskTextWatcher(telefone, simpleMaskTelefone);
        MaskTextWatcher maskArea = new MaskTextWatcher(area, simpleMaskArea);
        MaskTextWatcher maskDdd = new MaskTextWatcher(ddd, simpleMaskDdd);

        telefone.addTextChangedListener( maskTelefone );
        area.addTextChangedListener( maskArea );
        ddd.addTextChangedListener( maskDdd );


        btn_cadastrar = findViewById(R.id.btn_cadastrar);

        btn_cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String telefoneCompleto = area.getText().toString()
                        + ddd.getText().toString()
                        + telefone.getText().toString();
                String telefoneSemFormatacao = telefoneCompleto.replace("+", "");
                telefoneSemFormatacao = telefoneSemFormatacao.replace("-","");

                usuario = new Usuario();
                usuario.setNome( nome.getText().toString() );
                usuario.setEmail( email.getText().toString());
                usuario.setSenha( senha.getText().toString() );
                usuario.setTelefone(telefoneSemFormatacao);

                if (usuario.getNome().equals("")){
                    Toast.makeText(CadastroUsuario.this, "Usuário não pode ser vazio", Toast.LENGTH_SHORT).show();
                } else if ( usuario.getSenha().equals("")){
                    Toast.makeText(CadastroUsuario.this, "Senha não pode ser vazio", Toast.LENGTH_SHORT).show();
                } else if(usuario.getEmail().equals("")){
                    Toast.makeText(CadastroUsuario.this, "E-mail não pode ser vazio", Toast.LENGTH_SHORT).show();

                } else {
                    cadastrarUsuario();


                }

            }
        });

    }

    private void cadastrarUsuario() {


        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(), usuario.getSenha()
        ).addOnCompleteListener(CadastroUsuario.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if ( task.isSuccessful() ){
                    Toast.makeText(CadastroUsuario.this, "Cadastrado com sucesso", Toast.LENGTH_SHORT).show();

                    FirebaseUser usuarioFirebase = task.getResult().getUser();

                    String identificadorUsuario = Base64Custom.codificarBase64( usuario.getEmail() );


                    usuario.setId( identificadorUsuario );
                    usuario.setNivel_acesso("usuario");
                    usuario.salvar();



                    Preferencias preferencias = new Preferencias(CadastroUsuario.this);
                    preferencias.salvarDados(identificadorUsuario, usuario.getNome());

                    abrirLoginUsuario();


                } else  {

                    String erroExcesao = "";

                    try {
                        throw task.getException();
                    } catch ( FirebaseAuthWebException e ) {
                        erroExcesao = "O e-mail digitado é inválido por favor digite um novo";
                    } catch ( FirebaseAuthInvalidCredentialsException e ) {
                        erroExcesao = "Digite uma senha mais forte, contendo mais caracteres e com letras e números, ou verifique se o e-mail é válido!";
                    } catch ( FirebaseAuthUserCollisionException e) {
                        erroExcesao = "Esse e-mail já está em uso no App!";
                    } catch ( Exception e  ){
                        erroExcesao = "Erro ao cadastrar usuário!";
                        e.printStackTrace();
                    }

                    Toast.makeText(CadastroUsuario.this, " Erro: "+erroExcesao, Toast.LENGTH_LONG).show();

                }

            }
        });
    }

    public void abrirLoginUsuario(){


        Intent intent = new Intent(CadastroUsuario.this, AguardarLiberacao.class);
        startActivity(intent);
        finish();
        add();
    }

    public void add (){
        String emailContato = usuario.getEmail();

        //Verifica se o usuário já está cadastrado em nosso App
        identificadorContato = Base64Custom.codificarBase64(emailContato);

        firebase = ConfiguracaoFirebase.getFirebase();
        firebase = firebase.child("usuarios").child(identificadorContato);

        firebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if ( dataSnapshot.getValue() != null ) {

                    //Recuperar dados do contato a ser adicionado
                    Usuario usuarioContato = dataSnapshot.getValue( Usuario.class );

                    //Recuperar o administrador para logar (base64)
                    reference = ConfiguracaoFirebase.getFirebase();
                    reference = reference.child("usuarios").child("c2FuQGdtYWlsLmNvbQ==");


                    firebase = ConfiguracaoFirebase.getFirebase();
                    firebase = firebase.child("contatos")
                            .child(reference.getKey())
                            .child(identificadorContato);

                    Contato contato = new Contato();
                    contato.setIdentificadorUsuario(identificadorContato);
                    contato.setNome(usuarioContato.getNome());
                    contato.setEmail(usuarioContato.getEmail());
                    contato.setNivel_acesso("usuario");
                    contato.setStatus("false");


                    firebase.setValue(contato);

                    Toast.makeText(CadastroUsuario.this, "Adicionado com sucesso!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CadastroUsuario.this, "Usuário não possui cadastro", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void abrirLogin(View view) {
        Intent intent = new Intent(CadastroUsuario.this, LoginActivity.class);
        startActivity(intent);
        finish();

    }
}
