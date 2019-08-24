package com.wjtxyz.androidvectorframeanimation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.larvalabs.svgandroid.SVGParser;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;

/***
 * usage:<br>
 * 1. copy all .svg file  from your PC to /sdcard/input_dir/svg<br>
 * 2. adb shell am broadcast -a com.wjtxyz.androidvectorframeanimation.action.VECTOR_PICTURE<br>
 * 3. copy all the generated .bin file from /sdcard/input_dir/bin/ to your PC<br>
 */
public class SVGToBinaryReceiver extends BroadcastReceiver {

    static final String ACTION_VECTOR_PICTURE = "com.wjtxyz.androidvectorframeanimation.action.VECTOR_PICTURE";

    @Override
    public void onReceive(final Context context, final Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        if (intent != null) {
            if (ACTION_VECTOR_PICTURE.equals(intent.getAction())) {
                final PendingResult pendingResult = goAsync();
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        Bundle bundle = new Bundle();
                        try {
                            File inputDir = new File(intent.hasExtra("input_dir") ? intent.getStringExtra("input_dir") : "/sdcard/input_dir/svg/");
                            File outputDir = new File(intent.hasExtra("input_dir") ? intent.getStringExtra("input_dir") : "/sdcard/input_dir/bin/");
                            outputDir.mkdirs();
                            inputDir.mkdirs();

                            for (File srcFile : inputDir.listFiles(new FilenameFilter() {
                                @Override
                                public boolean accept(File dir, String name) {
                                    return name.endsWith(".svg");
                                }
                            })) {
                                File destFile = new File(outputDir, srcFile.getName() + ".bin");
                                try (BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(srcFile));
                                     BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(destFile))) {
                                    SVGParser.getSVGFromInputStream(inputStream).createPictureDrawable().getPicture().writeToStream(outputStream);
                                    bundle.putString(srcFile.getAbsolutePath(), destFile.getAbsolutePath());
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        pendingResult.setResultExtras(bundle);
                        pendingResult.finish();
                    }
                });
            }
        }
    }
}
