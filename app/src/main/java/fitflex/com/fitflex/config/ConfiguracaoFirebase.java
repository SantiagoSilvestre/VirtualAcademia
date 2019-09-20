package fitflex.com.fitflex.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

public final class ConfiguracaoFirebase {

    private static DatabaseReference referenciaFirebase;
    private static FirebaseAuth autenticacao;
    private static FirebaseStorage storage;


    public static DatabaseReference getFirebase(){

        if ( referenciaFirebase == null ){
            referenciaFirebase = FirebaseDatabase.getInstance().getReference();
        }

        return  referenciaFirebase;
    }

    public static FirebaseAuth getFirebaseAutenticacao() {

        if ( autenticacao == null) {

            autenticacao = FirebaseAuth.getInstance();
        }

        return autenticacao;
    }

    public static FirebaseStorage getFirebaseStorage(){
        if ( storage == null ) {

            storage = FirebaseStorage.getInstance();

        }

        return storage;

    }

}
