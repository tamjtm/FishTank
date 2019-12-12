
import java.io.*;
import java.net.Socket;
import java.net.SocketException;

import javafx.application.Platform;
import javafx.scene.image.ImageView;

public class FishHandler2 extends Thread
{
    private Socket s;
    private DataInputStream dis;
    private DataOutputStream dos;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;

    public FishHandler2(Socket s, DataInputStream dis, DataOutputStream dos, ObjectInputStream ois, ObjectOutputStream oos)
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
                MyFish2 fish = (MyFish2) ois.readObject();
                ImageView fishImg = fish.toImageView();
                fishImg.relocate(TankClient2.canvas.getBoundsInLocal().getMinX(), fishImg.getLayoutY());
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        TankClient2.canvas.getChildren().addAll(fishImg);
                    }
        
                });
                MyFish2.add(fish);
                TankClient2.allFishes.add(fishImg);
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
