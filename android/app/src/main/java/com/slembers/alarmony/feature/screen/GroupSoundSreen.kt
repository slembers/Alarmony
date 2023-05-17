package com.slembers.alarmony.feature.screen


import android.app.Application
import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.slembers.alarmony.R
import com.slembers.alarmony.feature.common.NavItem
import com.slembers.alarmony.feature.common.ui.theme.toColor
import com.slembers.alarmony.feature.ui.group.GroupToolBar
import com.slembers.alarmony.feature.ui.group.soundIconView
import com.slembers.alarmony.feature.ui.group.soundIconView2
import com.slembers.alarmony.model.db.SoundItem
import com.slembers.alarmony.util.DisplayDpUtil
import com.slembers.alarmony.util.Sound
import com.slembers.alarmony.util.groupSoundInfos
import com.slembers.alarmony.viewModel.GroupSoundViewModel
import com.slembers.alarmony.viewModel.GroupViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

var player : MediaPlayer? = null
val size : Int = 350
@Composable
@ExperimentalMaterial3Api
@ExperimentalGlideComposeApi
fun SoundScreen(
    navController : NavHostController = rememberNavController(),
    viewModel : GroupViewModel
) {
    val context = LocalContext.current
    // 미디어플레이어 초기화
    closePlayer()

    val sound : GroupSoundViewModel = viewModel(
        factory = GroupSoundViewModel.GroupSoundViewModelFactory(context.applicationContext as Application)
    )

    val list = sound.soundItemList.observeAsState()

    LaunchedEffect(Unit) {
        val store : MutableMap<String, Sound> = mutableStateMapOf()
        Sound().soundList().map { store.put(it.soundName, it) }
        sound.updateSoundItemAll(store)
    }
    val soundItems : List<SoundItem> = groupSoundInfos()
    val currentSound by viewModel.sound.observeAsState()
    var playSound by remember { mutableStateOf("") }

    val px = context.resources.displayMetrics.widthPixels
    val count = DisplayDpUtil.px2dp(px,context) / size

    Log.d("group","[그룸소리] 로그를 확인하기 위함 : ${list.value}")
    Log.d("group","[그룸소리] 미디어 플레이어 : $player")

    Scaffold(
        topBar = {
            GroupToolBar(
                title = NavItem.Sound.title,
                navClick = {
                    navController.popBackStack()
                    closePlayer()
                }
            )
        },
        containerColor = "#F9F9F9".toColor(),
        content = {
                innerPadding ->
            Column( modifier = Modifier
                .padding(innerPadding)
                .padding(10.dp)
                .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LazyVerticalGrid(
                    modifier = Modifier.fillMaxSize(),
                    columns = GridCells.Adaptive(minSize = size.dp),
                ) {
                    items(list.value ?: listOf()) {
                        soundIconView2(
                            soundItem = it,
                            onClick = it.soundName == currentSound?.soundName,
                            checkBox = {
                                viewModel.onChangeSound(it)
                                navController.popBackStack()},
                            isPlaySound = playSound == it.soundName,
                            currentPlayer = {
                                // 현재 플레이한 음악이라면
                                if(playSound == it.soundName) {
                                    if(player != null) {
                                        Log.d("group","[그룸소리] 소리변경 : $player 소리 : $playSound 이미지 : ${it.soundImageId}")
                                        stopAudio()
                                        playSound = ""
                                        sound.updateSoundStop(it.soundName)
                                    } else {
                                        Log.d("group","[그룸소리] 소리변경 : $player 소리 : $playSound")
                                        playSound = it.soundName
                                        playAudio(context, it.soundMp3Id)
                                        sound.updateSoundPlay(it.soundName)
                                    }
                                } else {
                                    if(player != null) {
                                        Log.d("group","[그룸소리] 소리변경 : $player 소리 : $playSound")
                                        // 이전에 실행하고 있던 오디오 리셋
                                        sound.updateSoundStop(playSound)
                                        // 현재 클릭한 오디오를 실행시키고
                                        playSound = it.soundName
                                        playAudio(context, it.soundMp3Id)
                                        sound.updateSoundPlay(it.soundName)
                                    } else {
                                        Log.d("group","[그룸소리] 소리변경 : $player 소리 : $playSound")
                                        playSound = it.soundName
                                        playAudio(context, it.soundMp3Id)
                                        sound.updateSoundPlay(it.soundName)
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    )
}

fun soundAllStop( soundItems : List<SoundItem> ) {
    for ( item in soundItems) {
        item.isPlaying = false
        item.soundMp3Content?.release()
    }
}

fun stopAudio() {
    if(player != null && player!!.isPlaying) {
        player!!.stop()
    }
}

fun playAudio(context : Context, playSound : Int) {
    try {
        closePlayer()
        player = MediaPlayer.create(context,playSound)
        player?.start()
    } catch (e : Exception) {

    }
}

fun closePlayer() {
    if(player != null) {
        player!!.release()
        player = null
    }
}