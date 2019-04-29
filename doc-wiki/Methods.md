[startUsbService](https://github.com/melihyarikkaya/react-native-serialport/wiki/Methods/#startUsbService)  
[stopUsbService](https://github.com/melihyarikkaya/react-native-serialport/wiki/Methods/#stopUsbService)  
[getDeviceList](https://github.com/melihyarikkaya/react-native-serialport/wiki/Methods/#getDeviceList)  
[connectDevice](https://github.com/melihyarikkaya/react-native-serialport/wiki/Methods/#connectDevice)  
[disconnect](https://github.com/melihyarikkaya/react-native-serialport/wiki/Methods/#disconnect)  
[isOpen](https://github.com/melihyarikkaya/react-native-serialport/wiki/Methods/#isOpen)  
[isSupported](https://github.com/melihyarikkaya/react-native-serialport/wiki/Methods/#isSupported)  
[isServiceStarted](https://github.com/melihyarikkaya/react-native-serialport/wiki/Methods/#isServiceStarted)  
[writeString](https://github.com/melihyarikkaya/react-native-serialport/wiki/Methods/#writeString)  
[writeBase64](https://github.com/melihyarikkaya/react-native-serialport/wiki/Methods/#writeBase64)  
[writeHexString](https://github.com/melihyarikkaya/react-native-serialport/wiki/Methods/#writeHexString)

#### Setter Methods

[setReturnedDataType](https://github.com/melihyarikkaya/react-native-serialport/wiki/Methods/#setReturnedDataType)  
[setDriver](https://github.com/melihyarikkaya/react-native-serialport/wiki/Methods/#setDriver)  
[setInterface](https://github.com/melihyarikkaya/react-native-serialport/wiki/Methods/#setInterface)  
[setAutoConnect](https://github.com/melihyarikkaya/react-native-serialport/wiki/Methods/#setAutoConnect)  
[setAutoConnectBaudRate](https://github.com/melihyarikkaya/react-native-serialport/wiki/Methods/#setAutoConnectBaudRate)  
[setDataBit](https://github.com/melihyarikkaya/react-native-serialport/wiki/Methods/#setDataBit)  
[setStopBit](https://github.com/melihyarikkaya/react-native-serialport/wiki/Methods/#setStopBit)  
[setParity](https://github.com/melihyarikkaya/react-native-serialport/wiki/Methods/#setParity)  
[setFlowControl](https://github.com/melihyarikkaya/react-native-serialport/wiki/Methods/#setFlowControl)  
[loadDefaultConnectionSetting](https://github.com/melihyarikkaya/react-native-serialport/wiki/Methods/#loadDefaultConnectionSetting)

### startUsbService

_Starts the service and usb broadcast receivers_

No Params

```javascript
RNSerialport.startUsbService();
```

---

### stopUsbService

_Stops the service and usb broadcast receivers_

No Params

```javascript
RNSerialport.stopUsbService();
```

---

### getDeviceList

_Receives device list_

No Param

```javascript
try {
  const deviceList = await RNSerialport.getDeviceList();
  if (deviceList.length > 0) {
    console.log(deviceList);
  } else {
    console.log("Device Not Found");
  }
} catch (err) {
  Alert.alert(
    "Error from getDeviceList()",
    err.errorCode + " " + err.errorMessage
  );
}
```

---

### connectDevice

_Use to manual connection_

Params:

| Name       | TYPE   | REQUIRED     |
| ---------- | ------ | ------------ |
| deviceName | string | yes for call |
| baudRate   | number | yes for call |

```javascript
RNSerialport.connectDevice("deviceName", 9600);
```

---

### disconnect

_Closes the connection_

No Params

```javascript
RNSerialport.disconnect();
```

---

### isOpen

_Returns connection status_

Params:

_No param_

```javascript
//1st way
try {
  const isOpen = await RNSerialport.isOpen();

  if (isOpen) console.log("Is open?", "yes");
  else console.log("Is open?", "no");
} catch (err) {
  console.log(err);
}

//2st way
RNSerialport.isOpen()
  .then(isOpen => {
    if (isOpen) {
      console.log("Is open?", "yes");
    } else {
      console.log("Is oprn?", "no");
    }
  })
  .catch(err => {
    console.log(err);
  });
```

---

### isSupported

_Returns support status_

Params:

| Name       | TYPE   | REQUIRED     |
| ---------- | ------ | ------------ |
| deviceName | string | yes for call |

```javascript
//1st way
try {
  const isSupported = await RNSerialport.isSupported("deviceName");

  if (isSupported) console.log("Is supported?", "yes");
  else console.log("Is supported?", "no");
} catch (err) {}

//2st way
RNSerialport.isSupported("deviceName")
  .then(isSupported => {
    if (isSupported) {
      console.log("Is supported?", "yes");
    } else {
      console.log("Is supported?", "no");
    }
  })
  .catch(err => {
    console.log(err);
  });
```

---

### isServiceStarted

_Returns service status_

No param

```javascript
//1st way
try {
  const isServiceStarted = await RNSerialport.isServiceStarted();

  if (isServiceStarted) console.log("Is ServiceStarted?", "yes");
  else console.log("Is ServiceStarted?", "no");
} catch (err) {}

//2st way
RNSerialport.isServiceStarted()
  .then(isServiceStarted => {
    if (isServiceStarted) {
      console.log("Is service started?", "yes");
    } else {
      console.log("Is service started?", "no");
    }
  })
  .catch(err => {
    console.log(err);
  });
```

---

### writeString

_Writes data to serial port_

| Name | TYPE   | REQUIRED     |
| ---- | ------ | ------------ |
| data | string | yes for call |

```javascript
RNSerialport.writeString("HELLO");
```

---

### writeBase64

_Writes data to serial port_

| Name | TYPE   | REQUIRED     |
| ---- | ------ | ------------ |
| data | string | yes for call |

```javascript
RNSerialport.writeBase64("SEVMTE8=");
```

---

### writeHexString

_Writes data to serial port_

`Note: Make sure the text has a valid hexadecimal number system! Otherwise this does nothing.`

| Name | TYPE   | REQUIRED     |
| ---- | ------ | ------------ |
| data | string | yes for call |

```javascript
RNSerialport.writeHexString("0F"); // 1 byte
RNSerialport.writeHexString("FF0F"); // 2 btye
RNSerialport.writeHexString("48454C4C4F"); // 5 byte

//The following are not recommended.
RNSerialport.writeHexString("F");
RNSerialport.writeHexString("FFF");
```

---

### setReturnedDataType

_Changes the data type in "ON_READ_DATA" event_

> Default: INTARRAY

Params:

| TYPE   | REQUIRED     |
| ------ | ------------ |
| number | yes for call |

```javascript
import { definitions } from "react-native-serialport
RNSerialport.setReturnedDataType(
  definitions.RETURNED_DATA_TYPES.HEXSTRING
)
// or
RNSerialport.setReturnedDataType(
  definitions.RETURNED_DATA_TYPES.INTARRAY
)
```

---

### setDriver

_Changes the driver_

[Why it is necessary? Using createUsbSerialDevice method specifying the driver](https://github.com/felHR85/UsbSerial/wiki/3.-Create-UsbSerialDevice#using-createusbserialdevice-method-specifying-the-driver)

> Default: AUTO

Params:

| TYPE   | REQUIRED     |
| ------ | ------------ |
| string | yes for call |

```javascript
import { definitions } from "react-native-serialport
RNSerialport.setDriver(definitions.DRIVER_TYPES.AUTO)
```

---

### setInterface

_Changes the serial interface_

> Default: -1

Params:

| TYPE   | REQUIRED     |
| ------ | ------------ |
| number | yes for call |

```javascript
RNSerialport.setInterface(1);
```

---

### setAutoConnectBaudRate

_Changes baud rate to be used on automatic connection_

`Used before starting the service.`

> Default: 9600

Params:

| TYPE   | REQUIRED     |
| ------ | ------------ |
| number | yes for call |

```javascript
RNSerialport.setAutoConnectBaudRate(115200);
```

---

### setAutoConnect

_Turns automatic connection on or off_

> Default: off

Params:

| TYPE    | REQUIRED     |
| ------- | ------------ |
| boolean | yes for call |

```javascript
RNSerialport.setAutoConnect(true);
```

---

### setDataBit

_Changes the data bit_

> Default: DATA_BITS_8

Params:

| TYPE   | REQUIRED     |
| ------ | ------------ |
| number | yes for call |

```javascript
import { definitions } from "react-native-serialport
RNSerialport.setDataBit(definitions.DATA_BITS.DATA_BITS_8)
```

---

### setStopBit

_Changes the stop bit_

> Default: STOP_BITS_1

Params:

| TYPE   | REQUIRED     |
| ------ | ------------ |
| number | yes for call |

```javascript
import { definitions } from "react-native-serialport";
RNSerialport.setStopBit(definitions.STOP_BITS.STOP_BITS_1);
```

---

### setParity

_Changes the parity_

> Default: PARITY_NONE

Params:

| TYPE   | REQUIRED     |
| ------ | ------------ |
| number | yes for call |

```javascript
import { definitions } from "react-native-serialport";
RNSerialport.setParity(definitions.PARITIES.PARITY_NONE);
```

---

### setFlowControl

_Changes the flow control mode_

> Default: FLOW_CONTROL_OFF

Params:

| TYPE   | REQUIRED     |
| ------ | ------------ |
| number | yes for call |

```javascript
import { definitions } from "react-native-serialport";
RNSerialport.setFlowControl(definitions.FLOW_CONTROLS.FLOW_CONTROL_OFF);
```

---

### loadDefaultConnectionSetting

_Loads the default settings_

> Defaults:  
> DATA_BIT: DATA_BITS_8  
> STOP_BIT: STOP_BITS_1  
> PARITY: PARITY_NONE  
> FLOW_CONTROL: FLOW_CONTROL_OFF

No Params

```javascript
RNSerialport.loadDefaultConnectionSetting();
```

---
