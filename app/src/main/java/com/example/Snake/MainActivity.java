package com.example.gestorandroid;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private SnakeGameView snakeGameView;
    private boolean isPaused = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        snakeGameView = findViewById(R.id.snakeGameView);
        Button pauseButton = findViewById(R.id.pauseButton);

        // Configurar el botón de pausa para alternar entre pausar y reanudar el juego
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPaused = !isPaused;
                snakeGameView.setPaused(isPaused); // Llamamos al método en SnakeGameView para manejar la pausa
                pauseButton.setText(isPaused ? "Reanudar" : "Pausa");
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        snakeGameView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        snakeGameView.resume();
    }
}

