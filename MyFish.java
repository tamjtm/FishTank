import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;

public class MyFish implements Serializable
{
    static final long serialVersionUID = 1L;

    /** fish for write/read in the stream */
    private transient Image fish;

    /** fish image for display */
    private transient ImageView fishImg;

    /** fish's speed */
    private int speed;

    /** collection of fish image in each tank */
    private static ArrayList<MyFish> fishInTank = new ArrayList<MyFish>();

    public MyFish()
    {

    }

    /**
     * instantiate MyFish Object
     *
     * @param filename
     * @param x
     * @param y
     * @throws FileNotFoundException
     */
    public MyFish(String filename, double x, double y) throws FileNotFoundException
    {
        fish = new Image(new FileInputStream(filename));
        fishImg = new ImageView(fish);
        fishImg.relocate(x, y);
        fishImg.setFitWidth(100);
        fishImg.setPreserveRatio(true);
        speed = (int) (Math.random()*10) + 1;

        fishInTank.add(this);
    }

    public double getX()
    {
        return this.fishImg.getLayoutX();
    }

    public double getY()
    {
        return this.fishImg.getLayoutY();
    }

    /**
     * get ImageView object of fish
     *
     * @return ImageView object of fish
     */
    public ImageView getImageView()
    {
        return fishImg;
    }

    public void swim()
    {
        fishImg.setLayoutX(fishImg.getLayoutX()+speed);
    }

    public static int count()
    {
        return fishInTank.size();
    }

    public static void add(MyFish fish)
    {

        fishInTank.add(fish);
    }

    public static void remove(MyFish fish)
    {
        System.out.println(count());
        fishInTank.remove(fish);
        System.out.println(count());
        System.out.println();

    }

    public static MyFish getFish(int index)
    {
        return fishInTank.get(index);
    }

    @Override
    public String toString()
    {
        return "FISH " + fish + " " + speed;
    }

    /**
     * read image object from ObjectInputStream
     *
     * @param ois
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public boolean readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException
    {
        try
        {
            ois.defaultReadObject();
            fish = SwingFXUtils.toFXImage(ImageIO.read(ois),null);
            fishImg = new ImageView(fish);
            //fishImg = (ImageView) ois.readObject();
            return true;
        }
        catch (NotActiveException e)
        {
            return false;
        }
    }

    /**
     * write image object to ObjectOutputStream
     *
     * @param oos
     * @throws IOException
     */
    public void writeObject(ObjectOutputStream oos) throws IOException
    {
        try
        {
            oos.defaultWriteObject();
        }
        catch (NotActiveException e)
        {
            return;
        }

        ImageIO.write(SwingFXUtils.fromFXImage(fish, null), "gif", oos);
        //oos.writeObject(fishImg);
        oos.flush();
        System.out.println("OK!");
    }
}
