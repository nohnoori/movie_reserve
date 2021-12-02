package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.Flow;

//Frame 클래스 따로 만들기
//홈화면, 영화 선택 화면
class Frame1 extends JFrame{
    public Frame1(){
        super("영화 예매");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600,600);
        setResizable(false);

        //layout 일일이 설정
        setLayout(null);
        JLabel name[]=new JLabel[6];
        Image img=new ImageIcon().getImage();
        JButton reserve[]=new JButton[6];

        //영화정보 가져오기
        databases db=new databases();
        Vector<String> m_name=new Vector<>();
        final ArrayList<String> list=new ArrayList<>();
        m_name=db.moviename();

        for(int i=0;i<m_name.size();i++){
            name[i]=new JLabel(m_name.get(i));
            name[i].setBounds(60+i*200,7,100,60);
            add(name[i]);
            list.add(m_name.get(i));
        }
        for(int i=0;i<m_name.size();i++){
            reserve[i]=new JButton("예매하기");
            reserve[i].setBounds(40+i*200,232,100,30);
            add(reserve[i]);
            int j=i;
            //버튼 클릭 이벤트
            reserve[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    setVisible(false);
                    new Frame2(list.get(j));
                }
            });
        }

        setVisible(true);
    }
}

//시간 선택 화면
class Frame2 extends JFrame{
    public Frame2(String moviename){
        super("영화 시간 선택");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300,300);
        setResizable(false);
        setLayout(null);

        //DB에서 시간값 가져오기(movie 이용)
        databases db=new databases();
        Vector<String> list=new Vector();
        list=db.movietime(moviename);

        JComboBox<String> combobox=new JComboBox<>(list);
        JButton prev=new JButton("<");
        JButton next=new JButton("다음");

        prev.setBounds(20,20,30,30);
        combobox.setBounds(55,100,180,40);
        next.setBounds(200,160,80,30);

        add(prev);
        add(combobox);
        add(next);

        setVisible(true);

        //화면 전환
        prev.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                new Frame1();
            }
        });
        next.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                new Frame3(moviename,combobox.getSelectedItem().toString());
            }
        });
    }
}

//성인, 청소년 선택 화면
class Frame3 extends JFrame{
    public Frame3(String movie,String time){
        super("영화 예매");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600,600);
        setResizable(false);
        setLayout(null);

        JLabel adult=new JLabel("성인");
        JLabel child=new JLabel("청소년");

        JButton prev=new JButton("<");
        JButton btn1=new JButton("-");
        JButton btn2=new JButton("+");
        JButton btn3=new JButton("-");
        JButton btn4=new JButton("+");
        JButton next=new JButton("다음");

        JTextField adult_f=new JTextField("1");
        JTextField child_f=new JTextField("0");

        //위치 설정
        prev.setBounds(20,20,30,30);
        adult.setBounds(70,150,100,50);
        child.setBounds(70,200,100,50);
        btn1.setBounds(200,150,30,30);
        adult_f.setBounds(250,150,100,30);
        btn2.setBounds(370,150,30,30);
        btn3.setBounds(200,200,30,30);
        child_f.setBounds(250,200,100,30);
        btn4.setBounds(370,200,30,30);
        next.setBounds(480,500,100,40);

        //버튼 클릭 이벤트
        btn1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int ad=Integer.parseInt(adult_f.getText());
                if(ad>0) {
                    adult_f.setText(ad - 1 + "");
                }
            }
        });
        btn2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int ad=Integer.parseInt(adult_f.getText());
                adult_f.setText(ad+1+"");
            }
        });
        btn3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int ad=Integer.parseInt(child_f.getText());
                if(ad>0) {
                    child_f.setText(ad - 1 + "");
                }
            }
        });
        btn4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int ad=Integer.parseInt(child_f.getText());
                child_f.setText(ad+1+"");
            }
        });

        add(prev);
        add(adult);
        add(child);
        add(btn1);
        add(btn2);
        add(btn3);
        add(btn4);
        add(adult_f);
        add(child_f);
        add(next);

        setVisible(true);

        //화면 전환
        prev.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                new Frame2(movie);
            }
        });
        next.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                new Frame4(movie,time,adult_f.getText(),child_f.getText());
            }
        });
    }
}

//좌석 선택 화면
class Frame4 extends JFrame{
    int mon=0;
    ArrayList<String> seats=new ArrayList();
    public Frame4(String movie, String time, String adult, String child){
        super("좌석 선택");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600,600);
        setResizable(false);
        setLayout(null);

        JPanel pan=new JPanel();
        pan.setLayout(new FlowLayout(0,40,40));
        pan.setBounds(0,90,600,380);

        JPanel pann=new JPanel();
        pann.setLayout(null);
        pann.setBounds(0,480,600,90);
        Color color=new Color(255,215,216);
        pann.setBackground(color);

        JButton prev=new JButton("<");
        JLabel label=new JLabel("결제금액은 "+mon+"원 입니다.");
        JButton credit=new JButton("결제하기");

        char al=65;
        JButton st[][]=new JButton[4][5];
        int member=Integer.parseInt(adult)+Integer.parseInt(child);     //인원수만큼 클릭할 수 있게

        //좌석버튼 생성(영어는 char형으로 받아서 string으로 전환)
        for(int i=0;i<4;i++) {
            String alpha = String.valueOf(al);
            int r=i;
            for (int j = 0; j < 5; j++) {
                int c=j;
                st[i][j]=new JButton(alpha+(j+1)+"");
                //클릭 이벤트
                st[i][j].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //맥에서 setBackground가 안먹힘
                        if(st[r][c].getBackground()==Color.BLACK){
                            st[r][c].setBackground(Color.WHITE);
                            mon-=10000;
                            label.setText("결제금액은 "+mon+"원 입니다.");
                            seats.remove(st[r][c].getText());
                        }else {
                            if(mon+10000<=member*10000) {
                                st[r][c].setBackground(Color.BLACK);
                                mon += 10000;
                                seats.add(st[r][c].getText());
                                label.setText("결제금액은 " +mon + "원 입니다.");
                            }
                        }
                    }
                });
                pan.add(st[i][j]);
            }
            al++;
        }

        prev.setBounds(20,20,30,30);
        label.setBounds(50,20,450,50);
        label.setFont(label.getFont().deriveFont(15,20));
        credit.setBounds(430,27,100,40);

        //프레임에 위치
        pann.add(label);
        pann.add(credit);
        add(prev);
        add(pan);
        add(pann);

        setVisible(true);

        //화면 전환
        prev.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                new Frame3(movie,time);
            }
        });

        credit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                new Frame5(movie,time,adult,child,seats);
            }
        });
    }
}

//예매완료 화면
class Frame5 extends JFrame{
    public Frame5(String movie,String time,String adult,String child,ArrayList seat){
        super("예매 완료");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600,600);
        setResizable(false);
        setLayout(null);

        //예매정보 확인
        JPanel pan=new JPanel(new FlowLayout());
        pan.setBounds(0,250,600,100);

        //데이터베이스에 값 저장
        databases db=new databases();
        int movieid=0;
        movieid=db.getmovieid(movie,time);
        db.insertinfo(movieid,seat);

        JLabel comp=new JLabel("예매 완료 되었습니다.");

        //데이터베이스에 저장된 값 가져오기
        dbBean bean=new dbBean();
        bean=db.getinfo(movieid);

        JLabel info=new JLabel(bean.getMoviename()+", "+bean.getMovietime()+", "+"성인 "+adult+"명, 청소년 "+child+"명"+bean.getSeats()+"입니다.");
        JButton home=new JButton("메인 화면");

        comp.setBounds(120,140,600,80);
        comp.setFont(comp.getFont().deriveFont(15,40));
        info.setBounds(100,30,200,50);
        home.setBounds(480,500,100,40);

        //버튼 클릭 이벤트

        add(comp);
        add(home);
        pan.add(info);
        add(pan);

        setVisible(true);

        home.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                new Frame1();
            }
        });
    }
}
public class Main {
    public static void main(String[] args) {
        Frame1 frame1=new Frame1();
        frame1.setVisible(true);
    }
}
