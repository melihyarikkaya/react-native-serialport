
# react-native-serialport

#### This library is for usb serial port communication on android platform 

### This module uses the [felHR85/UsbSerial](https://github.com/felHR85/UsbSerial) library

## Getting started

`$ npm install react-native-serialport --save`

### Mostly automatic installation

`$ react-native link react-native-serialport`

Change android/build.gradle minSdkVerision: 23

### Manual installation

#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import com.reactlibrary.RNSerialportPackage;` to the imports at the top of the file
  - Add `new RNSerialportPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-serialport'
  	project(':react-native-serialport').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-serialport/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-serialport')
  	```
4. Change android/build.gradle minSdkVerision: 23

## Usage

### Auto Connection

To open the connection when the service is started or the usb device is attached should be looked

```javascript
import { RNSerialport } from 'react-native-serialport'
import {DeviceEventEmitter} from 'react-native'
  .
  .
  .
  .
onUsbAttached() { //this._getDeviceList() }
onUsbDetached() {alert('Usb Detached')}
onUsbNotSupperted() {alert('Usb not supported')}
onError(error) {alert('Code: ' + error.errorCode + ' Message: ' +error.errorMessage)}
onConnectedDevice() {alert('Connected')}
onDisconnectedDevice() {alert('Disconnected')}
onServiceStarted() {alert('Service started')}
onServiceStopped() {alert('Service stopped')}
onReadData(data) {
  alert(data) //string
}
componentDidMount() {
  DeviceEventEmitter.addListener('onServiceStarted', this.onServiceStarted, this)
  DeviceEventEmitter.addListener('onServiceStopped', this.onServiceStopped,this)
  DeviceEventEmitter.addListener('onDeviceAttached', this.onUsbAttached, this)
  DeviceEventEmitter.addListener('onDeviceDetached', this.onUsbDetached, this)
  DeviceEventEmitter.addListener('onDeviceNotSupported', this.onUsbNotSupperted, this)
  DeviceEventEmitter.addListener('onError', this.onError, this)
  DeviceEventEmitter.addListener('onConnected', this.onConnectedDevice, this)
  DeviceEventEmitter.addListener('onDisconnected', this.onDisconnectedDevice, this)
  DeviceEventEmitter.addListener('onReadDataFromPort', this.onReadData, this)

  //Added listeners

  RNSerialport.setAutoConnectBaudRate(9600)
  RNSerialport.setAutoConnect(true)
  RNSerialport.startUsbService()
}

componentWillMount() {
    DeviceEventEmitter.removeAllListeners()
}
```
### Manuel Connection

```javascript

import { RNSerialport } from 'react-native-serialport'
import {DeviceEventEmitter} from 'react-native'
  .
  .
  .
  .
onUsbAttached() { //this._getDeviceList() }
onUsbDetached() {alert('Usb Detached')}
onUsbNotSupperted() {alert('Usb not supported')}
onError(error) {alert('Code: ' + error.errorCode + ' Message: ' +error.errorMessage)}
onConnectedDevice() {alert('Connected')}
onDisconnectedDevice() {alert('Disconnected')}
onServiceStarted() {
  alert('Service started')
  this._getDeviceList()
}
onServiceStopped() {alert('Service stopped')}
onReadData(data) {
  alert(data) //string
}

componentDidMount() {
  DeviceEventEmitter.addListener('onServiceStarted', this.onServiceStarted, this)
  DeviceEventEmitter.addListener('onServiceStopped', this.onServiceStopped,this)
  DeviceEventEmitter.addListener('onDeviceAttached', this.onUsbAttached, this)
  DeviceEventEmitter.addListener('onDeviceDetached', this.onUsbDetached, this)
  DeviceEventEmitter.addListener('onDeviceNotSupported', this.onUsbNotSupperted, this)
  DeviceEventEmitter.addListener('onError', this.onError, this)
  DeviceEventEmitter.addListener('onConnected', this.onConnectedDevice, this)
  DeviceEventEmitter.addListener('onDisconnected', this.onDisconnectedDevice, this)
  DeviceEventEmitter.addListener('onReadDataFromPort', this.onReadData, this)

  //Added listeners
  RNSerialport.setAutoConnect(false)
  RNSerialport.startUsbService()
}

componentWillMount() {
    DeviceEventEmitter.removeAllListeners()
}

_getDeviceList() {
  RNSerialport.getDeviceList((response) => {
    if(!response.status) {
      alert('Code: ' + response.errorCode + ' Message: ' +response.errorMessage)
      return
    } 
    console.log(response.devices)
    let deviceName = response.devices[0].name
    let baudRate = 9600

    RNSerialport.connectDevice(deviceName, baudRate)
  })
}
```


### Write Port
 ```javascript
 RNSerialport.writeString("HELLO")
```
## ERRORS
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

## ALL VOIDS

| VOID                                             | DESCRIPTION                                                              |
|--------------------------------------------------|--------------------------------------------------------------------------|
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
| writeString(data: string)                        | Writes data to serial port                                               |