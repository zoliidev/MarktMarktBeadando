package hu.marktmarkt.beadando;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


import hu.marktmarkt.beadando.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
    }}