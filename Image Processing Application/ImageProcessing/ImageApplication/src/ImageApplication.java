/*
	This simple extension of the java.awt.Frame class
	contains all the elements necessary to act as the
	main window of an application.
 */

import java.awt.*;

public class ImageApplication extends Frame
{
    //Some Class Variables
    Image img,bckimg; //represents current image working on.
    
    //Class to display current image
    ImageFrame frame_image_display = new ImageFrame();  //YOU MUST CREATE
    
    //Class to hold image data to be able to
    //manipulate
    //ImageData img_data;  //YOU MUST CREATE
    
	public ImageApplication()
	{
		// This code is automatically generated by Visual Cafe when you add
		// components to the visual environment. It instantiates and initializes
		// the components. To modify the code, only use code syntax that matches
		// what Visual Cafe can generate, or Visual Cafe may be unable to back
		// parse your Java file into its visual environment.
		
		//{{INIT_CONTROLS
		setLayout(null);
		setSize(405,305);
		setVisible(false);
		openFileDialog1.setMode(FileDialog.LOAD);
		openFileDialog1.setTitle("Open");
		//$$ openFileDialog1.move(24,312);
		label_title.setText("Lynne\' Image Toolkit");
		add(label_title);
		label_title.setFont(new Font("Dialog", Font.BOLD, 20));
		label_title.setBounds(84,36,245,51);
		//$$ openFileDialog2.move(40,277);
		setTitle("AWT Application");
		//}}
		
		//{{INIT_MENUS
		menu1.setLabel("File");
		menu1.add(openMenuItem);
		openMenuItem.setLabel("Open...");
		openMenuItem.setShortcut(new MenuShortcut(java.awt.event.KeyEvent.VK_O,false));
		menu1.add(saveMenuItem);
		saveMenuItem.setEnabled(false);
		saveMenuItem.setLabel("Save");
		saveMenuItem.setShortcut(new MenuShortcut(java.awt.event.KeyEvent.VK_S,false));
		menu1.add(separatorMenuItem);
		separatorMenuItem.setLabel("-");
		menu1.add(exitMenuItem);
		exitMenuItem.setLabel("Exit");
		mainMenuBar.add(menu1);
		menu3.setLabel("Help");
		menu3.add(aboutMenuItem);
		aboutMenuItem.setLabel("About...");
		mainMenuBar.add(menu3);
		//$$ mainMenuBar.move(0,312);
		setMenuBar(mainMenuBar);
		//}}
		
		//{{REGISTER_LISTENERS
		SymWindow aSymWindow = new SymWindow();
		this.addWindowListener(aSymWindow);
		SymAction lSymAction = new SymAction();
		openMenuItem.addActionListener(lSymAction);
		exitMenuItem.addActionListener(lSymAction);
		aboutMenuItem.addActionListener(lSymAction);
		//}}
		
		//create frame to display image in
		//frame_image_display = new ImageFrame();
		//frame_image_display.setVisible(false);
	}
	
	public ImageApplication(String title)
	{
		this();
		setTitle(title);
	}
	
    /**
     * Shows or hides the component depending on the boolean flag b.
     * @param b  if true, show the component; otherwise, hide the component.
     * @see java.awt.Component#isVisible
     */
    public void setVisible(boolean b)
	{
		if(b)
		{
			setLocation(50, 50);
		}	
		super.setVisible(b);
	}
	
	static public void main(String args[])
	{
		try
		{
			//Create a new instance of our application's frame, and make it visible.
    		(new ImageApplication()).setVisible(true);
		}
		catch (Throwable t)
		{
			System.err.println(t);
			t.printStackTrace();
			//Ensure the application exits with an error condition.
			System.exit(1);
		}
	}
	
	public void addNotify()
	{
		// Record the size of the window prior to calling parents addNotify.
		Dimension d = getSize();
		
		super.addNotify();
	
		if (fComponentsAdjusted)
			return;
	
		// Adjust components according to the insets
		setSize(getInsets().left + getInsets().right + d.width, getInsets().top + getInsets().bottom + d.height);
		Component components[] = getComponents();
		for (int i = 0; i < components.length; i++)
		{
			Point p = components[i].getLocation();
			p.translate(getInsets().left, getInsets().top);
			components[i].setLocation(p);
		}
		fComponentsAdjusted = true;
	}
	
	// Used for addNotify check.
	boolean fComponentsAdjusted = false;
	
	//{{DECLARE_CONTROLS
	java.awt.FileDialog openFileDialog1 = new java.awt.FileDialog(this);
	java.awt.Label label_title = new java.awt.Label();
	//}}
	
	//{{DECLARE_MENUS
	java.awt.MenuBar mainMenuBar = new java.awt.MenuBar();
	java.awt.Menu menu1 = new java.awt.Menu();
	java.awt.MenuItem openMenuItem = new java.awt.MenuItem();
	java.awt.MenuItem saveMenuItem = new java.awt.MenuItem();
	java.awt.MenuItem separatorMenuItem = new java.awt.MenuItem();
	java.awt.MenuItem exitMenuItem = new java.awt.MenuItem();
	java.awt.Menu menu3 = new java.awt.Menu();
	java.awt.MenuItem aboutMenuItem = new java.awt.MenuItem();
	//}}
	
	class SymWindow extends java.awt.event.WindowAdapter
	{
		public void windowClosing(java.awt.event.WindowEvent event)
		{
			Object object = event.getSource();
			if (object == ImageApplication.this)
				ImageApplication_WindowClosing(event);
		}
	}
	
	void ImageApplication_WindowClosing(java.awt.event.WindowEvent event)
	{
		// to do: code goes here.
			 
		ImageApplication_WindowClosing_Interaction1(event);
	}


	void ImageApplication_WindowClosing_Interaction1(java.awt.event.WindowEvent event)
	{
		try {
			// QuitDialog Create and show as modal
			(new QuitDialog(this, true)).setVisible(true);
		} catch (Exception e) {
		}
	}

	
	class SymAction implements java.awt.event.ActionListener
	{
		public void actionPerformed(java.awt.event.ActionEvent event)
		{
			Object object = event.getSource();
			if (object == openMenuItem)
				openMenuItem_ActionPerformed(event);
			else if (object == aboutMenuItem)
				aboutMenuItem_ActionPerformed(event);
			else if (object == exitMenuItem)
				exitMenuItem_ActionPerformed(event);
		}
	}
	
	void openMenuItem_ActionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		openMenuItem_ActionPerformed_Interaction1(event);
	}

    //Method to open Image, either JPEG or GIF
    //loads image into img class variable and
    //calls method to display the image in a
    //new frame, called frame_image_display.

	void openMenuItem_ActionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
			// OpenFileDialog Create and show as modal
		    int		defMode         = openFileDialog1.getMode();
		    String	defTitle        = openFileDialog1.getTitle();
		    String defDirectory     = openFileDialog1.getDirectory();
		    String defFile          = "*.jpg;*.gif";

		    openFileDialog1 = new java.awt.FileDialog(this, defTitle, defMode);
		    openFileDialog1.setDirectory(defDirectory);
		    openFileDialog1.setFile(defFile);
		    openFileDialog1.setVisible(true);
		    
		    int width, height;
			 
		
		    //Print out to console the name of file selected
		    String Filename1 = (openFileDialog1.getDirectory()).concat(openFileDialog1.getFile());
		    System.out.println("Opening: " + Filename1);
			 
		    //Open up image...INSTEAD use Media Tracker
		    //to make sure that image really does load
		    img = (java.awt.Toolkit.getDefaultToolkit()).getImage(Filename1);
		    MediaTracker tracker = new MediaTracker(this);
	        tracker.addImage(img, 0);
	        try {
                tracker.waitForID(0); //wait until image loaded
            }catch (InterruptedException e)
             { System.out.println("Can't load image " + Filename1); 
               return; }
	
		    
		    
		    //YOU code, set image for  frame_image_display
		    bckimg = img;
		    frame_image_display.setImage(img,0);
		    frame_image_display.setVisible(true);
		    
    		
    	        			 
    

		    //Create image_data object in which you
		    //store the data you wish to actually
		    //manipulate
		    //image_data = new ImageData(img1, "JPEG", height, width);
	        	    
	        //Enable all of your GUI components for image operations
		
		
		} catch (Exception e) {
		}
	}


	void aboutMenuItem_ActionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		aboutMenuItem_ActionPerformed_Interaction1(event);
	}


	void aboutMenuItem_ActionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
			// AboutDialog Create and show as modal
			(new AboutDialog(this, true)).setVisible(true);
		} catch (Exception e) {
		}
	}
	
	
	void exitMenuItem_ActionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		exitMenuItem_ActionPerformed_Interaction1(event);
	}


	void exitMenuItem_ActionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
			// QuitDialog Create and show as modal
		    (new QuitDialog(this, true)).setVisible(true);
		} catch (Exception e) {
		}
	}

}

