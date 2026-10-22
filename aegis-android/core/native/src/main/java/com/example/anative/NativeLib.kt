package com.example.anative

class NativeLib {
    /**
     * A native method that is implemented by the 'anative' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    companion object {
        // Used to load the 'anative' library on application startup.
        init {
            System.loadLibrary("anative")
        }
    }
}
