package fitflex.com.fitflex.adapter;

import fitflex.com.fitflex.R;
import fitflex.com.fitflex.model.Contato;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.ArrayList;

public class ContatosAdapter extends ArrayAdapter<Contato> {

    private ArrayList<Contato> contatos;
    private Context context;

    public ContatosAdapter(Context c, ArrayList<Contato> objects) {
        super(c, 0, objects);
        this.contatos = objects;
        this.context = c;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = null;

        //Verifica se a lista de contatos está vazia

        if ( contatos != null ) {

            //Inicializar o objeto para visualização da view
            LayoutInflater inflater = (LayoutInflater) context.getSystemService( context.LAYOUT_INFLATER_SERVICE );

            //Montar a view a partir do xml
            view = inflater.inflate(R.layout.lista_contato, parent, false);

            //Recupera o nome para exibição
            TextView nomeContato = view.findViewById(R.id.tv_contatos);
            TextView status = view.findViewById(R.id.tv_status);

            Contato contato = contatos.get( position );
            nomeContato.setText(contato.getNome());
            status.setText(contato.getStatus());

        }

        return view;

    }
}
