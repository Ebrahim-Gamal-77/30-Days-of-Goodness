package com.example.goodnessdays

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.goodnessdays.data.DaysRepo.days
import com.example.goodnessdays.model.Day
import com.example.goodnessdays.ui.theme.GoodnessMonthTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GoodnessMonthTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    GoodnessApp(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun GoodnessApp(
    modifier: Modifier = Modifier
) {

    // done 3 : Optimize how to change between pages
    var pageNum by remember {
        mutableIntStateOf(1)
    }

    var showDescription by remember {
        mutableStateOf(false)
    }

    // done 4 : Add scaffold with app name, and add Display type in typography

    // Scaffold With top app bar
    Scaffold(
        topBar = { GoodnessTopAppBar(modifier = Modifier.padding(top = 2.dp)) },
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .animateContentSize()
                .verticalScroll(rememberScrollState())
        ) {

            // This spacer is to preserve the size of top bar from overlapping
            Spacer(modifier = Modifier.height(56.dp))

            Spacer(modifier = Modifier.weight(1f))

            GoodnessCard(day = days[pageNum - 1],
                modifier = Modifier.offset(y = (-60).dp),
                descriptionState = showDescription,
                onCardClick = { showDescription = !showDescription })

            Spacer(modifier = Modifier.weight(1f))

            DayButtons(
                previousOnClick = {
                    if (pageNum > 1) {
                        showDescription = false
                        pageNum--
                    }
                },
                nextOnClick = {
                    if (pageNum < 30) {
                        showDescription = false
                        pageNum++
                    }
                },
                modifier = Modifier.padding(
                    bottom = 30.dp,
                    top = 4.dp
                )
            )
        }
    }

}


@Composable
fun GoodnessCard(
    day: Day,
    descriptionState: Boolean,
    onCardClick: () -> Unit,
    modifier: Modifier = Modifier
) {


    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = stringResource(id = R.string.day) + " ${day.dayNum}",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(
                start = 18.dp,
                top = 4.dp,
                bottom = 4.dp
            )
        )

        Card(
//            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp)
                .animateContentSize()
                .clickable(onClick = onCardClick),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(
                    top = 8.dp,
                    bottom = 2.dp,
                    start = 12.dp,
                    end = 12.dp
                )
            ) {
                // Title
                Text(
                    text = stringResource(id = day.title),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    textAlign = TextAlign.Center
                )

                // SubTitle
                day.subTitle?.let {
                    Text(
                        text = stringResource(id = it),
                        style = MaterialTheme.typography.headlineSmall,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                } ?: Spacer(modifier = Modifier.height(12.dp))

                // Image
                Image(
                    painter = painterResource(id = day.image),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .sizeIn(maxHeight = 300.dp)
                )

                // done : change contrast between icon background and card background

                // Show Description Icon
                AnimatedVisibility(visible = !descriptionState) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = stringResource(id = R.string.show_description),
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier
                            .size(26.dp)
                            .padding(
                                top = 5.dp,
                                bottom = 5.dp
                            )
                            .background(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                shape = RoundedCornerShape(size = 20.dp)
                            )
                    )
                }

                // Description
                AnimatedVisibility(visible = descriptionState) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = stringResource(id = day.description),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        textAlign = TextAlign.Start,
                        modifier = Modifier
                            .padding(
                                top = 6.dp,
                                bottom = 10.dp
                            )
                            .fillMaxWidth()
                    )
                }
            }
        }
    }
}


// done 2 : Add 'previous' and 'next' buttons

@Composable
fun DayButtons(
    previousOnClick: () -> Unit,
    nextOnClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        // Previous Button
        Button(
            modifier = Modifier.size(
                width = 140.dp,
                height = 60.dp
            ),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
            onClick = previousOnClick
        ) {
            Text(
                text = stringResource(id = R.string.previous),
                color = MaterialTheme.colorScheme.onError,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
        }

        // Next Button
        Button(
            modifier = Modifier.size(
                width = 140.dp,
                height = 60.dp
            ),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            onClick = nextOnClick
        ) {
            Text(
                text = stringResource(id = R.string.next),
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}

// done 5 : Add an Icon for the app

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoodnessTopAppBar(modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(id = R.string.app_name),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.displaySmall,
                    textAlign = TextAlign.Center
                )

                Image(
                    painter = if (isSystemInDarkTheme()) painterResource(id = R.drawable.mosque_icon)
                    else painterResource(id = R.drawable.mosque_icon_2),
                    modifier = Modifier
                        .size(54.dp)
                        .offset(y = (-4).dp)
                        .padding(8.dp),
                    alignment = Alignment.BottomEnd,
                    contentDescription = null
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHighest),
        modifier = modifier
    )
}

@Preview(
    "Light Mode",
    showBackground = true,
    showSystemUi = true,
)
@Preview(
    "Dark Mode",
    showBackground = true,
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun GoodnessPreview() {
    GoodnessMonthTheme(dynamicColor = false) {
//        GoodnessCard(days[0])
//        DayButtons()
        GoodnessApp()
    }
}
