import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class FishHandler extends Thread
{
    private Socket s;
    private DataInputStream dis;
    private DataOutputStream dos;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private MyFish newFish = new MyFish();
    private boolean alive = true;

    public FishHandler(Socket s, DataInputStream dis, DataOutputStream dos, ObjectInputStream ois, ObjectOutputStream oos)
    {
        this.s = s;
        this.dis = dis;
        this.dos = dos;
        this.ois = ois;
        this.oos = oos;
    }

    @Override
    public void run()
    {
        try
        {
            while (true)
            {
                if(newFish.readObject(ois))
                {
                    System.out.println("gotcha! " + newFish);
                }
            }
        }
        catch (SocketException e)
        {
            System.out.println("closed");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }
}
