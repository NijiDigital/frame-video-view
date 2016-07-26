package com.mklimek.frameviedoview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class FrameVideoView extends FrameLayout {

    private Impl impl;
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

    private Impl getImplInstance(final Context context) {
        if (Build.VERSION.SDK_INT >= 14) {
            final TextureViewImpl textureVideoImpl = new TextureViewImpl(context);
            addView(textureVideoImpl);
            return textureVideoImpl;
        } else {
            final VideoViewImpl videoViewImpl = new VideoViewImpl(context);
            addView(videoViewImpl);
            return videoViewImpl;
        }
    }

    public void setup(final int backgroundRes) {
        setup(null, backgroundRes);
    }

    @SuppressLint("NewApi")
    public void setup(final Uri videoUri, final int backgroundRes) {
        placeholderView.setBackgroundResource(backgroundRes);
        if (videoUri == null) {
            impl.onPause();
            placeholderView.setVisibility(VISIBLE);
        } else {
            impl.init(placeholderView, videoUri);
        }
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

    public View getPlaceholderView() {
        return placeholderView;
    }

    public void setFrameVideoViewListener(FrameVideoViewListener listener) {
        impl.setFrameVideoViewListener(listener);
    }

}
