package com.passive.api.ui.overlay;

import com.passive.api.bot.Passive_BOT;
import com.runemate.game.api.osrs.local.hud.interfaces.OptionsTab;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.ptr.IntByReference;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;
import java.util.ArrayList;

public class MouseApp extends Application {
    public Passive_BOT bot;
    private Thread updateThread;
    private Canvas canvas;
    public Rectangle originalClientBounds;
    private int fixedClientGameWidth = 770;


    public int getMouseX(){
        if (bot.isLoggedIn()){
            if (bot.getCanvasMode() == OptionsTab.CanvasMode.FIXED) {
                return getFixedModeMousePosition();
            }

            return mouseX;
        } else {
            return getFixedModeMousePosition();
        }
    }

    public int getFixedModeMousePosition(){
        if (originalClientBounds != null){
            if (originalClientBounds.width > fixedClientGameWidth){
                return (originalClientBounds.width - fixedClientGameWidth) / 2 + mouseX;
            }
        }

        return mouseX;
    }

    private int mouseX = 0;
    private int mouseY = 0;
    private final ArrayList<Point> mousePoints = new ArrayList<>();
    private int windowHeaderOffset = 32;
    private int cursorDiameter = 7;
    private Stage stage;
    private boolean visible;
    private String id;

    public MouseApp(Passive_BOT bot){
        this.bot = bot;
        id = bot.getProcessId();
        if (bot.currentClientBounds != null){
            originalClientBounds = bot.currentClientBounds;
        } else {
            originalClientBounds = new Rectangle(500, 500);
        }
    }

    private void handleOverlayIndex() {
        //Try and get the currently focused window
        try {
            WinDef.HWND fgWindow = User32.INSTANCE.GetForegroundWindow();
            IntByReference pointer = new IntByReference();
            User32.INSTANCE.GetWindowThreadProcessId(fgWindow, pointer);

            try {
                //Check to see if the game client is focused
                visible = pointer.getValue() == Integer.parseInt(id);
                if (visible){
                    Platform.runLater(() -> {
                        //Force always on top if the game client is focused to ensure overlay is shown above game
                        stage.setAlwaysOnTop(true);
                        stage.setAlwaysOnTop(false);
                    });
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        this.stage = primaryStage;
        stage.initStyle(StageStyle.TRANSPARENT);
        //stage.setAlwaysOnTop(true);
        stage.setResizable(false);
        stage.setX(originalClientBounds.x);
        stage.setY(originalClientBounds.y);
        canvas = new Canvas(originalClientBounds.width, originalClientBounds.height);
        canvas.setWidth(originalClientBounds.width);
        canvas.setHeight(originalClientBounds.height);

        GraphicsContext gc = canvas.getGraphicsContext2D();
        Pane root = new Pane();
        root.getChildren().add(canvas);
        root.setStyle("-fx-background-color: transparent");
        canvas.setStyle("-fx-background-color: transparent");

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.show();
        updateTask(gc);
    }

    public void close(){
        Platform.runLater(() -> stage.close());
    }

    public void updateTask(GraphicsContext gc){
        updateThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Point mPoint = bot.currentMousePosition;
                    if (mPoint != null){
                        mouseX = mPoint.x;
                        mouseY = mPoint.y + windowHeaderOffset;
                        handleOverlayIndex();

                        updateUi(gc);
                    }
                    Thread.sleep(50);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    ex.printStackTrace();
                }
            }
        });
        updateThread.start();
    }

    public void stopUpdateThread() {
        if (updateThread != null){
            updateThread.interrupt();
        }
    }

    private void updateUi(GraphicsContext gc){
        try {
            if (gc == null){
                return;
            }
            if (bot.currentClientBounds != null){
                if (canvas != null){
                    try {
                        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                        if (originalClientBounds != bot.currentClientBounds) {
                            stage.setX(bot.currentClientBounds.x);
                            stage.setY(bot.currentClientBounds.y);
                            stage.setWidth(bot.currentClientBounds.width);
                            stage.setHeight(bot.currentClientBounds.height);
                            originalClientBounds = bot.currentClientBounds;
                        }
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }

            gc.setFill(Color.GREEN);
            gc.setStroke(Color.BLUE);
            gc.fillOval(getMouseX(), mouseY, cursorDiameter, cursorDiameter);

            if (mousePoints.size() > 20) {
                removePoint(0);
            } else {
                // Otherwise, add another point of the current location of the mouse.
                Point m = new Point(getMouseX(), mouseY);
                addPoint(m);
            }

            gc.setFill(Color.WHITESMOKE);
            gc.setLineWidth(3);

            for (int i = 0; i < mousePoints.size()-1; i++) {
                Point p = mousePoints.get(i);
                Point p2 = mousePoints.get(i+1);
                gc.strokeLine(p.x, p.y, p2.x, p2.y);
            }
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    private void addPoint(Point p) {
        mousePoints.add(p);
    }

    private void removePoint(int index) {
        if (mousePoints.size() > 0) {
            mousePoints.remove(index);
        }
    }
}
