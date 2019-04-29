package com.melihyarikkaya.rnserialport;

/**
 * Created by melih on 12.10.2018.
 */

public class Definitions {

    ////////////////////////// Errors //////////////////////////

    public static final int ERROR_DEVICE_NOT_FOUND                = 1;
    public static final int ERROR_CONNECT_DEVICE_NAME_INVALID     = 2;
    public static final int ERROR_CONNECT_BAUDRATE_EMPTY          = 3;
    public static final int ERROR_CONNECTION_FAILED               = 4;
    public static final int ERROR_COULD_NOT_OPEN_SERIALPORT       = 5;
    public static final int ERROR_DISCONNECT_FAILED               = 6;
    public static final int ERROR_SERIALPORT_ALREADY_CONNECTED    = 7;
    public static final int ERROR_SERIALPORT_ALREADY_DISCONNECTED = 8;
    public static final int ERROR_USB_SERVICE_NOT_STARTED         = 9;
    public static final int ERROR_X_DEVICE_NOT_FOUND              = 10;
    public static final int ERROR_USER_DID_NOT_ALLOW_TO_CONNECT   = 11;
    public static final int ERROR_SERVICE_STOP_FAILED             = 12;
    public static final int ERROR_THERE_IS_NO_CONNECTION          = 13;
    public static final int ERROR_NOT_READED_DATA                 = 14;
    public static final int ERROR_DRIVER_TYPE_NOT_FOUND           = 15;
    public static final int ERROR_DEVICE_NOT_SUPPORTED            = 16;
    public static final int ERROR_SERVICE_ALREADY_STARTED         = 17;
    public static final int ERROR_SERVICE_ALREADY_STOPPED         = 18;


    public static final String ERROR_DEVICE_NOT_FOUND_MESSAGE                   = "Device not found!";
    public static final String ERROR_CONNECT_DEVICE_NAME_INVALID_MESSAGE        = "Device name cannot be invalid or empty!";
    public static final String ERROR_CONNECT_BAUDRATE_EMPTY_MESSAGE             = "BaudRate cannot be invalid!";
    public static final String ERROR_CONNECTION_FAILED_MESSAGE                  = "Connection Failed!";
    public static final String ERROR_COULD_NOT_OPEN_SERIALPORT_MESSAGE          = "Could not open Serial Port!";
    public static final String ERROR_DISCONNECT_FAILED_MESSAGE                  = "Disconnect Failed!";
    public static final String ERROR_SERIALPORT_ALREADY_CONNECTED_MESSAGE       = "Serial Port is already connected";
    public static final String ERROR_SERIALPORT_ALREADY_DISCONNECTED_MESSAGE    = "Serial Port is already disconnected";
    public static final String ERROR_USB_SERVICE_NOT_STARTED_MESSAGE            = "Usb service not started. Please first start Usb service!";
    public static final String ERROR_X_DEVICE_NOT_FOUND_MESSAGE                 = "No device with name ";
    public static final String ERROR_USER_DID_NOT_ALLOW_TO_CONNECT_MESSAGE      = "User did not allow to connect";
    public static final String ERROR_SERVICE_STOP_FAILED_MESSAGE                = "Service could not stopped. Please first close connection";
    public static final String ERROR_THERE_IS_NO_CONNECTION_MESSAGE             = "There is no connection";
    public static final String ERROR_NOT_READED_DATA_MESSAGE                    = "Error reading from port";
    public static final String ERROR_DRIVER_TYPE_NOT_FOUND_MESSAGE              = "Driver type is not defined";
    public static final String ERROR_DEVICE_NOT_SUPPORTED_MESSAGE               = "Device not supported";
    public static final String ERROR_SERVICE_ALREADY_STARTED_MESSAGE            = "Usb service is already started";;
    public static final String ERROR_SERVICE_ALREADY_STOPPED_MESSAGE            = "Usb service is already stopped";;
    ///////////////////////////////////////////////////////////

    public static final int RETURNED_DATA_TYPE_INTARRAY = 1;
    public static final int RETURNED_DATA_TYPE_HEXSTRING = 2;

    public final static String hexChars = "0123456789ABCDEF";
    private final static char[] hexArray = hexChars.toCharArray();

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

}
