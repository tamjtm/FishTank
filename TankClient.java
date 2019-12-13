import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class TankClient extends Application {
    private Socket s;
    public static ArrayList<ImageView> allFishes = new ArrayList<ImageView>();
    private DataInputStream dis;
    private DataOutputStream dos;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    public static Pane canvas;

    public void swim() {
        for (int i = 0; i < allFishes.size(); i++) {
            ImageView fishImg = allFishes.get(i);
            fishImg.setLayoutX(fishImg.getLayoutX()+5);
            final Bounds bounds = canvas.getBoundsInLocal();
            if ((fishImg.getLayoutX() <= bounds.getMinX()) || (fishImg.getLayoutX() >= bounds.getMaxX()-fishImg.getFitWidth())) {
                try {
                    MyFish fish = MyFish.getFish(i);
                    oos.writeObject(fish);
                    MyFish.remove(fish);
                    allFishes.remove(fishImg);
                    canvas.getChildren().removeAll(fishImg);
                    System.out.println("bye " + fish);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        String ip = "127.0.0.1";
        int port = 8001;
        s = new Socket(ip, port);

        primaryStage.setTitle("Fish Tank #" + s.getLocalPort());
        canvas = new Pane();
        Scene scene = new Scene(canvas, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();

        try {
            dis = new DataInputStream(s.getInputStream());
            dos = new DataOutputStream(s.getOutputStream());
            oos = new ObjectOutputStream(s.getOutputStream());
            // oos.flush();
            ois = new ObjectInputStream(s.getInputStream());

            System.out.println("########## CLIENT-" + s.getLocalPort() + " ##########");
            // System.out.println("server> " + dis.readUTF()); // display welcome message

            FishHandler t = new FishHandler(s, dis, dos, ois, oos);
            t.start();

            scene.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (event.isPrimaryButtonDown()) {
                        MyFish fish = new MyFish("src\\magikarp.png");
                        ImageView fishImg = fish.toImageView();
                        fishImg.relocate(event.getX(), event.getY());
                        fish.savePosition(event.getY());
                        //fishImg.setFitWidth(100);
                        fishImg.setPreserveRatio(true);
                        allFishes.add(fishImg);
                        canvas.getChildren().add(fishImg);
                    }
                }
            });

            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(50), event -> swim()));
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();

            primaryStage.setOnCloseRequest(event -> {
                try {
                    // dos.writeUTF("exit");
                    s.close();
                    dis.close();
                    dos.close();
                    ois.close();
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.exit(0);
            });


        } catch (Exception e) {
            s.close();
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}