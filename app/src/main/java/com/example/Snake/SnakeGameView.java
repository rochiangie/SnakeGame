package com.example.gestorandroid;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class SnakeGameView extends SurfaceView implements Runnable {
    private Thread gameThread;
    private boolean isPlaying;
    private boolean isPaused;
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;
    private int[] snakeX;
    private int[] snakeY;
    private int snakeLength;
    private int fruitX;
    private int fruitY;
    private int direction;
    private int speed;

    public SnakeGameView(Context context) {
        super(context);
        surfaceHolder = getHolder();
        paint = new Paint();
        snakeX = new int[100];
        snakeY = new int[100];
        snakeLength = 1;
        snakeX[0] = 100;
        snakeY[0] = 100;
        direction = 0; // Inicia hacia la derecha
        isPlaying = true;
        isPaused = false;
        speed = 150; // Aumentar la velocidad inicial

        spawnFruit();
    }

    @Override
    public void run() {
        while (isPlaying) {
            if (!isPaused) {
                update();
                draw();
                control();
            }
        }
    }

    public void setPaused(boolean paused) {
        isPaused = paused;
    }

    public void setDirection(int newDirection) {
        // Asegúrate de que la nueva dirección no sea opuesta a la actual
        if ((newDirection == 0 && direction != 2) ||
                (newDirection == 2 && direction != 0) ||
                (newDirection == 1 && direction != 3) ||
                (newDirection == 3 && direction != 1)) {
            direction = newDirection;
        }
    }

    private void update() {
        // Mover las partes del cuerpo de la serpiente
        for (int i = snakeLength - 1; i > 0; i--) {
            snakeX[i] = snakeX[i - 1];
            snakeY[i] = snakeY[i - 1];
        }

        // Actualizar la posición de la cabeza según la dirección
        switch (direction) {
            case 0: // Derecha
                snakeX[0] += 10;
                break;
            case 1: // Abajo
                snakeY[0] += 10;
                break;
            case 2: // Izquierda
                snakeX[0] -= 10;
                break;
            case 3: // Arriba
                snakeY[0] -= 10;
                break;
            default:
                break;
        }

        // Revisión de colisiones con fruta y el borde de la pantalla
        if (snakeX[0] == fruitX && snakeY[0] == fruitY) {
            snakeLength++;
            speed = Math.max(50, speed - 10); // Aumentar la velocidad al comer fruta
            spawnFruit();
        }

        checkCollisions();
    }

    private void spawnFruit() {
        fruitX = (int) (Math.random() * (getWidth() / 10)) * 10;
        fruitY = (int) (Math.random() * (getHeight() / 10)) * 10;
    }

    private void checkCollisions() {
        if (snakeX[0] < 0 || snakeX[0] >= getWidth() || snakeY[0] < 0 || snakeY[0] >= getHeight()) {
            resetGame();
        }

        for (int i = 1; i < snakeLength; i++) {
            if (snakeX[0] == snakeX[i] && snakeY[0] == snakeY[i]) {
                resetGame();
            }
        }
    }

    private void resetGame() {
        snakeLength = 1;
        snakeX[0] = 100;
        snakeY[0] = 100;
        direction = 0;
        speed = 150; // Reiniciar velocidad
        spawnFruit();
    }

    private void draw() {
        if (surfaceHolder.getSurface().isValid()) {
            canvas = surfaceHolder.lockCanvas();
            canvas.drawColor(Color.BLACK);

            paint.setColor(Color.CYAN);
            for (int i = 0; i < snakeLength; i++) {
                canvas.drawRect(snakeX[i], snakeY[i], snakeX[i] + 10, snakeY[i] + 10, paint);
            }

            paint.setColor(Color.WHITE);
            canvas.drawRect(fruitX, fruitY, fruitX + 20, fruitY + 20, paint);

            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void control() {
        try {
            Thread.sleep(speed);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void pause() {
        isPlaying = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume() {
        isPlaying = true;
        gameThread = new Thread(this);
        gameThread.start();
    }
}
