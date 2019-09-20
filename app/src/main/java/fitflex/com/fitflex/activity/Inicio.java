package fitflex.com.fitflex.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import fitflex.com.fitflex.R;
import fitflex.com.fitflex.config.ConfiguracaoFirebase;
import fitflex.com.fitflex.helper.Preferencias;

public class Inicio extends AppCompatActivity {
    DatabaseReference firebase;
    DatabaseReference reference;
    DatabaseReference firebases;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);



        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        if (autenticacao.getCurrentUser() == null) {
            Intent intent = new Intent(Inicio.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            verifica();
        }



    }

    public void verifica(){

        //Recuperar contatos do firebase
        Preferencias preferencias = new Preferencias(this);
        String identificadorUsuarioLogado = preferencias.getIdentificador();

        //identificadorUsuarioLogado = "c2FudGhpYWdvOTlAaG90bWFpbC5jb20=";

        firebase = ConfiguracaoFirebase.getFirebase()
                .child("usuarios")
                .child( identificadorUsuarioLogado ).child("nivel_acesso");


        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {



                String i =  dataSnapshot.getValue().toString();

                if (i.equals("usuario")) {
                    verificaPg();
                } else {
                    Intent intent = new Intent(Inicio.this, MainActivity.class);
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

        //Recuperar contatos do firebase
        Preferencias preferencias = new Preferencias(this);
        String identificadorUsuarioLogado = preferencias.getIdentificador();
        reference = ConfiguracaoFirebase.getFirebase()
                .child("contatos").child("c2FuQGdtYWlsLmNvbQ==")
                .child(identificadorUsuarioLogado).child("status");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String i =  dataSnapshot.getValue().toString();
                if (i.equals("true")) {
                    Intent intent = new Intent(Inicio.this, UsuarioMain.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(Inicio.this, AguardarLiberacao.class);
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
