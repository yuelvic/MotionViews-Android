package team.uptech.motionviews;

import android.graphics.Color;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

import com.devbrackets.android.exomedia.listener.OnCompletionListener;
import com.devbrackets.android.exomedia.listener.OnPreparedListener;
import com.devbrackets.android.exomedia.ui.widget.VideoView;
import com.xeleb.motionviews.ui.TextEditorDialogFragment;
import com.xeleb.motionviews.utils.FontProvider;
import com.xeleb.motionviews.viewmodel.Font;
import com.xeleb.motionviews.viewmodel.TextLayer;
import com.xeleb.motionviews.widget.MotionView;
import com.xeleb.motionviews.widget.entity.MotionEntity;
import com.xeleb.motionviews.widget.entity.TextEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import team.uptech.motionviews.models.User;

/**
 * Created by Emmanuel Victor Garcia on 6/14/17.
 */

public class PreActivity extends AppCompatActivity implements TextEditorDialogFragment.OnTextLayerCallback {

    private VideoView videoView;
    private MotionView motionView;
    private FontProvider fontProvider;

    private Random random;

    // entity saved state
    private List<MotionEntity> entities = new ArrayList<>();
    private TextEntity textEntity;
    private float savedScale;
    private float savedRotation;
    private float savedX;
    private float savedY;

    private static final String[] URLS = {"https://www.youtube.com/", "https://www.google.com.ph/", "https://www.facebook.com/"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_pre);

        if (getActionBar() != null)
            getActionBar().hide();

        initialize();
    }

    @Override
    protected void onPause() {
        if (videoView.isPlaying())
            videoView.stopPlayback();
        super.onPause();
    }

    private void initialize() {
        videoView = (VideoView) findViewById(R.id.video_view);
        videoView.setOnPreparedListener(new OnPreparedListener() {
            @Override
            public void onPrepared() {
                videoView.start();
            }
        });
        videoView.setOnCompletionListener(new OnCompletionListener() {
            @Override
            public void onCompletion() {
                videoView.restart();
            }
        });
        videoView.setVideoURI(
                Uri.parse("http://play.xeleblive.com/raymond/raymond-live-1497513460863-976vu02in5/raymond-live-1497513460863-976vu02in5.m3u8")
        );

        motionView = (MotionView) findViewById(R.id.motion_view);
        motionView.setMotionViewCallback(motionViewCallback);
        fontProvider = new FontProvider(getResources());

        random = new Random();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            for (int i = 0; i < 3; i++) addTextEntity();
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
        textLayer.setScale(1f);
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

    @Nullable
    private TextEntity currentTextEntity() {
        if (motionView != null && motionView.getSelectedEntity() instanceof TextEntity) {
            return ((TextEntity) motionView.getSelectedEntity());
        } else {
            return null;
        }
    }

    private void editTextEntity(TextEntity textEntity) {
//        this.entities = motionView.getEntities();
        this.textEntity = textEntity;
        savedX = textEntity.absoluteCenterX();
        savedY = textEntity.absoluteCenterY();
        savedScale = textEntity.getLayer().getScale();
        savedRotation = textEntity.getLayer().getRotationInDegrees();

        centerText(textEntity);
        motionView.setBackgroundColor(ContextCompat.getColor(this, R.color.transparent_dark));
//        motionView.hideNonSelectedEntities();

        TextEditorDialogFragment fragment = TextEditorDialogFragment
                .getInstance(textEntity.getLayer().getText(), textEntity.getLayer().getFont().getColor());
        fragment.show(getFragmentManager(), TextEditorDialogFragment.class.getName());
    }

    private void restoreEntity() {
        if (this.textEntity != null) {
            PointF center = new PointF();
            center.x = savedX;
            center.y = savedY;
            this.textEntity.moveCenterTo(center);
            this.textEntity.getLayer().setScale(savedScale);
            this.textEntity.getLayer().setRotationInDegrees(savedRotation);
//            motionView.restoreEntities(entities);
            this.textEntity = null;
        }
    }

    private void centerText(TextEntity textEntity) {
        // move text sticker up so that its not hidden under keyboard
        PointF center = textEntity.absoluteCenter();
        center.x = getScreenDimension().widthPixels / 2;
        center.y = getScreenDimension().heightPixels / 4;
        textEntity.moveCenterTo(center);
        textEntity.getLayer().setRotationInDegrees(0);
    }

    private DisplayMetrics getScreenDimension() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics;
    }

    MotionView.MotionViewCallback motionViewCallback = new MotionView.MotionViewCallback() {
        @Override
        public void onEntitySelected(@Nullable MotionEntity entity) {
//            if (entity instanceof TextEntity) {
//                Intent i = new Intent(Intent.ACTION_VIEW);
//                i.setData(Uri.parse(((User) ((TextEntity) entity).getLayer().getOverlay()).getUrl()));
//                startActivity(i);
//            }
            if (entity == null) {
                restoreEntity();
                motionView.setBackgroundColor(Color.TRANSPARENT);
                return;
            }
        }

        @Override
        public void onEntityDoubleTap(@NonNull MotionEntity entity) {
            editTextEntity((TextEntity) entity);
        }
    };

    @Override
    public void textChanged(@NonNull String text) {
        TextEntity textEntity = currentTextEntity();
        if (textEntity != null) {
            TextLayer textLayer = textEntity.getLayer();
            if (!text.equals(textLayer.getText())) {
                textLayer.setText(text);
                textEntity.updateEntity();
                motionView.invalidate();
            }
        }
    }

    @Override
    public void textSizeChanged(boolean increase) {
        TextEntity textEntity = currentTextEntity();
        if (textEntity != null) {
            if (increase) textEntity.getLayer().getFont().increaseSize(TextLayer.Limits.FONT_SIZE_STEP);
            else textEntity.getLayer().getFont().decreaseSize(TextLayer.Limits.FONT_SIZE_STEP);
            textEntity.updateEntity();
            motionView.invalidate();
        }
    }

    @Override
    public void colorChanged(int color) {
        TextEntity textEntity = currentTextEntity();
        if (textEntity != null) {
            textEntity.getLayer().getFont().setColor(color);
            textEntity.updateEntity();
            motionView.invalidate();
        }
    }
}
