package com.melihyarikkaya.rnserialport;

import android.app.PendingIntent;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;

import com.felhr.usbserial.UsbSerialDevice;
import com.felhr.usbserial.UsbSerialInterface;

public class RNSerialportModule extends ReactContextBaseJavaModule {

  private final ReactApplicationContext reactContext;
  public RNSerialportModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
    definitions = new Definitions();
  }

  @Override
  public String getName() {
    return "RNSerialport";
  }

  private static final String ACTION_USB_READY = "com.felhr.connectivityservices.USB_READY";
  private static final String ACTION_USB_ATTACHED = "android.hardware.usb.action.USB_DEVICE_ATTACHED";
  private static final String ACTION_USB_DETACHED = "android.hardware.usb.action.USB_DEVICE_DETACHED";
  private static final String ACTION_USB_NOT_SUPPORTED = "com.felhr.usbservice.USB_NOT_SUPPORTED";
  private static final String ACTION_NO_USB = "com.felhr.usbservice.NO_USB";
  private static final String ACTION_USB_PERMISSION_GRANTED = "com.felhr.usbservice.USB_PERMISSION_GRANTED";
  private static final String ACTION_USB_PERMISSION_NOT_GRANTED = "com.felhr.usbservice.USB_PERMISSION_NOT_GRANTED";
  private static final String ACTION_USB_DISCONNECTED = "com.felhr.usbservice.USB_DISCONNECTED";
  private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
  private int BAUD_RATE = 9600;
  private static final String ACTION_USB_NOT_OPENED = "com.melihyarikkaya.rnserialport.USB_NOT_OPENED";
  private static final String ACTION_USB_CONNECT = "com.melihyarikkaya.rnserialport.USB_CONNECT";

  //react-native events
  private static final String onErrorEvent              = "onError";
  private static final String onConnectedEvent          = "onConnected";
  private static final String onDisconnectedEvent       = "onDisconnected";
  private static final String onDeviceAttachedEvent     = "onDeviceAttached";
  private static final String onDeviceDetachedEvent     = "onDeviceDetached";
  private static final String onDeviceNotSupportedEvent = "onDeviceNotSupported";
  private static final String onServiceStarted          = "onServiceStarted";
  private static final String onServiceStopped          = "onServiceStopped";
  private static final String onReadDataFromPort        = "onReadDataFromPort";
  private static final String onUsbPermissionGranted    = "onUsbPermissionGranted";

  //Connection Settings
  private int DATA_BIT     = UsbSerialInterface.DATA_BITS_8;
  private int STOP_BIT     = UsbSerialInterface.STOP_BITS_1;
  private int PARITY       =  UsbSerialInterface.PARITY_NONE;
  private int FLOW_CONTROL = UsbSerialInterface.FLOW_CONTROL_OFF;

  private UsbManager usbManager;
  private UsbDevice device;
  private UsbDeviceConnection connection;
  private UsbSerialDevice serialPort;
  private boolean serialPortConnected;

  private boolean autoConnect = false;
  private String autoConnectDeviceName;
  private int autoConnectBaudRate = 9600;

  private Definitions definitions = new Definitions();

  private boolean usbServiceStarted = false;

  private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context arg0, Intent arg1) {
      Intent intent;
      switch (arg1.getAction()) {
        case ACTION_USB_CONNECT:
          eventEmit(onConnectedEvent, null);
          break;
        case ACTION_USB_DISCONNECTED:
          eventEmit(onDisconnectedEvent, null);
          break;
        case ACTION_USB_NOT_SUPPORTED:
          eventEmit(onDeviceNotSupportedEvent, null);
          break;
        case ACTION_USB_NOT_OPENED:
          eventEmit(onErrorEvent, createError(definitions.ERROR_COULD_NOT_OPEN_SERIALPORT, definitions.ERROR_COULD_NOT_OPEN_SERIALPORT_MESSAGE));
          break;
        case ACTION_USB_ATTACHED:
          if(autoConnect) {
            if(chooseFirstDevice()) {
              connectDevice(autoConnectDeviceName, autoConnectBaudRate);
            }
          } else {
            eventEmit(onDeviceAttachedEvent, null);
          }
          break;
        case ACTION_USB_DETACHED:
          if(serialPortConnected) {
            stopConnection();
          } else {
            eventEmit(onDeviceDetachedEvent, null);
          }
          break;
        case ACTION_USB_PERMISSION :
          boolean granted = arg1.getExtras().getBoolean(UsbManager.EXTRA_PERMISSION_GRANTED);
          startConnection(granted);
          break;
        case ACTION_USB_PERMISSION_GRANTED:
          eventEmit(onUsbPermissionGranted, null);
          break;
        case ACTION_USB_PERMISSION_NOT_GRANTED:
          eventEmit(onErrorEvent, createError(definitions.ERROR_USER_DID_NOT_ALLOW_TO_CONNECT, definitions.ERROR_USER_DID_NOT_ALLOW_TO_CONNECT_MESSAGE));
          break;
      }
    }
  };

  private void eventEmit(String eventName, Object data) {
    reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventName, data);
  }

  private WritableMap createError(int code, String message) {
    WritableMap err = Arguments.createMap();
    err.putBoolean("status", false);
    err.putInt("errorCode", code);
    err.putString("errorMessage", message);

    return err;
  }

  private void setFilters() {
    IntentFilter filter = new IntentFilter();
    filter.addAction(ACTION_USB_PERMISSION_GRANTED);
    filter.addAction(ACTION_NO_USB);
    filter.addAction(ACTION_USB_CONNECT);
    filter.addAction(ACTION_USB_DISCONNECTED);
    filter.addAction(ACTION_USB_NOT_SUPPORTED);
    filter.addAction(ACTION_USB_PERMISSION_NOT_GRANTED);
    filter.addAction(ACTION_USB_PERMISSION);
    filter.addAction(ACTION_USB_ATTACHED);
    filter.addAction(ACTION_USB_DETACHED);
    reactContext.registerReceiver(mUsbReceiver, filter);
  }

  @ReactMethod
  public void setDataBit(int DATA_BIT) {
    this.DATA_BIT = DATA_BIT;
  }
  @ReactMethod
  public void setStopBit(int STOP_BIT) {
    this.STOP_BIT = STOP_BIT;
  }
  @ReactMethod
  public void setParity(int PARITY) {
    this.PARITY = PARITY;
  }
  @ReactMethod
  public void setFlowControl(int FLOW_CONTROL) {
    this.FLOW_CONTROL = FLOW_CONTROL;
  }

  @ReactMethod
  public void loadDefaultConnectionSetting() {
    DATA_BIT     = UsbSerialInterface.DATA_BITS_8;
    STOP_BIT     = UsbSerialInterface.STOP_BITS_1;
    PARITY       =  UsbSerialInterface.PARITY_NONE;
    FLOW_CONTROL = UsbSerialInterface.FLOW_CONTROL_OFF;
  }

  @ReactMethod
  public void startUsbService() {
    if(usbServiceStarted) {
      return;
    }
    setFilters();

    usbManager = (UsbManager) reactContext.getSystemService(Context.USB_SERVICE);

    usbServiceStarted = true;
    eventEmit(onServiceStarted, null);
    checkAutoConnect();
  }

  @ReactMethod
  public void stopUsbService() {
    if(serialPortConnected) {
      eventEmit(onErrorEvent, createError(definitions.ERROR_SERVICE_STOP_FAILED, definitions.ERROR_SERVICE_STOP_FAILED_MESSAGE));
      return;
    }
    if(!usbServiceStarted) {
      return;
    }
    reactContext.unregisterReceiver(mUsbReceiver);
    usbServiceStarted = false;
    eventEmit(onServiceStopped, null);
  }

  @ReactMethod
  public void getDeviceList(Callback callback) {
    if(!usbServiceStarted) {
      callback.invoke(createError(definitions.ERROR_USB_SERVICE_NOT_STARTED, definitions.ERROR_USB_SERVICE_NOT_STARTED_MESSAGE));
      return;
    }

    UsbManager manager = (UsbManager) reactContext.getSystemService(Context.USB_SERVICE);

    HashMap<String, UsbDevice> devices = manager.getDeviceList();

    if(devices.isEmpty()) {
      callback.invoke(createError(definitions.ERROR_DEVICE_NOT_FOUND, definitions.ERROR_DEVICE_NOT_FOUND_MESSAGE));
      return;
    }

    WritableArray deviceList = Arguments.createArray();
    for(Map.Entry<String, UsbDevice> entry: devices.entrySet()) {
      UsbDevice d = entry.getValue();

      WritableMap map = Arguments.createMap();
      map.putString("name", d.getDeviceName());
      map.putInt("vendorId", d.getVendorId());
      map.putInt("productId", d.getProductId());

      deviceList.pushMap(map);
    }

    WritableMap map = Arguments.createMap();
    map.putBoolean("status", true);
    map.putArray("devices", deviceList);

    callback.invoke(map);
  }

  @ReactMethod
  public void setAutoConnect(boolean autoConnect) {
    this.autoConnect = autoConnect;
  }


  @ReactMethod
  public void setAutoConnectBaudRate(int baudRate) {
    autoConnectBaudRate = baudRate;
  }

  @ReactMethod
  public void connectDevice(String deviceName, int baudRate) {
    try {
      if(!usbServiceStarted){
        eventEmit(onErrorEvent, createError(definitions.ERROR_USB_SERVICE_NOT_STARTED, definitions.ERROR_USB_SERVICE_NOT_STARTED_MESSAGE));
        return;
      }

      if(serialPortConnected) {
        eventEmit(onErrorEvent, createError(definitions.ERROR_SERIALPORT_ALREADY_CONNECTED, definitions.ERROR_SERIALPORT_ALREADY_CONNECTED_MESSAGE));
        return;
      }

      if(deviceName.isEmpty() || deviceName.length() < 0) {
        eventEmit(onErrorEvent, createError(definitions.ERROR_CONNECT_DEVICE_NAME_INVALID, definitions.ERROR_CONNECT_DEVICE_NAME_INVALID_MESSAGE));
        return;
      }

      if(baudRate < 1){
        eventEmit(onErrorEvent, createError(definitions.ERROR_CONNECT_BAUDRATE_EMPTY, definitions.ERROR_CONNECT_BAUDRATE_EMPTY_MESSAGE));
        return;
      }

      if(!autoConnect) {
        this.BAUD_RATE = baudRate;
      }

      if(!chooseDevice(deviceName)) {
        eventEmit(onErrorEvent, createError(definitions.ERROR_X_DEVICE_NOT_FOUND, definitions.ERROR_X_DEVICE_NOT_FOUND_MESSAGE + deviceName));
        return;
      }

      requestUserPermission();

    } catch (Exception err) {
      eventEmit(onErrorEvent, createError(definitions.ERROR_CONNECTION_FAILED, definitions.ERROR_CONNECTION_FAILED_MESSAGE + " Catch Error Message:" + err.getMessage()));
    }
  }

  @ReactMethod
  public void disconnect() {
    if(!usbServiceStarted){
      eventEmit(onErrorEvent, createError(definitions.ERROR_USB_SERVICE_NOT_STARTED, definitions.ERROR_USB_SERVICE_NOT_STARTED_MESSAGE));
      return;
    }

    if(!serialPortConnected) {
      eventEmit(onErrorEvent, createError(definitions.ERROR_SERIALPORT_ALREADY_DISCONNECTED, definitions.ERROR_SERIALPORT_ALREADY_DISCONNECTED_MESSAGE));
      return;
    }
    stopConnection();
  }

  @ReactMethod
  public void isOpen(Callback callback) {
    if(serialPortConnected)
      callback.invoke(true);
    else
      callback.invoke(false);
  }

  @ReactMethod
  public void writeBytes(byte[] bytes) {
    if(!usbServiceStarted){
      eventEmit(onErrorEvent, createError(definitions.ERROR_USB_SERVICE_NOT_STARTED, definitions.ERROR_USB_SERVICE_NOT_STARTED_MESSAGE));
      return;
    }
    if(!serialPortConnected || serialPort == null) {
      eventEmit(onErrorEvent, createError(definitions.ERROR_THERE_IS_NO_CONNECTION, definitions.ERROR_THERE_IS_NO_CONNECTION_MESSAGE));
      return;
    }
    serialPort.write(bytes);
  }

  @ReactMethod
  public void writeString(String data) {
    if(!usbServiceStarted){
      eventEmit(onErrorEvent, createError(definitions.ERROR_USB_SERVICE_NOT_STARTED, definitions.ERROR_USB_SERVICE_NOT_STARTED_MESSAGE));
      return;
    }
    if(!serialPortConnected || serialPort == null) {
      eventEmit(onErrorEvent, createError(definitions.ERROR_THERE_IS_NO_CONNECTION, definitions.ERROR_THERE_IS_NO_CONNECTION_MESSAGE));
      return;
    }
    serialPort.write(data.getBytes());
  }

  ///////////////////////////////////////////////USB SERVICE /////////////////////////////////////////////////////////
  ///////////////////////////////////////////////USB SERVICE /////////////////////////////////////////////////////////

  private boolean chooseDevice(String deviceName) {
    HashMap<String, UsbDevice> usbDevices = usbManager.getDeviceList();
    if(usbDevices.isEmpty()) {
      return false;
    }

    boolean selected = false;

    for (Map.Entry<String, UsbDevice> entry: usbDevices.entrySet()) {
      UsbDevice d = entry.getValue();

      if(d.getDeviceName().equals(deviceName)) {
        device = d;
        selected = true;
        break;
      }
    }

    return selected;
  }

  private boolean chooseFirstDevice() {
    HashMap<String, UsbDevice> usbDevices = usbManager.getDeviceList();
    if(usbDevices.isEmpty()) {
      return false;
    }

    boolean selected = false;

    for (Map.Entry<String, UsbDevice> entry: usbDevices.entrySet()) {
      UsbDevice d = entry.getValue();

      int deviceVID = d.getVendorId();
      int devicePID = d.getProductId();

      if (deviceVID != 0x1d6b && (devicePID != 0x0001 && devicePID != 0x0002 && devicePID != 0x0003) && deviceVID != 0x5c6 && devicePID != 0x904c)
      {
        device = d;
        autoConnectDeviceName = d.getDeviceName();
        selected = true;
        break;
      }
    }
    return selected;
  }

  private void checkAutoConnect() {
    if(!autoConnect || serialPortConnected)
      return;

    if(chooseFirstDevice()) {
      connectDevice(autoConnectDeviceName, autoConnectBaudRate);
    }
  }
  private class ConnectionThread extends Thread {
    @Override
    public void run() {
      serialPort = UsbSerialDevice.createUsbSerialDevice(device, connection);
      if(serialPort == null) {
        // No driver for given device
        Intent intent = new Intent(ACTION_USB_NOT_SUPPORTED);
        reactContext.sendBroadcast(intent);
        return;
      }

      if(!serialPort.open()){
        Intent intent = new Intent(ACTION_USB_NOT_OPENED);
        reactContext.sendBroadcast(intent);
        return;
      }

      serialPortConnected = true;
      int baud;
      if(autoConnect){
        baud = autoConnectBaudRate;
      }else {
        baud = BAUD_RATE;
      }
      serialPort.setBaudRate(baud);
      serialPort.setDataBits(DATA_BIT);
      serialPort.setStopBits(STOP_BIT);
      serialPort.setParity(PARITY);
      serialPort.setFlowControl(FLOW_CONTROL);
      serialPort.read(mCallback);

      Intent intent = new Intent(ACTION_USB_READY);
      reactContext.sendBroadcast(intent);
      intent = new Intent(ACTION_USB_CONNECT);
      reactContext.sendBroadcast(intent);
    }
  }

  private void requestUserPermission() {
    if(device == null)
      return;
    PendingIntent mPendingIntent = PendingIntent.getBroadcast(reactContext, 0 , new Intent(ACTION_USB_PERMISSION), 0);
    usbManager.requestPermission(device, mPendingIntent);
  }

  private void startConnection(boolean granted) {
    if(granted) {
      Intent intent = new Intent(ACTION_USB_PERMISSION_GRANTED);
      reactContext.sendBroadcast(intent);
      connection = usbManager.openDevice(device);
      new ConnectionThread().start();
    } else {
      connection = null;
      device = null;
      Intent intent = new Intent(ACTION_USB_PERMISSION_NOT_GRANTED);
      reactContext.sendBroadcast(intent);
    }
  }

  private void stopConnection() {
    if (serialPortConnected) {
      serialPort.close();
      connection = null;
      device = null;
      Intent intent = new Intent(ACTION_USB_DISCONNECTED);
      reactContext.sendBroadcast(intent);
      serialPortConnected = false;
    } else {
      Intent intent = new Intent(ACTION_USB_DETACHED);
      reactContext.sendBroadcast(intent);
    }
  }

  private UsbSerialInterface.UsbReadCallback mCallback = new UsbSerialInterface.UsbReadCallback() {
    @Override
    public void onReceivedData(byte[] bytes) {
      try {
        Intent intent = new Intent("ONREADDATAFROMPORT");
        intent.putExtra("byteArray", bytes);
        reactContext.sendBroadcast(intent);

        String data = definitions.bytesToHex(bytes);

        eventEmit(onReadDataFromPort, data);

      } catch (Exception err) {
        eventEmit(onErrorEvent, createError(definitions.ERROR_NOT_READED_DATA, definitions.ERROR_NOT_READED_DATA_MESSAGE + " System Message: " + err.getMessage()));
      }
    }
  };
}