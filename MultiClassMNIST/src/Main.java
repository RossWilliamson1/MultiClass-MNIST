import java.io.File;
import java.util.ArrayList;

public class Main {

	public static void main(String[] args) {
		MNISTTrain trainer = new MNISTTrain(new File("src/training.txt"), new File("src/test.txt"));	
	}
}
