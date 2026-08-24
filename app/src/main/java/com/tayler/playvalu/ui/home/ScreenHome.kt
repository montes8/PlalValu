package com.tayler.playvalu.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.tayler.playvalu.R
import com.tayler.playvalu.component.LadMusicDetail
import com.tayler.playvalu.model.MusicModel
import com.tayler.playvalu.ui.AppViewModel
import com.valu.uitaycompose.utils.textGabbi14
import com.valu.uitaycompose.utils.textGabbi16

@Composable
fun ScreenHome(viewModel: AppViewModel, paddingValues: PaddingValues) {
    val context = LocalContext.current
    viewModel.loadMusic(context)
    viewModel.visibleToolbar = true

    Column(Modifier
        .padding(paddingValues)
        .background(MaterialTheme.colorScheme.surface)) {
        
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.surface)) {
            ConfigLisMusic(viewModel) { index ->
                viewModel.playMusic(index)
            }
            
            if (viewModel.visibleMusic) {
                LadMusicDetail(viewModel)
            }
        }
    }
}

@Composable
fun ConfigLisMusic(viewModel: AppViewModel, onClick: (Int) -> Unit) {
    Column(
        modifier = Modifier.padding(
            top = 12.dp, end = 8.dp, start = 8.dp
        )
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            itemsIndexed(viewModel.uiStateDataMusic.listMusic) { index, music ->
                MusicItem(music, index) {
                    onClick(it)
                }
            }
        }
    }
}

@Composable
fun MusicItem(model: MusicModel, position: Int, onClick: (Int) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(position) }
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.08f), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_music),
                    contentDescription = null,
                    modifier = Modifier.size(22.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = model.name.replace(".mp3", ""),
                    maxLines = 1,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = textGabbi16
                )
                Text(
                    text = "Audio local",
                    maxLines = 1,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                    style = textGabbi14
                )
            }

            Icon(
                painter = painterResource(R.drawable.ic_play),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                modifier = Modifier.size(20.dp)
            )
        }
        HorizontalDivider(
            modifier = Modifier.padding(start = 76.dp),
            thickness = 0.5.dp,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.1f)
        )
    }
}
