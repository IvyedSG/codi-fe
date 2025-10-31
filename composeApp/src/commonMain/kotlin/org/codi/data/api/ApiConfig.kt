package org.codi.data.api

/**
 * Expect declaration for the base API URL used by platform-specific implementations.
 * Each platform (android/ios/etc.) must provide an `actual` value.
 */
expect val BASE_API_URL: String

