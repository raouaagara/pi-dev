package tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDataBase {
    public final String url="jdbc:mysql://localhost:3306/ecosport";
    public final String user="root";
    public final String password="";
    private Connection cnx;
    private static MyDataBase myDataBase;
    private MyDataBase() {
        try {
            cnx = DriverManager.getConnection(url, user, password);
            System.out.println("cnx etabli");
        } catch (SQLException e){ System.out.println(e); }
    }

    public static MyDataBase getInstance(){
        if (myDataBase==null){
            myDataBase=new MyDataBase();
        }
        return myDataBase;
    }

    public Connection getCnx() {
        return cnx;
    }
}
