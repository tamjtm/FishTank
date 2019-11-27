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
    private Pane canvas;

    public void swim(FishHandler t)
    {
        for(int i=0; i<FishHandler.fishes.size(); i++ )
        {
            ImageView fish = FishHandler.fishes.get(i);
            String f = t.getFish(i);

            fish.setLayoutX(fish.getLayoutX());

            final Bounds bounds = canvas.getBoundsInLocal();
            if (fish.getLayoutX() <= (bounds.getMinX() - fish.getLayoutX() - 100)) {
                fish.setLayoutX(bounds.getMaxX() + 100);
                // GUITUARN WILL CODE HERE!!!!
                try
                {
                    dos.writeUTF(f);
                    dos.flush();
                    FishHandler.fishes.remove(fish);
                    t.removeFromTank(f);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            fish.setLayoutX(fish.getLayoutX() - 10);
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

            System.out.println("########## CLIENT-" + s.getLocalPort() + " ##########");
            System.out.println("server> " + dis.readUTF());      // display welcome message


            FishHandler t = new FishHandler(s,dis,dos);
            t.start();

            scene.setOnMousePressed(new EventHandler<MouseEvent>()
            {
                @Override
                public void handle(MouseEvent event)
                {
                    if(event.isPrimaryButtonDown())
                    {
                        Image img = null;
                        try
                        {
                            img = new Image(new FileInputStream("D:\\CPE342 Java\\Assignment03\\src\\magikarp.gif"));
                            ImageView fish = new ImageView(img);
                            fish.relocate(event.getX(),event.getY());
                            fish.setFitWidth(100);
                            fish.setPreserveRatio(true);
                            canvas.getChildren().addAll(fish);
                            FishHandler.addToSystem("FISH");    // Why it not be static!!!!
                            t.addToTank(FishHandler.fishInSystem.get(FishHandler.fishInSystem.size()-1));
                            FishHandler.fishes.add(fish);
                        }
                        catch (FileNotFoundException e)
                        {
                            e.printStackTrace();
                        }

                    }
                }
            });

            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(50), event -> swim(t)));
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
