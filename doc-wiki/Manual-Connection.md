```javascript
import { DeviceEventEmitter } from "react-native";
import { 
  RNSerialport,
  definitions,
  actions 
} from "react-native-serialport";

export default class App extends Component {

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
    RNSerialport.setReturnedDataType(definitions.RETURNED_DATA_TYPES.HEXSTRING); //default INT ARRAY
    RNSerialport.setAutoConnect(false) // must be false for manual connect
    RNSerialport.startUsbService(); //start usb listener
  }

  componentWillUnmount() {
    RNSerialport.stopUsbService(); // must be before removeAllListeners();
    DeviceEventEmitter.removeAllListeners();
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

  /* BEGIN Your Methods */
  _getFirstDevice() {
    return new Promise((resolve, reject) => {
      RNSerialport.getDeviceList((response) => {
        if(response.status) {
          resolve(response.devices[0]);
        } else {
          reject("Error from getDeviceList()", response.errorCode + " " + response.errorMessage);
        }
      })
    })
  }

  _connect = async() => {
    const device = await this._getFirstDevice();
    RNSerialport.connectDevice(device.name, parseInt(this.state.baudRate, 10));
  }
  /* END Your Methods */ 

}
```
