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

interface IIsSupportedResponse{
  status: boolean
}

type GetDeviceListResponse<T> = {
  [P in keyof T]: GetDeviceListResponseSuccess | GetDeviceListResponseError
};

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

type DataBits = 5 | 6 | 7 | 8;
type StopBits = 1 | 2 | 3;
type Parities = 0 | 1 | 2 | 3 | 4;
type FlowControls = 0 | 1 | 2 | 3;
type ReturnedDataTypes = 1 | 2;
type Drivers = "AUTO" | "cdc" | "ch34x" | "cp210x" | "ftdi" | "pl2303";

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
   * @param deviceName String
   * @returns status boolean on Promise then method
   */
  isSupported(deviceName : string) : Promise<IIsSupportedResponse>;

  //Begin setter methods

  /**
   * Set the returned data type
   */
  setReturnedDataType(type: ReturnedDataTypes): void;

  /**
   * Set the interface
   */
  setInterface(iFace: number): void;

  /**
   * Set the data bit
   */
  setDataBit(bit: DataBits): void;

  /**
   * Set the stop bit
   */
  setStopBit(bit: StopBits): void;

  /**
   * Set the parity
   */
  setParity(parity: Parities): void;

  /**
   * Set the flow control
   */
  setFlowControl(control: FlowControls): void;

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
  setDriver(driver: Drivers): void;

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

  /**
   * Writes hex string to port
   * @param data String to write
   */
  writeHexString(data: string): void

  /**
   * Hex string convert to Utf8 string
   * @param intArray Array
   * @return utf8 String
   */
  intArrayToUtf16(intArray: Array<number>): string

  /**
   * Hex string convert to Utf8 string
   * @param hex String
   * @retrun utf8 String
   */
  hexToUtf16(hex: string): string
}
export var RNSerialport: RNSerialportStatic;
