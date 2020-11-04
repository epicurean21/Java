package FinalTetris;
 
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
 
class Block extends JLabel
{
   int sz = 4; // 배열의 크기
   int idx = 0; 
   int[][] arr = new int[sz][sz];  // 선택된 [4][4] 배열
   Random ran = new Random();
   int x = 3; // 시작 x 좌표 변수
   int y = 0; // 시작 y 좌표 변수
   Color color = null; // 블록 색깔 변수
   boolean flag = true; 
   int[][][] blocks =
      {
         { // ㅁ 모양 블록 생성
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 1, 1, 0},
            {0, 1, 1, 0}
         },
         { // L 모양 블록 생성
            {0, 0, 0, 0},
            {0, 1, 0, 0},
            {0, 1, 0, 0},
            {0, 1, 1, 0}
         },
         { // 뒤집힌 L 모양 생성
            {0, 0, 0, 0},
            {0, 0, 0, 1},
            {0, 0, 0, 1},
            {0, 0, 1, 1}
         },
         { // ㅗ 모양
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 1, 1, 1},
            {0, 0, 1, 0}
         },
         { // 1 모양
            {0, 1, 0, 0},
            {0, 1, 0, 0},
            {0, 1, 0, 0},
            {0, 1, 0, 0}
         },
         { // Z 모양
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 1, 1, 0},
            {0, 0, 1, 1}
         },
         { // Z모양 뒤집힌
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 1, 1},
            {0, 1, 1, 0}
         }   
      };
 
   void rotate() // 회전 메소드
   {   
      int[][] tmp = new int[sz][sz];
 
      switch(idx)
      {
      case 0: // ㅁ 모양은 회전 없음
         break;
        
      case 1:
         // 1, 2, 3 동일 L모양 , 뒤집힌 L모양, ㅗ 모양
      case 2:
         // 1, 2, 3 동일
      case 3:
         for(int i = 0; i < sz; i++)
            for(int j = 0; j < sz; j++)
            {
               tmp[j][i] = arr[i][j];
               arr[i][j] = 0;
            }
 
         for(int i = 0; i < sz; i++)
            {
               for(int j = 0; j < sz; j++)
               {   
                  if(tmp[i][j] == 1 && i < 2)
                     arr[2 + Math.abs(i - 2)][j] = tmp[i][j];
                  else if(tmp[i][j] == 1)
                     arr[2 - Math.abs(i - 2)][j] = tmp[i][j];
               }
            }
 
         break;
      case 4:
         // 1모양  z 모양
      case 5:
         
      case 6:
         if(flag)
         {      
            for(int i = 0; i < sz; i++)
            { 
               for(int j = 0; j < sz; j++)
                 {
                    tmp[j][i] = arr[i][j];
                    arr[i][j] = 0;
                 }
            }
            
            
            for(int i = 0; i < sz; i++)
            {
               for(int j = 0; j < sz; j++)
               {
                  if(tmp[i][j] == 1 && i < 2)
                     arr[2 + Math.abs(i - 2)][j] = tmp[i][j];
                  else if(tmp[i][j] == 1)
                     arr[2 - Math.abs(i - 2)][j] = tmp[i][j];
               }
            }
            flag = false;
         }
         else
         {
            for(int i = 0; i < sz; i++)
            {
               for(int j = 0; j < sz; j++)
               {
                  arr[i][j] = blocks[idx][i][j];
               }
            }
            flag = true;
         }
      }   
   }
 
   public Block() // 블록 랜덤 선택 메소드
   { 
      idx = ran.nextInt(7); // 랜덤 int 형변수 생성 0 ~ 6
 
      for(int i = 0; i < sz; i++)
         for(int j = 0; j < sz; j++) // 4 * 4 배열에
            arr[i][j] = blocks[idx][i][j]; // 선택된 블록 모양 복사
 
      switch(idx) // 블록 색칠
      {
      case 0:
         color = Color.YELLOW;
         break;
      case 1:
         color = Color.BLACK;
         break;
      case 2:
         color = Color.GREEN;
         break;
      case 3:
         color = Color.BLUE;
         break;
      case 4:
        color = Color.RED;
         break;
      case 5:
         color = Color.PINK;
         break;
      case 6:
         color = Color.GRAY;
         break;
      }
   }
}

public class Tetris extends JFrame
{
   Block block = new Block();

   int gridX = 10; // Game X-length
   int gridY = 20; // Game Y-length
   JPanel gp = new JPanel();
   JButton[][] button = new JButton[gridY][gridX];

   int[][] game = new int[gridY][gridX];
   int[][] CurrentArray = new int[gridY][gridX];
   Down down = new Down();
   String file_save = "C:\\Users\\jaemi\\Desktop\\game.txt";
   ArrayList<Integer> line = new ArrayList<Integer>();
 
   ArrayList<Integer> LineCheck()
   {
      ArrayList<Integer> Item = new ArrayList<Integer>();
      int count;
      for(int i = 0; i < gridY; i++)
      {
         count = 0;
 
         for(int j = 0; j < gridX; j++)
            if(game[i][j] == 1) 
               count++;
 
         if(count == gridX) Item.add(i);
      }
 
      return Item;
   }
 
   void LineRemove()
   {
      line = LineCheck();
      Iterator<Integer> tmp = line.iterator();
      int index = 0;
      
      while(tmp.hasNext())
      {
         index = tmp.next();
         for(int i = index; i > 1; i--)
            for(int j = 0; j < gridX; j++)
               game[i][j] = game[i - 1][j];
 
      }
      index = 0;    
   }
 
   boolean Overlap()
   {
      try
      {
         for(int i = 0; i < block.sz; i++)
            for(int j = 0; j < block.sz; j++)
               if(block.arr[i][j] == 1 && 
               CurrentArray[i + block.y][j + block.x] == 1)
                  throw new Exception();
         
         return false;
      }
      catch(Exception e)
      {
         return true;
      }
 
   }
 
   void next_block()
   {
      try {
      for(int i = 0; i < gridY; i++)
         for(int j = 0; j < gridX; j++)
            CurrentArray[i][j] = game[i][j];
      block = new Block();
      }
      catch(Exception e)
      {
         JOptionPane.showMessageDialog(null, "game over");
      }
   }
 
   void CurrentView() // 매 스레드 화면 제 출력 (초기화)
   {
      for(int i = 0; i < gridY; i++)
         for(int j = 0; j < gridX; j++) //12 * 20 화면 재출력 블록 없으면 0 있으면 1
         {
            game[i][j] = CurrentArray[i][j];
 
            if(game[i][j] == 0)
               button[i][j].setBackground(null);
         }
 
      for(int i = 0; i < block.sz; i++)
         for(int j = 0; j < block.sz; j++)
            if(block.arr[i][j] == 1)
            {
               game[i + block.y][j + block.x] = 1;
               button[i + block.y][j + block.x].setBackground(block.color);
               button[i + block.y][j + block.x].setOpaque(true);
            }
   }
 
   class Down extends Thread // 게임 시작 부분. Thread
   {
      public void run() 
      {
         super.run();
         try 
         {
        	while(true)
            {
                block.y++;
		        if(Overlap() || block.y > 19) // 블록의 y 좌표 , 겹치는지 검사 
		            throw new Exception(); // 겹치면 error
		        CurrentView();
		        sleep(300);
            }
         }
         catch (InterruptedException e)
         {
              e.printStackTrace();
         }
         catch (Exception e)
         {
           try {
            LineRemove();
            next_block();
            run();
           }
           catch(Exception E)
           {
               JOptionPane.showMessageDialog(null, "gameover");
           }
            return;
         }
      }
   }
 
   class Keyboard implements KeyListener
   {
      @Override
      public void keyTyped(KeyEvent e) {}
 
      @Override
      public void keyPressed(KeyEvent e) 
      {
         try
         {
            if(e.getKeyCode() == 37)
            { 
               if(Overlap()) return;
               block.x--;
               CurrentView();
            }
            else if(e.getKeyCode() == 38)
            {
               block.rotate();
                 CurrentView();
            }
            else if(e.getKeyCode() == 39)
            {
               if(Overlap()) return;
                 block.x++;
                 CurrentView();
            }
            else if(e.getKeyCode() == 81) // Q 누르면 저장..
            {
               try 
                {
                   BufferedOutputStream fw = 
                		   new BufferedOutputStream(new FileOutputStream(file_save));
                   for(int i = 0; i < gridX; i++)
                      for(int j = 0; j < gridY; j++)
                         fw.write(block.arr[i][j]);   
                }
                catch(IOException IEOE)
                {}
            }
            else if(e.getKeyCode() == 82) // R 누르면 불러오기
            {
               try
                {
                   BufferedInputStream fr = 
                		   new BufferedInputStream(new FileInputStream(file_save));
                   int tmp;
                   while((tmp = fr.read()) != -1)
                   {
                      for(int i = 0; i < gridX; i++)
                         for(int j = 0; j < gridY; j++)
                            CurrentArray[i][j] = tmp;
                   }
                } catch(IOException eqwe)
                {}
            }
         }
         catch(ArrayIndexOutOfBoundsException a)
         {
            switch(e.getKeyCode())
            {
            case 37:
               block.x++;
               CurrentView();
               break;
            case 39:
               block.x--;
               CurrentView();
               break;
            }
         }
      }
 
      @Override
      public void keyReleased(KeyEvent e) {
 
      }   
   }
 
   Tetris()
   {
      setTitle("12161658 조재민의 Tetris");
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setLayout(null);
      gp.setSize(300, 500);
      gp.setLayout(new GridLayout(20, 10));
      gp.setBackground(Color.WHITE);
      gp.setOpaque(true);
 
      for(int i = 0; i < gridY; i++)
         for(int j = 0; j < gridX; j++)   // Tetris Game 초기화
         {
            button[i][j] = new JButton();
            gp.add(button[i][j]);
         }
      addKeyListener(new Keyboard());

      down.start();

      add(gp);
      setSize(316, 540);
      setResizable(true);
      setVisible(true);
      requestFocus();
   }
 
   public static void main(String[] args) 
   {
      new Tetris(); 
   }   
}