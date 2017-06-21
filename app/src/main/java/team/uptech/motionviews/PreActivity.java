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
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

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

import team.uptech.motionviews.models.User;

/**
 * Created by Emmanuel Victor Garcia on 6/14/17.
 */

public class PreActivity extends AppCompatActivity implements TextEditorDialogFragment.OnTextLayerCallback {

    private ImageView ivRemove;
    private ImageView ivAdd;
    private VideoView videoView;
    private MotionView motionView;
    private FontProvider fontProvider;

    // entity saved state
    private List<MotionEntity> entities = new ArrayList<>();
    private TextEntity textEntity;
    private float savedScale;
    private float savedRotation;
    private float savedX;
    private float savedY;

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
        ivRemove = (ImageView) findViewById(R.id.iv_remove);
        ivRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                motionView.deletedSelectedEntity();
                ivRemove.setVisibility(View.GONE);
                ivAdd.setVisibility(View.VISIBLE);
            }
        });

        ivAdd = (ImageView) findViewById(R.id.iv_add);
        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextEntity(addTextEntity());
                ivAdd.setVisibility(View.GONE);
            }
        });

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
    }

    private TextEntity addTextEntity() {
        TextLayer textLayer = createLayer();
        TextEntity textEntity = new TextEntity(
                textLayer, motionView.getWidth(), motionView.getHeight(), fontProvider
        );
        motionView.addEntityAndPosition(textEntity);
        motionView.invalidate();
        return textEntity;
    }

    private TextLayer createLayer() {
        TextLayer<User> textLayer = new TextLayer<>();
        Font font = new Font();

        font.setSize(TextLayer.Limits.INITIAL_FONT_SIZE);
        font.setColor(Color.WHITE);
        font.setTypeface(fontProvider.getDefaultFontName());

        textLayer.setFont(font);
        textLayer.setScale(1f);

        if (BuildConfig.DEBUG) {
            textLayer.setText("Overlay");
        }

        return textLayer;
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

        showTextEditor();
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

    private void showTextEditor() {
        TextEditorDialogFragment fragment = TextEditorDialogFragment
                .getInstance(textEntity.getLayer().getText(), textEntity.getLayer().getFont().getColor());
        fragment.setCallback(this);
        fragment.show(getSupportFragmentManager(), TextEditorDialogFragment.class.getName());

        ivRemove.setVisibility(View.GONE);
    }

    MotionView.MotionViewCallback motionViewCallback = new MotionView.MotionViewCallback() {
        @Override
        public void onEntitySelected(@Nullable MotionEntity entity) {
//            if (entity instanceof TextEntity) {
//                Intent i = new Intent(Intent.ACTION_VIEW);
//                i.setData(Uri.parse(((User) ((TextEntity) entity).getLayer().getOverlay()).getUrl()));
//                startActivity(i);
//            }
            ivAdd.setVisibility(entity == null ? View.VISIBLE : View.GONE);
            ivRemove.setVisibility(entity == null ? View.GONE : View.VISIBLE);
            if (entity == null) {
                restoreEntity();
                motionView.setBackgroundColor(Color.TRANSPARENT);
                return;
            }
        }

        @Override
        public void onEntityDoubleTap(@NonNull MotionEntity entity) {
            editTextEntity((TextEntity) entity);
            ivAdd.setVisibility(View.GONE);
        }
    };

    @Override
    public void textBorderChanged() {
        TextEntity textEntity = currentTextEntity();
        if (textEntity != null) {
            textEntity.setBorder(!textEntity.hasBorder());
            textEntity.updateEntity();
            motionView.invalidate();
        }
    }

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
    public void textColorChanged(int color) {
        TextEntity textEntity = currentTextEntity();
        if (textEntity != null) {
            textEntity.getLayer().getFont().setColor(color);
            textEntity.updateEntity();
            motionView.invalidate();
        }
    }
}
