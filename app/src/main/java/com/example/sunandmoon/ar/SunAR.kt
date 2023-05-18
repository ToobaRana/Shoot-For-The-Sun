package com.example.sunandmoon.ar

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sunandmoon.camera.CameraPreview
import com.example.sunandmoon.util.Permission
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.sqrt
import com.example.sunandmoon.R
import com.example.sunandmoon.data.ARUIState
import com.example.sunandmoon.ui.components.NavigationComposable
import com.example.sunandmoon.viewModel.ARViewModel


@Composable
fun SunAR(
    modifier: Modifier,
    navigateToNextBottomBar: (index: Int) -> Unit,
    packageManager: PackageManager,
    arViewModel: ARViewModel = hiltViewModel()
) {
    val arUIState by arViewModel.arUIState.collectAsState()

    Surface() {
        CameraContent(Modifier.fillMaxSize())
    }

    val hasMagnetometer = packageManager.hasSystemFeature(PackageManager.FEATURE_SENSOR_COMPASS)
    val hasGyroscope = packageManager.hasSystemFeature(PackageManager.FEATURE_SENSOR_GYROSCOPE)
    val hasAccelerometer =
        packageManager.hasSystemFeature(PackageManager.FEATURE_SENSOR_ACCELEROMETER)

    Log.i(
        "sensorStatus",
        "hasMagnetometer: $hasMagnetometer, hasGyroscope: $hasGyroscope, hasAccelerometer: $hasAccelerometer"
    )

    val sensorStatus = remember {
        mutableStateOf(FloatArray(0) { 0f })
    }

    if (hasMagnetometer) {
        val localContext = LocalContext.current

        val sensorManager: SensorManager =
            localContext.getSystemService(Context.SENSOR_SERVICE) as SensorManager

        var rotationVectorSensor: Sensor =
            sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)

        val rotationVectorSensorEventListener = object : SensorEventListener {
            override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
                if (sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
                    when (accuracy) {
                        SensorManager.SENSOR_STATUS_ACCURACY_LOW -> Log.i(
                            "sensorStatus",
                            "SENSOR_STATUS_ACCURACY: low"
                        )
                        SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM -> Log.i(
                            "sensorStatus",
                            "SENSOR_STATUS_ACCURACY: medium"
                        )
                        SensorManager.SENSOR_STATUS_ACCURACY_HIGH -> Log.i(
                            "sensorStatus",
                            "SENSOR_STATUS_ACCURACY: high"
                        )
                    }
                }
            }

            override fun onSensorChanged(event: SensorEvent) {
                if (event.sensor.type == Sensor.TYPE_ROTATION_VECTOR) {

                    val rotationMatrix = FloatArray(16)
                    SensorManager.getRotationMatrixFromVector(
                        rotationMatrix, event.values
                    );

                    val transformedRotationMatrix = FloatArray(16)
                    SensorManager.remapCoordinateSystem(
                        rotationMatrix,
                        SensorManager.AXIS_X,
                        SensorManager.AXIS_Z,
                        transformedRotationMatrix
                    )

                    val orientation = FloatArray(3)
                    SensorManager.getOrientation(transformedRotationMatrix, orientation)

                    sensorStatus.value = orientation

                    //println(sensorStatus.value[0])
                }
            }
        }

        sensorManager.registerListener(
            rotationVectorSensorEventListener,
            rotationVectorSensor,
            SensorManager.SENSOR_DELAY_GAME
        )
    }

    TheApp(Modifier, sensorStatus, hasMagnetometer, navigateToNextBottomBar, arUIState)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TheApp(
    modifier: Modifier,
    sensorStatus: MutableState<FloatArray>,
    hasMagnetometer: Boolean,
    navigateToNextBottomBar: (index: Int) -> Unit,
    arUIState: ARUIState,
) {
    // for the AR functionality
    val sunZenith = arUIState.sunZenith
    val sunAzimuth = arUIState.sunAzimuth
    if(sunZenith != null && sunAzimuth != null) {
        SunFinder(modifier, sensorStatus.value, sunZenith, sunAzimuth)
    }

    // for the non-AR UI part for this screen
    Scaffold(
        containerColor = Color.Transparent,
        bottomBar = { NavigationComposable(modifier = modifier, page = 1, navigateToNextBottomBar = navigateToNextBottomBar)}
    ) { val p = it /* just to remove the error message */ }
}

@Composable
fun SunFinder(modifier: Modifier, sensorStatus: FloatArray, sunZenith: Double, sunAzimuth: Double) {

    if(sensorStatus.size < 2 || sensorStatus[0].isNaN() || sensorStatus[1].isNaN()) return

    // sunZenith and sunAzimuth are in degrees
    val sunZenith: Double = sunZenith //41.19
    val sunAzimuth: Double = sunAzimuth //137.87

    val sun1ImagePos: List<Dp> = getARPos(sensorStatus, LocalContext.current, Math.toRadians(sunZenith), Math.toRadians(-sunAzimuth))
    val xPos1 = sun1ImagePos[0]
    val yPos1 = sun1ImagePos[1]

    val sun2ImagePos: List<Dp> = getARPos(sensorStatus, LocalContext.current, Math.toRadians(sunZenith), Math.toRadians(360 - sunAzimuth))
    val xPos2 = sun2ImagePos[0]
    val yPos2 = sun2ImagePos[1]

    Column() {
        Column() {
            for(element in sensorStatus) {
                Text(text = "        " + (element * 180.0 / Math.PI).toString(), color = Color.White)
            }
        }
        Column() {
            for(element in sensorStatus) {
                Text(text = "        " + (element * 180.0 / Math.PI).toString(), color = Color.Black)
            }
        }
    }
    //println(sensorStatus[2] * 180.0 / Math.PI)

    // sky directions
    skyDirections(modifier, sensorStatus, LocalContext.current)

    // the sun
    sun(modifier, sensorStatus, xPos1, yPos1, Color.Red, sunZenith)
    // the "other" sun
    sun(modifier, sensorStatus, xPos2, yPos2, Color.Green, sunZenith)

    // checks that the sun is far enough away from the center of the screen
    // (we do not want the arrow to show when the sun is close enough to the center of the screen)
    val sun1Distance = sqrt(xPos1.value * xPos1.value + yPos1.value * yPos1.value)
    val sun2Distance = sqrt(xPos2.value * xPos2.value + yPos2.value * yPos2.value)
    val closestSunPos = if(sun1Distance < sun2Distance) listOf<Dp>(xPos1,yPos1) else listOf<Dp>(xPos2,yPos2)
    val arrowDistanceThreshold = 100
    // an if-statement inside of an if-statement. This is kotlin. This is incredible
    if( (if(sun1Distance < sun2Distance) sun1Distance else sun2Distance) > arrowDistanceThreshold) {
        // arrow pointing towards the sun
        Box(
            modifier = modifier
                .fillMaxSize()
                .fillMaxHeight()
                .fillMaxWidth()
                .rotate((-sensorStatus[2] * 180.0 / Math.PI).toFloat())
                .rotate(
                    atan2(
                        closestSunPos[1].value.toDouble(),
                        closestSunPos[0].value.toDouble()
                    ).toFloat() * (180.0f / Math.PI.toFloat())
                )
        ) {
            Image(painter = painterResource(id = R.drawable.fancy_arrow_icon), contentDescription = "Arrow pointing towards the sun", modifier = modifier
                .align(Alignment.Center)
                .width(70.dp)
                .height(70.dp))
        }
    }
}

@Composable
fun sun(modifier: Modifier, sensorStatus: FloatArray, xPos: Dp, yPos: Dp, color: Color, sunZenith: Double) {
    // the sun
    Box(
        modifier = modifier
            .fillMaxSize()
            .fillMaxHeight()
            .fillMaxWidth()
            .rotate((-sensorStatus[2] * 180.0 / Math.PI).toFloat())
    ) {
        val sensitivityAdjuster = (1 - abs((sunZenith%90.0)/90.0)).toFloat()
        //Log.i("sensitivityAdjuster", sensitivityAdjuster.toString())
        Spacer(
            modifier = modifier
                .align(Alignment.Center)
                .offset(xPos * sensitivityAdjuster, yPos)
                //.background(Color.Red, CircleShape)
                .border(7.dp, color, CircleShape)
                .width(240.dp)
                .height(240.dp)
        )
    }
}

fun getARPos(sensorStatus: FloatArray, currentContext: Context, sunZenith: Double, sunAzimuth: Double): List<Dp> {
    val screenWidthHeight = getScreenWidthHeightInMm(currentContext)
    val screenWidth = screenWidthHeight[1] // + 80
    val screenHeight = screenWidthHeight[1] * 3/4 // - 30

    // When you rotate the device with the camera along the z-axis,
    // the width will change and gradually become the height.
    // And the height will change as well and gradually become the width
    val actualScreenWidth = screenWidth //+ (screenHeight-screenWidth) * abs(sin(sensorStatus[2]))
    val actualScreenHeight = screenHeight //+ (screenWidth-screenHeight) * abs(sin(sensorStatus[2]))

    println("actualScreenWidth: " + actualScreenWidth)

    val cameraFOV = getCameraFOV(currentContext)
    val horizontalFOV = cameraFOV[0] * (Math.PI / 180.0) // * 0.5
    val verticalFOV = cameraFOV[1] * (Math.PI / 180.0)

    println("screenWidth: $screenWidth. screenHeight: $screenHeight. horizontalFOV: $horizontalFOV. verticalFOV: $verticalFOV")
    //println("sensorStatus[1]: ${sensorStatus[1]}")
    //println("verticalFOV: $verticalFOV")
    //println("sensorStatus[1] / verticalFOV: ${sensorStatus[1] / verticalFOV}")
    //println("(screenHeight * (sensorStatus[1] / verticalFOV)): ${(screenHeight * (sensorStatus[1] / verticalFOV))}")

    //println("sunZenith: ${sunZenith * (180.0 / Math.PI)}, sunAzimuth: ${sunAzimuth * (180.0 / Math.PI)}")

    //val xPos = -mmToDp((actualScreenWidth * (sensorStatus[0] / horizontalFOV)), currentContext)
    //val yPos = -mmToDp((actualScreenHeight * (sensorStatus[1] / verticalFOV)), currentContext)
    val xPos = -mmToDp((actualScreenWidth * ((sensorStatus[0] + sunAzimuth) / horizontalFOV)), currentContext)
    val yPos = -mmToDp((actualScreenHeight * ((sensorStatus[1] + sunZenith) / verticalFOV)), currentContext)

    //println("dp xPos: $xPos. yPos: $yPos")
    //println("mm xPos: ${(screenWidth * (sensorStatus[0] / horizontalFOV))}. yPos: ${(screenHeight * (sensorStatus[1] / verticalFOV))}")

    return listOf(xPos, yPos)
}

fun mmToDp(mm: Double, currentContext: Context): Dp {
    val displayMetrics = currentContext.resources.displayMetrics
    val dpi = displayMetrics.densityDpi.toDouble()
    val density = displayMetrics.density

    val dp = (mm * dpi) / (25.4 * density)

    return dp.dp
}
/*fun mmToDp(mm: Double, currentContext: Context): Dp {
    val density: Float = currentContext.resources.displayMetrics.density
    println((mm / 25.4) * 160)
    val dp: Dp = ((mm / 25.4) * (/* *density? */ 160)).dp

    return dp
}*/

fun getScreenWidthHeightInMm(currentContext: Context): List<Float> {
    val displayMetrics = currentContext.resources.displayMetrics
    val widthPx = displayMetrics.widthPixels
    val heightPx = displayMetrics.heightPixels
    val densityDpi = displayMetrics.densityDpi
    val widthMm = widthPx / (densityDpi / 25.4f)
    val heightMm = heightPx / (densityDpi / 25.4f)
    return listOf(widthMm, heightMm)
}

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
    val horizontalFOV = 2.0 * Math.atan((sensorSize.width / (2.0 * focalLengths[0])).toDouble()) * 180.0 / Math.PI
    val verticalFOV = 2.0 * Math.atan((sensorSize.height / (2.0 * focalLengths[0])).toDouble()) * 180.0 / Math.PI

    return listOf(horizontalFOV, verticalFOV)
}

@Composable
fun skyDirections(modifier: Modifier, sensorStatus: FloatArray, current: Context) {
    val northTextPos: List<Dp> = getARPos(sensorStatus, LocalContext.current, Math.toRadians(0.0), Math.toRadians(0.0))
    SkyDirection(modifier, sensorStatus, northTextPos, "N")

    val northEastTextPos: List<Dp> = getARPos(sensorStatus, LocalContext.current, Math.toRadians(0.0), Math.toRadians(45.0))
    SkyDirection(modifier, sensorStatus, northEastTextPos, "NE")

    val eastTextPos: List<Dp> = getARPos(sensorStatus, LocalContext.current, Math.toRadians(0.0), Math.toRadians(90.0))
    SkyDirection(modifier, sensorStatus, eastTextPos, "E")

    val southEastTextPos: List<Dp> = getARPos(sensorStatus, LocalContext.current, Math.toRadians(0.0), Math.toRadians(135.0))
    SkyDirection(modifier, sensorStatus, southEastTextPos, "SE")

    val south2TextPos: List<Dp> = getARPos(sensorStatus, LocalContext.current, Math.toRadians(0.0), Math.toRadians(180.0))
    SkyDirection(modifier, sensorStatus, south2TextPos, "S")

    val south1TextPos: List<Dp> = getARPos(sensorStatus, LocalContext.current, Math.toRadians(0.0), Math.toRadians(-180.0))
    SkyDirection(modifier, sensorStatus, south1TextPos, "S")

    val southWestTextPos: List<Dp> = getARPos(sensorStatus, LocalContext.current, Math.toRadians(0.0), Math.toRadians(-135.0))
    SkyDirection(modifier, sensorStatus, southWestTextPos, "SW")

    val westTextPos: List<Dp> = getARPos(sensorStatus, LocalContext.current, Math.toRadians(0.0), Math.toRadians(-90.0))
    SkyDirection(modifier, sensorStatus, westTextPos, "W")

    val northWestTextPos: List<Dp> = getARPos(sensorStatus, LocalContext.current, Math.toRadians(0.0), Math.toRadians(-45.0))
    SkyDirection(modifier, sensorStatus, northWestTextPos, "NW")
}

@Composable
fun SkyDirection(modifier: Modifier, sensorStatus: FloatArray, pos: List<Dp>, directionText: String) {
    // the text showing a sky direction
    Box(
        modifier = modifier
            .fillMaxSize()
            .fillMaxHeight()
            .fillMaxWidth()
            .rotate((-sensorStatus[2] * 180.0 / Math.PI).toFloat())
    ) {
        Text(
            text = directionText,
            modifier = modifier
                .align(Alignment.Center)
                .offset(pos[0], pos[1]),
            color = Color.Black,
            fontSize = 35.sp
        )
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraContent(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Permission(
        permission = Manifest.permission.CAMERA,
        rationale = "To use AR-mode, you need to give the app permission to use your camera.",
        permissionNotAvailableContent = {
            Column(modifier) {
                Text("O noes! No Camera!")
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = {
                    context.startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", context.packageName, null)
                    })
                }) {
                    Text("Open Settings")
                }
            }
        }
    ) {
        CameraPreview(modifier)
    }
}