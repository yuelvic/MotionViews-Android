package com.xeleb.motionviews.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.xeleb.motionviews.R;

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
    public static final String ARG_COLOR = "editor_color_arg";

    protected EditText editText;
    protected ImageView ivBorder;
    protected ImageView ivColor;
    protected ImageView ivFontInc;
    protected ImageView ivFontDec;

    private OnTextLayerCallback callback;

    /**
     * deprecated
     * use {@link TextEditorDialogFragment#getInstance(String, int)}
     */
    @Deprecated
    public TextEditorDialogFragment() {
        // empty, use getInstance
    }

    public static TextEditorDialogFragment getInstance(String textValue, int textColor) {
        @SuppressWarnings("deprecation")
        TextEditorDialogFragment fragment = new TextEditorDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TEXT, textValue);
        args.putInt(ARG_COLOR, textColor);
        fragment.setArguments(args);
        return fragment;
    }

//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        if (activity instanceof OnTextLayerCallback) {
//            this.callback = (OnTextLayerCallback) activity;
//        } else {
//            throw new IllegalStateException(activity.getClass().getName()
//                    + " must implement " + OnTextLayerCallback.class.getName());
//        }
//    }
//

//    @Override
//    public void onAttachFragment(Fragment childFragment) {
//        super.onAttachFragment(childFragment);
//        if (childFragment instanceof OnTextLayerCallback) {
//            this.callback = (OnTextLayerCallback) childFragment;
//        } else {
//            throw new IllegalStateException(childFragment.getClass().getName()
//                    + " must implement " + OnTextLayerCallback.class.getName());
//        }
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.text_editor_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final Bundle args = getArguments();
        String text;

        if (args == null) return;

        text = args.getString(ARG_TEXT);

        ivBorder = (ImageView) view.findViewById(R.id.iv_border);
        ivBorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.textBorderChanged();
            }
        });

        ivFontInc = (ImageView) view.findViewById(R.id.iv_font_inc);
        ivFontInc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.textSizeChanged(true);
            }
        });

        ivFontDec = (ImageView) view.findViewById(R.id.iv_font_dec);
        ivFontDec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.textSizeChanged(false);
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

        initWithTextEntity(text);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (callback != null) {
                    callback.textChanged(s.toString());
                }
            }
        });

        view.findViewById(R.id.text_editor_root).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // exit when clicking on background
                dismiss();
            }
        });
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

    @Override
    public void dismiss() {
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

    /**
     * Callback that passes all user input through the method
     * {@link TextEditorDialogFragment.OnTextLayerCallback#textChanged(String)}
     */
    public interface OnTextLayerCallback {
        void textBorderChanged();
        void textChanged(@NonNull String text);
        void textSizeChanged(boolean increase);
        void textColorChanged(int color);
    }
}