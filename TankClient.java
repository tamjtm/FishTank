import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.net.Socket;

public class TankClient extends Application
{
    private Socket s;
    private DataInputStream dis;
    private DataOutputStream dos;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private Pane canvas;

    public void swim(FishHandler t)
    {
        for(int i=0; i<MyFish.count(); i++ )
        {
            MyFish fish = MyFish.getFish(i);
            fish.swim();

            final Bounds bounds = canvas.getBoundsInLocal();
            if (fish.getX()<=bounds.getMinX() || (fish.getX()+100)>=bounds.getMaxX())
            {
                try
                {
                    dos.writeUTF("continue");
                    fish.writeObject(oos);
                    MyFish.remove(fish);
                    System.out.println("bye " + fish);
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
                    if(event.isPrimaryButtonDown())
                    {
                        try
                        {
                            MyFish fish = new MyFish("magikarp.gif",event.getX(),event.getY());
                            canvas.getChildren().addAll(fish.getImageView());
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

            primaryStage.setOnCloseRequest(event -> {
                try
                {
                    dos.writeUTF("exit");
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
