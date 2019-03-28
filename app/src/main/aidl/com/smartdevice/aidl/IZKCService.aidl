// IZKCService.aidl
package com.smartdevice.aidl;

// Declare any non-default types here with import statements

interface IZKCService {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    int getDeviceModel();

    boolean setModuleFlag(int module);

    void openBackLight(int btFlg);
}
