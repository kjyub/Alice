package userFrame;
import java.awt.EventQueue;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.UIManager;


import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Vector;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;

public class MainUI extends JFrame implements ActionListener  {
      public MainUI() {
         

          initialize();
             
          }
         
         public void initialize() {
            
            JLabel l = new JLabel();
            JFrame f = new JFrame();
            JPanel p1 = new JPanel();
            JPanel p2 = new JPanel();
            JPanel p3 = new JPanel();
            JPanel p4 = new JPanel();
            
            JLabel l1=new JLabel("123");
            JLabel l2=new JLabel("123");
            JLabel l3=new JLabel("123");
            
             f.setTitle("User");
            f.setSize(1280, 720);       

               
            Container c = getContentPane();
            c.setLayout(new FlowLayout());

 
            f.add(l);
            

             File file= new File("./image/10.png");  //이미지 파일 경로 
               BufferedImage m;         
               
               try {   
                      m = ImageIO.read(file); //이미지 파일을 읽어와서 BufferedImage 에 넣음
                      l.setIcon(new ImageIcon(m)); //레이블에 이미지 표시
               }
               catch(Exception e) {}
                       
               
               
             
            
            f.setLayout(null);
            
            p1.setBounds(0, 0, 1280, 720);
               f.getContentPane().add(p1);
               p1.add(l);
               p1.setLayout(null);
               
               
               p2.setBounds(900,0,364,49);
               f.getContentPane().add(p2);
               p2.setBackground(Color.BLACK);
               l.add(p2);
               f.add(l1);
               p2.add(l1);

               
               p3.setBounds(900,50,364,49);
               f.getContentPane().add(p3);
               p3.setBackground(Color.RED);
               l.add(p3);
               p3.add(l2);

               
               p4.setBounds(900,100,364,49);
               f.getContentPane().add(p4);
               p4.setBackground(Color.BLUE);
               l.add(p4);
               p4.add(l3);

               
               
            JButton b1 = new JButton("인기게임");
              b1 = new JButton(new ImageIcon("./image/인기게임.png"));
            b1.addActionListener(new ActionListener() {
                   @Override
                   public void actionPerformed(ActionEvent e) {
//                       new PopGames();
                   }
                   
               });
            
            p1.add(b1);
            f.add(p1);
            l.add(b1);
            
            
            
            JButton b2 = new JButton("fps/스포츠");
              b2 = new JButton(new ImageIcon("./image/FS.png"));
            b2.addActionListener(new ActionListener() {
                   @Override
                   public void actionPerformed(ActionEvent e) {
//                       new FpSpGames(); 
                   }
                   
               });
            p1.add(b2);
            f.add(p1);
            l.add(b2);
            
            JButton b3 = new JButton("cd게임");
              b3 = new JButton(new ImageIcon("./image/CD.png"));
            b3.addActionListener(new ActionListener() {
                   
                   @Override
                   public void actionPerformed(ActionEvent e) {
//                       new Cdgame();
                   }
                   
               });
            p1.add(b3);
            f.add(p1);
            l.add(b3);
            
            
            JButton b4 = new JButton("메신저");
              b4 = new JButton(new ImageIcon("./image/메신저.png"));
            b4.addActionListener(new ActionListener() {
                   
                   @Override
                   public void actionPerformed(ActionEvent e) {
//                       new Messenger();
                   }
                   
               });
            p1.add(b4);
            f.add(p1);
            l.add(b4);
            
            
            JButton b5 = new JButton("상품추가");
            b5.addActionListener(new ActionListener() {
                                      @Override
                   public void actionPerformed(ActionEvent e) {
//                       new Product(); 
                   }
                   
               });
            p1.add(b5);
            f.add(p1);
            l.add(b5);
            
            
            
            
                  

              
            b1.setBounds(100,500,100,100);
            b2.setBounds(400, 500, 100, 100);
            b3.setBounds(680, 500, 100, 100);
            b4.setBounds(980, 500, 100, 100);
            b5.setBounds(445, 0, 400, 100);

            f.setVisible(true);
            c.setVisible(true);

            
         }
                        
            
            public void actionPerformed(ActionEvent e) {
               // TODO Auto-generated method stub
                     }


            
      
   public static void main(String[] args) {
            new MainUI();
      
   }


}