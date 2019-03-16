interface RNSerialportStatic {
  new (): RNSerialportStatic;

  /**
   * Starts the service and Usb listener
   */
  startUsbService()
  isOpen(): boolean
}
export const RNSerialport: RNSerialportStatic;
