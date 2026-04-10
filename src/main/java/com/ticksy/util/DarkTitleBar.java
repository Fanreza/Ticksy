package com.ticksy.util;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef;
import javafx.stage.Stage;

import java.lang.reflect.Method;

/**
 * Enables dark title bar on Windows 10 (build 17763+) and Windows 11.
 * Uses Windows DWM API via JNA + reflection for native window handle.
 */
public class DarkTitleBar {

    private static final int DWMWA_USE_IMMERSIVE_DARK_MODE = 20;

    public static void enable(Stage stage) {
        try {
            long hwndVal = getWindowHandle(stage);
            if (hwndVal == 0) return;

            WinDef.HWND hwnd = new WinDef.HWND(new Pointer(hwndVal));
            int[] value = new int[]{1};
            Dwmapi.INSTANCE.DwmSetWindowAttribute(hwnd, DWMWA_USE_IMMERSIVE_DARK_MODE, value, 4);
        } catch (Exception e) {
            // Silently fail on non-Windows or unsupported builds
        }
    }

    private static long getWindowHandle(Stage stage) {
        try {
            // Use reflection to avoid compile-time dependency on internal packages
            Class<?> helperClass = Class.forName("com.sun.javafx.stage.WindowHelper");
            Method getPeer = helperClass.getMethod("getPeer", javafx.stage.Window.class);
            Object tkStage = getPeer.invoke(null, stage);
            if (tkStage == null) return 0;

            Method getRawHandle = tkStage.getClass().getMethod("getRawHandle");
            getRawHandle.setAccessible(true);
            return (long) getRawHandle.invoke(tkStage);
        } catch (Exception e) {
            return 0;
        }
    }

    public interface Dwmapi extends com.sun.jna.Library {
        Dwmapi INSTANCE = Native.load("dwmapi", Dwmapi.class);

        int DwmSetWindowAttribute(WinDef.HWND hwnd, int dwAttribute, int[] pvAttribute, int cbAttribute);
    }
}
