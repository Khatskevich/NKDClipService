package com.example.lesah_000.ndkclipservice;

import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;


public class AutoStart extends BroadcastReceiver
{
    private Handler handler = new Handler();
    public static ClipboardManager mClipboard;
    public Context context;

    public void onReceive(Context context, Intent intent)
    {
        System.out.println ( "Application Started" );
        // put your TimerTask calling class here
        this.context = context;
        //mClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        mClipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);

        try
        {
            Toast.makeText(context, "Autostart truing to start service", Toast.LENGTH_SHORT).show();
            if ( !ServiceTools.isServiceRunning(context, "ClipServiceDaemon") ) {
                context.startService(new Intent(context, ClipServiceDaemon.class));
            }


        } catch ( Exception e )
        {
            System.out.println ( " Error while Starting Activity " + e.toString() );
        }
        mClipboard.addPrimaryClipChangedListener(new ClipboardListener() );
    }
    static
    {
        System.loadLibrary("JNIClipboard");
    }

    public native int dataAvailableJNI();

    class ClipboardListener implements ClipboardManager.OnPrimaryClipChangedListener
    {
        public void onPrimaryClipChanged()
        {
            ClipData abc = mClipboard.getPrimaryClip();
            try {
                final String label = abc.getDescription().getLabel().toString();
                if ( label.contentEquals("VBOX_CLIP_DATA"))
                {
                    return;
                }
            }catch (Exception e){
            }
            final int ret = dataAvailableJNI();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, "Clip provides data ret = "+ ret , Toast.LENGTH_SHORT).show();
                }
            });
            System.out.println( "DataAvailableJNI" );
        }
    }
}