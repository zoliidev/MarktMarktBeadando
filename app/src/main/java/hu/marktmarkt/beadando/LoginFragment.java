package hu.marktmarkt.beadando;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCaller;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Keresősáv, navbar elrejtés
        BottomNavigationView navBar = requireActivity().findViewById(R.id.bottomNavigationView);
        EditText search = requireActivity().findViewById(R.id.searchBar);
        navBar.setVisibility(View.GONE);
        search.setVisibility(View.GONE);
        //---

        View view = inflater.inflate(R.layout.fragment_login, container, false);

        Button logIn = view.findViewById(R.id.loginBt);
        EditText name = view.findViewById(R.id.loginUsername);
        EditText pass = view.findViewById(R.id.loginPassword);

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Login", "Katt");
                RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
                String url = "https://oldal.vaganyzoltan.hu/api/login.php";

                StringRequest getToken = new StringRequest(Request.Method.POST, url, response -> {
                    //MainActivity.loginToken = response;

                    JSONObject object;
                    String logToken = null;
                    String resp = "Hiba";
                    try {
                        object = new JSONObject(response);
                        if(!object.isNull("token")) logToken = object.get("token").toString();
                        if(!object.isNull("resp")) resp = object.get("resp").toString();
                    } catch (JSONException e) {
                        Log.e("SetToken @ LoginFragment.java", e.getMessage());
                    }

                    if (logToken != null) {
                        MainActivity.setToken(logToken);
                        Toast.makeText(getContext(), "Sikeres bejelentkezés!", Toast.LENGTH_LONG).show();

                        FragmentManager fragmentManager = getParentFragmentManager();
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.setReorderingAllowed(true);
                        transaction.replace(R.id.fragmentView, new MainFragment(), null);
                        search.setVisibility(View.VISIBLE);
                        navBar.setVisibility(View.VISIBLE);
                        transaction.commit();
                        FileKi(logToken, requireContext());
                    }else{
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
        });
        return view;
    }
    private void FileKi(String data, Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("loginToken.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
}