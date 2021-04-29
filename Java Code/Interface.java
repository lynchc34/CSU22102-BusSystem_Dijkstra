
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.*;

public class Interface {
   private JFrame mainFrame;
   private JLabel statusLabel;
   
   private JFrame Frame1;
   private JPanel controlPanel1;
   protected JTextArea textArea1;
   protected JPanel text;
   
   private JFrame Frame2;
   private JPanel controlPanel2;
   protected JTextArea textArea2;
   
   private JFrame Frame3;
   private JPanel controlPanel3;
   protected JTextArea textArea3;
  
   private JLabel statusLabel2;
   private JPanel controlPanel;
   private JLabel msglabel;
   protected JTextArea textArea;
   
   final static String BUTTONPANEL = "Card with JButtons";
   final static String TEXTPANEL = "Card with JTextField";

   public Interface(){
      prepareGUI();
   }
   public static void main(String[] args){
      Interface swingLayoutDemo = new Interface();  
      swingLayoutDemo.showCardLayoutDemo();       
   }
   private void prepareGUI(){
      mainFrame = new JFrame("Vancouver Bus Planner");
      mainFrame.setSize(600,400);
     mainFrame.setLayout(new GridLayout(3,1));
     
     
     statusLabel = new JLabel("",JLabel.CENTER); 
     
      textArea = new JTextArea(20,5);
     textArea.setSize(600, 100);
      textArea.setEditable(false);
      JScrollPane scrollPane = new JScrollPane(textArea);

      mainFrame.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent windowEvent){
            System.exit(0);
         }        
      });    
      controlPanel = new JPanel();
      controlPanel.setLayout(new FlowLayout());
      mainFrame.add(statusLabel);
      mainFrame.add(textArea);
      mainFrame.add(controlPanel);
      mainFrame.setVisible(true);  
      ///////////////////////////////////////
      ///////////////////////////////////////
      // Part1
      Frame1 = new JFrame("Vancouver Bus Planner");
      Frame1.setSize(600,600);
      Frame1.setLayout(new GridLayout(4,1));
      textArea1 = new JTextArea(20,5);
      textArea1.setSize(300, 100);
      textArea1.setEditable(false);
      JScrollPane scrollPane1 = new JScrollPane(textArea1);
      controlPanel1 = new JPanel();
      //controlPanel1.setLayout();
      statusLabel2 = new JLabel("",JLabel.CENTER); 
      
      text = new JPanel();
  
      
      Frame1.add(statusLabel2);
       
      Frame1.add(text);
      Frame1.add(textArea1);
      Frame1.add(controlPanel1);
     //Frame1.add(scrollPane1);
      Frame1.setVisible(false);
      
      ///////////////////////////////////////
      ///////////////////////////////////////
      
      
      
      
   }
   private void showCardLayoutDemo(){
	   statusLabel.setText("----------------------------VANCOUVER BUS PLANNER----------------------------");
	   
      textArea.setText( "-Press Bus Route to find the shortest path between two routes" +
    		  			"\n" + "-Press Search to search for a bus stop and get information" + "\n" 
    		  			+  "-Press Bus times to search for what buses will be available at any time" );  
      textArea.setFont(new Font("Serif",Font.CENTER_BASELINE, 16));
      JButton a = new JButton("Bus Route");
      a.addActionListener(new ActionListener() {
    		    @Override
    		    public void actionPerformed(ActionEvent e) {
    		        //your actions
    		    	 mainFrame.setVisible(false);
    		    	 Frame1.setVisible(true);
    		    }
      });
      JButton b= new JButton("Search");
      b.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        //your actions
		    	 mainFrame.setVisible(false);
		    	 Frame1.setVisible(true);
		    }
});
      JButton c = new JButton("Bus Times");
      c.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        //your actions
		    	 mainFrame.setVisible(false);
		    	 Frame1.setVisible(true);
		    }
});
      controlPanel.add(a);
      controlPanel.add(b);
      controlPanel.add(c);
      ///////////////////////////////////////
      ///////////////////////////////////////
    
      statusLabel2.setText("Enter 2 bus stop names into the text box and press submit to get the bus journey(in the format stop1,stop2");
      JTextField textField = new JTextField(20);
      text.add(textField);
      JButton submit = new JButton("Submit");
      submit.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        //your actions
		    	String text = textField.getText();
		    	
		    	if(text == "") {
		    		textArea1.setText("Wrong or null input");
		    	}
		    	else {
		    	textArea1.setText("working");
		    	}
		    }
});
      text.add(submit);
      JButton cp1 = new JButton("Return");;
      cp1.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        //your actions
		    	 mainFrame.setVisible(true);
		    	 Frame1.setVisible(false);
		    }
});
      controlPanel1.add(cp1);
  
      //controlPanel.add();
     // controlPanel.add(card3);
   
   }
}