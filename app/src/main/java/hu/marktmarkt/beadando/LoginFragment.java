package hu.marktmarkt.beadando;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import hu.marktmarkt.beadando.Collection.FileManager;
import hu.marktmarkt.beadando.Collection.Util;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LoginFragment() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
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

    private Util change;
    private Button logIn;
    private EditText name;
    private EditText pass;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch sw;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        change = new Util();

        logIn = view.findViewById(R.id.loginBt);
        name = view.findViewById(R.id.loginUsername);
        pass = view.findViewById(R.id.loginPassword);
        sw = view.findViewById(R.id.login_sw);

        change.removeBars(requireActivity());

        if (Objects.equals(new FileManager().fileOlvas(requireContext(), "saveMe.txt"), "\ntrue"))
            sw.setChecked(true);

        sw.setOnClickListener(swListener);

        logIn.setOnClickListener(loginListener);
        return view;
    }

    View.OnClickListener swListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            new FileManager().FileKi(sw.isChecked() + "", requireContext(), "saveMe.txt");
        }
    };

    View.OnClickListener loginListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
            String url = "https://oldal.vaganyzoltan.hu/api/login.php";

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

                    change.addBars(requireActivity());
                    change.setFragment(getParentFragmentManager(), new MainFragment());
                    new FileManager().FileKi(logToken, requireContext(), "loginToken.txt");
                } else {
                    Toast.makeText(getContext(), resp, Toast.LENGTH_LONG).show();
                }

            }, error -> Toast.makeText(getContext(), error.getMessage() + "", Toast.LENGTH_LONG).show()) {
                protected Map<String, String> getParams() {
                    Map<String, String> MyData = new HashMap<>();
                    MyData.put("name", name.getText().toString());
                    MyData.put("password", pass.getText().toString());
                    return MyData;
                }
            };
            requestQueue.add(getToken);
        }
    };
}