package com.currencyconvertor.ui.conversionhistory

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottomAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStartAxis
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineSpec
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent
import com.patrykandpatrick.vico.compose.common.shader.color
import com.patrykandpatrick.vico.compose.common.shader.verticalGradient
import com.patrykandpatrick.vico.core.cartesian.axis.Axis
import com.patrykandpatrick.vico.core.cartesian.axis.AxisItemPlacer
import com.patrykandpatrick.vico.core.cartesian.axis.AxisPosition
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModel
import com.patrykandpatrick.vico.core.cartesian.data.LineCartesianLayerModel
import com.patrykandpatrick.vico.core.common.shader.DynamicShader

private val model =
    CartesianChartModel(
        LineCartesianLayerModel.build {
            series(x = listOf(1, 2, 3, 4), y = listOf(2, 5, 3, 4))
        },
    )
private val startAxis: Axis<AxisPosition.Vertical.Start>
    @Composable
    get() =
        rememberStartAxis(
            label = rememberTextComponent(color = Color.Black),
            itemPlacer = remember { AxisItemPlacer.Vertical.count(count = { 4 }) },
        )


@Composable
private fun getLineLayer(verticalAxisPosition: AxisPosition.Vertical? = null) =
    rememberLineCartesianLayer(
        lines =
        listOf(
            rememberLineSpec(
                shader = DynamicShader.color(Color.DarkGray),
                backgroundShader =
                DynamicShader.verticalGradient(
                    arrayOf(
                        Color.DarkGray,
                        Color.DarkGray.copy(alpha = 0f)
                    )
                ),
            )
        ),
        verticalAxisPosition = verticalAxisPosition,
    )

@Composable
@Preview("Line chart", widthDp = 350)
fun LineChart(modifier: Modifier = Modifier) {
    CartesianChartHost(
        chart =
        rememberCartesianChart(
            getLineLayer(),
            startAxis = startAxis,
            bottomAxis = rememberBottomAxis(),
        ),
        model = model,
        modifier = modifier,
    )
}