import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class TankHandler extends Thread
{
    private DataInputStream dis;
    private DataOutputStream dos;
    private Socket s;
    private static ArrayList<TankHandler> handlerCollection = new ArrayList<TankHandler>();


    public TankHandler(Socket s, DataInputStream dis, DataOutputStream dos)
    {
        this.s = s;
        this.dis = dis;
        this.dos = dos;
    }

    public void addTo()
    {
        handlerCollection.add(this);
    }

    public void sendLeft(String fish)
    {
        int index = handlerCollection.indexOf(this);

        try
        {
            if(handlerCollection.size() == 1)
            {
                this.dos.writeUTF(fish);
            }
            else if(index+1 < handlerCollection.size())
            {
                TankHandler receiver = handlerCollection.get(index+1);
                receiver.dos.writeUTF(fish);
            }
            else
            {
                TankHandler receiver = handlerCollection.get(0);
                receiver.dos.writeUTF(fish);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void sendRight(String fish)
    {
        int index = handlerCollection.indexOf(this);

        try
        {
            if(handlerCollection.size() == 1)
            {
                TankHandler receiver = handlerCollection.get(index);
                receiver.dos.writeUTF(fish);
            }
            else if(index-1 == 0)
            {
                TankHandler receiver = handlerCollection.get(index-1);
                receiver.dos.writeUTF(fish);
            }
            else
            {
                TankHandler receiver = handlerCollection.get(handlerCollection.size()-1);
                receiver.dos.writeUTF(fish);
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
            dos.writeUTF("Welcome to Server-" + s.getLocalPort());  // send message to new client
            while (true)
            {
                message = dis.readUTF();        // wait for fish
                System.out.println(message + " from " + s.getPort());
                this.sendRight(message);

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
    }
}
