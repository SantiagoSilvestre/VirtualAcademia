package fitflex.com.fitflex.helper;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class Permissao {

    public static boolean validaPermissao(int requestCode, Activity activity, String[] permissoes) {

        if (Build.VERSION.SDK_INT >= 23) {

            List<String> listaPermissoes = new ArrayList<String>();

            //Percorrer as permissões passadas, verificando um a um se já tem a permissão liberada

            for (String permissao : permissoes) {

                Boolean validaPermissao = ContextCompat.checkSelfPermission(activity, permissao) == PackageManager.PERMISSION_GRANTED;

                if( !validaPermissao) listaPermissoes.add(permissao);
            }

            // Caso  a Lista esteja vazia não é necessário pedir a permissão
            if( listaPermissoes.isEmpty() ) return true;

            String[] novasPermissoes = new String[ listaPermissoes.size() ];
            listaPermissoes.toArray( novasPermissoes );

            //Solicitar a permissão
            ActivityCompat.requestPermissions(activity, novasPermissoes, requestCode);


        }

        return true;
    }

}
