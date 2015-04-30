package jp.co.cyberagent.stf.rotationwatcher;

import android.content.Context;
import android.os.Build;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.view.IRotationWatcher;
import android.view.IWindowManager;

public class RotationWatcher extends Thread {
    private static final String TAG = "RotationWatcher";

    // Get an IWindowManager using private APIs.
    private IWindowManager wm = IWindowManager.Stub.asInterface(
            ServiceManager.getService(Context.WINDOW_SERVICE));

    @Override
    public void run() {
        IRotationWatcher watcher = new IRotationWatcher.Stub() {
            @Override
            public void onRotationChanged(int rotation) throws RemoteException {
                report(rotation);
            }
        };

        try {
            // Get the rotation we have right now.
            report(wm.getRotation());

            // Watch for changes in rotation.
            wm.watchRotation(watcher);

            // Just keep waiting.
            synchronized (this) {
                while (!isInterrupted()) {
                    wait();
                }
            }
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
        catch (InterruptedException e) {
            // Okay
        }
        finally {
            // Sadly, wm.removeRotationWatcher() is only available on API >= 18. Instead, we
            // must make sure that whole process dies, causing DeathRecipient to reap the
            // watcher.
            if (Build.VERSION.SDK_INT >= 18) {
                try {
                    wm.removeRotationWatcher(watcher);
                }
                catch (RemoteException e) {
                    // No-op
                }
            }
        }
    }

    private synchronized void report(int rotation) {
        // The internal values are very convenient, we can simply multiply by 90 to get the
        // actual degree.
        System.out.println(rotation * 90);
    }

    public static void main(String[] args) {
        try {
            RotationWatcher monitor = new RotationWatcher();
            monitor.start();
            monitor.join();
        }
        catch (InterruptedException e) {
            // Okay
        }
    }
}
