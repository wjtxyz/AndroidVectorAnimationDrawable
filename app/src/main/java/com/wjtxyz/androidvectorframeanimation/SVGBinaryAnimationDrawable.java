package com.wjtxyz.androidvectorframeanimation;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Picture;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.PictureDrawable;
import android.support.annotation.RawRes;
import android.util.AttributeSet;

import org.xmlpull.v1.XmlPullParser;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/***
 * use this class to create AnimationDrawable by xml (require Android 7+)
 */
public class SVGBinaryAnimationDrawable extends AnimationDrawable {
    static final int ATTRS[] = new int[]{android.R.attr.src, android.R.attr.duration};

    @Override
    public void inflate(Resources r, XmlPullParser parser, AttributeSet attrs, Resources.Theme theme) throws IOException {
        final TypedArray typedArray = (theme == null) ? r.obtainAttributes(attrs, ATTRS) : theme.obtainStyledAttributes(attrs, ATTRS, 0, 0);
        final int src = typedArray.getResourceId(0, -1);
        final int duration = typedArray.getInt(1, 100);
        init(this, r, src, duration);
        typedArray.recycle();
    }

    public static AnimationDrawable create(Resources resources, @RawRes int resId, int duration) throws IOException {
        return init(new AnimationDrawable(), resources, resId, duration);
    }

    public static AnimationDrawable init(AnimationDrawable animationDrawable, Resources resources, @RawRes int resId, int duration) throws IOException {
        try (ZipInputStream inputStream = new ZipInputStream(new BufferedInputStream(resources.openRawResource(resId)))) {
            ZipEntry entry;
            while ((entry = inputStream.getNextEntry()) != null) {
                if (!entry.isDirectory()) {
                    animationDrawable.addFrame(new PictureDrawable(Picture.createFromStream(inputStream)), duration);
                }
            }
        }
        return animationDrawable;
    }
}
