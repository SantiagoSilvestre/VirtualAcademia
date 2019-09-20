package fitflex.com.fitflex.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import fitflex.com.fitflex.R;
import fitflex.com.fitflex.adapter.TreinoAAdapter;
import fitflex.com.fitflex.config.ConfiguracaoFirebase;
import fitflex.com.fitflex.model.Video;

public class CalculoImcFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match

    // TODO: Rename and change types of parameters
    private TextView txt_result_Imc;
    private EditText editPeso;
    private EditText editAltura;
    private Button btnCalcular;
    private String pesos;
    private String alturas;
    private TextView txt_resultP;



    public CalculoImcFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();
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
    public static CalculoImcFragment newInstance(String param1, String param2) {
        CalculoImcFragment fragment = new CalculoImcFragment();
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



        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_calc_imc, container, false);
        txt_result_Imc = view.findViewById(R.id.txt_resultImc);
        editAltura = view.findViewById(R.id.editAltura);
        editPeso = view.findViewById(R.id.editPeso);
        btnCalcular = view.findViewById(R.id.btnCalcularImc);
        txt_resultP = view.findViewById(R.id.txt_resultP);

        btnCalcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Pega as variaveis do e passa para Float
                editAltura.getText().toString();
                pesos = editPeso.getText().toString();
                alturas = editAltura.getText().toString();

                if (pesos.equals("")) {
                    Toast.makeText(getActivity(), "Digite seu peso", Toast.LENGTH_SHORT).show();
                } else if  (alturas.equals("")) {
                    Toast.makeText(getActivity(), "Digite sua Altura", Toast.LENGTH_SHORT).show();
                } else {
                    Double altura = Double.parseDouble(alturas);
                    Double peso = Double.parseDouble(pesos);
                    calcularImc(peso, altura);
                }

            }
        });


        return view;
    }

    private void calcularImc(Double peso, Double altura) {

        Double result = peso / (altura * altura) ;

        txt_result_Imc.setText(result.toString());

        if ( result < 17 ) {
            txt_resultP.setText("Muito Abaixo do Peso");
        } else if ( (result >= 17 ) && ( result <= 18.49 ) ) {
            txt_resultP.setText("Abaixo do peso");
        } else if ( (result >= 18.50 ) && ( result <= 24.99 ) ) {
            txt_resultP.setText("Peso Normal");
        } else if ( (result >= 24.99 ) && ( result <= 29.99 ) ) {
            txt_resultP.setText("Acima do Peso");
        } else if ( (result >= 30 ) && ( result <= 34.99 ) ) {
            txt_resultP.setText("Obesidade I");
        } else if ( (result >= 34.99 ) && ( result <= 39.99 ) ) {
            txt_resultP.setText("Obesidade II (Severa)");
        } else  {
            txt_resultP.setText("Obesidade III (Mórbida)");
        }

    }

}
