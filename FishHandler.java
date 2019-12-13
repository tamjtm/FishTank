import java.io.*;
import java.net.Socket;
import java.net.SocketException;

import javafx.application.Platform;
import javafx.scene.image.ImageView;

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
            while (true)
            {
                MyFish fish = (MyFish) ois.readObject();
                System.out.println("hi " + fish);
                ImageView fishImg = fish.toImageView();
                //fishImg.relocate(TankClient.canvas.getBoundsInLocal().getMinX(), fishImg.getLayoutY());
                fishImg.relocate(TankClient.canvas.getBoundsInLocal().getMinX(), fish.getPosition());
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        TankClient.canvas.getChildren().addAll(fishImg);
                    }

                });
                MyFish.add(fish);
                TankClient.allFishes.add(fishImg);
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