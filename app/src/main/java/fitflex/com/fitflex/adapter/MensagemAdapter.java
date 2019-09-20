package fitflex.com.fitflex.adapter;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import fitflex.com.fitflex.R;
import fitflex.com.fitflex.helper.Preferencias;
import fitflex.com.fitflex.model.Mensagem;
import android.widget.*;

public class MensagemAdapter extends ArrayAdapter<Mensagem> {

    private Context context;
    private ArrayList<Mensagem> mensagens;

    public MensagemAdapter(Context c, ArrayList<Mensagem> objetcts){
        super(c, 0, objetcts);
        this.context = c;
        this.mensagens = objetcts;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {


        View view = null;

        if ( mensagens != null ){

            //Recupera dados do remetente
            Preferencias preferencias = new Preferencias(context);
            String idUserRemetente = preferencias.getIdentificador();


            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            //Recupera a mensagem para exibição
            Mensagem mensagem = mensagens.get(position);

            //Monta layout a partir da xml
            if (idUserRemetente.equals(mensagem.getIdUsuario())) {
                view = inflater.inflate(R.layout.intem_mensagem_direita, parent, false);
            } else {
                view = inflater.inflate(R.layout.item_mensagem_esquerda, parent, false);
            }

            TextView textoMensagem = view.findViewById(R.id.tv_mensagem);
            textoMensagem.setText(mensagem.getMensagem());
        }

        return view;
    }
}
