package com.enmanuelbergling.feature.tvshows.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalGridApi
import androidx.compose.foundation.layout.Grid
import androidx.compose.foundation.layout.GridTrackSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.enmanuelbergling.core.ui.core.dimen
import com.enmanuelbergling.core.ui.theme.CornTimeTheme

/**
 * Showcase for the experimental Grid API item properties.
 * 
 * Demonstrates:
 * - Manual placement using 1-based indexing (row, column).
 * - Spanning multiple cells (rowSpan, columnSpan).
 * - End-relative indexing using negative values (-1 for last).
 * - Alignment within cells.
 */

@OptIn(ExperimentalGridApi::class)
@Preview
@Composable
fun GridItemPropertiesPreview() {
    CornTimeTheme {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp)
                .padding(16.dp),
            color = MaterialTheme.colorScheme.background,
            shape = MaterialTheme.shapes.large,
            shadowElevation = 4.dp
        ) {
            Grid(
                config = {
                    // 3 equal columns
                    column(1.fr)
                    column(1.fr)
                    column(1.fr)

                    // 4 rows
                    row(1.fr)
                    row(1.fr)
                    row(1.fr)
                    row(1.fr)
                    
                    gap(8.dp)
                },
                modifier = Modifier.padding(8.dp)
            ) {
                // Item 1: Manual placement at Row 1, Col 1, Spanning 2 columns
                GridCell(
                    text = "Header\n(R1, C1, Span 2C)",
                    color = Color(0xFFE91E63),
                    modifier = Modifier.gridItem(row = 1, column = 1, columnSpan = 2)
                )

                // Item 2: Manual placement at Row 1, Col 3, Spanning 2 rows
                GridCell(
                    text = "Sidebar\n(R1, C3, Span 2R)",
                    color = Color(0xFF3F51B5),
                    modifier = Modifier.gridItem(row = 1, column = 3, rowSpan = 2)
                )

                // Item 3: Manual placement at Row 2, Col 1, Spanning 1 row/col (default)
                GridCell(
                    text = "Box 1\n(R2, C1)",
                    color = Color(0xFF4CAF50),
                    modifier = Modifier.gridItem(row = 2, column = 1)
                )

                // Item 4: Manual placement at Row 2, Col 2
                GridCell(
                    text = "Box 2\n(R2, C2)",
                    color = Color(0xFFFFC107),
                    modifier = Modifier.gridItem(row = 2, column = 2)
                )

                // Item 5: Large Center Box spanning 2 rows and 2 columns
                // Placed at Row 3, Column 1
                GridCell(
                    text = "Main Content\n(R3, C1, Span 2R, 2C)",
                    color = Color(0xFF00BCD4),
                    modifier = Modifier.gridItem(row = 3, column = 1, rowSpan = 2, columnSpan = 2)
                )

                // Item 6: Using negative indexing to place at the very last row and last column
                // In a 4x3 grid, row -1 is 4, column -1 is 3.
                GridCell(
                    text = "Footer\n(R-1, C-1)",
                    color = Color(0xFF9C27B0),
                    modifier = Modifier.gridItem(row = -1, column = -1)
                )
                
                // Item 7: Using negative indexing for a sidebar bottom element
                GridCell(
                    text = "Extra\n(R3, C-1)",
                    color = Color(0xFF795548),
                    modifier = Modifier.gridItem(row = 3, column = -1)
                )
            }
        }
    }
}

@Composable
private fun GridCell(
    text: String,
    color: Color,
    modifier: Modifier = Modifier,
) {
    Surface(
        color = color.copy(alpha = 0.8f),
        shape = MaterialTheme.shapes.medium,
        modifier = modifier.fillMaxSize()
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(4.dp)
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }
    }
}



@OptIn(ExperimentalGridApi::class)
@Preview
@Composable
fun GridTextPreview() {
    CornTimeTheme {
        Grid(
            config = {
                // Define 2 columns with fractional widths
                column(1.fr)
                column(2.fr)

                // Define different row behaviors
                row(100.dp)          // Fixed height
                row(GridTrackSize.Auto) // Height based on content
                row(1.fr)            // Fill remaining space
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(480.dp)
                .background(MaterialTheme.colorScheme.surface)
                .padding(MaterialTheme.dimen.small)
        ) {
            // Row 1, Col 1
            Surface(
                color = Color.Red.copy(alpha = 0.5f),
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.fillMaxSize()
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text("Fixed 100dp\nCol 1.fr", textAlign = TextAlign.Center)
                }
            }

            // Row 1, Col 2
            Surface(
                color = Color.Blue.copy(alpha = 0.5f),
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.fillMaxSize()
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text("Fixed 100dp\nCol 2.fr", textAlign = TextAlign.Center)
                }
            }

            // Row 2, Col 1
            Surface(
                color = Color.Green.copy(alpha = 0.5f),
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    "Auto row height\nExpands with content\nLine 3\nLine 4",
                    modifier = Modifier.padding(MaterialTheme.dimen.small),
                    textAlign = TextAlign.Center
                )
            }

            // Row 2, Col 2
            Surface(
                color = Color.Yellow.copy(alpha = 0.5f),
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.fillMaxSize()
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text("Auto height\nMatching sibling", textAlign = TextAlign.Center)
                }
            }

            // Row 3, Col 1
            Surface(
                color = Color.Magenta.copy(alpha = 0.5f),
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.fillMaxSize()
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text("Flexible 1.fr", textAlign = TextAlign.Center)
                }
            }

            // Row 3, Col 2
            Surface(
                color = Color.Cyan.copy(alpha = 0.5f),
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.fillMaxSize()
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text("Flexible 1.fr\nCol 2.fr", textAlign = TextAlign.Center)
                }
            }
        }
    }
}