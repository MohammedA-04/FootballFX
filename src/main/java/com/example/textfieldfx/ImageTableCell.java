package com.example.textfieldfx;

import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ImageTableCell extends TableCell<PremLeagueController.Standing, String> {
    // private
    public final ImageView imageView;

    public ImageTableCell() {
        this.imageView = new ImageView();
        this.imageView.setFitWidth(25); // Set width as needed
        this.imageView.setFitHeight(25); // Set height as needed
        this.imageView.setPreserveRatio(true);

        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
    }

    @Override
    // protect
    public void updateItem(String imageUrl, boolean empty) {
        super.updateItem(imageUrl, empty);

        if (empty || imageUrl == null) {
            setGraphic(null);
        } else {
            try {
                Image image = new Image(imageUrl);
                imageView.setImage(image);
                setGraphic(imageView);
            } catch (Exception e) {
                System.err.println("Error loading image: " + e.getMessage());
            }
        }
    }
}
