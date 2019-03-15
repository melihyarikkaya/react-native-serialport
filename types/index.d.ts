interface RNSerialportStatic {
  new (): RNSerialportStatic;

  /**
   * Starts the service and Usb listener
   */
  startUsbService()
}
export const RNSerialport: RNSerialportStatic;
