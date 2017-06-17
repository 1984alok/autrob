package com.home.autrob;

import android.app.ActionBar;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import bluetoothchat.BluetoothChatService;
import bluetoothchat.BluetoothConnector;
import bluetoothchat.Constants;
import bluetoothchat.DeviceListActivity;

public class HomeActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener,AdapterView.OnItemSelectedListener,View.OnClickListener {

    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;
    private static final String TAG = "Bluetooth";
    String address = "";

    Switch aSwitch,bSwitch,cSwitch,dSwitch,eSwitch,fSwitch,gSwitch,hSwitch,
            iSwitch;
    Spinner oneSpinner,twoSpinner,threeSpinner,
            fourSpinner,fiveSpinner,sixSpinner,sevenSpinner,
            eightSpinner,nineSpinner;

    Button save,edit;

    HashMap<Integer,Integer> spinnerSelectionMap = new HashMap<>();
    HashMap<Integer,Integer> switchSelectionMap = new HashMap<>();


    Timer timer;
    TimerTask timerTask;
    final Handler handler = new Handler();

    /**
     * Name of the connected device
     */
    private String mConnectedDeviceName = null;

    /**
     * Array adapter for the conversation thread
     */
    //  private ArrayAdapter<String> mConversationArrayAdapter;

    /**
     * String buffer for outgoing messages
     */
    // private StringBuffer mOutStringBuffer;

    /**
     * Local Bluetooth adapter
     */
    private BluetoothAdapter mBluetoothAdapter = null;
    private BluetoothSocket btSocket = null;
    private OutputStream outStream = null;

    // SPP UUID service
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private static final UUID MY_UUID_SECURE =
            UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");

    SettingManager settingManager;

    /**
     * Member object for the chat services
     */
    //  private BluetoothChatService mChatService = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        aSwitch = (Switch) findViewById(R.id.switch1);
        bSwitch = (Switch) findViewById(R.id.switch2);
        cSwitch = (Switch) findViewById(R.id.switch3);

        dSwitch = (Switch) findViewById(R.id.switch4);
        eSwitch = (Switch) findViewById(R.id.switch5);
        fSwitch = (Switch) findViewById(R.id.switch6);

        gSwitch = (Switch) findViewById(R.id.switch7);
        hSwitch = (Switch) findViewById(R.id.switch8);
        iSwitch = (Switch) findViewById(R.id.switch9);

        oneSpinner = (Spinner) findViewById(R.id.spinnerOne);
        twoSpinner = (Spinner) findViewById(R.id.spinnerTwo);
        threeSpinner = (Spinner) findViewById(R.id.spinnerThree);

        fourSpinner = (Spinner) findViewById(R.id.spinner4);
        fiveSpinner = (Spinner) findViewById(R.id.spinner5);
        sixSpinner = (Spinner) findViewById(R.id.spinner6);

        sevenSpinner = (Spinner) findViewById(R.id.spinner7);
        eightSpinner = (Spinner) findViewById(R.id.spinner8);
        nineSpinner = (Spinner) findViewById(R.id.spinner9);


        save = (Button) findViewById(R.id.button_save);
        edit = (Button) findViewById(R.id.button_edt);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setIcon(R.drawable.splashimg);
        setStatus("Not Connected.");
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
        }
        switchSelectionMap = SettingManager.getHashMapfromSharedPref(this,SettingManager.SWITCH_KEY);
        spinnerSelectionMap = SettingManager.getHashMapfromSharedPref(this,SettingManager.SPINNER_KEY);

        if(switchSelectionMap!=null){
            setSwitchState(aSwitch,getCheckedStatus(aSwitch));
            setSwitchState( bSwitch,getCheckedStatus(bSwitch));
            setSwitchState(cSwitch,getCheckedStatus(cSwitch));

            setSwitchState( dSwitch,getCheckedStatus(dSwitch));
            setSwitchState( eSwitch,getCheckedStatus(eSwitch));
            setSwitchState( fSwitch,getCheckedStatus(fSwitch));

            setSwitchState( gSwitch,getCheckedStatus(gSwitch));
            setSwitchState( hSwitch,getCheckedStatus(hSwitch));
            setSwitchState( iSwitch,getCheckedStatus(iSwitch));
        }else {
            switchSelectionMap = new HashMap<>();
        }


        if(spinnerSelectionMap!=null){
            oneSpinner.setSelection(spinnerSelectionMap.get(oneSpinner.getId()));
            twoSpinner.setSelection(spinnerSelectionMap.get(twoSpinner.getId()));
            threeSpinner.setSelection(spinnerSelectionMap.get(threeSpinner.getId()));

            fourSpinner.setSelection(spinnerSelectionMap.get(fourSpinner.getId()));
            fiveSpinner.setSelection(spinnerSelectionMap.get(fiveSpinner.getId()));

            sixSpinner.setSelection(spinnerSelectionMap.get(sixSpinner.getId()));
            sevenSpinner.setSelection(spinnerSelectionMap.get(sevenSpinner.getId()));
            eightSpinner.setSelection(spinnerSelectionMap.get(eightSpinner.getId()));

            nineSpinner.setSelection(spinnerSelectionMap.get(nineSpinner.getId()));

        }else{

            spinnerSelectionMap = new HashMap<>();
        }

        aSwitch.setOnCheckedChangeListener(this);
        bSwitch.setOnCheckedChangeListener(this);
        cSwitch.setOnCheckedChangeListener(this);

        dSwitch.setOnCheckedChangeListener(this);
        eSwitch.setOnCheckedChangeListener(this);
        fSwitch.setOnCheckedChangeListener(this);

        gSwitch.setOnCheckedChangeListener(this);
        hSwitch.setOnCheckedChangeListener(this);
        iSwitch.setOnCheckedChangeListener(this);

        oneSpinner.setOnItemSelectedListener(this);
        twoSpinner.setOnItemSelectedListener(this);
        threeSpinner.setOnItemSelectedListener(this);

        fourSpinner.setOnItemSelectedListener(this);
        fiveSpinner.setOnItemSelectedListener(this);
        sixSpinner.setOnItemSelectedListener(this);

        sevenSpinner.setOnItemSelectedListener(this);
        eightSpinner.setOnItemSelectedListener(this);
        nineSpinner.setOnItemSelectedListener(this);

        save.setOnClickListener(this);
        edit.setOnClickListener(this);

        settingManager =  SettingManager.getInstance(this);
        if(settingManager.getSave()){
            disableSpinner();
        }else{
            enableSpinner();
        }



    }


    @Override
    public void onStart() {
        super.onStart();
        // If BT is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the chat session
        }


    }



    private boolean getCheckedStatus(Switch mSwitch){

        return switchSelectionMap.get(mSwitch.getId())==null || switchSelectionMap.get(mSwitch.getId())==0?false:true;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        dissconnect();

    }

    @Override
    public void onResume() {
        super.onResume();

        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.

    }

    /**
     * Makes this device discoverable for 300 seconds (5 minutes).
     */
    private void ensureDiscoverable() {

        if (mBluetoothAdapter.getScanMode() !=
                BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
    }





    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE_SECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, true);
                }
                break;
            case REQUEST_CONNECT_DEVICE_INSECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, false);
                }
                break;
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a chat session
                    // setupChat();
                } else {
                    // User did not enable Bluetooth or an error occurred
                    Log.d(TAG, "BT not enabled");
                    Toast.makeText(this, R.string.bt_not_enabled_leaving,
                            Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
    }

    /**
     * Establish connection with other device
     *
     * @param data   An {@link Intent} with {@link DeviceListActivity#EXTRA_DEVICE_ADDRESS} extra.
     * @param secure Socket Security type - Secure (true) , Insecure (false)
     */
    private void connectDevice(Intent data, boolean secure) {
        // Get the device MAC address
        address = data.getExtras()
                .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        connect(address);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bluetooth_chat, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.secure_connect_scan: {
                // Launch the DeviceListActivity to see devices and do scan
                Intent serverIntent = new Intent(this, DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
                return true;
            }
            /*case R.id.insecure_connect_scan: {
                // Launch the DeviceListActivity to see devices and do scan
                Intent serverIntent = new Intent(this, DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_INSECURE);
                return true;
            }*/
            case R.id.discoverable: {
                // Ensure this device is discoverable by others
                ensureDiscoverable();
                return true;
            }
        }
        return false;
    }





    /**
     * Updates the status on the action bar.
     *
     * @param subTitle status
     */
    private void setStatus(CharSequence subTitle) {

        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (null == actionBar) {
            return;
        }
        actionBar.setSubtitle(subTitle);
        Toast.makeText(getBaseContext(), subTitle, Toast.LENGTH_LONG).show();
    }

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        if(Build.VERSION.SDK_INT >= 10){
            try {
                final Method m = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", new Class[] { UUID.class });
                return (BluetoothSocket) m.invoke(device, MY_UUID_SECURE);
            } catch (Exception e) {
                Log.e(TAG, "Could not create Insecure RFComm Connection",e);
            }
        }
        return  device.createRfcommSocketToServiceRecord(MY_UUID_SECURE);
    }


    private void errorExit(String title, String message){
        Toast.makeText(getBaseContext(), title + " - " + message, Toast.LENGTH_LONG).show();
        finish();
    }



    private void connect(String address){
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);


        try {
            btSocket = createBluetoothSocket(device);
        } catch (IOException e1) {
            errorExit("Fatal Error", "In onResume() and socket create failed: " + e1.getMessage() + ".");
        }

        mBluetoothAdapter.cancelDiscovery();

        // Establish the connection.  This will block until it connects.
        Log.d(TAG, "...Connecting...");
        setStatus("Connecting...");
        try {
            btSocket.connect();
            Log.d(TAG, "...Connection ok...");
            setStatus("Conneced...");
        } catch (IOException e) {
      /*try {
        btSocket.close();
      } catch (IOException e2) {
        errorExit("Fatal Error", "In onResume() and unable to close socket during connection failure" + e2.getMessage() + ".");
      }*/

            try {
                Log.e("","trying fallback...");
                setStatus("Trying fallback...");

                btSocket =(BluetoothSocket) device.getClass().getMethod("createRfcommSocket", new Class[] {int.class}).invoke(device,1);
                btSocket.connect();

                Log.e("","Connected");
                setStatus("Connected...");
            }
            catch (Exception e2) {
                Log.e("", "Couldn't establish Bluetooth connection!");
                setStatus("Connection Failed...");

            }
        }


        Log.d(TAG, "...Create Socket...");

        try {
            outStream = btSocket.getOutputStream();

        } catch (IOException e) {
            errorExit("Fatal Error", "In onResume() and output stream creation failed:" + e.getMessage() + ".");
        }


        if (btSocket.isConnected()){
            startTimer();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "...In onPause()...");

       // dissconnect();

        SettingManager.putHashMapIntoSharedPref(this,switchSelectionMap,SettingManager.SWITCH_KEY);
        SettingManager.putHashMapIntoSharedPref(this,spinnerSelectionMap,SettingManager.SPINNER_KEY);

    }



    private void dissconnect(){
        if (outStream != null) {
            try {
                outStream.flush();
            } catch (IOException e) {
                errorExit("Fatal Error", "In onPause() and failed to flush output stream: " + e.getMessage() + ".");
            }
        }
        if (btSocket != null) {
            try {
                btSocket.close();
            } catch (IOException e2) {
                errorExit("Fatal Error", "In onPause() and failed to close socket." + e2.getMessage() + ".");
            }
        }

        setStatus("Disconnected...");

        Toast.makeText(getBaseContext(), "Disconnected...", Toast.LENGTH_LONG).show();
    }



    private void setSwitchState(Switch aSwitch,boolean status){
        aSwitch.setChecked(status);
        if(status) {

            aSwitch.setText("ON");
            aSwitch.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
            switchSelectionMap.put(aSwitch.getId(),SettingManager.ON);
        }else{
            aSwitch.setText("OFF");
            aSwitch.setTextColor(getResources().getColor(android.R.color.holo_red_light));
            switchSelectionMap.put(aSwitch.getId(),SettingManager.OFF);
        }
    }

    private void sendData(String message,Switch aSwitch) {
        if (btSocket!=null && btSocket.isConnected()&&outStream!=null) {
            byte[] msgBuffer = message.getBytes();

            Log.d(TAG, "...Send data: " + message + "...");

            try {
                outStream.write(msgBuffer);
                setSwitchState(aSwitch,aSwitch.isChecked());
            } catch (IOException e) {
                String msg = "In onResume() and an exception occurred during write: " + e.getMessage();
                if (address.equals("00:00:00:00:00:00"))
                    msg = msg + ".\n\nUpdate your server address from 00:00:00:00:00:00 to the correct address on line 35 in the java code";
                msg = msg +  ".\n\nCheck that the SPP UUID: " + MY_UUID_SECURE.toString() + " exists on server.\n\n";
                setSwitchState(aSwitch,false);
                errorExit("Fatal Error", msg);
            }
        }else{
            Toast.makeText(getBaseContext(), "Please connect first.", Toast.LENGTH_LONG).show();
            setSwitchState(aSwitch,getCheckedStatus(aSwitch));


        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        switch (buttonView.getId()){
            case R.id.switch1:
                if(isChecked){
                    sendData("aON", (Switch) buttonView);
                }else{
                    sendData("aOFF", (Switch) buttonView);
                }
                break;
            case R.id.switch2:
                if(isChecked){
                    sendData("bON", (Switch) buttonView);
                }else{
                    sendData("BOFF", (Switch) buttonView);
                }
                break;
            case R.id.switch3:
                if(isChecked){
                    sendData("cON", (Switch) buttonView);
                }else{
                    sendData("COFF", (Switch) buttonView);
                }
                break;
            case R.id.switch4:
                if(isChecked){
                    sendData("dON", (Switch) buttonView);
                }else{
                    sendData("DOFF", (Switch) buttonView);
                }
                break;
            case R.id.switch5:
                if(isChecked){
                    sendData("eON", (Switch) buttonView);
                }else{
                    sendData("EOFF", (Switch) buttonView);
                }
                break;
            case R.id.switch6:
                if(isChecked){
                    sendData("fON", (Switch) buttonView);
                }else{
                    sendData("FOFF", (Switch) buttonView);
                }
                break;
            case R.id.switch7:
                if(isChecked){
                    sendData("gON", (Switch) buttonView);
                }else{
                    sendData("GOFF", (Switch) buttonView);
                }
                break;
            case R.id.switch8:
                if(isChecked){
                    sendData("hON", (Switch) buttonView);
                }else{
                    sendData("HOFF", (Switch) buttonView);
                }
                break;
            case R.id.switch9:
                if(isChecked){
                    sendData("iON", (Switch) buttonView);
                }else{
                    sendData("IOFF", (Switch) buttonView);
                }
                break;
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        Log.d("alok id ::"+parent.getId(),"::"+position);
        spinnerSelectionMap.put(parent.getId(),position);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_save:
                disableSpinner();
                break;
            case R.id.button_edt:
                enableSpinner();
                break;
        }
    }

    private void disableSpinner() {
        oneSpinner.setEnabled(false);
        twoSpinner.setEnabled(false);
        threeSpinner.setEnabled(false);

        fourSpinner.setEnabled(false);
        fiveSpinner.setEnabled(false);
        sixSpinner.setEnabled(false);

        sevenSpinner.setEnabled(false);
        eightSpinner.setEnabled(false);
        nineSpinner.setEnabled(false);

        save.setEnabled(false);
        edit.setEnabled(true);
        settingManager.setSave(true);
        Toast.makeText(this,"Settings saved.",Toast.LENGTH_LONG).show();
    }

    private void enableSpinner() {

        oneSpinner.setEnabled(true);
        twoSpinner.setEnabled(true);
        threeSpinner.setEnabled(true);

        fourSpinner.setEnabled(true);
        fiveSpinner.setEnabled(true);
        sixSpinner.setEnabled(true);

        sevenSpinner.setEnabled(true);
        eightSpinner.setEnabled(true);
        nineSpinner.setEnabled(true);
        save.setEnabled(true);
        edit.setEnabled(false);

        Toast.makeText(this,"Edit mode enable.",Toast.LENGTH_LONG).show();
    }



    /* *//**
     * The Handler that gets information back from the BluetoothChatService
     *//*
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothChatService.STATE_CONNECTED:
                            setStatus(getString(R.string.title_connected_to, mConnectedDeviceName));
                            mConversationArrayAdapter.clear();
                            break;
                        case BluetoothChatService.STATE_CONNECTING:
                            setStatus(getResources().getString(R.string.title_connecting));
                            break;
                        case BluetoothChatService.STATE_LISTEN:
                        case BluetoothChatService.STATE_NONE:
                            setStatus(getResources().getString(R.string.title_not_connected));
                            break;
                    }
                    break;
                case Constants.MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);
                    mConversationArrayAdapter.add("Me:  " + writeMessage);
                    break;
                case Constants.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    mConversationArrayAdapter.add(mConnectedDeviceName + ":  " + readMessage);
                    break;
                case Constants.MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(Constants.DEVICE_NAME);
                        Toast.makeText(HomeActivity.this, "Connected to "
                                + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    break;
                case Constants.MESSAGE_TOAST:
                        Toast.makeText(HomeActivity.this, msg.getData().getString(Constants.TOAST),
                                Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };*/



    public void startTimer() {

       /* timer = new Timer();

        initializeTimerTask();

        timer.schedule(timerTask, 10000, 100000); //*/

       handler.postDelayed(new Runnable() {
           @Override
           public void run() {
               if (btSocket.isConnected())
                   dissconnect();
           }
       },100000);

    }

    public void stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

   /* public void initializeTimerTask() {

        timerTask = new TimerTask() {
            public void run() {

                //use a handler to run a toast that shows the current timestamp
                handler.post(new Runnable() {
                    public void run() {

                        if (btSocket.isConnected())
                        dissconnect();
                        else
                            stoptimertask();


                    }
                });
            }
        };
    }*/


}
