package com.imac.voice_app.module.net;

import android.app.Activity;
import android.os.Environment;

import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.model.FileList;
import com.imac.voice_app.module.net.base.BaseGoogleDrive;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Collections;

public class FileUploader extends BaseGoogleDrive {
    private final static String TAG = "result";
    public final static String ACCOUNT_NAME = "voice.dr.wang@gmail.com";
    public final static int ASK_ACCESS_ACCOUNT_PERMISSION = 123;
    public final static int SET_ACCOUNT = 321;
    private String fileName = "0919333333(林毅鑫)";
    private String name = "";
    private String account = "";

    private Activity activity;
    private File file;

    public FileUploader(Activity activity, String name, String account) {
        super(activity);
        this.activity = activity;
        fileName = account + "(" + name + ")";
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
                folderId = result.getFiles().get(0).getId();
                com.google.api.services.drive.model.File fileMetadata = new com.google.api.services.drive.model.File();
                fileMetadata.setName(file.getName().replace("-","/"));
                fileMetadata.setParents(Collections.singletonList(folderId));
                java.io.File filePath = new java.io.File(path);
                FileContent mediaContent = new FileContent("text/csv", filePath);
                com.google.api.services.drive.model.File uploadFile = drive.files().create(fileMetadata, mediaContent)
                        .setFields("id, parents")
                        .execute();
                file.delete();
                isSuccess = true;
            }

        } catch (UserRecoverableAuthIOException e) {
            //跳出權限dialog
            activity.startActivityForResult(e.getIntent(), ASK_ACCESS_ACCOUNT_PERMISSION);
            e.printStackTrace();
        } catch (SocketTimeoutException e) {

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
        return ACCOUNT_NAME;
    }

    public void connect(File file) {
        this.file = file;
        if (file != null)
            execute();
    }
}