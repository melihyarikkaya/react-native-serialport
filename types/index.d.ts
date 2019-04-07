interface GetDeviceListResponseSuccess {
  status: boolean;
  devices: Array<GetDeviceListResponseDevices>;
}
interface GetDeviceListResponseError {
  status: boolean;
  errorCode: number;
  errorMessage: string;
}
interface GetDeviceListResponseDevices {
  name: string;
  vendorId: number;
  productId: number;
}
type GetDeviceListResponse<T> = {
  [P in keyof T]: GetDeviceListResponseSuccess | GetDeviceListResponseError
};

interface RNSerialportStatic {
  /**
   * Starts the service and Usb listener
   */
  startUsbService(): void;
  /**
   * Stops the service and Usb listener
   */
  stopUsbService(): void;
  /**
   * @param callback
   * @returns boolean on callback method
   */
  isOpen(callback: (status: boolean) => void): void;

  /**
   * Returns support status
   * @param callback
   * @returns boolean on callback method
   */
  isSupported(deviceName : string) : Promise;

  //Begin setter methods

  /**
   * Set the returned data type
   */
  setReturnedDataType(type: number): void;

  /**
   * Set the interface
   */
  setInterface(iFace: number): void;

  /**
   * Set the data bit
   */
  setDataBit(bit: number): void;

  /**
   * Set the stop bit
   */
  setStopBit(bit: number): void;

  /**
   * Set the parity
   */
  setParity(parity: number): void;

  /**
   * Set the flow control
   */
  setFlowControl(control: number): void;

  /**
   * Set the auto connection baudrate
   */
  setAutoConnectBaudRate(baudRate: number): void;

  /**
   * Set the auto connection status
   */
  setAutoConnect(status: boolean): void;

  /**
   * Set the driver type
   */
  setDriver(driver: string): void;

  //End setter methods

  /**
   * Load the default connection settings
   */
  loadDefaultConnectionSetting(): void;

  /**
   * Get the device list
   */
  getDeviceList(callback: (response: GetDeviceListResponse<any>) => void): void;

  /**
   * Connect to device with device name and baud rate
   * @param deviceName Device Name
   * @param baudRate Baud Rate
   */
  connectDevice(deviceName: string, baudRate: number): void;

  /**
   * Closes the connection
   */
  disconnect(): void;

  /**
   * Writes string to port
   * @param data String to write
   */
  writeString(data: string): void;

  /**
   * Writes Base64 string to port
   * @param data Base64 string to write
   */
  writeBase64(data: string): void;
}
export var RNSerialport: RNSerialportStatic;

interface DefinitionsStatic {
  DATA_BITS: {
    DATA_BITS_5: number
    DATA_BITS_6: number;
    DATA_BITS_7: number;
    DATA_BITS_8: number;
  };
  STOP_BITS: {
    STOP_BITS_1 : number;
    STOP_BITS_15: number;
    STOP_BITS_2 : number;
  };
  PARITIES: {
    PARITY_NONE : number;
    PARITY_ODD  : number;
    PARITY_EVEN : number;
    PARITY_MARK : number;
    PARITY_SPACE: number;
  };
  FLOW_CONTROLS: {
    FLOW_CONTROL_OFF        : number;
    FLOW_CONTROL_RTS_CTS    : number;
    FLOW_CONTROL_DSR_DTR    : number;
    FLOW_CONTROL_XON_XOFF   : number;
  };
  RETURNED_DATA_TYPES: {
    INTARRAY : number;
    HEXSTRING: number;
  };
  DRIVER_TYPES: {
    AUTO : string,
    CDC     : string,
    CH34x   : string,
    CP210x  : string,
    FTDI    : string,
    PL2303  : string
  };
}
export var definitions: DefinitionsStatic;

interface ActionsStatic {
  ON_SERVICE_STARTED      : string,
  ON_SERVICE_STOPPED      : string,
  ON_DEVICE_ATTACHED      : string,
  ON_DEVICE_DETACHED      : string,
  ON_ERROR                : string,
  ON_CONNECTED            : string,
  ON_DISCONNECTED         : string,
  ON_READ_DATA            : string
}
export var actions: ActionsStatic;