package com.example.sunandmoon.ar

import android.content.Context
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.atan

//returns the modifier-offset used to show the location of the sun
fun getARPos(
    sensorStatus: FloatArray,
    currentContext: Context,
    sunZenith: Double,
    sunAzimuth: Double
): List<Dp> {
    val screenWidthHeight = getScreenWidthHeightInMm(currentContext)
    val screenWidth = screenWidthHeight[1] // + 80
    val screenHeight = screenWidthHeight[1] * 3/4 // - 30

    // Theory: When you rotate the device with the camera along the z-axis,
    // the width will change and gradually become the height.
    // And the height will change as well and gradually become the width
    //val actualScreenWidth = screenWidth //+ (screenHeight-screenWidth) * abs(sin(sensorStatus[2]))
    //val actualScreenHeight = screenHeight //+ (screenWidth-screenHeight) * abs(sin(sensorStatus[2]))

    val cameraFOV = getCameraFOV(currentContext)
    val horizontalFOV = cameraFOV[0] * (Math.PI / 180.0) // * 0.5
    val verticalFOV = cameraFOV[1] * (Math.PI / 180.0)

    //println("screenWidth: $screenWidth. screenHeight: $screenHeight. horizontalFOV: $horizontalFOV. verticalFOV: $verticalFOV")

    val xPos = -mmToDp((screenWidth * ((sensorStatus[0] + sunAzimuth) / horizontalFOV)), currentContext)
    val yPos = -mmToDp((screenHeight * ((sensorStatus[1] + sunZenith) / verticalFOV)), currentContext)

    return listOf(xPos, yPos)
}

//converts the distance of the sun to device-independent pixels
fun mmToDp(mm: Double, currentContext: Context): Dp {
    val displayMetrics = currentContext.resources.displayMetrics
    val dpi = displayMetrics.densityDpi.toDouble()
    val density = displayMetrics.density

    val dp = (mm * dpi) / (25.4 * density)

    return dp.dp
}

//uses context to find the dimensions of the device, and returns a list containing the values as floats
fun getScreenWidthHeightInMm(currentContext: Context): List<Float> {
    val displayMetrics = currentContext.resources.displayMetrics
    val widthPx = displayMetrics.widthPixels
    val heightPx = displayMetrics.heightPixels
    val densityDpi = displayMetrics.densityDpi
    val widthMm = widthPx / (densityDpi / 25.4f)
    val heightMm = heightPx / (densityDpi / 25.4f)
    return listOf(widthMm, heightMm)
}

//returns the Angle of view for the sun(FOV) as a list of doubles
fun getCameraFOV(currentContext: Context): List<Double> {
    val cameraManager = currentContext.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    val cameraId = cameraManager.cameraIdList[0] // Rear-facing camera

    val characteristics = cameraManager.getCameraCharacteristics(cameraId)

    // Get the physical dimensions of the camera sensor
    val sensorSize = characteristics.get(CameraCharacteristics.SENSOR_INFO_PHYSICAL_SIZE)

    // Get the available focal lengths of the camera lens
    val focalLengths = characteristics.get(CameraCharacteristics.LENS_INFO_AVAILABLE_FOCAL_LENGTHS)


    if(sensorSize == null || focalLengths == null) return listOf(-1.0,-1.0)

    // Calculate the FOV in degrees
    val horizontalFOV = getFov(sensorSize.width, focalLengths[0])
    val verticalFOV = getFov(sensorSize.height, focalLengths[0])

    return listOf(horizontalFOV, verticalFOV)
}

//used to get fov for along a specified axis/dimension of the device and returns it as double
fun getFov(sensorDimension: Float, focalLength: Float): Double{
    return 2.0 * atan((sensorDimension / (2.0 * focalLength))) * 180.0 / Math.PI
}