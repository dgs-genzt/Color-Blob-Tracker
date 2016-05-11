import java.awt.*;
import java.io.*;
import java.awt.image.*;


/**

Class ImageData is a base class which
respresents image data and the methods for
producing the corresponding wavelet image,
as well as methods to access both of these
datas. </p>

@author L. Grewe
@version 0.0a Feb. 1999
*/

//Note: extends Component to inherit its createImage() method
/**
 * @author Gowrishankar
 *
 */
class ImageData extends Component
{    boolean verbose = false;

     //File where data stored and format
     String filename = "";
     String format   = "";

 
     // Num Rows, columns
     public int rows=0, cols=0;

     //image data
     public int data[];
     public float minDataRange = Float.MAX_VALUE;
     public float maxDataRange = Float.MIN_VALUE;

     int[][] img2D;



    //**METHODS: for image data*/
     int getData(int row, int col)
      { if (row < rows && col <cols )
            return data[(row*cols)+col];
        else
            return 0;
      }


      int getDataForDisplay(int row, int col)
      {   if (row < rows && col <cols )
            return data[(row*cols)+col];
        else
            return 0;
      }


      void setData(int row, int col, int value)
      {  data[(row*cols)+col] = (int) value;

      }



     

  /**
   * Constructs a ImageData object using the
   * specified by an instance of java.awt.Image,
   * format, and size indicated by numberRows and
   * numberColumns.
   * @param img an Image object containing the data.
   * @param DataFormat the format of the data
   * @param numberRows the number of rows of data
   * @param numberColumns the number of columns of data
   * @exception IOException if there is an error during
   *  reading of the rangeDataFile.
   */
   public ImageData(Image img, String DataFormat,
                    int numberRows, int numberColumns) throws IOException
     {
      int pixel, red, green, blue, r,c;
      format = DataFormat;
      rows = numberRows;
      cols = numberColumns;
      PixelGrabber pg;

      //From the image passed retrieve the pixels by
      //creating a pixelgrabber and dump pixels
      //into the data[] array.
      data = new int[rows*cols];
      img2D = new int[rows][cols];
      pg = new PixelGrabber(img, 0, 0, cols, rows, data, 0, cols);
      try {
          pg.grabPixels();
      } catch (InterruptedException e) {
          System.err.println("interrupted waiting for pixels!");
          return;
      }

      
      for(int i=0;i<rows;i++)
	   {
		   System.arraycopy(data, (i*cols), img2D[i], 0, cols);
	   }
      
//  	int i, j, counter=0;
//
//  	for (i = 0; i < rows; i++) {
//
//  	for (j = 0; j < cols; j++) {
//
//  	// System.out.println(counter);
//
//
//  		img2D[i][j] = data[counter];
//
//  	counter = counter + 1;

  //	}

  	
      //Convert the PixelGrabber pixels to greyscale
      // from the {Alpha, Red, Green, Blue} format 
//      // PixelGrabber uses.
//      for(r=0; r<rows; r++)
//      for(c=0; c<cols; c++)
//        {   pixel = data[r*cols + c];
//	        red   = (pixel >> 16) & 0xff;
//            green = (pixel >>  8) & 0xff;
//            blue  = (pixel      ) & 0xff;
//            if(verbose)
//                System.out.println("RGB: " + red + "," + green +"," +blue);
//            data[r*cols+c] = (int)((red+green+blue)/3);
//            if(verbose)
//                System.out.println("Pixel: " + (int)((red+green+blue)/3));
//            minDataRange = Math.min(minDataRange, data[r*cols+c]);
//            maxDataRange = Math.max(maxDataRange, data[r*cols+c]);           
//        }                
//	        
//      for(int i=0;i<rows;i++)
//	   {
//		   System.arraycopy(data, (i*cols), img2D[i], 0, cols);
//	   } 
            
     
		//{{INIT_CONTROLS
		setBackground(java.awt.Color.white);
		setSize(0,0);
		//}}
	}

/* ******************************** Image Edge Detection **************************** */
   
   public ImageData(Image img, String DataFormat,
           int numberRows, int numberColumns, int Bit) throws IOException
{
int pixel, red, green, blue, r,c;
format = DataFormat;
rows = numberRows;
cols = numberColumns;
PixelGrabber pg;

//From the image passed retrieve the pixels by
//creating a pixelgrabber and dump pixels
//into the data[] array.
data = new int[rows*cols];
img2D = new int[rows][cols];
pg = new PixelGrabber(img, 0, 0, cols, rows, data, 0, cols);
try {
 pg.grabPixels();
} catch (InterruptedException e) {
 System.err.println("interrupted waiting for pixels!");
 return;
}


for(int i=0;i<rows;i++)
{
  System.arraycopy(data, (i*cols), img2D[i], 0, cols);
}

//int i, j, counter=0;
//
//for (i = 0; i < rows; i++) {
//
//for (j = 0; j < cols; j++) {
//
//// System.out.println(counter);
//
//
//	img2D[i][j] = data[counter];
//
//counter = counter + 1;

//	}


//Convert the PixelGrabber pixels to greyscale
// from the {Alpha, Red, Green, Blue} format 
// PixelGrabber uses.
for(r=0; r<rows; r++)
for(c=0; c<cols; c++)
{   pixel = data[r*cols + c];
   red   = (pixel >> 16) & 0xff;
   green = (pixel >>  8) & 0xff;
   blue  = (pixel      ) & 0xff;
   if(verbose)
       System.out.println("RGB: " + red + "," + green +"," +blue);
   data[r*cols+c] = (int)((red+green+blue)/3);
   if(verbose)
       System.out.println("Pixel: " + (int)((red+green+blue)/3));
   minDataRange = Math.min(minDataRange, data[r*cols+c]);
   maxDataRange = Math.max(maxDataRange, data[r*cols+c]);           
}                
   
for(int i=0;i<rows;i++)
{
  System.arraycopy(data, (i*cols), img2D[i], 0, cols);
} 
   

//{{INIT_CONTROLS
setBackground(java.awt.Color.white);
setSize(0,0);
//}}
}


  /**
   * Constructs a ImageData object using the
   * specified  size indicated by
   * numberRows and numberColumns that is EMPTY.
   * @param numberRows the number of rows of data
   * @param numberColumns the number of columns of data
   */
   public ImageData(int numberRows, int numberColumns){

      rows = numberRows;
      cols = numberColumns;
      
     

   }
   
   
   
   /**
   * Constructs a ImageData object using the
   * specified  size indicated by
   * numberRows and numberColumns.  Fill the data[]
   * array with the information stored in
   * the ImageData instance ID, from the 2D
   * neighborhood starting at the upper-left coordinate
   * (rStart,cStart) 
   * @param numberRows the number of rows of data
   * @param numberColumns the number of columns of data
   * @param ID image data to copy data from
   * @param rStart,cStart  Start of Neighborhood copy
   */
   public ImageData(int numberRows, int numberColumns, ImageData ID,
                    int rStart,int cStart){


      //saftey check: Retrieval in ID outside of boundaries
      if(ID.rows<(rStart+numberRows) || ID.cols<(cStart+numberColumns))
      {  rows = 0;
         cols = 0;
         return;
      }   
      
      
      rows = numberRows;
      cols = numberColumns;
      
      //create data[] array.
      data = new int[rows*cols];
      
      //Copy data from ID.
      for(int i=0; i<rows; i++)
      for(int j=0; j<cols; j++)
        {   data[i*cols+j] = ID.data[(rStart+i)*ID.cols + j + cStart];
            minDataRange = Math.min(minDataRange, data[i*cols+j]);
            maxDataRange = Math.max(maxDataRange, data[i*cols+j]);
        }    
      
      
   }   



//METHODS
 

 
   


  /**
   * creates a java.awt.Image from the pixels stored 
   * in the array data using 
   * java.awt.image.MemoryImageSource
   */
  public Image createImage()
   {
        int pixels[], t;
        pixels = new int[rows*cols];
    
        //translate the data in data[] to format needed
        for(int r=0;r<rows; r++)
        for(int c=0;c<cols; c++)
        {  t = data[r*cols + c];
           /*if(t == 999) //due to reg. transformation boundaries produced
            { t = 0; }  // see Transform.ApplyToImage() method
           if(t<0) //due to processing
            { t = -t; }
           else if(t>255) //due to processing
            { t = 255; }*/
           
//           pixels[r*cols+c] = (255 << 24) | (t >> 16) | (t >> 8) | t;
           pixels[r*cols+c] = t;
           //note data is greyscale so red=green=blue above (alpha first)
        }
    
        //Now create Image using new MemoryImageSource
        return ( super.createImage(new MemoryImageSource(cols, rows, pixels, 0, cols)));
	
   }
  
  public Image createImage( int grey)
  {
       int pixel, t, red, green , blue,r,c,gr;
       //pixels = new int[rows*cols];
       //System.out.println(" in create image of contrast");
       for(r=0; r<rows; r++)
         for(c=0; c<cols; c++)
           {   pixel = data[r*cols + c];
   	        red   = (pixel >> 16) & 0xff;
               green = (pixel >>  8) & 0xff;
               blue  = (pixel      ) & 0xff;
               if(verbose)
                   System.out.println("RGB: " + red + "," + green +"," +blue);
               gr = (int)((red+green+blue)/3);
               data[r*cols+c] = (255<<24)| gr << 16 | gr << 8 | gr;
         
           }
   
       //Now create Image using new MemoryImageSource
       return ( super.createImage(new MemoryImageSource(cols, rows, data, 0, cols)));
	
       }  
   
   
  public Image createImage(int pics[] )
  {
       int pixels[] = pics;
   
       
   
       //Now create Image using new MemoryImageSource
       return ( super.createImage(new MemoryImageSource(cols, rows, pixels, 0, cols)));
	
  } 
  
  public Image Sobel ()
  {
	  int r1,c1,t=0;
	  r1=rows;
	  c1=cols;
	  
	  double[][] ImageNew=new double[rows][cols];
	  int[] pix = new int[(rows*cols)];
	 double SobelX[][] = {{-1,0,1},{-2,0,2},{-1,0,1}};    //{{-1, 0, 1}, {-2, 0, 2}, {-1, 0, 1}};
	  double SobelY[][] = {{-1,-2,-1},{0, 0, 0},{1,2,1}};
	  double mag,magX = 0.0,magY = 0.0; // this is your magnitude 
	  
	  // Part 1
	  //The Sobel Algorithm
	  for(int r=1;r<rows-1;r++)
	  {
		  for(int c=1;c<cols-1;c++)
		  {
	  
		  magX = img2D[r-1][c-1] * SobelX[0][0] + img2D[r-1][c] * SobelX[0][1] + img2D[r-1][c+1] * SobelX[0][2] + img2D[r][c-1] * SobelX[1][0] +  img2D[r][c] * SobelX[1][1] + img2D[r][c+1]* SobelX[1][2] + img2D[r+1][c-1] * SobelX[2][0] +  img2D[r+1][c] * SobelX[2][1] + img2D[r+1][c+1]* SobelX[2][2];
		  
		  magY = img2D[r-1][c-1] * SobelY[0][0] + img2D[r-1][c] * SobelY[0][1] + img2D[r-1][c+1] * SobelY[0][2] + img2D[r][c-1] * SobelY[1][0] +  img2D[r][c] * SobelY[1][1] + img2D[r][c+1]* SobelY[1][2] + img2D[r+1][c-1] * SobelY[2][0] +  img2D[r+1][c] * SobelY[2][1] + img2D[r+1][c+1]* SobelY[2][2];
		  //  magY = you figure it out using the y mask
	
		  mag = Math.sqrt(magX*magX + magY*magY);
	  		ImageNew[r][c] = mag;
		  }
	  }
	  
	  // Part 2
	  // Here I am creating a 1D array from ImageNew which
	  // contains the computed pixels from the Sobel Algorithm
	  // These pixels are made absolute to get Int values for the array.
	  
	    int[] pixels = new int[(rows*cols)];
	  
	    int s = 0;
	    for(int i = 0; i < ImageNew.length; i ++) 
	          for(int j = 0; j < ImageNew[0].length; j ++){                           
	              pixels[s] = (int)(ImageNew[i][j]);
	              s++;
	          }   
	    
	    // The For loops are then used to get Red Green Blue values from the arbitrary
	    // values from the 1D array of pixels to be put into the final imgpix array.
	    // This is necessary to send the array to the createImage function.
	       for(int r=0; r<rows; r++)
	    	      for(int c=0; c<cols; c++)
	    	        {   
	    	    	  	t = pixels[r*cols + c];
	  
	    	    	  	int r2,g2,b2,gr;
	    	              
	    	                Color cc=new Color(t);
	    	                r2=cc.getRed();
	    	                g2=cc.getGreen();
	    	                b2=cc.getBlue();
	
	    	                t=(r2+g2+b2)/3;
	    	                
	    	        
	    
	    	                pix[r*cols+c] = (255 << 24) | (t << 16) | (t << 8) | t;
	    	           
	    	        }      
	    return ( super.createImage(new MemoryImageSource(cols, rows, pix, 0, cols)));
  }
   
  
 
   
   
   /**
	 *Stores the data image to a 
	 * a file as COLOR raw image data format
	 */
	public void storeImage(String filename)throws IOException
	{ 
	   
	    int  pixel, alpha, red, green,blue;
	    
	    
	        
        //Open up file	
        FileOutputStream file_output = new FileOutputStream(filename);
        DataOutputStream DO = new DataOutputStream(file_output);
 
 
        //Write out each pixel as integers
        
	
         
        for(int r=0; r<rows; r++)
	    for(int c=0; c<cols; c++) {
            pixel = data[r*cols + c];
	        red = pixel;
            green = pixel;
            blue = pixel;
            if(verbose)//verbose
    	        {System.out.println("value: " + (int)((red+green+blue)/3));
    	         System.out.println(" R,G,B: " + red +"," + green +"," + blue); }
	   
 	        DO.writeByte(red);
 	        DO.writeByte(green);
 	        DO.writeByte(blue);
        }	

        //flush Stream
        DO.flush();
        //close Stream
        DO.close();

    }
	
	public BufferedImage writeColorImageValueToFile(BufferedImage in, int maxH) {
		  int width = in.getWidth();
		  int height = in.getHeight();
		  
		  int min = 0;  //stretch min level
		  int max = maxH; //stretch max level

		  try {



		int[] r = new int[width * height];
		  int[] g = new int[width * height];
		  int[] b = new int[width * height];
		  int[] e = new int[width * height];
		  int[] data = new int[width * height];
		  in.getRGB(0, 0, width, height, data, 0, width);



		int[] hist_refore_r = new int[256];
		  int[] hist_refore_g = new int[256];
		  int[] hist_refore_b = new int[256];


		int[] hist_after_r = new int[256];
		  int[] hist_after_g = new int[256];
		  int[] hist_after_b = new int[256];


		for (int i = 0; i < (height * width); i++) {
		  r[i] = (int) ((data[i] >> 16) & 0xff);
		  g[i] = (int) ((data[i] >> 8) & 0xff);
		  b[i] = (int) (data[i] & 0xff);


		hist_refore_r[r[i]]++;
		  hist_refore_g[g[i]]++;
		  hist_refore_b[b[i]]++;
		  
		  //System.out.println(r[i]+" "+g[i]+" "+b[i]);


		//stretch them to 0 to 255
		  r[i] = (int) (1.0*( r[i] - min) / (max - min) * 255);
		  g[i] = (int) (1.0*( g[i] - min) / (max - min) * 255);
		  b[i] = (int) (1.0*( b[i] - min) / (max - min) * 255);
		  
		  if(r[i]> 255) r[i]=255;
		  if(g[i]> 255) g[i]=255;
		  if(b[i]> 255) b[i]=255;
		  
		  if(r[i]<0) r[i]=0;
		  if(g[i]<0) g[i]=0;
		  if(b[i]<0) b[i]=0;


		//System.out.println(r[i]+" "+g[i]+" "+b[i]);
		  
		  hist_after_r[r[i]]++;
		  hist_after_g[g[i]]++;
		  hist_after_b[b[i]]++;


		//convert it back
		  e[i] = (r[i] << 16) | (g[i] << 8) | b[i];


		}
		  //convert e back to say jpg
		  in.setRGB(0, 0, width, height, e, 0, width);
		 



		} catch (Exception e) {
		  System.err.println("Error: " + e);
		  Thread.dumpStack();


		}
		return in;
		  }
   
   
  
     
 
	//{{DECLARE_CONTROLS
	//}}
}//End ImageData
