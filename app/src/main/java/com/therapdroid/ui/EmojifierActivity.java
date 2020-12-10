package com.therapdroid.ui;

import android.graphics.PointF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.affectiva.android.affdex.sdk.Frame;
import com.affectiva.android.affdex.sdk.detector.CameraDetector;
import com.affectiva.android.affdex.sdk.detector.Detector;
import com.affectiva.android.affdex.sdk.detector.Face;
import com.medicaldroid.R;

import java.util.List;

public class EmojifierActivity extends AppCompatActivity
    implements Detector.ImageListener, CameraDetector.CameraEventListener {

    private SurfaceView surfaceView;
    private RelativeLayout parent;
    private CameraDetector cameraDetector;

    private int fullWidth = 0, fullHeight = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emojifier);

        surfaceView = findViewById(R.id.fullscreen_content);
        parent = findViewById(R.id.emoji_parent);

        init();
    }

    @Override
    protected void onStop() {
        super.onStop();
        cameraDetector.stop();
    }

    private void init () {
        cameraDetector = new CameraDetector(
                this, CameraDetector.CameraType.CAMERA_BACK, surfaceView, 1, Detector.FaceDetectorMode.LARGE_FACES
        );
        cameraDetector.setImageListener(this);
        cameraDetector.setMaxProcessRate(50);
        cameraDetector.setOnCameraEventListener(this);

        // define the expressions to be detected
        cameraDetector.setDetectJoy(true);
        cameraDetector.setDetectSurprise(true);
        cameraDetector.setDetectSadness(true);
        cameraDetector.setDetectAnger(true);

        cameraDetector.start();
    }

    @Override
    public void onCameraSizeSelected(int width, int height, Frame.ROTATE rotate) {
        fullWidth = height;
        fullHeight = width;
        ViewGroup.LayoutParams params = surfaceView.getLayoutParams();
        params.height = height;
        params.width = width;
        surfaceView.setLayoutParams(params);
    }

    @Override
    public void onImageResults(List<Face> faces, Frame frame, float v) {
        removeEmojis();
        addEmoji(faces);
    }

    private void addEmoji (List<Face> faces) {
        for (Face face: faces) {
            PointF [] points = face.getFacePoints();
            PointF chin = points[2];
            PointF right = points[5];
            PointF left = points[10];

            ImageView emoji = new ImageView(this);

            int height = (int) (chin.y - Math.min(right.y, left.y)) + 10;
            int width = (int) Math.abs(right.x - left.x);
            int fit = Math.min(height, width);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(fit, fit);

            float x = fullWidth - left.x + (float) (parent.getWidth() - surfaceView.getWidth()) / 2 + (float) (fit - width) / 2;
            float y = Math.min(right.y, left.y) + (float) (parent.getHeight() - surfaceView.getHeight()) / 2 + (float) (fit - height) / 2;

            emoji.setLayoutParams(params);
            emoji.setX(x);
            emoji.setY(y);

            float [] emotions = {
                    face.emotions.getJoy(),
                    face.emotions.getSurprise(),
                    face.emotions.getAnger(),
                    face.emotions.getSadness()
            };

            float largest = 0;
            int index = 0;
            for (int i = 0; i < emotions.length; i++) {
                float val = emotions[i];
                if (largest < val) {
                    largest = val;
                    index = i;
                }
            }

            if (largest < 20) {
                emoji.setImageResource(R.drawable.emoji_neutral);
            } else {
                switch (index) {

                    case 0:
                        emoji.setImageResource(R.drawable.emoji_haha);
                        break;

                    case 1:
                        emoji.setImageResource(R.drawable.emoji_surprised);
                        break;

                    case 2:
                        emoji.setImageResource(R.drawable.emoji_angry);
                        break;

                    case 3:
                        emoji.setImageResource(R.drawable.emoji_sad);
                        break;

                }
            }

            parent.addView(emoji);

        }
    }

    private void removeEmojis () {
        parent.removeAllViews();
    }
}
