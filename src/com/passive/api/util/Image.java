package com.passive.api.util;

import javafx.scene.image.ImageView;

import java.io.InputStream;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Image {
    public static ImageView getImageViewWithSize(Future<InputStream> img, int width, int height){

        try {
            ImageView newView = new ImageView(new javafx.scene.image.Image(img.get()));
            newView.setFitWidth(width);
            newView.setFitHeight(height);
            return newView;

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return null;
    }
}
