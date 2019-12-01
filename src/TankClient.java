import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class TankClient extends Application
{
    private Socket s;
    private DataInputStream dis;
    private DataOutputStream dos;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private Pane canvas;

    public void swim()      // it cannot swim!!!!!
    {
        for(int i=0; i<MyFish.countFish(); i++ )
        {
            MyFish fish = MyFish.getFish(i);
            ImageView fxFish = fish.toImageView();
            fxFish.setLayoutX(fxFish.getLayoutX()+fish.getDx());
            System.out.println(fish.getX()+" "+fxFish.getLayoutX());
            fish.updateX(fxFish.getLayoutX());

            final Bounds bounds = canvas.getBoundsInLocal();
            if (fxFish.getLayoutX() <= (bounds.getMinX() - fxFish.getLayoutX() - 100))
            {
                try
                {
                    oos.writeObject(fish);
                    oos.flush();
                    fish.removeFromTank();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        String ip = "127.0.0.1";
        int port = 8001;
        s = new Socket(ip, port);

        primaryStage.setTitle("Fish Tank #" + s.getLocalPort());
        canvas = new Pane();
        Scene scene = new Scene(canvas, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();

        try
        {
            dis = new DataInputStream(s.getInputStream());
            dos = new DataOutputStream(s.getOutputStream());
            oos = new ObjectOutputStream(s.getOutputStream());
            oos.flush();
            ois = new ObjectInputStream(s.getInputStream());

            System.out.println("########## CLIENT-" + s.getLocalPort() + " ##########");
            System.out.println("server> " + dis.readUTF());      // display welcome message

            FishHandler t = new FishHandler(s,dis,dos,ois,oos);
            t.start();

            scene.setOnMousePressed(new EventHandler<MouseEvent>()
            {
                @Override
                public void handle(MouseEvent event)
                {
                    MyFish fish = new MyFish("D:\\CPE342 Java\\Assignment03\\src\\magikarp.gif", event.getX(), event.getY());
                    ImageView fxFish = fish.toImageView();
                    fxFish.relocate(event.getX(), event.getY());
                    fxFish.setFitWidth(100);
                    fxFish.setPreserveRatio(true);
                    canvas.getChildren().addAll(fxFish);     // why fish doesn't show in the first click!!!
                }
            });

            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(50), event -> swim()));
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();

        }
        catch (Exception e)
        {
            s.close();
            e.printStackTrace();
        }


    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
