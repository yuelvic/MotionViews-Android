package team.uptech.motionviews;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.xeleb.motionviews.utils.FontProvider;
import com.xeleb.motionviews.viewmodel.Font;
import com.xeleb.motionviews.viewmodel.TextLayer;
import com.xeleb.motionviews.widget.MotionView;
import com.xeleb.motionviews.widget.entity.MotionEntity;
import com.xeleb.motionviews.widget.entity.TextEntity;

import java.util.Random;

import team.uptech.motionviews.models.User;

/**
 * Created by Emmanuel Victor Garcia on 6/14/17.
 */

public class PreActivity extends AppCompatActivity {

    private MotionView motionView;
    private FontProvider fontProvider;

    private Random random;

    private static final String[] URLS = {"https://www.youtube.com/", "https://www.google.com.ph/", "https://www.facebook.com/"};

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

        random = new Random();

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
        motionView.addEntity(textEntity);
        motionView.invalidate();
    }

    private TextLayer createLayer() {
        TextLayer<User> textLayer = new TextLayer<>();
        Font font = new Font();

        font.setColor(Color.rgb(getRandomColorValue(), getRandomColorValue(), getRandomColorValue()));
        font.setSize(TextLayer.Limits.INITIAL_FONT_SIZE);
        font.setTypeface(fontProvider.getDefaultFontName());

        textLayer.setFont(font);
        textLayer.setScale(System.currentTimeMillis() % 2 == 0 ? 1f : 1.3f);
        textLayer.setRotationInDegrees((float) random.nextInt(360 + 1));
        textLayer.setX(random.nextFloat() * (0.45f - -0.45f) + -0.45f);
        textLayer.setY(random.nextFloat() * (0.85f - 0.1f) + 0.1f);

        String url = URLS[random.nextInt(2)];

        User user = new User();
        user.setUserID(13);
        user.setUsername("John Doe");
        user.setUrl(url);

        textLayer.setOverlay(user);

        if (BuildConfig.DEBUG) {
            textLayer.setText(url);
        }

        return textLayer;
    }

    private int getRandomColorValue() {
        return random.nextInt(255 + 1);
    }

    MotionView.MotionViewCallback motionViewCallback = new MotionView.MotionViewCallback() {
        @Override
        public void onEntitySelected(@Nullable MotionEntity entity) {
            if (entity instanceof TextEntity) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(((User) ((TextEntity) entity).getLayer().getOverlay()).getUrl()));
                startActivity(i);
            }
        }

        @Override
        public void onEntityDoubleTap(@NonNull MotionEntity entity) {
            Log.d("X", entity.getLayer().getX() + "");
            Log.d("Y", entity.getLayer().getY() + "");
        }
    };

}
