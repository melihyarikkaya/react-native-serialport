
# react-native-serialport

#### This library is for usb serial port communication on android platform 

### This module uses the [felHR85/UsbSerial](https://github.com/felHR85/UsbSerial) library

### Documents
1. [Download & Installation](https://github.com/melihyarikkaya/react-native-serialport/wiki/Download-&-Installation)  
2. [Auto Connection](https://github.com/melihyarikkaya/react-native-serialport/wiki/Auto-Connection)  
3. [Manual Connection](https://github.com/melihyarikkaya/react-native-serialport/wiki/Manual-Connection)  
4. [Methods](https://github.com/melihyarikkaya/react-native-serialport/wiki/Methods)  
5. [Error Descriptions]()

### For Write Port
```javascript
 RNSerialport.writeString("HELLO")
 RNSerialport.writeBase64("SEVMTE8=")
```
### DEFAULT DEFINITIONS
| KEY                    | VALUE                                    |
|------------------------|------------------------------------------|
| RETURNED DATA TYPE     | INT ARRAY (Options: INTARRAY, HEXSTRING) |
| BAUND RATE             | 9600                                     |
| AUTO CONNECT BAUD RATE | 9600                                     |
| PORT INTERFACE         | -1                                       |
| DATA BIT               | 8                                        |
| STOP BIT               | 1                                        |
| PARITY                 | NONE                                     |
| FLOW CONTROL           | OFF                                      |

### ERRORS
| CODE |                            MESSAGE                           |
|:----:|:------------------------------------------------------------:|
|   1  | Device not found!                                            |
|   2  | Device name cannot be invalid or empty!                      |
|   3  | BaudRate cannot be invalid!                                  |
|   4  | Connection Failed!                                           |
|   5  | Could not open Serial Port!                                  |
|   6  | Disconnect Failed!                                           |
|   7  | Serial Port is already connected                             |
|   8  | Serial Port is already disconnected                          |
|   9  | Usb service not started. Please first start Usb service!     |
|  10  | No device with name {Device name}                            |
|  11  | User did not allow to connect                                |
|  12  | Service could not stopped off. Please close connection first |
|  13  | There is no connection                                       |
|  14  | Error reading from port                                      |
|  15  | Driver type is not defined                                   |
|  16  | Device not supported                                         |

### ALL VOIDS

| VOID                                             | DESCRIPTION                                                              |
|--------------------------------------------------|--------------------------------------------------------------------------|
| setReturnedDataType(dataType: int)               | To set the returned data type int array or hex string                    |
| setInterface(interface: int)                     | To set the device interface                                              |
| setDriver(driver: string)                        | To set the driver                                                        |
| setDataBit(dataBit: int)                         | To set the data bit                                                      |
| setStopBit(stopBit: int)                         | To set the stop bit                                                      |
| setParity(parity: int)                           | To set the parity                                                        |
| setFlowControl(flowControl: int)                 | To set the flow control                                                  |
| loadDefaultConnectionSetting()                   | Loads default connection settings (DATABIT, STOPBIT,PARITY, FLOWCONTROL) |
| setAutoConnectBaudRate(baudRate: int)            | Changes the Baud Rate used in auto-connection                            |
| setAutoConnect(state: boolean)                   | Turns automatic connection on or off                                     |
| startUsbService()                                | Starts the service and broadcast receivers                               |
| stopUsbService()                                 | Stops the service and broadcast receivers                                |
| getDeviceList(callback: void)                    | Receives device list                                                     |
| connectDevice(deviceName: string, baudRate: int) | Used for manual connection                                               |
| disconnect()                                     | Used to break the connection.                                            |
| isOpen(callback: void)                           | Receives connection status                                               |
| isSupported(deviceName: string): promise         | Check device support                                                     |
| writeString(data: string)                        | Writes data to serial port                                               |
| writeBase64(data: string)                        | Writes Base64 to serial port                                             |

### Java Package Name
#### com.melihyarikkaya.rnserialport