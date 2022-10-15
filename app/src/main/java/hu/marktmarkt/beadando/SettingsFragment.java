package hu.marktmarkt.beadando;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import hu.marktmarkt.beadando.Collection.FileManager;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private Spinner nyelv;
    private Button jlszReset;
    private Switch hubMode;
    private EditText regi;
    private EditText uj2;
    private EditText uj1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        nyelv = view.findViewById(R.id.spNyelv);
        ArrayAdapter<CharSequence> adapter= ArrayAdapter.createFromResource(getContext(), R.array.Nyelvek, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        nyelv.setAdapter(adapter);
        nyelv.setOnItemSelectedListener(this);

        jlszReset=view.findViewById(R.id.btnJelszoValtas);
        jlszReset.setOnClickListener(btnJelszoValtasOnClick);
        regi=view.findViewById(R.id.editTextTextPasswordRegi1);
        uj1=view.findViewById(R.id.editTextTextPasswordNew);
        uj2=view.findViewById(R.id.editTextTextPasswordNew2);
        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        String url = "https://oldal.vaganyzoltan.hu/api/passChange.php";

        StringRequest getToken = new StringRequest(Request.Method.POST, url, response -> {
            //lekérés után itt fut a kód
        }, error -> Toast.makeText(getContext(), error.getMessage() + "", Toast.LENGTH_LONG).show()) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<>();
                MyData.put("Régi jelszó", regi.getText().toString());
                MyData.put("új jelszó", uj1.getText().toString());
                MyData.put("új jelszó", uj2.getText().toString());
                return MyData;
            }
        };
        requestQueue.add(getToken);


        hubMode=view.findViewById(R.id.swDarkMode);
        int csekd = 0;
        csekd=AppCompatDelegate.getDefaultNightMode();
        if(csekd==0)hubMode.setChecked(false);
        else hubMode.setChecked(true);

        hubMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    new FileManager().FileKi("true", requireActivity(), "mode.txt");}
                else {AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    new FileManager().FileKi("false", requireActivity(), "mode.txt");}
            }
        });
        return view;
    }
    View.OnClickListener btnJelszoValtasOnClick= view ->{

        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        String url = "https://oldal.vaganyzoltan.hu/api/passChange.php";

        StringRequest getToken = new StringRequest(Request.Method.POST, url, response -> {
            JSONObject object;
            String logToken = null;
            String resp = "Hiba";
            try {
                object = new JSONObject(response);
                if (!object.isNull("token")) logToken = object.get("token").toString();
                if (!object.isNull("resp")) resp = object.get("resp").toString();
            } catch (JSONException e) {
                Log.e("SetToken @ LoginFragment.java", e.getMessage());
            }

            if (logToken != null) {
                MainActivity.setToken(logToken);
                Toast.makeText(requireContext(), "Sikeres bejelentkezés!", Toast.LENGTH_LONG).show();

                //change.addBars(requireActivity());
                //change.setFragment(getParentFragmentManager(), new MainFragment());
                new FileManager().FileKi(logToken, requireContext(), "loginToken.txt");
            } else {
                Toast.makeText(getContext(), resp, Toast.LENGTH_LONG).show();
            }

        }, error -> Toast.makeText(getContext(), error.getMessage() + "", Toast.LENGTH_LONG).show()) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<>();
                MyData.put("Régi jelszó", regi.getText().toString());
                MyData.put("új jelszó", uj1.getText().toString());
                MyData.put("új jelszó", uj2.getText().toString());
                return MyData;
            }
        };
        requestQueue.add(getToken);
    };

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text=parent.getItemAtPosition(position).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}