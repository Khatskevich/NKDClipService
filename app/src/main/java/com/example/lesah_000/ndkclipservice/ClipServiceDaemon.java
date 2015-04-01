package com.example.lesah_000.ndkclipservice;



import android.app.IntentService;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;

/**
 * Created by lesah_000 on 4/1/2015.
 */
public class ClipServiceDaemon extends IntentService {
    public ClipboardManager mClipboard;
    public Context context;
    private Handler handler = new Handler();
    public ClipServiceDaemon() {
        super("Name for Service");
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {
        System.out.println("Valera working!");
        context = getApplicationContext();
        //mClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        mClipboard = (ClipboardManager) this.getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);

        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, "Clip service started!: ", Toast.LENGTH_SHORT).show();
            }
        });
        while(1>0) {
            String msg;
            try {
                msg = takeMesgJNI();
            }catch (Exception e){
                msg = "";
            }
            final String mesg = msg;
            System.out.println("HelloJniMesg = " + mesg);
            System.out.println("HelloJniMesgLen = "+ mesg.length());
            if ( mesg.length()!=0 ) {
                ClipData clip = ClipData.newPlainText("VBOX_CLIP_DATA", mesg);
                mClipboard.setPrimaryClip(clip);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "Clip gets data!: " + mesg, Toast.LENGTH_SHORT).show();
                    }
                });
            }else{
                ClipData abc = mClipboard.getPrimaryClip();
                ClipData.Item item;
                String tempText;
                try{
                    item = abc.getItemAt(0);
                    tempText = item.getText().toString();
                }catch (Exception e){
                    tempText = "";
                }
                final String text = tempText;
                final int ret =dataSendMesgToHostJNI(text);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "Clip send data: "+text+"ret = " + ret, Toast.LENGTH_SHORT).show();
                    }
                });
            }
            /*try {
                Thread.sleep(8000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
        }
    }
    static
    {
        System.loadLibrary("JNIClipboard");
    }

    private native String takeMesgJNI();
    //public native int dataAvailableJNI();
    public native int dataSendMesgToHostJNI( String mesg);
}
