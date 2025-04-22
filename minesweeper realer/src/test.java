import java.util.*;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

@SuppressWarnings("unused")

public class test {
	
	public static Image display(Image i) {
		return i;
	}
	
    public static void main(String[] args) {
    	
    	//MSFrame f = new MSFrame();
    	//f.initializeGame();  
    	
    	Image flag;
    	
    	flag = Toolkit.getDefaultToolkit().getImage("/flag.png");
    	
    	display(flag);
    	
    }

}