package fitflex.com.fitflex.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import fitflex.com.fitflex.R;
import fitflex.com.fitflex.config.ConfiguracaoFirebase;
import fitflex.com.fitflex.helper.Base64Custom;
import fitflex.com.fitflex.helper.Permissao;
import fitflex.com.fitflex.helper.Preferencias;
import fitflex.com.fitflex.model.Contato;
import fitflex.com.fitflex.model.Usuario;

import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.*;

//import com.github.rtoshiro.util.format.SimpleMaskFormatter;
//import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class LoginActivity extends AppCompatActivity {

    private DatabaseReference firebase;
    private DatabaseReference reference;
    private DatabaseReference referenceFirebase;

    private EditText usuarioL;
    private EditText senhaL;
    private Button btn_logar;
    private Usuario user;
    private FirebaseAuth auth;
    private ValueEventListener eventListener;
    private String identificarUsuarioLogado;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        verificarUsuarioLogado();

        usuarioL = findViewById(R.id.edit_login_email);
        senhaL = findViewById(R.id.edit_login_senha);
        btn_logar = findViewById(R.id.btnLogar);


        btn_logar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String usuario = usuarioL.getText().toString().trim();
                String senha = senhaL.getText().toString();


                user = new Usuario();
                user.setEmail(usuario);
                user.setSenha(senha);



                if ( user.getEmail().equals("") & user.getSenha().equals("") ) {
                    Toast.makeText(LoginActivity.this, "E-mail ou senha incorreto", Toast.LENGTH_SHORT).show();
                } else {
                    validarLogin();
                }



            }
        });

    }
    private void verificarUsuarioLogado(){
        auth = ConfiguracaoFirebase.getFirebaseAutenticacao();
        if (auth.getCurrentUser() != null) {

            verifica();
            }

    }

    private void validarLogin() {

        try {
            auth = ConfiguracaoFirebase.getFirebaseAutenticacao();
            auth.signInWithEmailAndPassword(user.getEmail(), user.getSenha()
            ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {

                        Log.i("verificaMetodo", "metodo validar Login ativado");
                       identificarUsuarioLogado = Base64Custom.codificarBase64(user.getEmail());

                        referenceFirebase = ConfiguracaoFirebase.getFirebase().child("usuarios")
                                .child(identificarUsuarioLogado);

                        eventListener = new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                Usuario usuarioRecuperado = dataSnapshot.getValue( Usuario.class );

                                Preferencias preferencias = new Preferencias(LoginActivity.this);
                                preferencias.salvarDados(identificarUsuarioLogado, usuarioRecuperado.getNome());
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        };
                        referenceFirebase.addListenerForSingleValueEvent(eventListener);



                        Toast.makeText(LoginActivity.this, "Sucesso ao fazer Login", Toast.LENGTH_SHORT).show();
                        Log.i("testeUser", identificarUsuarioLogado);
                        verifica();

                    } else {
                        Toast.makeText(LoginActivity.this, "Usuário e/ou senha incorreto", Toast.LENGTH_SHORT).show();
                    }


                }
            });
        } catch ( Exception e ){
            Toast.makeText(this, "Campos não podem ser vazios", Toast.LENGTH_SHORT).show();
        }



    }

    public void abrirCadastroUsuario(View view) {
        Intent intent = new Intent(LoginActivity.this, CadastroUsuario.class);
        startActivity(intent);
    }

    private void abrirTelaPrincipal(){
        Intent i = new Intent(LoginActivity.this, UsuarioMain.class);
        startActivity(i);
        finish();
    }




    public void verifica(){

        firebase = ConfiguracaoFirebase.getFirebase()
                .child("usuarios")
                .child( identificarUsuarioLogado ).child("nivel_acesso");

        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String i =  dataSnapshot.getValue().toString();
                if (i.equals("usuario")) {
                    Log.i("testeVariavel", identificarUsuarioLogado);
                    verificaPg();
                } if (i.equals("administrador")) {
                    Log.i("testeVariavel", identificarUsuarioLogado);
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    public void verificaPg(){
        Preferencias preferencias = new Preferencias(this);
        String identificarUsuarioLogado = preferencias.getIdentificador();

        reference = ConfiguracaoFirebase.getFirebase()
                .child("contatos").child("c2FuQGdtYWlsLmNvbQ==")
                .child(identificarUsuarioLogado).child("status");

        //Recuperar contatos do firebase
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String i =  dataSnapshot.getValue().toString();
                if (i.equals("true")) {
                    abrirTelaPrincipal();
                } else {
                    Intent intent = new Intent(LoginActivity.this, AguardarLiberacao.class);
                    startActivity(intent);
                    finish();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}
