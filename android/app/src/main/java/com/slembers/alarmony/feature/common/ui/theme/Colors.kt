package com.slembers.alarmony.feature.common.ui.theme

import androidx.compose.ui.graphics.Color

// 색상 벡터값 받아서 컬러로 바꿔줌
fun String.toColor() = Color(android.graphics.Color.parseColor(this))
val primaryColor = Color(0xff00B4D8)
val cardColor = Color(0xfffcfcfc)
val textColor = Color(0xff000000)
val backgroundColor = Color(0xffEDEDED)