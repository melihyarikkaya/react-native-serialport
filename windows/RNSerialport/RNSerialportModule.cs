using ReactNative.Bridge;
using System;
using System.Collections.Generic;
using Windows.ApplicationModel.Core;
using Windows.UI.Core;

namespace Serialport.RNSerialport
{
    /// <summary>
    /// A module that allows JS to share data.
    /// </summary>
    class RNSerialportModule : NativeModuleBase
    {
        /// <summary>
        /// Instantiates the <see cref="RNSerialportModule"/>.
        /// </summary>
        internal RNSerialportModule()
        {

        }

        /// <summary>
        /// The name of the native module.
        /// </summary>
        public override string Name
        {
            get
            {
                return "RNSerialport";
            }
        }
    }
}
