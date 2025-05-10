package com.example.morpiongame.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import java.util.Random;

public class AnimatedTicTacToeView extends View {

    private Paint paintGrid, paintX, paintO;
    private char[][] board = new char[3][3];
    private int moveCount = 0;
    private Handler handler = new Handler();
    private Random random = new Random();

    public AnimatedTicTacToeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paintGrid = new Paint();
        paintGrid.setColor(Color.DKGRAY);
        paintGrid.setStrokeWidth(8);

        paintX = new Paint();
        paintX.setColor(Color.RED);
        paintX.setStrokeWidth(10);
        paintX.setStyle(Paint.Style.STROKE);

        paintO = new Paint();
        paintO.setColor(Color.BLUE);
        paintO.setStrokeWidth(10);
        paintO.setStyle(Paint.Style.STROKE);

        resetBoard();
        startAnimation();
    }

    private void resetBoard() {
        moveCount = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = ' ';
            }
        }
    }

    private void startAnimation() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (moveCount < 9) {
                    int i, j;
                    do {
                        i = random.nextInt(3);
                        j = random.nextInt(3);
                    } while (board[i][j] != ' ');
                    board[i][j] = (moveCount % 2 == 0) ? 'X' : 'O';
                    moveCount++;
                    invalidate();
                    handler.postDelayed(this, 500);
                } else {
                    handler.postDelayed(() -> {
                        resetBoard();
                        invalidate();
                        startAnimation();
                    }, 1000);
                }
            }
        }, 500);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float cellWidth = getWidth() / 3f;
        float cellHeight = getHeight() / 3f;

        // Dessiner la grille
        for (int i = 1; i < 3; i++) {
            canvas.drawLine(cellWidth * i, 0, cellWidth * i, getHeight(), paintGrid);
            canvas.drawLine(0, cellHeight * i, getWidth(), cellHeight * i, paintGrid);
        }

        // Dessiner les X et O
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                float cx = j * cellWidth + cellWidth / 2;
                float cy = i * cellHeight + cellHeight / 2;
                float radius = Math.min(cellWidth, cellHeight) / 4;

                if (board[i][j] == 'O') {
                    canvas.drawCircle(cx, cy, radius, paintO);
                } else if (board[i][j] == 'X') {
                    float offset = radius;
                    canvas.drawLine(cx - offset, cy - offset, cx + offset, cy + offset, paintX);
                    canvas.drawLine(cx - offset, cy + offset, cx + offset, cy - offset, paintX);
                }
            }
        }
    }
}
