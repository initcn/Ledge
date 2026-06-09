package app.initcn.ledge.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val LedgeShapes = Shapes(
    // Used for nested controls, inner elements, and action triggers (e.g., Save button)
    small = RoundedCornerShape(14.dp),

    // Used for standard interaction forms, text input fields, and dropdown containers
    medium = RoundedCornerShape(16.dp),

    // Used for historical items, rows, lists, and interactive swipable layouts
    large = RoundedCornerShape(28.dp),

    // Used for master dashboard layouts and focal analytics containers
    extraLarge = RoundedCornerShape(30.dp)
)