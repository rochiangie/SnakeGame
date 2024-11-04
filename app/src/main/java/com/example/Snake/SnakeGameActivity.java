package com.example.gestorandroid;

import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class SnakeGameActivity extends AppCompatActivity {

    private SnakeGameView gameView;
    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameView = new SnakeGameView(this);
        setContentView(gameView);

        gestureDetector = new GestureDetector(this, new GestureListener());
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume();
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float deltaX = e2.getX() - e1.getX();
            float deltaY = e2.getY() - e1.getY();

            // Comprobar la dirección del movimiento
            if (Math.abs(deltaX) > Math.abs(deltaY)) {
                // Movimiento horizontal
                if (deltaX > 100) { // Desplazamiento a la derecha
                    gameView.setDirection(0); // Derecha
                } else if (deltaX < -100) { // Desplazamiento a la izquierda
                    gameView.setDirection(2); // Izquierda
                }
            } else {
                // Movimiento vertical
                if (deltaY > 100) { // Desplazamiento hacia abajo
                    gameView.setDirection(1); // Abajo
                } else if (deltaY < -100) { // Desplazamiento hacia arriba
                    gameView.setDirection(3); // Arriba
                }
            }
            return true;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }
}
