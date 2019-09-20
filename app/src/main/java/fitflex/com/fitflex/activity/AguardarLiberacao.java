package fitflex.com.fitflex.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import fitflex.com.fitflex.R;
import fitflex.com.fitflex.config.ConfiguracaoFirebase;
import fitflex.com.fitflex.helper.Preferencias;

import android.view.View;
import android.widget.*;

public class AguardarLiberacao extends AppCompatActivity {

    private DatabaseReference firebase;
    private Button btnVoltarLogin;
    private FirebaseAuth usuarioAutenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aguardar_liberacao);

        btnVoltarLogin = findViewById(R.id.btnVoltarLogin);

        btnVoltarLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usuarioAutenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
                usuarioAutenticacao.signOut();
                Intent intent = new Intent(AguardarLiberacao.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }


}
