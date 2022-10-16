package hu.marktmarkt.beadando;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class InformaciokFragment extends Fragment {

    public InformaciokFragment() {
    }

    public static InformaciokFragment newInstance() {
        InformaciokFragment fragment = new InformaciokFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_informaciok, container, false);
    }
}