import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class MyFish extends ImageIcon
{
    private static final long serialVersionUID = 1L;
    private double x;
    private double y;
    private double dx = 10;
    private double dy = 10;
    private static ArrayList<MyFish> fishInTank = new ArrayList<MyFish>();

    public MyFish(String filename, double x, double y)
    {
        super(filename);
        this.x = x;
        this.y = y;
        addToTank();
    }

    public void updateX(double newX)
    {
        this.x = newX;
    }

    public double getX()
    {
        return this.x;
    }

    public double getDx()
    {
        return dx;
    }

    public void addToTank()
    {
        fishInTank.add(this);
        System.out.println("Hi! " + this + " = " + fishInTank.size());
    }

    public void removeFromTank()
    {
        fishInTank.remove(this);
        System.out.println("Bye! " + this + " = " + fishInTank.size());
    }

    public static MyFish getFish(int index)
    {
        return fishInTank.get(index);
    }

    public static int countFish()
    {
        return fishInTank.size();
    }

    public ImageView toImageView()
    {
        BufferedImage buffer;
        if(this.getImage() instanceof BufferedImage)
        {
            buffer = (BufferedImage) this.getImage();
        }
        else
        {
            buffer = new BufferedImage(this.getImage().getWidth(this.getImageObserver()),this.getImage().getHeight(this.getImageObserver()),BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics = buffer.createGraphics();
            graphics.drawImage(this.getImage(),0,0,this.getImageObserver());
            graphics.dispose();
        }
        Image fxImage = SwingFXUtils.toFXImage(buffer,null);
        ImageView fxFish = new ImageView(fxImage);
        fxFish.relocate(x, y);
        return fxFish;
    }
}
