package fitflex.com.fitflex.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fitflex.com.fitflex.R;
import fitflex.com.fitflex.adapter.ContatosAdapter;
import fitflex.com.fitflex.config.ConfiguracaoFirebase;
import fitflex.com.fitflex.helper.Base64Custom;
import fitflex.com.fitflex.helper.Preferencias;
import fitflex.com.fitflex.model.Contato;

import android.widget.*;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.EventListener;


public class ContatosFragment extends Fragment {


    // TODO: Rename and change types of parameters
    private ListView listView;
    private ArrayAdapter adapter;
    private ArrayList<Contato> contatos;
    private DatabaseReference firebase;
    private ValueEventListener valueEventListenerContatos;


    public ContatosFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        firebase = ConfiguracaoFirebase.getFirebase()
                .child("contatos").child("c2FuQGdtYWlsLmNvbQ==");
        firebase.addValueEventListener( valueEventListenerContatos );

    }

    @Override
    public void onStop() {
        super.onStop();

        firebase.removeEventListener( valueEventListenerContatos );
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ContatosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ContatosFragment newInstance(String param1, String param2) {
        ContatosFragment fragment = new ContatosFragment();
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
        final String identificadorUsuarioLogado;
        final ArrayList<Contato> cont = new ArrayList<Contato>();
        //Instânciar Objetos

        contatos = new ArrayList<>();
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_contatos, container, false);

        listView = view.findViewById(R.id.lv_contato);

        /*adapter = new ArrayAdapter(
                getActivity(),
                R.layout.lista_contato,
                contatos
        );
        listView.setAdapter( adapter );*/

        adapter = new ContatosAdapter(getActivity(), contatos);
        listView.setAdapter( adapter );

        //Recuperar contatos do firebase
        Preferencias preferencias = new Preferencias(getActivity());
        identificadorUsuarioLogado = preferencias.getIdentificador();


        //Listener para recuperar contatos
        valueEventListenerContatos = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {

                //Limpar a lista
                contatos.clear();

                //Listar Contatos
                for ( DataSnapshot dados: dataSnapshot.getChildren() ) {

                    Contato contato = dados.getValue( Contato.class );
                    String email = contato.getEmail();
                    String nome = contato.getNome();
                    String status = contato.getStatus();
                    contatos.add( contato );

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            FragmentActivity act = getActivity();

                            if (act != null) {
                                Contato contato = contatos.get((i));
                                Intent intent = new Intent(act,VisualizarUser.class);
                                intent.putExtra("nome", contato.getNome());
                                intent.putExtra("email", contato.getEmail());
                                intent.putExtra("status", contato.getStatus());

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
