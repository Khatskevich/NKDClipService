package com.example.lesah_000.ndkclipservice;

/**
 * Created by lesah_000 on 4/1/2015.
 */
public class ClipServiceDaemon {

    private native String takeMesgJNI();
    public native int dataAvailableJNI();
    public native int dataSendMesgToHostJNI( String mesg);
    static
    {
        System.loadLibrary("JNIClipboard");
    }
}
