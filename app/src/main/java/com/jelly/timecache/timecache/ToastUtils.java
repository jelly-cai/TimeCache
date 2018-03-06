package com.jelly.timecache.timecache;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Administrator on 2018/3/6.
 */

public class ToastUtils {

    private static Toast toast;

    public static void showMessage(Context context, String message){
        if(toast == null){
            toast = Toast.makeText(context,message,Toast.LENGTH_SHORT);
        }else{
            toast.setText(message);
        }
        toast.show();
    }

}
