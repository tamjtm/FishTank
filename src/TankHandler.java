import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class TankHandler extends Thread
{
    private DataInputStream dis;
    private DataOutputStream dos;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private Socket s;
    private static ArrayList<TankHandler> handlerCollection = new ArrayList<TankHandler>();


    public TankHandler(Socket s, DataInputStream dis, DataOutputStream dos, ObjectInputStream ois, ObjectOutputStream oos)
    {
        this.s = s;
        this.dis = dis;
        this.dos = dos;
        this.ois = ois;
        this.oos = oos;
        handlerCollection.add(this);
    }

    public int sendLeft(MyFish fish)
    {
        int index = handlerCollection.indexOf(this);
        TankHandler receiver = null;

        try
        {
            if(handlerCollection.size() == 1)
            {
                receiver = this;
                receiver.oos.writeObject(fish);
            }
            else if(index+1 < handlerCollection.size())
            {
                receiver = handlerCollection.get(index+1);
                receiver.oos.writeObject(fish);
            }
            else
            {
                receiver = handlerCollection.get(0);
                receiver.oos.writeObject(fish);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return receiver.s.getLocalPort();
    }

    public int sendRight(MyFish fish)
    {
        int index = handlerCollection.indexOf(this);
        TankHandler receiver = null;
        try
        {

            if(handlerCollection.size() == 1)
            {
                receiver = this;
                receiver.oos.writeObject(fish);
            }
            else if(index-1 == 0)
            {
                receiver = handlerCollection.get(index-1);
                receiver.oos.writeObject(fish);
            }
            else
            {
                receiver = handlerCollection.get(handlerCollection.size()-1);
                receiver.oos.writeObject(fish);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return receiver.s.getLocalPort();
    }

    @Override
    public void run()
    {
        try
        {
            String message = "bbb";
            MyFish fish;
            dos.writeUTF("Welcome to Server-" + s.getLocalPort());  // send message to new client
            while (true)
            {
                fish = (MyFish) ois.readObject();
                System.out.println(" from " + s.getPort() + " to " + this.sendRight(fish));

                if (message.equals("exit"))
                {
                    System.out.println("Goodbye Client-" + s.getPort());
                    handlerCollection.remove(this);
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
