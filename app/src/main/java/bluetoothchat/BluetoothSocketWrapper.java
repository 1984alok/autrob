package bluetoothchat;

import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by muvi on 6/6/17.
 */

public interface BluetoothSocketWrapper {
    InputStream getInputStream() throws IOException;

    OutputStream getOutputStream() throws IOException;

    String getRemoteDeviceName();

    void connect() throws IOException;

    String getRemoteDeviceAddress();

    void close() throws IOException;

    BluetoothSocket getUnderlyingSocket();

}
