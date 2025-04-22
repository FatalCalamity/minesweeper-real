import java.util.*;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
@SuppressWarnings({"serial", "unused"})

public class TimerThing extends JTextField {

	private int timer;
	private Timer clock;
	
	public TimerThing() {
		
		timer = 0;
		setEditable(false);
		setFocusable(false);
		setPreferredSize(new Dimension(90, 40));
		setHorizontalAlignment(JTextField.CENTER);
		setText("Time: 00:00");
		
		clock = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tick();
            }
        });
		
	}
	
	private void tick() {
		++timer;
		setText(String.format("Time: %02d:%02d", (timer / 60), (timer % 60)));
	}
	
	public String getTime() {
		return String.format("%02d:%02d", (timer / 60), (timer % 60));
	}
	
	public int getRawTime() {
		return timer;
	}
	
	public void start() {
		timer = 0;
		clock.start();
	}
	
	public void stop() {
		clock.stop();
	}
	
}
