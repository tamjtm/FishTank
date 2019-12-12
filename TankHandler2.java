import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class TankHandler2 extends Thread
{
    private DataInputStream dis;
    private DataOutputStream dos;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private Socket s;
    private static ArrayList<TankHandler2> handlerCollection = new ArrayList<TankHandler2>();


    public TankHandler2(Socket s, DataInputStream dis, DataOutputStream dos, ObjectInputStream ois, ObjectOutputStream oos)
    {
        this.s = s;
        this.dis = dis;
        this.dos = dos;
        this.ois = ois;
        this.oos = oos;
    }
    public void send(MyFish2 fish){
        int index = handlerCollection.indexOf(this);

        try
        {
            // if(handlerCollection.size() == 1)
            // {
                System.out.println((index+1)%handlerCollection.size());
                TankHandler2 receiver = handlerCollection.get((index+1)%handlerCollection.size());
                receiver.oos.writeObject(fish);
                // fish.writeObject(receiver.oos);
                // oos.flush();
            // }
            // else if(index-1 == 0)
            // {
            //     TankHandler2 receiver = handlerCollection.get(index-1);
            //     receiver.oos.writeObject(fish);
            //     // fish.writeObject(receiver.oos);
            //     // oos.flush();
            // }
            // else
            // {
            //     TankHandler2 receiver = handlerCollection.get(handlerCollection.size()-1);
            //     receiver.oos.writeObject(fish);
            //     // fish.writeObject(receiver.oos);
            //     // oos.flush();
            // }
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
            handlerCollection.add(this);
            //dos.writeUTF("Welcome to Server-" + s.getLocalPort());  // send message to new client
            while (true)
            {
                MyFish2 getFish = (MyFish2) ois.readObject();
                send(getFish);
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