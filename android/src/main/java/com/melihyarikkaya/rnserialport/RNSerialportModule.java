package com.melihyarikkaya.rnserialport;

import android.app.PendingIntent;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.facebook.react.bridge.WritableNativeArray;

import android.util.Base64;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.primitives.UnsignedBytes;

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
    fillDriverList();
  }

  @Override
  public String getName() {
    return "RNSerialport";
  }

  private final String ACTION_USB_READY = "com.felhr.connectivityservices.USB_READY";
  private final String ACTION_USB_ATTACHED = "android.hardware.usb.action.USB_DEVICE_ATTACHED";
  private final String ACTION_USB_DETACHED = "android.hardware.usb.action.USB_DEVICE_DETACHED";
  private final String ACTION_USB_NOT_SUPPORTED = "com.felhr.usbservice.USB_NOT_SUPPORTED";
  private final String ACTION_NO_USB = "com.felhr.usbservice.NO_USB";
  private final String ACTION_USB_PERMISSION_GRANTED = "com.felhr.usbservice.USB_PERMISSION_GRANTED";
  private final String ACTION_USB_PERMISSION_NOT_GRANTED = "com.felhr.usbservice.USB_PERMISSION_NOT_GRANTED";
  private final String ACTION_USB_DISCONNECTED = "com.felhr.usbservice.USB_DISCONNECTED";
  private final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
  private final String ACTION_USB_NOT_OPENED = "com.melihyarikkaya.rnserialport.USB_NOT_OPENED";
  private final String ACTION_USB_CONNECT = "com.melihyarikkaya.rnserialport.USB_CONNECT";

  //react-native events
  private final String onErrorEvent              = "onError";
  private final String onConnectedEvent          = "onConnected";
  private final String onDisconnectedEvent       = "onDisconnected";
  private final String onDeviceAttachedEvent     = "onDeviceAttached";
  private final String onDeviceDetachedEvent     = "onDeviceDetached";
  private final String onServiceStarted          = "onServiceStarted";
  private final String onServiceStopped          = "onServiceStopped";
  private final String onReadDataFromPort        = "onReadDataFromPort";
  private final String onUsbPermissionGranted    = "onUsbPermissionGranted";

  //SUPPORTED DRIVER LIST

  private List<String> driverList;

  private UsbManager usbManager;
  private UsbDevice device;
  private UsbDeviceConnection connection;
  private UsbSerialDevice serialPort;
  private boolean serialPortConnected;

  //Connection Settings
  private int DATA_BIT     = UsbSerialInterface.DATA_BITS_8;
  private int STOP_BIT     = UsbSerialInterface.STOP_BITS_1;
  private int PARITY       =  UsbSerialInterface.PARITY_NONE;
  private int FLOW_CONTROL = UsbSerialInterface.FLOW_CONTROL_OFF;
  private int BAUD_RATE = 9600;


  private boolean autoConnect = false;
  private String autoConnectDeviceName;
  private int autoConnectBaudRate = 9600;
  private int portInterface = -1;
  private int returnedDataType = Definitions.RETURNED_DATA_TYPE_INTARRAY;
  private String driver = "AUTO";


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
          eventEmit(onErrorEvent, createError(Definitions.ERROR_DEVICE_NOT_SUPPORTED, Definitions.ERROR_DEVICE_NOT_SUPPORTED_MESSAGE));
          break;
        case ACTION_USB_NOT_OPENED:
          eventEmit(onErrorEvent, createError(Definitions.ERROR_COULD_NOT_OPEN_SERIALPORT, Definitions.ERROR_COULD_NOT_OPEN_SERIALPORT_MESSAGE));
          break;
        case ACTION_USB_ATTACHED:
          eventEmit(onDeviceAttachedEvent, null);
          if(autoConnect && chooseFirstDevice()) {
            connectDevice(autoConnectDeviceName, autoConnectBaudRate);
          }
          break;
        case ACTION_USB_DETACHED:
          eventEmit(onDeviceDetachedEvent, null);
          if(serialPortConnected) {
            stopConnection();
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
          eventEmit(onErrorEvent, createError(Definitions.ERROR_USER_DID_NOT_ALLOW_TO_CONNECT, Definitions.ERROR_USER_DID_NOT_ALLOW_TO_CONNECT_MESSAGE));
          break;
      }
    }
  };

  private void eventEmit(String eventName, Object data) {
    try {
      if(reactContext.hasActiveCatalystInstance()) {
        reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventName, data);
      }
    }
    catch (Exception error) {}
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

  private void fillDriverList() {
    driverList = new ArrayList<>();
    driverList.add("ftdi");
    driverList.add("cp210x");
    driverList.add("pl2303");
    driverList.add("ch34x");
    driverList.add("cdc");
  }

  /******************************* BEGIN PUBLIC SETTER METHODS **********************************/

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
  public void setAutoConnect(boolean autoConnect) {
    this.autoConnect = autoConnect;
  }
  @ReactMethod
  public void setAutoConnectBaudRate(int baudRate) {
    this.autoConnectBaudRate = baudRate;
  }
  @ReactMethod
  public void setInterface(int iFace) {
    this.portInterface = iFace;
  }
  @ReactMethod
  public void setReturnedDataType(int type) {
    if(type == Definitions.RETURNED_DATA_TYPE_HEXSTRING || type == Definitions.RETURNED_DATA_TYPE_INTARRAY) {
      this.returnedDataType = type;
    }
  }

  @ReactMethod
  public void setDriver(String driver) {
    if(driver.isEmpty() || !driverList.contains(driver.trim())) {
      eventEmit(onErrorEvent, createError(Definitions.ERROR_DRIVER_TYPE_NOT_FOUND, Definitions.ERROR_DRIVER_TYPE_NOT_FOUND_MESSAGE));
      return;
    }

    this.driver = driver;
  }

  /********************************************* END **********************************************/

  @ReactMethod
  public void startUsbService() {
    if(usbServiceStarted) {
      return;
    }
    setFilters();

    usbManager = (UsbManager) reactContext.getSystemService(Context.USB_SERVICE);

    usbServiceStarted = true;

    //Return usb status when service is started.
    WritableMap map = Arguments.createMap();

    map.putBoolean("deviceAttached", !usbManager.getDeviceList().isEmpty());

    eventEmit(onServiceStarted, map);

    checkAutoConnect();
  }

  @ReactMethod
  public void stopUsbService() {
    if(serialPortConnected) {
      eventEmit(onErrorEvent, createError(Definitions.ERROR_SERVICE_STOP_FAILED, Definitions.ERROR_SERVICE_STOP_FAILED_MESSAGE));
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
      callback.invoke(createError(Definitions.ERROR_USB_SERVICE_NOT_STARTED, Definitions.ERROR_USB_SERVICE_NOT_STARTED_MESSAGE));
      return;
    }

    UsbManager manager = (UsbManager) reactContext.getSystemService(Context.USB_SERVICE);

    HashMap<String, UsbDevice> devices = manager.getDeviceList();

    if(devices.isEmpty()) {
      callback.invoke(createError(Definitions.ERROR_DEVICE_NOT_FOUND, Definitions.ERROR_DEVICE_NOT_FOUND_MESSAGE));
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
  public void connectDevice(String deviceName, int baudRate) {
    try {
      if(!usbServiceStarted){
        eventEmit(onErrorEvent, createError(Definitions.ERROR_USB_SERVICE_NOT_STARTED, Definitions.ERROR_USB_SERVICE_NOT_STARTED_MESSAGE));
        return;
      }

      if(serialPortConnected) {
        eventEmit(onErrorEvent, createError(Definitions.ERROR_SERIALPORT_ALREADY_CONNECTED, Definitions.ERROR_SERIALPORT_ALREADY_CONNECTED_MESSAGE));
        return;
      }

      if(deviceName.isEmpty() || deviceName.length() < 0) {
        eventEmit(onErrorEvent, createError(Definitions.ERROR_CONNECT_DEVICE_NAME_INVALID, Definitions.ERROR_CONNECT_DEVICE_NAME_INVALID_MESSAGE));
        return;
      }

      if(baudRate < 1){
        eventEmit(onErrorEvent, createError(Definitions.ERROR_CONNECT_BAUDRATE_EMPTY, Definitions.ERROR_CONNECT_BAUDRATE_EMPTY_MESSAGE));
        return;
      }

      if(!autoConnect) {
        this.BAUD_RATE = baudRate;
      }

      if(!chooseDevice(deviceName)) {
        eventEmit(onErrorEvent, createError(Definitions.ERROR_X_DEVICE_NOT_FOUND, Definitions.ERROR_X_DEVICE_NOT_FOUND_MESSAGE + deviceName));
        return;
      }

      requestUserPermission();

    } catch (Exception err) {
      eventEmit(onErrorEvent, createError(Definitions.ERROR_CONNECTION_FAILED, Definitions.ERROR_CONNECTION_FAILED_MESSAGE + " Catch Error Message:" + err.getMessage()));
    }
  }

  @ReactMethod
  public void disconnect() {
    if(!usbServiceStarted){
      eventEmit(onErrorEvent, createError(Definitions.ERROR_USB_SERVICE_NOT_STARTED, Definitions.ERROR_USB_SERVICE_NOT_STARTED_MESSAGE));
      return;
    }

    if(!serialPortConnected) {
      eventEmit(onErrorEvent, createError(Definitions.ERROR_SERIALPORT_ALREADY_DISCONNECTED, Definitions.ERROR_SERIALPORT_ALREADY_DISCONNECTED_MESSAGE));
      return;
    }
    stopConnection();
  }

  @ReactMethod
  public void isOpen(Promise promise) {
    promise.resolve(serialPortConnected);
  }

 @ReactMethod
 public void isServiceStarted(Promise promise) {
    promise.resolve(usbServiceStarted);
 }

  @ReactMethod
  public void isSupported(String deviceName, Promise promise) {
    if(!chooseDevice(deviceName)) {
      promise.reject(String.valueOf(Definitions.ERROR_DEVICE_NOT_FOUND), Definitions.ERROR_DEVICE_NOT_FOUND_MESSAGE);
    } else {
      promise.resolve(UsbSerialDevice.isSupported(device));
    }
  }

  @ReactMethod
  public void writeBytes(byte[] bytes) {
    if(!usbServiceStarted){
      eventEmit(onErrorEvent, createError(Definitions.ERROR_USB_SERVICE_NOT_STARTED, Definitions.ERROR_USB_SERVICE_NOT_STARTED_MESSAGE));
      return;
    }
    if(!serialPortConnected || serialPort == null) {
      eventEmit(onErrorEvent, createError(Definitions.ERROR_THERE_IS_NO_CONNECTION, Definitions.ERROR_THERE_IS_NO_CONNECTION_MESSAGE));
      return;
    }
    serialPort.write(bytes);
  }

  @ReactMethod
  public void writeString(String message) {
    if(!usbServiceStarted){
      eventEmit(onErrorEvent, createError(Definitions.ERROR_USB_SERVICE_NOT_STARTED, Definitions.ERROR_USB_SERVICE_NOT_STARTED_MESSAGE));
      return;
    }
    if(!serialPortConnected || serialPort == null) {
      eventEmit(onErrorEvent, createError(Definitions.ERROR_THERE_IS_NO_CONNECTION, Definitions.ERROR_THERE_IS_NO_CONNECTION_MESSAGE));
      return;
    }

    serialPort.write(message.getBytes());
  }

  @ReactMethod
  public void writeBase64(String message) {
    if(!usbServiceStarted){
      eventEmit(onErrorEvent, createError(Definitions.ERROR_USB_SERVICE_NOT_STARTED, Definitions.ERROR_USB_SERVICE_NOT_STARTED_MESSAGE));
      return;
    }
    if(!serialPortConnected || serialPort == null) {
      eventEmit(onErrorEvent, createError(Definitions.ERROR_THERE_IS_NO_CONNECTION, Definitions.ERROR_THERE_IS_NO_CONNECTION_MESSAGE));
      return;
    }

    byte [] data = Base64.decode(message, Base64.DEFAULT);
    serialPort.write(data);
  }

  @ReactMethod
  public void writeHexString(String message) {
    if(!usbServiceStarted){
      eventEmit(onErrorEvent, createError(Definitions.ERROR_USB_SERVICE_NOT_STARTED, Definitions.ERROR_USB_SERVICE_NOT_STARTED_MESSAGE));
      return;
    }
    if(!serialPortConnected || serialPort == null) {
      eventEmit(onErrorEvent, createError(Definitions.ERROR_THERE_IS_NO_CONNECTION, Definitions.ERROR_THERE_IS_NO_CONNECTION_MESSAGE));
      return;
    }

    if(message.length() < 1) {
      return;
    }

    byte[] data = new byte[message.length() / 2];
    for (int i = 0; i < data.length; i++) {
      int index = i * 2;

      String hex = message.substring(index, index + 2);

      if(Definitions.hexChars.indexOf(hex.substring(0, 1)) == -1 || Definitions.hexChars.indexOf(hex.substring(1, 1)) == -1) {
          return;
      }

      int v = Integer.parseInt(hex, 16);
      data[i] = (byte) v;
    }
    serialPort.write(data);
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
      try {
        if(driver.equals("AUTO")) {
          serialPort = UsbSerialDevice.createUsbSerialDevice(device, connection, portInterface);
        } else {
          serialPort = UsbSerialDevice.createUsbSerialDevice(driver, device, connection, portInterface);
        }
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
      } catch (Exception error) {
        WritableMap map = createError(Definitions.ERROR_CONNECTION_FAILED, Definitions.ERROR_CONNECTION_FAILED_MESSAGE);
        map.putString("exceptionErrorMessage", error.getMessage());
        eventEmit(onErrorEvent, map);
      }
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

        String payloadKey = "payload";

        WritableMap params = Arguments.createMap();

        if(returnedDataType == Definitions.RETURNED_DATA_TYPE_INTARRAY) {

          WritableArray intArray = new WritableNativeArray();
          for(byte b: bytes) {
            intArray.pushInt(UnsignedBytes.toInt(b));
          }
          params.putArray(payloadKey, intArray);

        } else if(returnedDataType == Definitions.RETURNED_DATA_TYPE_HEXSTRING) {
          String hexString = Definitions.bytesToHex(bytes);
          params.putString(payloadKey, hexString);
        } else
          return;

        eventEmit(onReadDataFromPort, params);

      } catch (Exception err) {
        eventEmit(onErrorEvent, createError(Definitions.ERROR_NOT_READED_DATA, Definitions.ERROR_NOT_READED_DATA_MESSAGE + " System Message: " + err.getMessage()));
      }
    }
  };
}