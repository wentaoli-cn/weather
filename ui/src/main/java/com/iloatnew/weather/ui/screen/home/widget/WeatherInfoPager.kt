package com.iloatnew.weather.ui.screen.home.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.iloatnew.weather.R
import com.iloatnew.weather.domain.model.WeatherInfo
import com.iloatnew.weather.ui.theme.WeatherColor
import java.time.format.DateTimeFormatter

@Composable
fun WeatherInfoPager(weatherInfoList: List<WeatherInfo>) {
    val pagerState = rememberPagerState(pageCount = { weatherInfoList.size })
    Column {
        HorizontalPager(
            state = pagerState,
            verticalAlignment = Alignment.Top,
        ) { index ->
            weatherInfoList[index].let {
                Column {
                    Text(
                        stringResource(
                            R.string.home_weather_latitude,
                            it.locationInfo.latLng.latitude,
                        )
                    )
                    Text(
                        stringResource(
                            R.string.home_weather_longitude,
                            it.locationInfo.latLng.longitude,
                        )
                    )
                    if (index == 0) {
                        Text(
                            stringResource(
                                R.string.home_weather_accuracy,
                                it.locationInfo.accuracy,
                            )
                        )
                    }
                    Text(
                        stringResource(
                            R.string.home_weather_address,
                            it.locationInfo.address
                        )
                    )
                    Text(
                        stringResource(R.string.home_weather_elevation, it.elevation)
                    )
                    Text(
                        stringResource(
                            R.string.home_weather_temperature,
                            it.temperatureCelsius
                        )
                    )
                    Text(
                        stringResource(
                            R.string.home_weather_apparent_temperature,
                            it.apparentTemperatureCelsius,
                        )
                    )
                    Text(
                        stringResource(
                            R.string.home_weather_time,
                            it.time.format(TIME_FORMATTER),
                        )
                    )
                    Text(
                        stringResource(R.string.home_weather_description, it.description)
                    )
                }
            }
        }
        Row(
            Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(top = 24.dp, bottom = 12.dp),
            horizontalArrangement = Arrangement.Center,
        ) {
            repeat(pagerState.pageCount) { index ->
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(
                            if (pagerState.currentPage == index)
                                Color.Black
                            else
                                WeatherColor.darkGray
                        )
                        .size(8.dp),
                )
            }
        }
    }
}

private const val TIME_PATTERN = "yyyy-MM-dd HH:mm:ss"
private val TIME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern(TIME_PATTERN)
