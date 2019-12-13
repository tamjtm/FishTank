import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class TankHandler extends Thread {
    private DataInputStream dis;
    private DataOutputStream dos;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private Socket s;
    private static ArrayList<TankHandler> handlerCollection = new ArrayList<TankHandler>();

    public TankHandler(Socket s, DataInputStream dis, DataOutputStream dos, ObjectInputStream ois,
            ObjectOutputStream oos) {
        this.s = s;
        this.dis = dis;
        this.dos = dos;
        this.ois = ois;
        this.oos = oos;
    }

    public void send(MyFish fish) {
        int index = handlerCollection.indexOf(this);

        try {

            System.out.println((index + 1) % handlerCollection.size());
            TankHandler receiver = handlerCollection.get((index + 1) % handlerCollection.size());
            receiver.oos.writeObject(fish);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            handlerCollection.add(this);
            // dos.writeUTF("Welcome to Server-" + s.getLocalPort()); // send message to new
            // client
            while (true) {
                MyFish getFish = (MyFish) ois.readObject();
                send(getFish);
            }
        } catch (EOFException e) {
            System.out.println("Goodbye Client-" + s.getPort());
            handlerCollection.remove(this);
            try {
                this.s.close();
                this.dis.close();
                this.dos.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}