# War of Realms - Setup Instructions

## Prerequisites

- Java Development Kit (JDK) 17 or higher
- JavaFX SDK 21.0.0 or compatible with your Java version

### 1. Build and Run

After setting the environment variable, restart VS Code and run:

```bash
# Using VS Code tasks
Build: Ctrl+Shift+B  (Compile Java Files)
Run: Ctrl+Shift+B then select "Run War of Realms"

# Or from terminal
javac -d bin -cp "src;C:/javafx-sdk-25.0.2/lib/*" --module-path C:/javafx-sdk-25.0.2/lib/ --add-modules javafx.base,javafx.controls,javafx.fxml,javafx.graphics,javafx.media,javafx.swing,javafx.web -sourcepath src src/**/*.java
java --module-path C:/javafx-sdk-25.0.2/lib/ --add-modules javafx.base,javafx.controls,javafx.fxml,javafx.graphics,javafx.media,javafx.swing,javafx.web -cp bin main.Main
```

## Troubleshooting

### "Module javafx.base not found"
- Ensure `JAVAFX_SDK_PATH` is set correctly and points to the `lib` directory
- Restart VS Code after setting the environment variable

### "Unsupported major.minor version"
- Version mismatch between JDK and JavaFX SDK
- Verify both are compatible versions (e.g., JDK 21 with JavaFX 21)
