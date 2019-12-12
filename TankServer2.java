

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TankServer2
{

    public static void main(String[] args)
    {
        try
        {
            int port = 8001;
            ServerSocket ss = new ServerSocket(port);
            DataInputStream dis;
            DataOutputStream dos;
            ObjectInputStream ois;
            ObjectOutputStream oos;
            Socket s = null;

            System.out.println("########## SERVER-" + ss.getLocalPort() + " ##########");

            while (true)
            {
                s = ss.accept();
                System.out.println("Add new tank #" + s.getPort());
                dis = new DataInputStream(s.getInputStream());
                dos = new DataOutputStream(s.getOutputStream());
                oos = new ObjectOutputStream(s.getOutputStream());
                //oos.flush();
                ois = new ObjectInputStream(s.getInputStream());

                TankHandler2 t = new TankHandler2(s,dis,dos,ois,oos);
                t.start();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
