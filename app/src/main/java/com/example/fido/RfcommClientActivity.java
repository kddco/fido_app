package com.example.fido;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RfcommClientActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final UUID SERVICE_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket socket;
    private BluetoothDevice selectedDevice;

    private Spinner deviceSpinner;
    private Button connectButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rfcomm_client);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        deviceSpinner = findViewById(R.id.deviceSpinner);
        connectButton = findViewById(R.id.connectButton);

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
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        final List<BluetoothDevice> devices = new ArrayList<>(bluetoothAdapter.getBondedDevices());

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
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (ActivityCompat.checkSelfPermission(RfcommClientActivity.this, android.Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: 考慮調用 ActivityCompat#requestPermissions
                            // 在這裡請求缺失的權限，然後覆寫
                            // public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // 以處理用戶授予權限的情況。請參考 Android 文檔中的 ActivityCompat#requestPermissions 以獲取更多詳細信息。
                            return;
                        }
                        socket = selectedDevice.createRfcommSocketToServiceRecord(SERVICE_UUID);
                        socket.connect();
                        // 在這裡處理連接成功後的操作
                    } catch (IOException e) {
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
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedDevice = (BluetoothDevice) parent.getItemAtPosition(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        selectedDevice = null;
    }
}
