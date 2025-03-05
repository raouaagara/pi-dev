package tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDataBase {
    public final String url
            ="jdbc:mysql://localhost:3306/ecosport3A30";
    public final String user="root";
    public final String mdp ="";
    private Connection cnx;
    private static MyDataBase myDataBase;
    private MyDataBase(){
        try {
            cnx = DriverManager.getConnection(url,user,mdp);
            System.out.println("cnx etablie");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
    public static MyDataBase getInstance(){
        if(myDataBase==null)
            myDataBase=new MyDataBase();
        return myDataBase;
    }

    public Connection getCnx() {
        return cnx;
    }
}
