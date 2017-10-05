package com.xeleb.motionviews.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.otaliastudios.autocomplete.Autocomplete;
import com.otaliastudios.autocomplete.AutocompleteCallback;
import com.otaliastudios.autocomplete.CharPolicy;
import com.xeleb.motionviews.R;
import com.xeleb.motionviews.viewmodel.MentionAdapter;
import com.xeleb.motionviews.viewmodel.Mention;

import java.util.List;

/**
 * Transparent Dialog Fragment, with no title and no background
 * <p>
 * The fragment imitates capturing input from keyboard, but does not display anything
 * the result from input from the keyboard is passed through {@link TextEditorDialogFragment.OnTextLayerCallback}
 * <p>
 * Activity that uses {@link TextEditorDialogFragment} must implement {@link TextEditorDialogFragment.OnTextLayerCallback}
 * <p>
 * If Activity does not implement {@link TextEditorDialogFragment.OnTextLayerCallback}, exception will be thrown at Runtime
 */
public class TextEditorDialogFragment extends DialogFragment {

    public static final String ARG_TEXT = "editor_text_arg";
    public static final String ARG_SIZE = "editor_size_arg";
    public static final String ARG_COLOR = "editor_color_arg";
    public static final String ARG_BG = "editor_bg_arg";

    private static final int TEXT_MAX_SIZE = 120;
    private static final int TEXT_MIN_SIZE = 8;

    private float currentTextSize;
    private boolean hasBackground;

    protected EditText editText;
    protected TextView tvDone;
    protected ImageView ivBorder;
    protected ImageView ivColor;
    protected ImageView ivFontInc;
    protected ImageView ivFontDec;

    private OnTextLayerCallback callback;

    private Autocomplete mention;
    private MentionAdapter mentionAdapter;
    private boolean hasMention = false;
    private CharSequence mentionFilter = " ";

    /**
     * deprecated
     * use {@link TextEditorDialogFragment#getInstance(String, int, float, boolean)}
     */
    @Deprecated
    public TextEditorDialogFragment() {
        // empty, use getInstance
    }

    public static TextEditorDialogFragment getInstance(String textValue, int textColor,
                                                       float textSize, boolean hasBackground) {
        @SuppressWarnings("deprecation")
        TextEditorDialogFragment textEditorDialogFragment = new TextEditorDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TEXT, textValue);
        args.putInt(ARG_COLOR, textColor);
        args.putFloat(ARG_SIZE, textSize);
        args.putBoolean(ARG_BG, hasBackground);
        textEditorDialogFragment.setArguments(args);
        return textEditorDialogFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.text_editor_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final Bundle args = getArguments();
        String text;
        float size;
        final boolean[] hasBg = new boolean[1];
        final int[] color = new int[1];

        if (args == null) return;

        text = args.getString(ARG_TEXT);
        color[0] = args.getInt(ARG_COLOR);
        size = args.getFloat(ARG_SIZE);
        hasBg[0] = args.getBoolean(ARG_BG);

        ivBorder = (ImageView) view.findViewById(R.id.iv_border);
        ivBorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hasBg[0] = !hasBg[0];
                editText.setBackgroundColor(hasBg[0] ? (color[0] == Color.WHITE ? Color.parseColor("#ffb74d") : color[0]) : Color.TRANSPARENT);
                editText.setTextColor(hasBg[0] ? Color.WHITE : color[0]);
//                callback.textBorderChanged();
            }
        });

        ivFontInc = (ImageView) view.findViewById(R.id.iv_font_inc);
        ivFontInc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentTextSize >= TEXT_MAX_SIZE) return;
                editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, currentTextSize += TEXT_MIN_SIZE);
                callback.textSizeChanged(true, currentTextSize);
            }
        });

        ivFontDec = (ImageView) view.findViewById(R.id.iv_font_dec);
        ivFontDec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentTextSize <= TEXT_MIN_SIZE) return;
                editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, currentTextSize -= TEXT_MIN_SIZE);
                callback.textSizeChanged(false, currentTextSize);
            }
        });

        ivColor = (ImageView) view.findViewById(R.id.iv_color);
        ivColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ColorPickerDialogBuilder
                        .with(getActivity())
                        .setTitle("Select a color")
                        .initialColor(args.getInt(ARG_COLOR))
                        .wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE)
                        .density(8) // magic number
                        .setPositiveButton("OK", new ColorPickerClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                                color[0] = selectedColor;
                                editText.setBackgroundColor(hasBg[0] ? selectedColor : Color.TRANSPARENT);
                                editText.setTextColor(hasBg[0] ? Color.WHITE : selectedColor);
                                callback.textColorChanged(selectedColor);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .build()
                        .show();
            }
        });

        editText = (EditText) view.findViewById(R.id.edit_text_view);
        editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        editText.setTextColor(hasBg[0] ? Color.WHITE : color[0]);
        editText.setBackgroundColor(hasBg[0] ? (color[0] == Color.WHITE ? Color.parseColor("#ffb74d") : color[0]) : Color.TRANSPARENT);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
//                if (callback != null) {
//                    callback.textChanged(s.toString());
//                }
            }
        });
        currentTextSize = size;

        tvDone = (TextView) view.findViewById(R.id.tv_done);
        tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hasBackground = hasBg[0];
                dismiss();
            }
        });

        view.findViewById(R.id.text_editor_root).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // exit when clicking on background
                hasBackground = hasBg[0];
                dismiss();
            }
        });

        AutocompleteCallback<Mention> mentionCallback = new AutocompleteCallback<Mention>() {
            @Override
            public boolean onPopupItemClicked(Editable editable, Mention profile) {
                String s = editable.toString().replace(mentionFilter, "");
                editable.clear();
                editable.append(s).append(profile.getNickname()).append(' ');
                return true;
            }

            @Override
            public void onPopupVisibilityChanged(boolean b) {

            }

            @Override
            public void getDataFromService(CharSequence charSequence) {
                mentionFilter = charSequence;
                mention.showPopup(charSequence);
            }
        };
        mentionAdapter = new MentionAdapter(getActivity());
        mention = Autocomplete.<Mention>on(editText)
                .with(6f)
                .with(new ColorDrawable(Color.WHITE))
                .with(mentionAdapter)
                .with(mentionCallback)
                .with(new CharPolicy('@'))
                .viaService(true)
                .build();

        initWithTextEntity(text);
    }

    private void initWithTextEntity(String text) {
        editText.setText(text);
        editText.post(new Runnable() {
            @Override
            public void run() {
                if (editText != null) {
                    Selection.setSelection(editText.getText(), editText.length());
                }
            }
        });
    }

    public void setCallback(OnTextLayerCallback callback) {
        this.callback = callback;
    }

    public EditText getEditText() {
        return editText;
    }

    @Override
    public void dismiss() {
        callback.textChanged(editText.getText().toString());
        callback.textBorderChanged(hasBackground);
        callback.onEditorDismiss();
        super.dismiss();

        // clearing memory on exit, cos manipulating with text uses bitmaps extensively
        // this does not frees memory immediately, but still can help
        System.gc();
        Runtime.getRuntime().gc();
    }

    @Override
    public void onDetach() {
        // release links
        this.callback = null;
        super.onDetach();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.requestWindowFeature(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            Window window = dialog.getWindow();
            if (window != null) {
                // remove background
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

                // remove dim
                WindowManager.LayoutParams windowParams = window.getAttributes();
                window.setDimAmount(0.0F);
                window.setAttributes(windowParams);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        editText.post(new Runnable() {
            @Override
            public void run() {
                // force show the keyboard
                setEditText(true);
                editText.requestFocus();
                InputMethodManager ims = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                ims.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
            }
        });
    }

    private void setEditText(boolean gainFocus) {
        if (!gainFocus) {
            editText.clearFocus();
            editText.clearComposingText();
        }
        editText.setFocusableInTouchMode(gainFocus);
        editText.setFocusable(gainFocus);
    }

    public void setMentionItems(List<Mention> mentions) {
        mentionAdapter.setMentions(mentions);
        if (!hasMention) hasMention = true;
    }

    /**
     * Callback that passes all user input through the method
     * {@link TextEditorDialogFragment.OnTextLayerCallback#textChanged(String)}
     */
    public interface OnTextLayerCallback {
        void textBorderChanged(boolean hasBackground);
        void textChanged(@NonNull String text);
        void textSizeChanged(boolean increase, float textSize);
        void textColorChanged(int color);
        void onEditorDismiss();
    }
}