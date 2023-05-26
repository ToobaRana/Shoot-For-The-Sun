package com.example.sunandmoon.ar

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sunandmoon.R
import com.example.sunandmoon.camera.CameraPreview
import com.example.sunandmoon.data.ARUIState
import com.example.sunandmoon.ui.components.NavigationComposable
import com.example.sunandmoon.ui.components.buttonComponents.OpenARSettingsButton
import com.example.sunandmoon.ui.components.infoComponents.CalibrateMagnetometerDialogue
import com.example.sunandmoon.ui.components.infoComponents.GiveLocationPermissionPleaseDialogue
import com.example.sunandmoon.ui.components.infoComponents.MissingSensorsDialogue
import com.example.sunandmoon.ui.components.userInputComponents.EditARSettings
import com.example.sunandmoon.util.Permission
import com.example.sunandmoon.viewModel.ARViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi


//The main component of the AR-functionality that makes everything come together
@Composable
fun SunAR(
    modifier: Modifier,
    navigateToNextBottomBar: (index: Int) -> Unit,
    packageManager: PackageManager,
    arViewModel: ARViewModel = hiltViewModel()
) {
    val arUIState by arViewModel.arUIState.collectAsState()

    Surface {
        CameraContent(Modifier.fillMaxSize())
    }

    val hasMagnetometer = packageManager.hasSystemFeature(PackageManager.FEATURE_SENSOR_COMPASS)
    val hasGyroscope = packageManager.hasSystemFeature(PackageManager.FEATURE_SENSOR_GYROSCOPE)
    val hasAccelerometer = packageManager.hasSystemFeature(PackageManager.FEATURE_SENSOR_ACCELEROMETER)

    Log.i(
        "sensorStatus",
        "hasMagnetometer: $hasMagnetometer, hasGyroscope: $hasGyroscope, hasAccelerometer: $hasAccelerometer"
    )

    val sensorStatus = remember {
        mutableStateOf(FloatArray(0) { 0f })
    }

    if (hasMagnetometer && hasGyroscope && hasAccelerometer) {
        val localContext = LocalContext.current

        val sensorManager: SensorManager =
            localContext.getSystemService(Context.SENSOR_SERVICE) as SensorManager

        val rotationVectorSensor: Sensor =
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
                    )

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
    else {
        if(!arUIState.hasShownMissingSensorsMessage) {
            MissingSensorsDialogue(modifier)
        }
    }
    Log.i("ararar", "aaa-11")
    SunARUI(modifier, sensorStatus, navigateToNextBottomBar, arUIState)
}
//draws the rest of the ui-components for the AR-screen (bottom bar, top-bar and editSettings)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SunARUI(
    modifier: Modifier,
    sensorStatus: MutableState<FloatArray>,
    navigateToNextBottomBar: (index: Int) -> Unit,
    arUIState: ARUIState,
    arViewModel: ARViewModel = viewModel()
) {
    // for the AR functionality
    val sunZenith = arUIState.sunZenith
    val sunAzimuth = arUIState.sunAzimuth
    Log.i("ararar", sunZenith.toString())
    if(arUIState.location == null) {
        GiveLocationPermissionPleaseDialogue(modifier) { navigateToNextBottomBar(0) }
    }
    else if(sunZenith != null && sunAzimuth != null) {
        if(!arUIState.hasShownCalibrateMagnetMessage) {
            CalibrateMagnetometerDialogue(modifier)
        }
        SunFinder(modifier, sensorStatus.value, sunZenith, sunAzimuth)
    }

    // for the non-AR UI part for this screen
    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
             Row(
                 modifier
                     .fillMaxWidth()
                     .background(Color.Transparent), horizontalArrangement = Arrangement.End) {
                 OpenARSettingsButton(
                     modifier,
                     MaterialTheme.colorScheme.primary,
                     MaterialTheme.colorScheme.onPrimary
                 ) { arViewModel.openCloseARSettings() }
             }
        },
        bottomBar = { NavigationComposable(modifier = modifier, page = 1, navigateToNextBottomBar = navigateToNextBottomBar)}
    ) {
        if(arUIState.editARSettingsIsOpened) {
            EditARSettings(
                modifier = modifier,
                arUIState = arUIState
            )
        }
        val p = it /* just to remove the error message */
    }
}


//tells the user if camera permissions are not given to the app, and requests them if necessary
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraContent(modifier: Modifier = Modifier) {

    Permission(
        permission = Manifest.permission.CAMERA,
        rationale = stringResource(id = R.string.GiveCameraPermissionForAR),
        permissionNotAvailableContent = {
            Column(modifier) {
                Text(stringResource(id = R.string.MissingCameraPermission))
            }
        }
    ) {
        CameraPreview(modifier)
    }
}