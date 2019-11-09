
class ImageData
{

	static int NUM_LINES = 28; // Store number of lines in images used
	static int IMAGE_SIZE = NUM_LINES*NUM_LINES; // Store the size of the image in pixels (lines*lines) as square image
	static int BIAS = 1;
	
	int label; // Label for the image being represented
	int[] pixels; // hold all of the pixels in the image being represented plus one space for the bias
	
	// constructor
	public ImageData()
	{
		 pixels = new int[IMAGE_SIZE+1]; // Initialise pixels array to fit image plus bias
		 pixels[IMAGE_SIZE] = BIAS;
	}
	
	// set pixel to value
	public void set(int pixel, int value)
	{
		pixels[pixel] = value;
	}
	
	// set the label for the representation
	public void setLabel(int value)
	{
		label = value;
	}
	
}
