### Should be see for open the connection when the service is started or the usb device is attached.

**Import Library and Requirements**

```javascript
import { RNSerialport, definitions, actions } from "react-native-serialport";
import { DeviceEventEmitter } from "react-native";
```

**Set Your Callback Methods**

```javascript
componentDidMount() {
  DeviceEventEmitter.addListener(
    actions.ON_SERVICE_STARTED,
    this.onServiceStarted,
    this
  );
  DeviceEventEmitter.addListener(
    actions.ON_SERVICE_STOPPED,
    this.onServiceStopped,
    this
  );
  DeviceEventEmitter.addListener(
    actions.ON_DEVICE_ATTACHED,
    this.onDeviceAttached,
    this
  );
  DeviceEventEmitter.addListener(
    actions.ON_DEVICE_DETACHED,
    this.onDeviceDetached,
    this
  );
  DeviceEventEmitter.addListener(actions.ON_ERROR, this.onError, this);
  DeviceEventEmitter.addListener(actions.ON_CONNECTED, this.onConnected, this);
  DeviceEventEmitter.addListener(
    actions.ON_DISCONNECTED,
    this.onDisconnected,
    this
  );
  DeviceEventEmitter.addListener(actions.ON_READ_DATA, this.onReadData, this);
}
```

**Set Serialport Settings**

```javascript
componentDidMount() {
  //.
  // Set Your Callback Methods in here
  //.
  RNSerialport.setReturnedDataType(definitions.RETURNED_DATA_TYPES.HEXSTRING);
  RNSerialport.setAutoConnect(true);
  RNSerialport.setAutoConnectBaudRate(9600);
  RNSerialport.startUsbService();
  //Started usb listener
}
```
**Some precautions**

```javascript
componentWillUnmount = async() => {
  DeviceEventEmitter.removeAllListeners();
  const isOpen = await RNSerialport.isOpen();
  if (isOpen) {
    Alert.alert("isOpen", isOpen);
    RNSerialport.disconnect();
  }
  RNSerialport.stopUsbService();
}
```

_Installation Successfuly_

[See for methods](https://github.com/melihyarikkaya/react-native-serialport/wiki/Methods)

### Full Example

```javascript
import { DeviceEventEmitter } from "react-native";
import { 
  RNSerialport,
  definitions,
  actions 
} from "react-native-serialport";

export default class App extends Component<Props> {

  componentDidMount() {
    DeviceEventEmitter.addListener(
      actions.ON_SERVICE_STARTED,
      this.onServiceStarted,
      this
    );
    DeviceEventEmitter.addListener(
      actions.ON_SERVICE_STOPPED,
      this.onServiceStopped,
      this
    );
    DeviceEventEmitter.addListener(
      actions.ON_DEVICE_ATTACHED,
      this.onDeviceAttached,
      this
    );
    DeviceEventEmitter.addListener(
      actions.ON_DEVICE_DETACHED,
      this.onDeviceDetached,
      this
    );
    DeviceEventEmitter.addListener(
      actions.ON_ERROR,
      this.onError,
      this
    );
    DeviceEventEmitter.addListener(
      actions.ON_CONNECTED,
      this.onConnected,
      this
    );
    DeviceEventEmitter.addListener(
      actions.ON_DISCONNECTED,
      this.onDisconnected,
      this
    );
    DeviceEventEmitter.addListener(
      actions.ON_READ_DATA,
      this.onReadData,
      this
    );
    RNSerialport.setInterface(-1); //default -1
    RNSerialport.setReturnedDataType(definitions.RETURNED_DATA_TYPES.HEXSTRING); //default INTARRAY
    RNSerialport.setAutoConnectBaudRate(9600)
    RNSerialport.setAutoConnect(true) // must be true for auto connect
    RNSerialport.startUsbService(); //start usb listener
  }

  componentWillUnmount = async() => {
    DeviceEventEmitter.removeAllListeners();
    RNSerialport.isOpen(isOpen => {
      if(isOpen) {
        RNSerialport.disconnect();
        RNSerialport.stopUsbService();
      } else {
        RNSerialport.stopUsbService();
      }
    });
  }

  /* BEGIN Listener Methods */

  onDeviceAttached() { console.log("Device Attached"); }

  onDeviceDetached() { console.log("Device Detached") }

  onError(error) { console.log("Code: " + error.errorCode + " Message: " + error.errorMessage)}

  onConnected() { console.log("Connected") }

  onDisconnected() { console.log("Disconnected") }

  onServiceStarted(response) {
    //returns usb status when service started
    if(response.deviceAttached) { 
      this.onDeviceAttached();
    }
  }

  onServiceStopped() { console.log("Service stopped") }

  onReadData(data) {
   console.log(data.payload)
  }

  /* END Listener Methods */

}
```
