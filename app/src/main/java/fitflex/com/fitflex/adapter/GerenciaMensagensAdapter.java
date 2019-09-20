package fitflex.com.fitflex.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import fitflex.com.fitflex.R;
import fitflex.com.fitflex.model.Contato;
import fitflex.com.fitflex.model.Conversa;

public class GerenciaMensagensAdapter extends ArrayAdapter<Conversa> {

    private ArrayList<Conversa> conversas;
    private Context context;
    private DatabaseReference firebase;

    public GerenciaMensagensAdapter(Context c, ArrayList<Conversa> objects) {
        super(c, 0, objects);
        this.conversas = objects;
        this.context = c;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = null;

        //Verifica se a lista de contatos está vazia

        if ( conversas != null ) {

            //Inicializar o objeto para visualização da view
            LayoutInflater inflater = (LayoutInflater) context.getSystemService( context.LAYOUT_INFLATER_SERVICE );

            //Montar a view a partir do xml
            view = inflater.inflate(R.layout.lista_contatos_mensagem, parent, false);

            //Recupera o nome para exibição
            TextView nome = view.findViewById(R.id.tv_titulo);
            TextView ultimaMensagem = view.findViewById(R.id.tv_subtitulo);

            Conversa conversa = conversas.get( position );

            nome.setText(conversa.getNome());
            ultimaMensagem.setText(conversa.getMensagem());
            //ultimaMensagem.setText("teste");

        }

        return view;

    }
}
