
# react-native-serialport

## Getting started

`$ npm install react-native-serialport --save`

### Mostly automatic installation

`$ react-native link react-native-serialport`

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

## Usage

### Auto Connection

To open the connection when the service is started or the device is inserted should be looked

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