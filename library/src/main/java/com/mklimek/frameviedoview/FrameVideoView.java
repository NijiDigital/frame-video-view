package com.mklimek.frameviedoview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class FrameVideoView extends FrameLayout {

    private Impl impl;
    private ImplType implType;
    private View placeholderView;

    public FrameVideoView(Context context) {
        this(context, null);
    }

    public FrameVideoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FrameVideoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        placeholderView = createPlaceholderView(context);
        impl = getImplInstance(context);
        addView(placeholderView);
    }

    @SuppressLint("NewApi")
    public void setDefaultPlaceholder(Drawable placeholderDrawable) {
        if (Build.VERSION.SDK_INT < 16) {
            placeholderView.setBackgroundDrawable(placeholderDrawable);
        } else {
            placeholderView.setBackground(placeholderDrawable);
        }
    }

    private Impl getImplInstance(final Context context) {
        if (Build.VERSION.SDK_INT >= 14) {
            implType = ImplType.TEXTURE_VIEW;
            final TextureViewImpl textureVideoImpl = new TextureViewImpl(context);
            addView(textureVideoImpl);
            return textureVideoImpl;
        } else {
            implType = ImplType.VIDEO_VIEW;
            final VideoViewImpl videoViewImpl = new VideoViewImpl(context);
            addView(videoViewImpl);
            return videoViewImpl;
        }
    }

    public void setup(final Uri videoUri) {
        final ColorDrawable colorDrawable;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            colorDrawable = new ColorDrawable(getResources().getColor(android.R.color.white, getContext().getTheme()));
        } else {
            colorDrawable = new ColorDrawable(getResources().getColor(android.R.color.white));
        }
        setup(videoUri, colorDrawable);
    }

    @SuppressLint("NewApi")
    public void setup(final Uri videoUri, final Drawable placeholderDrawable) {
        if (Build.VERSION.SDK_INT < 16) {
            placeholderView.setBackgroundDrawable(placeholderDrawable);
        } else {
            placeholderView.setBackground(placeholderDrawable);
        }
        impl.init(placeholderView, videoUri);
    }

    private View createPlaceholderView(Context context) {
        View placeholder = new View(context);
        placeholder.setBackgroundColor(Color.BLACK); // default placeholderView background color
        final LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        placeholder.setLayoutParams(params);
        return placeholder;
    }

    public void onResume() {
        impl.onResume();
    }

    public void onPause() {
        impl.onPause();
    }

    public ImplType getImplType() {
        return implType;
    }

    public View getPlaceholderView() {
        return placeholderView;
    }

    public void setFrameVideoViewListener(FrameVideoViewListener listener) {
        impl.setFrameVideoViewListener(listener);
    }

}
