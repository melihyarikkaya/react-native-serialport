interface GetDeviceListResponseSuccess {
  status: boolean,
  devices: Array<GetDeviceListResponseDevices>
}
interface GetDeviceListResponseError {
  status: boolean,
  errorCode: number,
  errorMessage: string
}
interface GetDeviceListResponseDevices {
  name: string,
  vendorId: number,
  productId: number
}
type GetDeviceListResponse<T> = { [P in keyof T]: GetDeviceListResponseSuccess | GetDeviceListResponseError }

interface RNSerialportStatic {
  /**
   * Starts the service and Usb listener
   */
  startUsbService() : void
  /**
   * Stops the service and Usb listener
   */
  stopUsbService() : void
  /**
   * Returns connection status
   * @returns boolean
   */
  isOpen(callback: (status: boolean) => void)

  //Begin setter methods

  /**
   * Set the returned data type
   */
  setReturnedDataType(type: number) : void

  /**
   * Set the interface
   */
  setInterface(iFace: number) : void

  /**
   * Set the data bit
   */
  setDataBit(bit: number) : void

  /**
   * Set the stop bit
   */
  setStopBit(bit: number) : void

  /**
   * Set the parity
   */
  setParity(parity: number) : void

  /**
   * Set the flow control
   */
  setFlowControl(control: number) : void

  /**
   * Set the auto connection baudrate
   */
  setAutoConnectBaudRate(baudRate: number) : void

  /**
   * Set the auto connection status
   */
  setAutoConnect(status: boolean) : void

  //End setter methods

  /**
   * Load the default connection settings
   */
  loadDefaultConnectionSetting() : void

  /**
   * Get the device list
   */
  getDeviceList(callback: (response: GetDeviceListResponse<any>) => void);

  /**
   * Connect to device with device name and baud rate
   * @param deviceName Device Name
   * @param baudRate Baud Rate
   */
  connectDevice(deviceName: string, baudRate: number) : void

  /**
   * Closes the connection
   */
  disconnect() : void

  /**
   * Writes string to port
   * @param data String to write
   */
  writeString(data: string) : void

  /**
   * Writes Base64 string to port
   * @param data Base64 string to write
   */
  writeBase64(data: string) : void
}
export var RNSerialport : RNSerialportStatic;