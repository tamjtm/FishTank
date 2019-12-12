import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MyFish2 extends ImageIcon {

	private static final long serialVersionUID = 1L;

	public static ArrayList<MyFish2> fishInTank = new ArrayList<MyFish2>();


	public MyFish2() {
		super();
	}

	public MyFish2(String filename) {
		super(filename);

        fishInTank.add(this);
	}

	
	public static void add(MyFish2 fish){
		fishInTank.add(fish);
	}
	
	public static void remove(MyFish2 fish)
    {
        fishInTank.remove(fish);

	}
	
	public static int count()
    {
        return fishInTank.size();
    }

	public static MyFish2 getFish(int index)
    {
        return fishInTank.get(index);
	}
	



	

	public ImageView toImageView() {
		BufferedImage bImg;
		if (this.getImage() instanceof BufferedImage) {
			bImg = (BufferedImage) this.getImage();
		} else {
			bImg = new BufferedImage(this.getImage().getWidth(this.getImageObserver()),this.getImage().getHeight(this.getImageObserver()), BufferedImage.TYPE_INT_ARGB);
			Graphics2D graphics = bImg.createGraphics();
			graphics.drawImage(this.getImage(), 0, 0, this.getImageObserver());
			graphics.dispose();
		}
		Image fxImage = SwingFXUtils.toFXImage(bImg, null);
		return new ImageView(fxImage);
	}
}
