package fitflex.com.fitflex.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.*;

import java.util.ArrayList;

import fitflex.com.fitflex.R;
import fitflex.com.fitflex.activity.CadastrarExercicio;
import fitflex.com.fitflex.model.Contato;
import fitflex.com.fitflex.model.Video;

public class TreinoAAdapter extends ArrayAdapter<Video> {

    private ArrayList<Video> videos;
    private Context context;

    public TreinoAAdapter(Context c, ArrayList<Video> objects) {
        super(c, 0, objects);
        this.videos = objects;
        this.context = c;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {

        View view = null;

        //Verifica se a lista de treinos A está vazia

        if (videos != null) {

            //Inicializar o objeto para visualização da view
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            //Montar a view a partir do xml
            view = inflater.inflate(R.layout.lista_videos, parent, false);

            //Recupera o nome para exibição
            TextView nomeVideo = view.findViewById(R.id.tv_nomeV);
            TextView descVideo = view.findViewById(R.id.tv_descVideo);
            TextView categoria = view.findViewById(R.id.tv_nomeCategoria);


            Video videoA = videos.get(position);
            nomeVideo.setText(videoA.getNome());
            descVideo.setText(videoA.getDicas());
            categoria.setText(videoA.getCategoria());
        }

        return view;

    }
}