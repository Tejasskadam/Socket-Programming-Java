package com.socket;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class Client extends JFrame{
    BufferedReader br;
    PrintWriter wr;
    Socket socket;

    private JLabel heading = new JLabel("Client Area");
    private  JTextArea messageArea= new JTextArea();
    private JTextField messageInput = new JTextField();
    private Font font=new Font("Roboto",Font.BOLD,20);

    public Client (){
        try{
            socket=new Socket("127.0.0.1",8888);
            System.out.println("Conection established");

            br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            wr=new PrintWriter(socket.getOutputStream());
            createGui();
            handelEvent();
            startreading();
            //startwriting();

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public void createGui(){
        this.setTitle("ClientMesssage");
        this.setSize(500,500);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);
        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        messageArea.setEditable(false);

        this.setLayout(new BorderLayout());
        this.add(heading,BorderLayout.NORTH);
        JScrollPane jScrollPane=new JScrollPane(messageArea);
        this.add(jScrollPane,BorderLayout.CENTER);
        this.add(messageInput,BorderLayout.SOUTH);
        this.setVisible(true);


    }
    public void handelEvent(){
        messageInput.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode()==10){
                    if (messageInput.getText().equals("exit")){
                        messageInput.setEditable(false);


                    }
                    System.out.println("pressed enter button");
                    String contenttosend=messageInput.getText();
                    wr.println(contenttosend);
                    wr.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();
                    messageArea.append("Me: "+contenttosend+"\n");
                    messageArea.setAutoscrolls(true);
                }

            }
        });
    }
    public void startreading(){
        Runnable r1=()->{
            System.out.println("Reading started");
            try{
                while (true&& !socket.isClosed()){
                    String msg =br.readLine();
                    if (msg.equals("exit")){
                        JOptionPane.showMessageDialog(this,"Server Terminated");
                        socket.close();
                        messageInput.setEditable(false);
                        break;
                    }
                    else {
                        messageArea.append("Server: "+msg+"\n");
                        messageArea.setAutoscrolls(true);
                    }
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        };new Thread(r1).start();

    }
    public void startwriting(){
        Runnable r2 =()->{
            try{
                while (true&& !socket.isClosed()){
                    BufferedReader br1 =new BufferedReader(new InputStreamReader(System.in));
                    String content=br1.readLine();
                    wr.println(content);
                    wr.flush();

                }

            }catch (Exception e){
                e.printStackTrace();
            }
        };new Thread(r2).start();

    }

    public static void main(String[] args) {
        System.out.println("com.socket.Client is online");
        new Client();
    }
}