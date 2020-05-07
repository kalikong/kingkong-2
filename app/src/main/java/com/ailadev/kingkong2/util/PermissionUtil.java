package com.ailadev.kingkong2.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

public class PermissionUtil implements MultiplePermissionsListener, PermissionRequestErrorListener {
    private Activity activity;
    private String[] permissions;

    public PermissionUtil(Activity activity) {
        this.activity = activity;
    }

    public PermissionUtil setPermissions(String[] permissions) {
        this.permissions = permissions;
        return this;
    }

    public void check() {
        Dexter.withActivity(this.activity)
                .withPermissions(this.permissions)
                .withListener(this)
                .withErrorListener(this)
                .onSameThread()
                .check();
    }


    @Override
    public void onError(DexterError error) {

    }

    @Override
    public void onPermissionsChecked(MultiplePermissionsReport report) {
        if (report.isAnyPermissionPermanentlyDenied()) {
            _showSettingDialog();
        }
    }

    @Override
    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
        token.continuePermissionRequest();
    }

    private void _showSettingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.activity);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                _openSettings();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void _openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", this.activity.getPackageName(), null);
        intent.setData(uri);
        activity.startActivityForResult(intent, 101);
    }
}
