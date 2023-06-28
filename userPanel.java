import javax.swing.*;

import java.awt.event.*;

public class userPanel extends JFrame {

    private static JFrame frame; 
    private static JTextField senderField;
    private static JTextField passwordField;
    private static JTextField recipientField;
    private static JTextField subjectField; 
    private static JTextField data2Field;
    private static JTextField data1Field;
    private static JTextField data3Field;
    private static JTextArea messageArea;
    private boolean aiFirstClick = true;  
    private boolean firstLogin = true;  

    private static EmailSend email; 
   

    public userPanel() {
        frame = new JFrame("Email Service");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 300);

        // Set up the JFrame components
        JLabel senderLabel = new JLabel("User Email Address:");
        senderField = new JTextField(20);

        JLabel passwordLabel = new JLabel("User Password:");
        passwordField = new JPasswordField(20);

        JButton loginButton = new JButton("Next"); 

        JLabel recipientLabel = new JLabel("Recipient Email Addresses");
        recipientField = new JTextField(50);

        JLabel data1Label = new JLabel ("Data 1"); 
        data1Field = new JTextField(50);

        JLabel data2Label = new JLabel ("Data 2"); 
        data2Field = new JTextField(50);

        JLabel data3Label = new JLabel ("Data 3"); 
        data3Field = new JTextField(50);

        JLabel subjectLabel = new JLabel ("Subject"); 
        subjectField = new JTextField(50);

        JLabel messageLabel = new JLabel("Enter Message " );
        messageArea = new JTextArea(20, 50);
        JScrollPane scrollPane = new JScrollPane(messageArea); 

        JButton sendButton = new JButton("Send Email");
        JButton aiButton = new JButton("Ask AI");
        
    
        senderLabel.setBounds(50, 50, 200, 30);
        senderField.setBounds(250, 50, 300, 30);

        passwordLabel.setBounds(50, 100, 200, 30);
        passwordField.setBounds(250, 100, 300, 30);

        loginButton.setBounds(50, 150, 100, 30);

        recipientLabel.setBounds(50, 50, 200, 30);
        recipientField.setBounds(250, 50, 600, 30);

        data1Label.setBounds(50, 100, 200, 30);
        data1Field.setBounds(250, 100, 600, 30);

        data2Label.setBounds(50, 150, 200, 30);
        data2Field.setBounds(250, 150, 600, 30);

        data3Label.setBounds(50, 200, 200, 30);
        data3Field.setBounds(250, 200, 600, 30);

        subjectLabel.setBounds(50, 250, 200, 30);
        subjectField.setBounds(250, 250, 600, 30);

        messageLabel.setBounds( 50, 300, 200, 30 );
        scrollPane.setBounds(50, 330, 800, 300);

        sendButton.setBounds(50, 650, 100, 30);
        aiButton.setBounds(150, 650, 100, 30);


        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        panel1.setLayout(null);
        panel2.setLayout(null);

        panel1.add(senderLabel);
        panel1.add(senderField);
        panel1.add(passwordField);
        panel1.add(passwordLabel);
        panel1.add(loginButton);

        panel2.add(recipientLabel);
        panel2.add(recipientField);
        panel2.add(data1Label);
        panel2.add(data1Field);
        panel2.add(data2Label);
        panel2.add(data2Field);
        panel2.add(data3Label);
        panel2.add(data3Field);
        panel2.add(subjectLabel);
        panel2.add(subjectField);
        panel2.add(messageLabel);
        panel2.add(scrollPane);
        panel2.add(sendButton);
        panel2.add(aiButton);

        frame.add( panel1);

        loginButton.addActionListener(new ActionListener()  {
            @Override
            public void actionPerformed(ActionEvent e) {
                
             // Perform login validation here
             // If login is successful, switch to the email panel
                if(firstLogin){
                    email = new EmailSend();
                    firstLogin = false;
                }
                if(email.authenticateUser(senderField.getText(), passwordField.getText())){
                    frame.remove(panel1);
                    frame.add(panel2);
                    frame.setSize(900, 750);
                    frame.revalidate();
                }else{
                    error("Invalid email or password");
                }
                
            }
        });

        sendButton.addActionListener(new ActionListener()  {
            @Override
            public void actionPerformed(ActionEvent e) {
            
                if(send()){
                    frame.remove(panel2);

                    frame.add(new JLabel(" SENT ") );
                    frame.setSize(300, 150);
                    frame.revalidate();
                }
            }
        });

        aiButton.addActionListener(new ActionListener()  {
            @Override
            public void actionPerformed(ActionEvent e) {
                
             // Perform login validation here
             // If login is successful, switch to the email panel
                if(aiFirstClick){
                    JOptionPane.showMessageDialog(frame, "1. In the message box write the names of the data sets used in first line seperated by spaces \n 2. In a new line provide a single sentence prompt of what the email will pertain to", "AI Instructions", JOptionPane.INFORMATION_MESSAGE);
                    aiFirstClick = false; 
                }
                else{
                    String[] prompt = messageArea.getText().split("\n");

                    String[] dataNames = prompt[0].split(" ");

                    String message  = "Generate an email: "+prompt[1] + ". replace " + dataNames[0] + "=DATA1, ";
                    if(dataNames.length > 1){
                        message += dataNames[1] + "=DATA2";
                    }
                    if(dataNames.length > 2){
                        message += dataNames[2] + "=DATA3";
                    }

                    AIchat chat = new AIchat(); 

                    try {
                        messageArea.setText( chat.chatGPT(message) );
                    } catch (Exception e1) {
                        error("Failed to call OpenAI API");
                        e1.printStackTrace();
                    } 
                }
            }
        });
        
        frame.setVisible(true);
    }
    
    public boolean send( ){
        
        String[] recips = recipientField.getText().split(" "); 
        String[] data1 = data1Field.getText().split(" "); 
        String[] data2 = data2Field.getText().split(" "); 
        String[] data3 = data3Field.getText().split(" "); 

        int len = recips.length;
        if( (data1.length > 0 && data1.length != len) || (data2.length > 0 && data2.length != len) || (data3.length > 0 && data3.length != len)){
            error("Incomplete data set");
            return false; 
        }

        for(int i=0; i<recips.length; i++){
            String ms = messageArea.getText();
            if(data1.length>0){
                System.out.println("hello");
                ms = ms.replace("DATA1", data1[i]);
            }
            if(data2.length>0){
                ms = ms.replace("DATA2", data2[i]);
            }
            if(data3.length>0){
                ms = ms.replace("DATA3", data3[i]);
            }

            if(!email.send(ms, recips[i], subjectField.getText())){
                error("Message to " + recips[i]+ " failed to send");
            }
        
        }

        return true;

    }

    public static void error(String e){
        JOptionPane.showMessageDialog(frame, e, "Error", JOptionPane.ERROR_MESSAGE);

    }


    public static void main (String[] args){
        new userPanel();
    }

    
}
