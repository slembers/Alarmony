package com.slembers.alarmony.feature.group

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.slembers.alarmony.feature.common.CardBox
import com.slembers.alarmony.feature.common.CardTitle
import com.slembers.alarmony.feature.common.ui.AlamonyBottomButton
import com.slembers.alarmony.feature.common.ui.AlamonyTopBar
import com.slembers.alarmony.feature.common.ui.compose.SoundChooseGrid
import com.slembers.alarmony.model.db.SoundItem
import com.slembers.alarmony.model.db.SoundModel

@ExperimentalMaterial3Api
@ExperimentalGlideComposeApi
class GroupDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            DetailsScreen()
        }
    }
}

@Preview
@Composable
@ExperimentalMaterial3Api
@ExperimentalGlideComposeApi
fun DetailsScreen(soundModel : SoundModel = viewModel()) {

    val scrollState = rememberScrollState()
    val sound : List<SoundItem> by soundModel.items.observeAsState(listOf<SoundItem>())

    Scaffold(
        topBar = { AlamonyTopBar(title = "알람 선택") },
        bottomBar = { AlamonyBottomButton(text = "저장")},
        content = {innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .verticalScroll(scrollState),
            ) {
                CardBox(
                    title = { CardTitle(title = "사용가능한 알람") },
                    content = { SoundChooseGrid(
                        modifier = Modifier.fillMaxWidth(),
                        itemList = sound
                    ) }
                )
            }
        }
    )
}