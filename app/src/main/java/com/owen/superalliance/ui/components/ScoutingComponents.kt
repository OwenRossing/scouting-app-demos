package com.owen.superalliance.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.owen.superalliance.ui.theme.*

/**
 * Reusable UI components for the scouting app
 */

@Composable
fun ScoutCard(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = DarkPanel.copy(alpha = 0.82f),
                shape = RoundedCornerShape(16.dp)
            )
            .border(
                width = 1.dp,
                color = DarkStroke.copy(alpha = 0.9f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(14.dp)
    ) {
        Text(
            text = title.uppercase(),
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = DarkMuted,
            letterSpacing = 0.2.sp,
            modifier = Modifier.padding(bottom = 10.dp)
        )
        content()
    }
}

@Composable
fun SegmentedButton(
    options: List<String>,
    selectedOption: String?,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    columns: Int = 3
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        options.forEach { option ->
            val isSelected = option == selectedOption
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(
                        if (isSelected) DarkAccent.copy(alpha = 0.18f)
                        else DarkPanel2.copy(alpha = 0.9f)
                    )
                    .border(
                        width = 1.dp,
                        color = if (isSelected) DarkAccent.copy(alpha = 0.95f)
                        else DarkStroke.copy(alpha = 0.95f),
                        shape = RoundedCornerShape(14.dp)
                    )
                    .clickable { onOptionSelected(option) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = option,
                    fontWeight = FontWeight.ExtraBold,
                    color = if (isSelected) DarkText else DarkMuted,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun BucketButton(
    label: String,
    sublabel: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .height(56.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(
                if (isSelected) DarkGood.copy(alpha = 0.14f)
                else DarkPanel2.copy(alpha = 0.9f)
            )
            .border(
                width = 1.dp,
                color = if (isSelected) DarkGood.copy(alpha = 0.95f)
                else DarkStroke.copy(alpha = 0.95f),
                shape = RoundedCornerShape(14.dp)
            )
            .clickable { onClick() }
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = label,
            fontWeight = FontWeight.ExtraBold,
            color = if (isSelected) DarkText else DarkMuted,
            fontSize = 14.sp
        )
        Text(
            text = sublabel,
            fontWeight = FontWeight.Bold,
            color = DarkMuted,
            fontSize = 11.sp
        )
    }
}

@Composable
fun TagButton(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(48.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(
                if (isSelected) DarkAccent.copy(alpha = 0.16f)
                else DarkPanel2.copy(alpha = 0.85f)
            )
            .border(
                width = 1.dp,
                color = if (isSelected) DarkAccent.copy(alpha = 0.95f)
                else DarkStroke.copy(alpha = 0.95f),
                shape = RoundedCornerShape(14.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontWeight = FontWeight.ExtraBold,
            color = if (isSelected) DarkText else DarkMuted,
            fontSize = 13.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = DarkAccent,
    enabled: Boolean = true
) {
    Box(
        modifier = modifier
            .height(48.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(
                if (enabled) color.copy(alpha = 0.18f)
                else DarkPanel2.copy(alpha = 0.5f)
            )
            .border(
                width = 1.dp,
                color = if (enabled) color.copy(alpha = 0.95f)
                else DarkStroke.copy(alpha = 0.5f),
                shape = RoundedCornerShape(14.dp)
            )
            .clickable(enabled = enabled) { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.Bold,
            color = if (enabled) DarkText else DarkMuted.copy(alpha = 0.5f),
            fontSize = 14.sp
        )
    }
}

@Composable
fun CounterDisplay(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(44.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(DarkPanel2.copy(alpha = 0.7f))
                .border(
                    width = 1.dp,
                    color = DarkStroke.copy(alpha = 0.85f),
                    shape = RoundedCornerShape(14.dp)
                )
                .padding(horizontal = 12.dp, vertical = 10.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = label,
                fontWeight = FontWeight.Bold,
                color = DarkMuted,
                fontSize = 13.sp
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        Box(
            modifier = Modifier
                .widthIn(min = 80.dp)
                .height(44.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(DarkPanel2.copy(alpha = 0.7f))
                .border(
                    width = 1.dp,
                    color = DarkStroke.copy(alpha = 0.85f),
                    shape = RoundedCornerShape(14.dp)
                )
                .padding(horizontal = 12.dp, vertical = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = value,
                fontWeight = FontWeight.ExtraBold,
                color = DarkText,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun CounterDisplay(
    label: String,
    count: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(44.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(DarkPanel2.copy(alpha = 0.7f))
                .border(
                    width = 1.dp,
                    color = DarkStroke.copy(alpha = 0.85f),
                    shape = RoundedCornerShape(14.dp)
                )
                .padding(horizontal = 12.dp, vertical = 10.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = label,
                fontWeight = FontWeight.Bold,
                color = DarkMuted,
                fontSize = 13.sp
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        Box(
            modifier = Modifier
                .width(80.dp)
                .height(44.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(DarkPanel2.copy(alpha = 0.7f))
                .border(
                    width = 1.dp,
                    color = DarkStroke.copy(alpha = 0.85f),
                    shape = RoundedCornerShape(14.dp)
                )
                .padding(horizontal = 12.dp, vertical = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = count.toString(),
                fontWeight = FontWeight.ExtraBold,
                color = DarkText,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun SectionLabel(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold,
        color = DarkMuted,
        modifier = modifier.padding(bottom = 6.dp)
    )
}

@Composable
fun Pill(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = DarkMuted
) {
    Box(
        modifier = modifier
            .height(34.dp)
            .clip(RoundedCornerShape(999.dp))
            .background(DarkPanel2.copy(alpha = 0.8f))
            .border(
                width = 1.dp,
                color = DarkStroke.copy(alpha = 0.9f),
                shape = RoundedCornerShape(999.dp)
            )
            .padding(horizontal = 10.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.SemiBold,
            color = color,
            fontSize = 12.sp
        )
    }
}
