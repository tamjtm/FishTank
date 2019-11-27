import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class FishHandler extends Thread
{
    private Socket s;
    private DataInputStream dis;
    private DataOutputStream dos;
    private int countFish = 0;
    ArrayList<String> fishInTank = new ArrayList<String>();
    static ArrayList<String> fishInSystem = new ArrayList<String>();
    static ArrayList<ImageView> fishes = new ArrayList<ImageView>();

    public FishHandler(Socket s, DataInputStream dis, DataOutputStream dos)
    {
        this.s = s;
        this.dis = dis;
        this.dos = dos;
    }

    public void addToTank(String fish)
    {
        fishInTank.add(fish);
        System.out.println("Hi! " + fish + " = " + fishInTank.size());
    }

    public void removeFromTank(String fish)
    {
        fishInTank.remove(fish);
        System.out.println("Bye! " + fish + " = " + fishInTank.size());
    }

    public String getFish(int index)
    {
        return fishInTank.get(index);
    }

    public static void addToSystem(String fish)
    {
        fish = fish + (fishInSystem.size()+1);
        fishInSystem.add(fish);
        System.out.println("Welcome " + fish);
    }

    public static void removeFromSystem(String fish)
    {
        fishInSystem.remove(fish);
        System.out.println("Bye! " + fish);
    }

    @Override
    public void run()
    {
        try
        {
            String fish;
            while (true)
            {
                fish = dis.readUTF();        // wait for fish
                addToTank(fish);
                if (fish.equals("exit"))
                {
                    System.out.println("Goodbye Client-" + s.getPort());
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
    }
}
