import { Definitions } from 'react-native-serialport';

declare module "react-native-serialport" {
  import { NativeModules } from 'react-native'
  import { RNSerialport, Definitions} from 'react-native-serialport'

  interface Serialport {
    /**
     * Brings device list
     * @param {void} callback
     * @example
     * RNSerialport.getDeviceList((response) => {
     *    if(!response.status) {
     *      console.log('Error code: ' response.errorCode + ' Error message: ' +response.errorMessage)
     *      return;
     *    }
     *   console.log(response.devices)
     * }) 
     */
    getDeviceList(callback: (response: void) => {
      //your code
    }): void

    /**
     * if true, connects when the service is started or when the usb attach.
     * @default false
     * @param {boolean} autoConnect
     * @example
     * RNSerialport.setAutoConnect(true)
     */
    setAutoConnect(autoConnect: boolean): void

    /**
     * @default 8
     * @param {integer} dataBit
     */
    setDataBit(dataBit: Int32Array): void

    /**
     * @default 1
     * @param stopBit 
     */
    setStopBit(stopBit: Int32Array): void

    /**
     * @default 0
     * @param setParity 
     */
    setParity(setParity: Int32Array): void

    /**
     * @default 0
     * @param flowControl 
     */
    setFlowControl(flowControl: Int32Array): void

    /**
     * @default 9600
     * @param {integer} baudRate
     * @example
     * RNSerialport.setAutoConnectBaudRate(9600)
     */
    setAutoConnectBaudRate(baudRate: Int32Array)

    /**
     * @default DATA_BIT:8,STOP_BIT:1,PARITY:0,FLOW_CONTROL:0
     */
    loadDefaultConnectionSetting(): void

    /**
     * Brings connection status
     * @param {void} callback 
     * @returns {boolean} connectionState
     * @example
     * RNSerialport.isOpen((status) => {
     *    if(status) {
     *      //you have a connection
     *    } else {
     *      //you haven't a connection
     *    }
     * })
     */
    isOpen(callback: void): void

    /**
     * Opens connection with device name
     * @param {String} deviceName 
     * @param {integer} baudRate 
     * @example 
     *  RNSerialport.connectDevice('dev/usb/usb1', 9600)
     */
    connectDevice(deviceName: String, baudRate: Int32Array): void

    /**
     * Closes the connection. If there is no connection, the error triggers the event listener
     * @example
     * RNSerialport.isOpen((status) => {
     *    if(status) {
     *      RNSerialport.disconnect()
     *    }
     * })
     */
    disconnect()

    /**
     * It is used to start the service. Other functions will not work if the service is not started
     */
    startUsbService(): void

    /**
     * It is used to stop the service. Service is not stopped if there is a connection
     * @example
     * RNSerialport.isOpen((status) => {
     *    if(!status) {
     *      RNSerialport.stopUsbService()
     *    }
     * })
     */
     stopUsbService(): void    
     writeBytes(data: []): void
     writeString(data: String): void
  }
  export var RNSerialport: Serialport;
}
