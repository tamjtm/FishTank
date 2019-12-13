import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MyFish extends ImageIcon
{

    private static final long serialVersionUID = 1L;

    private double position = -1;

    public static ArrayList<MyFish> fishInTank = new ArrayList<MyFish>();

    public MyFish() {
        super();
    }

    public MyFish(String filename) {
        super(filename);

        fishInTank.add(this);
    }


    public static void add(MyFish fish){
        fishInTank.add(fish);
    }

    public static void remove(MyFish fish)
    {
        fishInTank.remove(fish);

    }

    public static int count()
    {
        return fishInTank.size();
    }

    public static MyFish getFish(int index)
    {
        return fishInTank.get(index);
    }

    public double getPosition()
    {
        return position;
    }

    public void savePosition(double position)
    {
        this.position = position;
    }


    public ImageView toImageView() {
        BufferedImage bImg;
        if (this.getImage() instanceof BufferedImage)
        {
            bImg = (BufferedImage) this.getImage();
        }
        else
        {
            bImg = new BufferedImage(this.getImage().getWidth(this.getImageObserver()),this.getImage().getHeight(this.getImageObserver()), BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics = bImg.createGraphics();
            graphics.drawImage(this.getImage(), 0, 0, this.getImageObserver());
            graphics.dispose();
        }
        Image fxImage = SwingFXUtils.toFXImage(bImg, null);
        ImageView imageView = new ImageView(fxImage);
        imageView.setFitWidth(100);
        imageView.setPreserveRatio(true);
        return imageView;
    }
}