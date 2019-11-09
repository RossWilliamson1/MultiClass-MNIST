import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;

public class MNISTTrain 
{

	static int NUM_EPOCHS = 20; // Number of epochs to be performed per dataset
	static int NUM_LINES_PER_IMAGE = 28; // Store number of lines in images used
	static int NUM_CASES = 10; // Number of unique cases: digits from 0 to 9
	
	private File file_test;
	private File file_train;
	private ArrayList<ImageData> data_test;
	private ArrayList<ImageData> data_train;
	private static double[][] W; // Hold the weights for the classes in the algorithm algorithm
	
	// Constructor
	// Take in files to run the algorithm on
	public MNISTTrain(File train, File test)
	{
		
		// Initialise the testing file and the training file
		file_test = test;
		file_train = train;
		
		W = new double[NUM_CASES][(NUM_LINES_PER_IMAGE*NUM_LINES_PER_IMAGE)+1]; // 10 cases * number of pixels plus bias
		// Initialise all weights (including bias) as 1
		for (int i=0;i<W.length;i++)
		{
			for (int j=0;j<NUM_CASES+1;j++)
				W[i][j] = 1;
		}
			
		// Load the training and testing data
		data_train = readImages(file_train);
		data_test = readImages(file_test);
		
		run();
		
	}
	
	// Run the algorithm on n sets from the set list
	private void run()
	{
		
		// For every needed epoch
		for (int e=1;e<=NUM_EPOCHS;e++)
		{
			
			// For the number of images in training case
			for (int i=0; i<data_train.size(); i++)
			{	
				// Initial output for the image
				//System.out.print("Label: " + data_train.get(i).label + "\t");
				// Train on those images
				train(data_train.get(i));
			}
			
			// Output for the epoch
			System.out.print("Epoch: " + e + "\t");
			System.out.print("Train: ");
			printAccuracy(data_train);
			System.out.print("\tTrain: ");
			printAccuracy(data_test);
			System.out.println();
			
		}
		
	}
	
	// Train weights using provided imagedata
	private void train(ImageData image)
	{
		
		// Calculate the prediction
		int prediction = output(image.pixels);
		//System.out.println("Prediction: " + prediction);
		
		// Correct weights as necessary
		// For every pixel/input
		for (int i=0; i<image.pixels.length; i++)
		{
			W[prediction][i]-= image.pixels[i];
			W[image.label][i]+= image.pixels[i];
		}
		
	}
	
	// read images from the files given as cases and store appropriately
	private ArrayList<ImageData> readImages(File f)
	{
	
		// Get number of images in file
		int NUM_IMAGES = numLines(f)/28;

		// List to hold the newly read images
		ArrayList<ImageData> retList = new ArrayList<ImageData>();
		
		try
		{
		
			// Utilities for file reading
			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
		
			for (int i=0; i<NUM_IMAGES; i++)
			{
				// Create object to hold current image being read
				ImageData currentImageData = new ImageData();
				
				for (int row=0; row<NUM_LINES_PER_IMAGE; row++)
				{

					int label;
					
					// read the lines from the file
					String line = br.readLine();
					String[] lineParts = line.split("[\t ]+");
					//System.out.println("LABEL " + lineParts[0]);
					label = Integer.parseInt(lineParts[0]); // Get the label from the file (first part of the line)
					
					// For every pixel on the line
					for (int pixel=0; pixel<28; pixel++)
					{
						int val = Integer.parseInt(lineParts[pixel+1]);
						if (val>0)
							val = 1;
						else
							val = 0;
						
						currentImageData.set(row*28+pixel, val);
					}
						
					currentImageData.setLabel(label);
					
				}
			
				//System.out.println("LABEL: " + currentImageData.label);
				retList.add(currentImageData);
				
			}
		
		}
		catch(Exception e)
		{
			System.out.println("ERROR IN IMAGE READING");
			System.out.println(e.getMessage());
			e.printStackTrace();
			System.exit(0);
		}
		
		return retList;
		
	}
		
	
	// Output the activation for inputs X
	public static int output(int[] pixels)
	{
		
		int prediction = 0;
		
		check(pixels.length==W[0].length);
		
		int[] score = new int[W.length];
		
		// Get the score of the image
		for (int i=0; i<W.length; i++) 
        {
            score[i]=0;
            
            for (int j=0; j<W[0].length;j++) 
            {
                score[i]+=W[i][j]*pixels[j];                    
            }                
        }
		
		// Find the highest score
		for (int i=0; i<W.length;i++) 
        {
            if (score[i]>score[prediction]) 
            {
                prediction=i;
            }
        }
		
		return prediction;
		
	}
	
	// Print the accuracy of the algorithm on set data
	public static void printAccuracy(ArrayList<ImageData> data)
	{
		
		float error = 0;
		
		for (int i=0; i<data.size(); i++)
		{
			
			int p = output(data.get(i).pixels);
			
			if (p!=data.get(i).label)
				error+=1;
			
		}
		
		System.out.print(100- (int)(100 * (error/data.size())));
		
	}
	
	// Assert the value of b
	public static void check(Boolean b)
	{
		if (!b)
		{
			System.out.println("Error: Unexpected result. Closing.");
			System.exit(0);
		}
	}
	
	// Retrieve number of images in a file
	private int numLines(File f)
	{
		
		int numLines = 0;
		
		// read the lines from the file
		try 
		{
			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
			
			String currentLine;
			while ((currentLine = br.readLine()) != null)
			{
				numLines++;	
			}
			
		} 
		catch (Exception e) 
		{
			System.out.println(e.getMessage());
		}
		
		return numLines;
		
	}
	
	
}
