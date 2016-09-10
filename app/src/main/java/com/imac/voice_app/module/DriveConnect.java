package com.imac.voice_app.module;

import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.MetadataChangeSet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.OutputStream;

/**
 * google Drive connect
 */
public class DriveConnect implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    private final static String TAG = "result";
    //    private int fileIndex = 0;//上傳檔案編號
    private Context context;
    private File file;
    private GoogleApiClient client;

    public DriveConnect(final Context context, File file) {
        super();
        this.context = context;
        this.file = file;
        if (client == null) {
            client = new GoogleApiClient.Builder(context)
                    //設定API
                    .addApi(Drive.API)
                            //設定存取範圍
                    .addScope(Drive.SCOPE_FILE)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
        }
    }

    private void StartConnect() {
        Drive.DriveApi.newDriveContents(client)
                .setResultCallback(new ResultCallback<DriveApi.DriveContentsResult>() {
                    @Override
                    public void onResult(DriveApi.DriveContentsResult driveContentsResult) {
                        //判斷狀態
                        if (!driveContentsResult.getStatus().isSuccess()) {
                            Log.e(TAG, "faild");
                            return;
                        }
                        //取得檔案路徑
//                        File file = new File(Environment.getExternalStorageDirectory().getPath()
//                                + "/" + context.getPackageName());
//                        //取得目錄下的檔案List
//                        File[] fileList = file.listFiles();
//                        if (fileList != null) {
                        Log.e("connnet", "newDriveContents result successful");
                        upLoad(driveContentsResult, file);
//                        }
                    }
                });
    }

    /**
     * 連結成功
     */
    @Override
    public void onConnected(Bundle bundle) {
        Log.e("resylt", "onConnected");
//        連結成功要做的事
        StartConnect();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e("resylt", "onConnectionSuspended");

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e("resylt", "onConnectionFailed" + connectionResult);

        if (!connectionResult.hasResolution()) {
            GoogleApiAvailability.getInstance().getErrorDialog((Activity) context, connectionResult.getErrorCode(), 0).show();
            return;
        }
//        是否未取得權限權限
//        跳出取得權限Dialog
        try {
            connectionResult.startResolutionForResult((Activity) context, 3);
        } catch (IntentSender.SendIntentException e) {
            Log.e(TAG, "Exception while starting resolution activity", e);
        }
    }

    /**
     * GoogleApiClient  connect
     */
    public void connect() {
        Log.e("resylt", "connect");
        client.connect();
    }

    /**
     * 上傳檔案
     *
     * @param driveContentsResult Google Drive DriveContentsResult
     * @param uploadFile          目錄下的檔案
     */
    private void upLoad(final DriveApi.DriveContentsResult driveContentsResult, final File uploadFile) {
        try {
            //取得 OutputStream
            OutputStream outputStream = driveContentsResult.getDriveContents().getOutputStream();
            //取得路徑
            String path = Environment.getExternalStorageDirectory().getPath()
                    + "/" + context.getPackageName() + "/" + uploadFile.getName();
            FileReader fileReader = new FileReader(path);
            BufferedReader reader = new BufferedReader(fileReader);
            String line = reader.readLine();
            //寫入檔案
            while (line != null) {
                byte[] b = (line + "\n").getBytes("UTF8");
                outputStream.write(b);
                line = reader.readLine();
            }
            MetadataChangeSet metadataChangeSet = new MetadataChangeSet.Builder()
                    // 定義格式
                    .setMimeType("text/csv")
                            //設定Title
                    .setTitle(uploadFile.getName())
                    .build();

            Drive.DriveApi.getRootFolder(client)
                    .createFile(client, metadataChangeSet, driveContentsResult.getDriveContents())
                    .setResultCallback(new ResultCallback<DriveFolder.DriveFileResult>() {
                        @Override
                        public void onResult(DriveFolder.DriveFileResult driveFileResult) {
                            Log.e(TAG, driveContentsResult.getStatus().isSuccess() + "");
                            if (!driveContentsResult.getStatus().isSuccess()) {
                                Log.e(TAG, "fail");
                                return;
                            }
                            Log.e(TAG, "上傳成功");
                            //成功後刪除手機的檔案
                            uploadFile.delete();
                            Log.e(TAG, "刪除成功");
                            client.disconnect();
                            Log.e(TAG, "Google 取消連結");
                        }
                    });


        } catch (Exception e) {
            e.printStackTrace();
            Log.e("result", e.toString());
        }
    }
}
