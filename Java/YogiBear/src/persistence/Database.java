package persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import model.Game;
import model.GameID;
import view.MainWindow;

public class Database {
    private Game game;
    private MainWindow main;
    private final String tableName = "highscore";
    private final Connection conn;
    private final HashMap<String, Integer> highScores;
    
    public Database(Game g){
        this.game = g;
        Connection c = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            c = DriverManager.getConnection("jdbc:mysql://localhost/yogibear?"
                    + "serverTimezone=UTC&user=student&password=asd123");
        } catch (Exception ex) {
            System.out.println("No connection");
        }
        this.conn = c;
        highScores = new HashMap<>();
        loadHighScores();
    }
    
    public boolean storeHighScore(String name, int newScore){
        return mergeHighScores(name, newScore, newScore > 0);
    }
    
    public ArrayList<HighScore> getHighScores(){
        ArrayList<HighScore> scores = new ArrayList<>();
        for (String name : highScores.keySet()){
            if(name != null){
                HighScore h = new HighScore(name, highScores.get(name));
                scores.add(h);}
            //System.out.println(h);
        }
        return scores;
    }
    
    private void loadHighScores(){
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName + " ORDER BY Baskets DESC LIMIT 10");
            
            while (rs.next()){
                String name = rs.getString("NameOfPlayer");
                int baskets = rs.getInt("Baskets");
                
                mergeHighScores(name, baskets, false);
            }
        } catch (Exception e){ System.out.println("loadHighScores error: " + e.getMessage());}
    }
    
    private boolean mergeHighScores(String name, int baskets, boolean store){
        System.out.println("Merge: " + name + ": " + baskets);
        boolean doUpdate = true;
        if (highScores.containsKey(name)){
            int oldScore = highScores.get(name);
            doUpdate = ((baskets > oldScore && baskets != 0) || oldScore == 0 );
        }
        if (doUpdate){
            highScores.remove(name);
            highScores.put(name, baskets);
            if (store) return storeToDatabase(name, baskets) > 0;
            return true;
        }
        return false;
    }
    
    private int storeToDatabase(String name, int baskets){
        try (Statement stmt = conn.createStatement()){
            String s ="INSERT INTO " + tableName + 
                    " (NameOfPlayer, Baskets) " + 
                    "VALUES('" + name + 
                    "'," + baskets + 
                    ") ON DUPLICATE KEY UPDATE Baskets=" + baskets;
            return stmt.executeUpdate(s);
        } catch (Exception e){
            System.out.println("storeToDatabase error");
        }
        return 0;
    }
    
}
