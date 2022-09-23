package hu.marktmarkt.beadando;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity<ResultProfileBinding> extends AppCompatActivity {
    private ResultProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

}}