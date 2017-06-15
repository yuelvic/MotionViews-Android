package team.uptech.motionviews;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.xeleb.motionviews.utils.FontProvider;
import com.xeleb.motionviews.viewmodel.Font;
import com.xeleb.motionviews.viewmodel.Layer;
import com.xeleb.motionviews.viewmodel.TextLayer;
import com.xeleb.motionviews.widget.MotionView;
import com.xeleb.motionviews.widget.entity.MotionEntity;
import com.xeleb.motionviews.widget.entity.TextEntity;

/**
 * Created by Emmanuel Victor Garcia on 6/14/17.
 */

public class PreActivity extends AppCompatActivity {

    private MotionView motionView;
    private FontProvider fontProvider;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre);
        initialize();
    }

    private void initialize() {
        motionView = (MotionView) findViewById(R.id.motion_view);
        motionView.setMotionViewCallback(motionViewCallback);
        fontProvider = new FontProvider(getResources());

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            for (int i = 0; i < 5; i++) addTextEntity();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void addTextEntity() {
        TextLayer textLayer = createLayer();
        TextEntity textEntity = new TextEntity(
                textLayer, motionView.getWidth(), motionView.getHeight(), fontProvider
        );
        motionView.addEntityAndPosition(textEntity);
        motionView.invalidate();
    }

    private TextLayer createLayer() {
        TextLayer textLayer = new TextLayer();
        Font font = new Font();

        font.setColor(System.currentTimeMillis() % 2 == 0 ? Color.RED : Color.BLACK);
        font.setSize(TextLayer.Limits.INITIAL_FONT_SIZE);
        font.setTypeface(fontProvider.getDefaultFontName());

        textLayer.setFont(font);
        textLayer.setRotationInDegrees(System.currentTimeMillis() % 2 == 0 ? 35f : 300f);
        textLayer.setScale(System.currentTimeMillis() % 2 == 0 ? 1f : 2f);

        if (BuildConfig.DEBUG) {
            textLayer.setText("Text overlay");
        }

        return textLayer;
    }

    MotionView.MotionViewCallback motionViewCallback = new MotionView.MotionViewCallback() {
        @Override
        public void onEntitySelected(@Nullable MotionEntity entity) {

        }

        @Override
        public void onEntityDoubleTap(@NonNull MotionEntity entity) {

        }
    };

}
