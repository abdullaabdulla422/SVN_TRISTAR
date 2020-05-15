package com.tristar.main;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by oded on 10/18/15.
 */
@SuppressWarnings("ALL")
public class PermissionsHelper {

    private static final List<String> permissions = Arrays.asList(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_NETWORK_STATE
    );
    private static final int MAX_PERMISSION_LABEL_LENGTH = 30;

    static List<String> getPermissionConstants(Context context) {
        return permissions;
    }

    private static List<PermissionInfo> getPermissions(Context context) {

        List<PermissionInfo> permissionInfoList = new ArrayList<>();

        PackageManager pm = context.getPackageManager();
        for (String permission : getPermissionConstants(context)) {
            PermissionInfo pinfo;
            try {
                pinfo = pm.getPermissionInfo(permission, PackageManager.GET_META_DATA);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                continue;
            }

            permissionInfoList.add(pinfo);
        }
        return permissionInfoList;
    }

    private static CharSequence[] getPermissionNames(Context context) {
        PackageManager pm = context.getPackageManager();
        CharSequence[] names = new CharSequence[getPermissions(context).size()];
        int i = 0;
        for (PermissionInfo permissionInfo : getPermissions(context)) {
            CharSequence label = permissionInfo.loadLabel(pm);
            if (label.length() > MAX_PERMISSION_LABEL_LENGTH) {
                label = label.subSequence(0, MAX_PERMISSION_LABEL_LENGTH);
            }
            names[i] = label;
            i++;
        }
        return names;
    }

    private static boolean[] getPermissionsState(Context context) {
        boolean[] states = new boolean[getPermissionConstants(context).size()];
        int i = 0;
        for (String permission : getPermissionConstants(context)) {
            states[i] = isPermissionGranted(context, permission);
            i++;
        }
        return states;
    }


    public static void show(final Context context, String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (title != null) {
            builder.setTitle(title);
        }
        builder.setMultiChoiceItems(getPermissionNames(context),
                getPermissionsState(context),
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        ActivityCompat.requestPermissions(scanForActivity(context),
                                new String[]{getPermissionConstants(context).get(which)}, 23);
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private static Activity scanForActivity(Context cont) {
        if (cont == null)
            return null;
        else if (cont instanceof Activity)
            return (Activity) cont;
        else if (cont instanceof ContextWrapper)
            return scanForActivity(((ContextWrapper) cont).getBaseContext());

        return null;
    }

    public static boolean isPermissionGranted(Context context, String permission) {
        return PermissionChecker.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean areExplicitPermissionsRequired() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public static void show(final Context context) {
        show(context, null);
    }
}