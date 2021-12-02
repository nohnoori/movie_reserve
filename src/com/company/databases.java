package com.company;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Vector;

public class databases {
    Connection con=null;
    PreparedStatement pstmt;
    ResultSet rs;
    public void getCon(){
        //JDBC 연동
        String url="jdbc:mysql://localhost:3306/moviedb?useSSL=false";
        try{
            Class.forName("com.mysql.jdbc.Driver");
            con= DriverManager.getConnection(url,"root","snfl");
        }catch (Exception e){
            System.out.println("DB load fail "+e.toString());
        }

    }
    //영화 정보 가져오기(전체/사진 빼고 제목만 가져오겠음..)
    public Vector<String> moviename(){
        Vector<String> v=new Vector<>();
        try{
            getCon();

            String sql="select distinct name from movie";

            pstmt=con.prepareStatement(sql);
            rs=pstmt.executeQuery();
            while(rs.next()){
                v.add(rs.getString(1));
            }
            con.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return v;
    }


    //영화의 시간값 가져오기(여러)
    public Vector<String> movietime(String moviename){
        Vector<String> vs=new Vector<>();
        try {
            //JDBC 연동
            getCon();

            String sql = "select * from movie where name='"+moviename+"';";
            pstmt = con.prepareStatement(sql);
            rs=pstmt.executeQuery();

            while(rs.next()){
                vs.add(rs.getString(3));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return vs;
    }

    //영화제목+시간에 따른 영화 id 값
    public int getmovieid(String moviename,String movietime){
        int movieid=0;
        try{
            getCon();
            String sql="select id from movie where name='"+moviename+"' and time='"+movietime+"';";

            pstmt=con.prepareStatement(sql);
            rs=pstmt.executeQuery();

            rs.next();
            movieid=rs.getInt(1);

            con.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return movieid;
    }

    //예매 정보 저장하기
    public void insertinfo(int movie, ArrayList<String> seat){
        try{
            getCon();
            String seats="";
            for(String s:seat){
                seats+=s+" ";
            }
            String sql="insert into reserve (movieid, seat) values (?,?);";

            pstmt=con.prepareStatement(sql);
            pstmt.setInt(1,movie);
            pstmt.setString(2,seats);

            pstmt.executeUpdate();

            con.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    //예매 정보 가져오기
    public dbBean getinfo(int movieid){
        dbBean bean=new dbBean();
        getCon();
        try{
            String sql="select * from movie as m join reserve as r on m.id=r.movieid where m.id="+movieid+" order by r.id desc limit 1;";

            pstmt=con.prepareStatement(sql);

            rs=pstmt.executeQuery();
            if(rs.next()){
                bean.setMoviename(rs.getString(2));
                bean.setMovietime(rs.getString(3));
                bean.setSeats(rs.getString(7));
            };

            con.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return bean;
    }
}
