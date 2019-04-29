import { NativeModules } from 'react-native';

const { RNSerialport } = NativeModules;

const definitions = {
  DATA_BITS :{
    DATA_BITS_5: 5,
    DATA_BITS_6: 6,
    DATA_BITS_7: 7,
    DATA_BITS_8: 8
  },
  STOP_BITS: {
    STOP_BITS_1 : 1,
    STOP_BITS_15: 3,
    STOP_BITS_2 : 2
  },
  PARITIES: {
    PARITY_NONE : 0,
    PARITY_ODD  : 1,
    PARITY_EVEN : 2,
    PARITY_MARK : 3,
    PARITY_SPACE: 4
  },
  FLOW_CONTROLS: {
    FLOW_CONTROL_OFF     : 0,
    FLOW_CONTROL_RTS_CTS : 1,
    FLOW_CONTROL_DSR_DTR : 2,
    FLOW_CONTROL_XON_XOFF: 3
  },
  RETURNED_DATA_TYPES: {
    INTARRAY : 1,
    HEXSTRING: 2
  },
  DRIVER_TYPES: {
    AUTO    : "AUTO",
    CDC     : "cdc",
    CH34x   : "ch34x",
    CP210x  : "cp210x",
    FTDI    : "ftdi",
    PL2303  : "pl2303"
  }
};

const actions = {
  ON_SERVICE_STARTED      : 'onServiceStarted',
  ON_SERVICE_STOPPED      : 'onServiceStopped',
  ON_DEVICE_ATTACHED      : 'onDeviceAttached',
  ON_DEVICE_DETACHED      : 'onDeviceDetached',
  ON_ERROR                : 'onError',
  ON_CONNECTED            : 'onConnected',
  ON_DISCONNECTED         : 'onDisconnected',
  ON_READ_DATA            : 'onReadDataFromPort'
};

RNSerialport.intArrayToUtf16 = (intArray) => {
  var str = "";
  for (var i = 0; i < intArray.length; i++) {
    str += String.fromCharCode(intArray[i]);
  }
  return str;
}
RNSerialport.hexToUtf16 = (hex) => {
  var str = "";
  var radix = 16;
  for (var i = 0; i < hex.length && hex.substr(i, 2) !== "00"; i += 2) {
    str += String.fromCharCode(parseInt(hex.substr(i, 2), radix));
  }
  return str;
}

module.exports = { RNSerialport, definitions, actions };
