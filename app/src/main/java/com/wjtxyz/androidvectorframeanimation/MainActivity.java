package com.wjtxyz.androidvectorframeanimation;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.XmlResourceParser;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //request the WRITE_EXTERNAL_STORAGE permission for SVGToBinaryReceiver
        if (PackageManager.PERMISSION_GRANTED != checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

        findViewById(R.id.container).setOnClickListener(new View.OnClickListener() {
            int i = 0;

            @Override
            public void onClick(View v) {
                switch (++i % 6) {
                    case 1:
                        try {
                            long timestamp = System.currentTimeMillis();
                            setAnimationDrawable(v, getXmlDrawableNoCache(R.drawable.png_animation_drawable));
                            Log.d(TAG, "onClick:: load png animation drawable: " + (System.currentTimeMillis() - timestamp) + "ms");
                        } catch (IOException | XmlPullParserException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 3:
                        try {
                            long timestamp = System.currentTimeMillis();
                            setAnimationDrawable(v, getXmlDrawableNoCache(R.drawable.vector_animation_drawable));
                            Log.d(TAG, "onClick:: load vector animation drawable: " + (System.currentTimeMillis() - timestamp) + "ms");
                        } catch (IOException | XmlPullParserException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 5:
                        try {
                            long timestamp = System.currentTimeMillis();
                            setAnimationDrawable(v, getXmlDrawableNoCache(R.drawable.svg_binary_animation_drawable));
//                            setAnimationDrawable(v, SVGBinaryAnimationDrawable.create(getResources(), R.raw.svg2bin, 40));
                            Log.d(TAG, "onClick:: load svg binary animation drawable: " + (System.currentTimeMillis() - timestamp) + "ms");
                        } catch (IOException | XmlPullParserException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 0:
                    case 2:
                    case 4:
                        setAnimationDrawable(v, null);
                        break;
                }
            }
        });
    }

    void setAnimationDrawable(View view, Drawable newDrawable) {
        Drawable oldDrawable = ((ImageView) view).getDrawable();
        if (oldDrawable instanceof Animatable) {
            ((Animatable) oldDrawable).stop();
            oldDrawable = null;
        }
        ((ImageView) view).setImageDrawable(newDrawable);
        if (newDrawable instanceof Animatable) {
            ((Animatable) newDrawable).start();
        }

        //call Runtime.gc to release oldDrawable if possible, to release memory
        Runtime.getRuntime().gc();
    }

    /***
     * avoid to cache the drawable loading, because we want to measure the performance (cpu & momory)
     * you should use getDrawable(resId) directly in your project
     * @param resId
     * @return
     * @throws IOException
     * @throws XmlPullParserException
     */
    Drawable getXmlDrawableNoCache(int resId) throws IOException, XmlPullParserException {
        try (XmlResourceParser parser = getResources().getLayout(resId)) {
            return Drawable.createFromXml(getResources(), parser);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
