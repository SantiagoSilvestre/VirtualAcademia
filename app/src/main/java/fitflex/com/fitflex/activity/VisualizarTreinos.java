package fitflex.com.fitflex.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fitflex.com.fitflex.R;
import fitflex.com.fitflex.adapter.TreinoAAdapter;
import fitflex.com.fitflex.config.ConfiguracaoFirebase;
import fitflex.com.fitflex.model.Video;

import android.widget.*;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link VisualizarTreinos#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VisualizarTreinos extends Fragment {

    // TODO: Rename and change types of parameters
    private ListView listView;
    private ArrayAdapter adapter;
    private ArrayList<Video> vvideos;
    private DatabaseReference firebase;
    private ValueEventListener eventListenerVideos;


    public VisualizarTreinos() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        firebase.addValueEventListener( eventListenerVideos );
    }

    @Override
    public void onStop() {
        super.onStop();
        firebase.removeEventListener( eventListenerVideos );
    }

    // TODO: Rename and change types and number of parameters
    public static VisualizarTreinos newInstance(String param1, String param2) {
        VisualizarTreinos fragment = new VisualizarTreinos();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

       //Instânciar objetos
        vvideos = new ArrayList<>();





        View view =  inflater.inflate(R.layout.fragment_vis_treinos, container, false);

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
                                Intent intent = new Intent(act,VisualizarTreinoA.class);

                                intent.putExtra("nome", video.getNome());
                                intent.putExtra("uri", video.getUri());

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




        return view;
    }






}
