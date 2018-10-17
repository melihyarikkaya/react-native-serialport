package com.reactlibrary;

/**
 * Created by melih on 12.10.2018.
 */

public class Definitions {

    ////////////////////////// Errors //////////////////////////

    public int ERROR_DEVICE_NOT_FOUND = 1;
    public String ERROR_DEVICE_NOT_FOUND_MESSAGE = "Device not found!";

    public int ERROR_DEVICE_READ_FAILED = 2;

    public int ERROR_CONNECT_DEVICE_NAME_INVALID = 3;
    public String ERROR_CONNECT_DEVICE_NAME_INVALID_MESSAGE = "Device name cannot be invalid or empty!";

    public int ERROR_CONNECT_BAUDRATE_EMPTY = 4;
    public String ERROR_CONNECT_BAUDRATE_EMPTY_MESSAGE = "BaudRate cannot be invalid!";

    public int ERROR_CONNECTION_FAILED = 5;
    public String ERROR_CONNECTION_FAILED_MESSAGE =  "Connection Failed!";

    public int ERROR_COULD_NOT_OPEN_SERIALPORT = 6;
    public String ERROR_COULD_NOT_OPEN_SERIALPORT_MESSAGE = "Could not open Serial Port!";

    public int ERROR_DISCONNECT_FAILED = 7;
    public String ERROR_DISCONNECT_FAILED_MESSAGE = "Disconnect Failed!";

    public int ERROR_SERIALPORT_ALREADY_CONNECTED = 8;
    public String ERROR_SERIALPORT_ALREADY_CONNECTED_MESSAGE = "Serial Port is already connected";

    public int ERROR_SERIALPORT_ALREADY_DISCONNECTED = 8;
    public String ERROR_SERIALPORT_ALREADY_DISCONNECTED_MESSAGE = "Serial Port is already disconnected";

    public int ERROR_USB_SERVICE_NOT_STARTED = 9;
    public String ERROR_USB_SERVICE_NOT_STARTED_MESSAGE = "Usb service not started. Please first start Usb service!";

    public int ERROR_X_DEVICE_NOT_FOUND = 10;
    public String ERROR_X_DEVICE_NOT_FOUND_MESSAGE = "No device with name ";

    public int ERROR_USER_DID_NOT_ALLOW_TO_CONNECT = 11;
    public String ERROR_USER_DID_NOT_ALLOW_TO_CONNECT_MESSAGE = "User did not allow to connect";

    public int ERROR_SERVICE_STOP_FAILED = 12;
    public String ERROR_SERVICE_STOP_FAILED_MESSAGE = "Service could not stopped off. Please close connection first";
    ///////////////////////////////////////////////////////////

}
