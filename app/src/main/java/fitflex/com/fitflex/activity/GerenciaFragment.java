package fitflex.com.fitflex.activity;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import fitflex.com.fitflex.R;
import fitflex.com.fitflex.adapter.TreinoAAdapter;
import fitflex.com.fitflex.config.ConfiguracaoFirebase;
import fitflex.com.fitflex.model.Video;


/**
 * A simple {@link Fragment} subclass.
 */
public class GerenciaFragment extends Fragment {


    FragmentActivity act;
    private ListView listView;
    private ArrayAdapter adapter;
    private ArrayList<Video> vvideos;
    private DatabaseReference firebase;
    private ValueEventListener eventListenerVideos;


    public GerenciaFragment() {
        // Required empty public constructor
    }
    @Override
    public void onStart() {
        super.onStart();
        firebase.addValueEventListener( eventListenerVideos);
    }

    @Override
    public void onStop() {
        super.onStop();
        firebase.removeEventListener( eventListenerVideos );
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Instânciar objetos
        vvideos = new ArrayList<>();
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_gerencia, container, false);


        listView = view.findViewById(R.id.lv_treinoA);

        /*adapter = new ArrayAdapter(
                getActivity(),
                R.layout.lista_treinoa,
                vvideos
        );*/

        adapter = new TreinoAAdapter(getActivity(), vvideos);
        listView.setAdapter( adapter );

        //recuperar videos Firebase
        firebase = ConfiguracaoFirebase.getFirebase()
                .child("Video");
        eventListenerVideos = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //Limpar a lista de vidos
                vvideos.clear();

                //Listar videos
                for ( DataSnapshot dados: dataSnapshot.getChildren() ) {

                    Video video = dados.getValue( Video.class );

                    vvideos.add( video );

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            FragmentActivity act = getActivity();

                            if (act != null) {
                                Video video = vvideos.get((i));
                                Intent intent = new Intent(act,VisualizarVideoactivity.class);

                                intent.putExtra("nome", video.getNome());
                                intent.putExtra("uri", video.getUri());
                                intent.putExtra("filename", video.getFilename());

                                startActivity(intent);
                            }
                        }
                    });



                }

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        act = getActivity();

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                act = getActivity();

                if (act != null) {
                    startActivity(new Intent(act, CadastrarExercicio.class));
                }

            }
        });


        return view;

    }






}
