
import java.io.*;
import java.net.SocketException;

import javafx.application.Platform;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class FishHandler extends Thread {
    private ObjectInputStream ois;
    private Pane canvas;

    public FishHandler(ObjectInputStream ois, Pane canvas) {
        
        this.ois = ois;
        this.canvas = canvas;
    }

    @Override
    public void run() {
        try {
            while (true) {
                MyFish fish = (MyFish) ois.readObject();
                ImageView fishImg = fish.toImageView();
                fishImg.setFitWidth(100);
                fishImg.setPreserveRatio(true);
                fishImg.relocate(canvas.getBoundsInLocal().getMinX(), fish.getY());
                MyFish.add(fish);
                TankClient.allFishes.add(fishImg);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        canvas.getChildren().add(fishImg);
                    }

                });
            }
        } catch (SocketException e) {
            System.out.println("closed");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
