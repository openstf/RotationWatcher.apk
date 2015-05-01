# RotationWatcher.apk

RotationWatcher is a simple command-line application for detecting rotation changes on Android. While fairly useless by itself, it can be used together with other components such as [minicap](https://github.com/openstf/minicap) for screen capturing.

## Features

* Supports Android 2.3.3+
* Directly runnable as a command line application via adb shell
* Tracks and outputs current rotation in degrees to stdout

## Building

Build with `./gradlew assembleDebug`.

## Running

Like mentioned earlier, RotationWatcher is a command line application. It does not have a launcher activity, or any activity for that matter. To run the application and read its output, the following steps must be performed:

1. Build the project. See above.
2. Choose a module based on your platform. While the mobile module should run on most platforms as-is, wear and tv version are also provided just in case.

    ```bash
    cd mobile
    ```

3. Install the application.

    ```bash
    adb install ./build/outputs/apk/*-debug.apk
    ```
    
4. Figure out where the application was installed. Note that the location can and will change between every install, so always rerun this after installing.
 
    ```bash
    apk_path=$(adb shell pm path jp.co.cyberagent.stf.rotationwatcher | tr -d '\r' | cut -d: -f 2)
    ```

5. Run the application. Note that there's only ONE COMMAND to run, both the export and the exec run inside the device (the semicolon is escaped). The `export CLASSPATH` is required for app_process to do its magic.

    ```bash
    adb shell export CLASSPATH="$apk_path"\; \
    exec app_process /system/bin jp.co.cyberagent.stf.rotationwatcher.RotationWatcher
    ```

Alternatively, you may use the `./run.sh` script in each module to do the same.

## Good to know

This application requires access to some private and/or hidden APIs. See [shim/README.md](shim/README.md) for details.

## License

See [LICENSE](LICENSE).

Copyright © CyberAgent, Inc. All Rights Reserved.
