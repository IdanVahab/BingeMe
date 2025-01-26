package com.example.bingeme

import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule

/**
 * Custom Glide module for configuring Glide settings in the application.
 * This class is required to use the @GlideModule annotation and extend AppGlideModule.
 */
@GlideModule
class MyAppGlideModule : AppGlideModule()
