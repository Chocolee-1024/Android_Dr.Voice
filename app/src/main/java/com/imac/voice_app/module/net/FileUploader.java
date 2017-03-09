package com.imac.voice_app.module.net;

import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.model.FileList;

import android.app.Activity;
import android.os.Environment;

import com.imac.voice_app.module.net.base.BaseGoogleDrive;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Collections;

public class FileUploader extends BaseGoogleDrive {
    private final static String TAG = FileUploader.class.getName();
    private final static int ASK_ACCESS_ACCOUNT_PERMISSION = 123;
    private final static int SET_ACCOUNT = 321;
    public final static String FILE_VOICE_SPEED = "speed";
    public final static String FILE_WEEKLY_SOUND = "record";
    private final static String MINE_TYPE = "text/csv";
    private String fileName;
    private String name;
    private String account;
    private Activity activity;
    private File file;
    private String witchFile;

    public FileUploader(Activity activity, String name, String account, String witchFile) {
        super(activity);
        this.activity = activity;
        this.name = name;
        this.account = account;
        fileName = account + "(" + name + ")";
        if (FILE_VOICE_SPEED.equals(witchFile))
            this.witchFile = FILE_VOICE_SPEED + "(" + name + ")";
        else
            this.witchFile = FILE_WEEKLY_SOUND + "(" + name + ")";
    }

    public FileUploader(Activity activity, String account, String witchFile) {
        super(activity);
        this.activity = activity;
        this.account = account;
        fileName = account;
        if (FILE_VOICE_SPEED.equals(witchFile))
            this.witchFile = FILE_VOICE_SPEED + "(" + account + ")";
        else
            this.witchFile = FILE_WEEKLY_SOUND + "(" + account + ")";
    }

    @Override
    protected void beforeAccess() {
    }

    @Override
    protected boolean onAccess() {
        FileList result = null;
        String folderId = null;
        boolean isSuccess = false;
        String path = Environment.getExternalStorageDirectory().getPath()
                + "/" + activity.getPackageName() + "/" + file.getName();
        try {
            if (credential.getSelectedAccountName() == null) {
                activity.startActivityForResult(credential.newChooseAccountIntent(), SET_ACCOUNT);
                return false;
            }
            result = drive.files().list()
                    .setQ("name='" + fileName + "'")
                    .execute();
            if (result.getFiles().size() != 0) {
                FileList outputFile = drive.files().list().setQ("name='" + witchFile + "'").execute();
                folderId = result.getFiles().get(0).getId();
                com.google.api.services.drive.model.File file = new com.google.api.services.drive.model.File();
                file.setName(witchFile);
                file.setMimeType(MINE_TYPE);
                java.io.File filePath = new java.io.File(path);
                FileContent fileContent = new FileContent(MINE_TYPE, filePath);
                if (outputFile.getFiles().size() == 0) {
                    file.setParents(Collections.singletonList(folderId));
                    drive.files().create(file, fileContent)
                            .setFields("id, parents")
                            .execute();
                } else
                    drive.files().update(outputFile.getFiles().get(0).getId(), file, fileContent)
                            .execute();
                this.file.delete();
                isSuccess = true;
            }
        } catch (UserRecoverableAuthIOException e) {
            activity.startActivityForResult(e.getIntent(), ASK_ACCESS_ACCOUNT_PERMISSION);
            e.printStackTrace();
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isSuccess;
    }

    @Override
    protected void successAccess() {
    }

    @Override
    protected void failAccess() {
    }

    @Override
    protected String setAccount() {
        return LoginChecker.ACCOUNT_NAME;
    }

    public void connect(File file) {
        this.file = file;
        if (file != null)
            execute();
    }
}