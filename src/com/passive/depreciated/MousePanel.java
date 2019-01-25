package com.passive.api.ui.overlay;

import com.passive.api.bot.Passive_BOT;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;

import java.awt.*;
import java.util.ArrayList;

public class MousePanel extends JFXPanel {
    public int mouseX;
    public int mouseY;
    private final ArrayList<Point> mousePoints = new ArrayList<>();
    private Passive_BOT bot;
    private Rectangle originalClientBounds;
    private Frame parentFrame;
    private Thread updateThread;
    private int windowHeaderOffset = 32;
    private int fade = 0;

    private void addPoint(Point p) {
        mousePoints.add(p);
    }

    private void removePoint(int index) {
        if (mousePoints.size() > 0) {
            mousePoints.remove(index);
        }
    }

    public void stopUpdateThread() {
        updateThread.interrupt();
    }

    public MousePanel(Passive_BOT bot, Frame parentFrame){
        this.bot = bot;
        this.parentFrame = parentFrame;
        if (bot.currentClientBounds != null){
            this.originalClientBounds = bot.currentClientBounds;
        }

        //setBorder(BorderFactory.createLineBorder(Color.BLACK));
        //setOpaque(false);
        //setBackground(Color.BLUE);
        setBackground(new Color(1.0f,1.0f,1.0f,0.0f));

        //setForeground(Color.BLUE);
        //setLayout(new GridBagLayout());
        Platform.runLater(() -> UpdateTask());
    }

    public void UpdateTask(){
        updateThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Point mPoint = bot.currentMousePosition;
                    mouseX = mPoint.x;
                    mouseY = mPoint.y + windowHeaderOffset;
                    paint(this.getGraphics());
                    repaint();
                    Thread.sleep(50);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        updateThread.start();
    }

    public Dimension getPreferredSize() {
        return new Dimension(bot.currentClientBounds.width, bot.currentClientBounds.height);
    }

    public void paint(Graphics g)
    {
        System.out.println("X:"+mouseX+" Y:"+mouseY);

        super.paint(g);
        Rectangle currentBounds = bot.currentClientBounds;
        if (currentBounds != null && originalClientBounds != null){
            if (originalClientBounds != currentBounds){
                parentFrame.setSize(currentBounds.width, currentBounds.height);
                parentFrame.setLocation(currentBounds.x, currentBounds.y);
                setSize(currentBounds.width, currentBounds.height);
                originalClientBounds = currentBounds;
            }
        }
        g.setColor(Color.RED);

        g.drawOval(mouseX, mouseY, 20, 20);

        g.fillOval(mouseX,mouseY,5,5);
        g.setColor(Color.BLACK);
        g.drawOval(mouseX,mouseY,5,5);
    }

    /*
    public void paint(Graphics g) {
        super.paint(g);

        Rectangle currentBounds = bot.currentClientBounds;
        if (currentBounds != null && originalClientBounds != null){
            if (originalClientBounds != currentBounds){
                parentFrame.setSize(currentBounds.width, currentBounds.height);
                parentFrame.setLocation(currentBounds.x, currentBounds.y);
                setSize(currentBounds.width, currentBounds.height);
                originalClientBounds = currentBounds;
            }
        }

        Graphics2D g1 = (Graphics2D)g.create();
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(getBackground());
        g2d.setColor(Color.BLACK);
        g2d.drawRect(0, 0, getWidth()-1, getHeight()-1);
        g2d.dispose();

        g.setColor(Color.RED);
        g.fillOval(mouseX,mouseY,5,5);
        g.setColor(Color.BLACK);
        g.drawOval(mouseX,mouseY,5,5);

        if (mousePoints.size() > 20) {
            removePoint(0);
        } else {
            // Otherwise, add another point of the current location of the mouse.
            Point m = new Point(bot.currentMousePosition.x, bot.currentMousePosition.y + windowHeaderOffset);
            addPoint(m);
        }
        // Loop through all the points in the array...
        for (int i = 0; i < mousePoints.size()-1; i++) {
            // Create two points which we'll draw a line between.
            Point p = mousePoints.get(i);
            Point p2 = mousePoints.get(i+1);
            int alpha;
            // This handles the fading of the line. While not necessary, it looks a bit nicer.
            if (fade != 0) {
                if (255-((20-i)*fade) >= 0) {
                    alpha = 255-((20-i)*fade);
                } else {
                    alpha = 0;
                }
            } else {
                alpha = 255;
            }
            // Set our color to the color specified in the parameter, but override the alpha with our faded one.
            g1.setColor(new Color(0,191,255, alpha));
            // Sets the width of the line (and therefore the trail)
            g1.setStroke(new BasicStroke(3));
            // Draws a line between the two points.
            g1.drawLine(p.x, p.y, p2.x, p2.y);
            // Sets the stroke width back to 1.
            g1.setStroke(new BasicStroke(1));
        }

    }*/
}
