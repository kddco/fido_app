package com.example.fido;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class RfcommClientActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket socket;
    private BluetoothDevice selectedDevice;
    private List<BluetoothDevice> devices;

    private Spinner deviceSpinner;
    private Button connectButton;
    private TextView msgView;

    private static final int PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rfcomm_client);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        devices = new ArrayList<>(bluetoothAdapter.getBondedDevices()); // 将此行代码放在 onCreate() 方法中

        deviceSpinner = findViewById(R.id.deviceSpinner);
        connectButton = findViewById(R.id.connectButton);
        msgView = findViewById(R.id.textView2);
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectToServer();
            }
        });

        setupDeviceSpinner();
    }

    private void setupDeviceSpinner() {
        List<String> deviceNames = new ArrayList<>();
        if (checkSelfPermission(android.Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.BLUETOOTH, android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSION_REQUEST_CODE);
            return;
        }

        for (BluetoothDevice device : devices) {
            deviceNames.add(device.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, deviceNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        deviceSpinner.setAdapter(adapter);
        deviceSpinner.setOnItemSelectedListener(this);
    }


    private void connectToServer() {
        if (selectedDevice != null) {
            // 存储蓝牙地址到SharedPreferences
            SharedPreferences sharedPreferences = getSharedPreferences("btServer", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("ServerAddress", selectedDevice.getAddress());
            editor.apply();

            //從SharedPreferences取出藍芽地址
            SharedPreferences sharedPreferences2 = getSharedPreferences("btServer", MODE_PRIVATE);
            String serverAddress = sharedPreferences2.getString("ServerAddress", "");
            Log.d("SharedPreferences",serverAddress);

            if (ActivityCompat.checkSelfPermission(RfcommClientActivity.this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(RfcommClientActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(RfcommClientActivity.this,
                        new String[]{Manifest.permission.BLUETOOTH, Manifest.permission.ACCESS_COARSE_LOCATION},
                        PERMISSION_REQUEST_CODE);
            } else {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Method m = selectedDevice.getClass().getMethod("createRfcommSocket", new Class[]{int.class});
                            socket = (BluetoothSocket) m.invoke(selectedDevice, 1);
                            socket.connect();

                            OutputStream outputStream = socket.getOutputStream();
                            outputStream.write("hello".getBytes());
                            Log.d("MyTag", "SEND HELLO TO SERVER");
                        } catch (Exception e) {
                            e.printStackTrace();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(RfcommClientActivity.this, "Failed to connect to the server", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }).start();
            }
        } else {
            Toast.makeText(RfcommClientActivity.this, "No device selected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String deviceName = (String) parent.getItemAtPosition(position);
        Log.d("MyTag", "Selected Device Name: " + deviceName);
        selectedDevice = findDeviceByName(deviceName);

        Toast.makeText(this, "Selected Device: " + deviceName, Toast.LENGTH_SHORT).show();
    }

    private BluetoothDevice findDeviceByName(String deviceName) {
        for (BluetoothDevice device : devices) {
            if (device.getName() != null && device.getName().equalsIgnoreCase(deviceName)) {
                return device;
            }
        }
        return null;
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        selectedDevice = null;
    }
}
