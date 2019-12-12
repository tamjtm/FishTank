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
    }

    public void addTo()
    {
        handlerCollection.add(this);
    }

    public void sendLeft(MyFish fish)
    {
        int index = handlerCollection.indexOf(this);

        try
        {
            if(handlerCollection.size() == 1)
            {
                //this.oos.writeObject(fish);
                fish.writeObject(this.oos);
                oos.flush();
            }
            else if(index+1 < handlerCollection.size())
            {
                TankHandler receiver = handlerCollection.get(index+1);
                //receiver.oos.writeObject(fish);
                fish.writeObject(receiver.oos);
                oos.flush();
            }
            else
            {
                TankHandler receiver = handlerCollection.get(0);
                //receiver.oos.writeObject(fish);
                fish.writeObject(receiver.oos);
                oos.flush();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void sendRight(MyFish fish)
    {
        int index = handlerCollection.indexOf(this);

        try
        {
            if(handlerCollection.size() == 1)
            {
                TankHandler receiver = handlerCollection.get(index);
                //receiver.oos.writeObject(fish);
                fish.writeObject(receiver.oos);
                oos.flush();
            }
            else if(index-1 == 0)
            {
                TankHandler receiver = handlerCollection.get(index-1);
                //receiver.oos.writeObject(fish);
                fish.writeObject(receiver.oos);
                oos.flush();
            }
            else
            {
                TankHandler receiver = handlerCollection.get(handlerCollection.size()-1);
                //receiver.oos.writeObject(fish);
                fish.writeObject(receiver.oos);
                oos.flush();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void run()
    {
        try
        {
            String message;
            MyFish fish = new MyFish();
            dos.writeUTF("Welcome to Server-" + s.getLocalPort());  // send message to new client
            while (true)
            {
                message = dis.readUTF();
                fish.readObject(ois);
                System.out.println(fish + " from " + s.getPort());
                this.sendRight(fish);
            }
        }
        catch (EOFException e)
        {
            System.out.println("Goodbye Client-" + s.getPort());
            handlerCollection.remove(this);
            try
            {
                this.s.close();
                this.dis.close();
                this.dos.close();
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
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
