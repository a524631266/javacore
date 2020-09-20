package com.zhangll.core.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class GrameFrame  extends JFrame {
    private final Label[][] label;
    GridLayout grid;
    JPanel chessboard;
    public GrameFrame() {

        this(10, 10);
    }

    public GrameFrame(int row , int column){
        chessboard = new JPanel();
        grid = new GridLayout(row,column);

        chessboard.setLayout(grid);

        add(chessboard,BorderLayout.CENTER);
        label = new Label[row][column];
        update();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void update() {
//        doUpdate(label);
        for (int i = 0; i < label.length; i++) {
            for (int i1 = 0; i1 < label[i].length; i1++) {
                label[i][i1] = new Label();
//                if ((i + i1) % 2 == 0) {
//                    label[i][i1].setBackground(Color.RED);
//                }

                chessboard.add(label[i][i1]);
            }
        }
        pack();
    }

    /**
     *
     * @param label 0 => 红色,1为白色
     */
    public void refreshFrame(int[][] label){
        chessboard.removeAll();
        for (int i = 0; i < label.length; i++) {
            for (int i1 = 0; i1 < label[i].length; i1++) {
                Label label1 = new Label();
                if (label[i][i1] == 1) {
                    label1.setBackground(Color.RED);
                }
                chessboard.add(label1);
            }
        }
        pack();
    }

    public void randomUpdate() {
//        chessboard.
        chessboard.removeAll();
        for (int i = 0; i < label.length; i++) {
            for (int i1 = 0; i1 < label[i].length; i1++) {
                label[i][i1] = new Label();
                Random random = new Random();
                int i2 = random.nextInt(2);
//                System.out.println(i2);
                if(i2 == 0){
                    label[i][i1].setBackground(Color.RED);
                }
//                chessboard.remove(0);
                chessboard.add(label[i][i1]);

            }
        }
//        chessboard.update(g);
        pack();
    }

    public static void main(String[] args) {
        GrameFrame grameFrame = new GrameFrame(20, 20);

        new Thread(()->{

            while (true){
                try {
                    grameFrame.randomUpdate();
                    Thread.sleep(1000);
                } catch (InterruptedException e) {


                }
            }
        }).start();

    }
}

class Squares extends JPanel {
    private static final int PREF_W = 500;
    private static final int PREF_H = PREF_W;
    private ArrayList<Rectangle> squares = new ArrayList<Rectangle>();

    public void addSquare(int x, int y, int width, int height) {
        Rectangle rect = new Rectangle(x, y, width, height);
        squares.add(rect);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(PREF_W, PREF_H);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        for (Rectangle rect : squares) {
//            g2.draw(rect);
            Image tmp  = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
            tmp.getGraphics().setColor(Color.RED);

//            g2.drawImage(tmp, 0, 0, null);
            g2.drawRect(100, 100, 200,200);
        }

    }

}
