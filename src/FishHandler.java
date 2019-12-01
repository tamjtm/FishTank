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
import java.util.ArrayList;

public class FishHandler extends Thread
{
    private Socket s;
    private DataInputStream dis;
    private DataOutputStream dos;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;


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
            String message = "bbb";
            MyFish fish;
            while (true)
            {
                fish = (MyFish) ois.readObject();
                fish.addToTank();
                if (message.equals("exit"))
                {
                    System.out.println("Goodbye Mr. Server");
                    break;
                }

            }
            this.s.close();
            this.dis.close();
            this.dos.close();
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
