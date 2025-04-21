package com.iloatnew.weather.ui.screen

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.google.android.gms.maps.model.LatLng
import com.iloatnew.weather.BuildConfig
import com.iloatnew.weather.ui.screen.addlocation.AddLocationScreen
import com.iloatnew.weather.ui.screen.home.HomeScreen
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.serialDescriptor
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure
import timber.log.Timber
import kotlin.reflect.typeOf

@Serializable
object Home

// https://issuetracker.google.com/issues/348468840
@Serializable
data class AddLocation(val wrappedLatLng: WrappedLatLng)

@Serializable
data class WrappedLatLng(@Serializable(with = LatLngSerializer::class) val latLng: LatLng)

object WrappedLatLngNavType : NavType<WrappedLatLng>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): WrappedLatLng? {
        val value = bundle.getString(key) ?: return null
        return fromString(value)
    }

    override fun parseValue(value: String): WrappedLatLng = fromString(value)


    override fun put(bundle: Bundle, key: String, value: WrappedLatLng) {
        bundle.putString(key, "${value.latLng.latitude},${value.latLng.longitude}")
    }

    override fun serializeAsValue(value: WrappedLatLng): String =
        "${value.latLng.latitude},${value.latLng.longitude}"

    private fun fromString(value: String): WrappedLatLng {
        val (latitude, longitude) = value.split(",").map { it.toDouble() }
        return WrappedLatLng(LatLng(latitude, longitude))
    }
}

object LatLngSerializer : KSerializer<LatLng> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor(serialName = "LatLng") {
        element(elementName = "latitude", descriptor = serialDescriptor<Double>())
        element(elementName = "longitude", descriptor = serialDescriptor<Double>())
    }

    override fun serialize(encoder: Encoder, value: LatLng) {
        encoder.encodeStructure(descriptor = descriptor) {
            encodeDoubleElement(descriptor = descriptor, index = 0, value = value.latitude)
            encodeDoubleElement(descriptor = descriptor, index = 1, value = value.longitude)
        }
    }

    override fun deserialize(decoder: Decoder): LatLng {
        var latitude = 0.0
        var longitude = 0.0
        decoder.decodeStructure(descriptor = descriptor) {
            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> latitude = decodeDoubleElement(descriptor = descriptor, index = 0)
                    1 -> longitude = decodeDoubleElement(descriptor = descriptor, index = 1)
                    CompositeDecoder.DECODE_DONE -> break
                    else -> {
                        if (BuildConfig.DEBUG) {
                            Timber.tag("LatLngSerializer").d("Unexpected index: %i", index)
                        }
                    }
                }
            }
        }
        return LatLng(latitude, longitude)
    }
}

@Composable
fun WeatherNavHost(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Home,
    ) {
        composable<Home> {
            HomeScreen(
                resultLatLng = navController.currentBackStackEntry?.savedStateHandle?.get<LatLng?>(
                    key = LAT_LNG_RESULT_KEY
                ),
                navigateToAddLocation = { latLng ->
                    navController.navigate(route = AddLocation(WrappedLatLng(latLng)))
                },
            )
        }
        composable<AddLocation>(
            typeMap = mapOf(typeOf<WrappedLatLng>() to WrappedLatLngNavType)
        ) { backStackEntry ->
            AddLocationScreen(
                initialLatLng = backStackEntry.toRoute<AddLocation>().wrappedLatLng.latLng,
                navigateBackWithLatLng = { latLng ->
                    navController
                        .previousBackStackEntry
                        ?.savedStateHandle
                        ?.set(key = LAT_LNG_RESULT_KEY, value = latLng)
                    navController.popBackStack()
                },
            )
        }
    }
}

private const val LAT_LNG_RESULT_KEY = "LatLng_Result_Key"
