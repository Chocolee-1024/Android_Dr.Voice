package com.imac.voice_app.module.net;

import android.app.Activity;
import android.os.Environment;

import com.google.api.services.drive.model.FileList;
import com.imac.voice_app.module.net.base.BaseGoogleDrive;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static com.imac.voice_app.module.net.FileUploader.FILE_VOICE_SPEED;
import static com.imac.voice_app.module.net.FileUploader.FILE_WEEKLY_SOUND;

/**
 * Created by isa on 2016/12/12.
 */

public class DriveFile extends BaseGoogleDrive {
    private String path;
    private Activity activity;
    private CallbackEvent callbackEvent;
    private FileWriter writer;
    private String fileName;
    private final static String MINE_TYPE = "text/csv";

    public DriveFile(Activity activity, CallbackEvent callbackEvent,String fileName,String name) {
        super(activity);
        this.activity = activity;
        this.callbackEvent = callbackEvent;
        if (FILE_VOICE_SPEED.equals(fileName))
            this.fileName = FILE_VOICE_SPEED + "(" + name + ")";
        else
            this.fileName = FILE_WEEKLY_SOUND + "(" + name + ")";

    }

    @Override
    protected void beforeAccess() {
        path = Environment.getExternalStorageDirectory().getPath()
                + "/" + activity.getPackageName() + "/" + fileName + ".csv";
    }

    @Override
    protected boolean onAccess() {
        FileList outputFile = null;
        try {
            writer = new FileWriter(new File(path));
            outputFile = drive.files().list().setQ("name='" + fileName + "'").execute();
            if (outputFile.getFiles().size() != 0) {
                String outputFileId = outputFile.getFiles().get(0).getId();
                InputStream inputStream = drive.files().export(outputFileId,MINE_TYPE).executeMediaAsInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    writer.write(line + "\n");
                }
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }finally {
            try {
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return true;
    }

    @Override
    protected void successAccess() {
        callbackEvent.onCallback();

    }

    @Override
    protected void failAccess() {
        callbackEvent.onFailCallback();
    }

    @Override
    protected String setAccount() {
        return LoginChecker.ACCOUNT_NAME;
    }

    public interface CallbackEvent {
        void onCallback();

        void onFailCallback();
    }
}
