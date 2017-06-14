package team.uptech.motionviews;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.xeleb.motionviews.utils.FontProvider;
import com.xeleb.motionviews.viewmodel.Font;
import com.xeleb.motionviews.viewmodel.TextLayer;
import com.xeleb.motionviews.widget.MotionView;
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
        fontProvider = new FontProvider(getResources());

        for (int i = 0; i < 3; i++) addTextEntity(i);
    }

    private void addTextEntity(int i) {
        TextEntity textEntity = new TextEntity(
                createLayer(i), motionView.getWidth(), motionView.getHeight(), fontProvider
        );
        motionView.addEntityAndPosition(textEntity);
        motionView.invalidate();
    }

    private TextLayer createLayer(int i) {
        Font font = new Font();
        font.setColor(i % 2 == 0 ? Color.RED : Color.BLACK);
        font.setSize(i % 2 == 0 ? 0.08f : 0.04f);
        font.setTypeface(fontProvider.getDefaultFontName());

        TextLayer textLayer = new TextLayer();
        textLayer.setText("Sample label");
        textLayer.setFont(font);
        textLayer.setRotationInDegrees(i % 2 == 0 ? 0.45f : 0f);
        textLayer.setScale(i % 2 == 0 ? 0.5f : 0.8f);

        return textLayer;
    }

}
