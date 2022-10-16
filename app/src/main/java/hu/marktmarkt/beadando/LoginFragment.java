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

public class LoginFragment extends Fragment {

    public LoginFragment() {
    }

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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