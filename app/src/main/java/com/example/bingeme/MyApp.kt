package com.example.bingeme

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Base Application class annotated with @HiltAndroidApp to enable Dagger-Hilt dependency injection.
 */
@HiltAndroidApp
class MyApp : Application()
