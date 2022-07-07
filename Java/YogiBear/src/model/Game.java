package model;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Scanner;
import persistence.Database;
import persistence.HighScore;
import resource.ResourceLoader;
import view.MainWindow;

public class Game {
    private final HashMap<String, HashMap<Integer, GameLevel>> gameLevels;
    private GameLevel gameLevel = null;
    private final Database database;
    private boolean isBetterHighScore = false;
    private MainWindow main;
    private String name;
    private int allBaskets;

    public Game() {
        gameLevels = new HashMap<>();
        
        database = new Database(this);
        readLevels();
    }

    // ------------------------------------------------------------------------
    // The 'interesting' part :)
    // ------------------------------------------------------------------------

    public void loadGame(GameID gameID){
        gameLevel = new GameLevel(gameLevels.get(gameID.difficulty).get(gameID.level));
        isBetterHighScore = false;
       
    }
    
    
    public boolean step(Direction d, String name){
        int baskets = getAllBaskets();//gameLevel.getScore();
        /*if(name != null){
            isBetterHighScore = database.storeHighScore(name,baskets);
        }*/
        return (gameLevel.movePlayer(d) );
    }
    
    public boolean collidesOrNot(Position p, int tile_size){
        return gameLevel.collides(p,tile_size);
    }
    
    // ------------------------------------------------------------------------
    // Getter methods
    // ------------------------------------------------------------------------
    
   
    public Database getDatabase() {
        return database;
    }
    public boolean isLevelLoaded(){ return gameLevel != null; }
    public int getLevelRows(){ return gameLevel.rows; }
    public int getLevelCols(){ return gameLevel.cols; }
    public LevelItem getItem(int row, int col){ return gameLevel.level[row][col]; }
    public GameID getGameID(){ return (gameLevel != null) ? gameLevel.gameID : null; }
    public int getLevelNumBaskets(){ return (gameLevel != null) ? gameLevel.getNumBaskets() : 0; }
    public int getNumBasketsTotal(){ return (gameLevel != null) ? gameLevel.getNumBasketsTotal(): 0; }
    public int getLevelNumBasketsTotal(){ return (gameLevel != null) ? gameLevel.getLevelNumBasketsTotal(): 0; }
    public int getNumberOfSteps(){ return (gameLevel != null) ? gameLevel.getNumSteps(): 0; }
    public boolean isPosBasket(Position p) { return gameLevel.isBasket(p);}
    public void setNumOfBaskets(int num){gameLevel.setNumBaskets(num);}
    public boolean onBasket(){return gameLevel.onBasket(); }
    public int getLifeNumber(int number){ return (gameLevel != null) ? gameLevel.getLifeNum(number) : 0; }
    public int getScore(){ return (gameLevel != null) ? gameLevel.getScore() : 0; }
    public void setLifeNum(int number){ gameLevel.setLifeNum(number);}
    public boolean isBetterHighScore(){ return isBetterHighScore; }
    public void moveRangers(){gameLevel.moveRanger();}
    
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return this.name;
    }
    public void setAllBasketsToNull(){ allBaskets = 0;}
    public void setAllBaskets(int b){ allBaskets += b;}
    public int getAllBaskets(){return allBaskets;}
   
    
    public ArrayList<HighScore> getHighScores() {
        return database.getHighScores();
    }
    
    public Position getPlayerPos(){ // MAKE IT ~IMMUTABLE
        return new Position(gameLevel.player.x, gameLevel.player.y); 
    }
    
    public ArrayList<Position> getRangers() {
        return gameLevel.getRangers();
    }
    
   
    
    // ------------------------------------------------------------------------
    // Utility methods to load game levels from res/levels.txt resource file.
    // ------------------------------------------------------------------------

    private void readLevels(){
        //ClassLoader cl = getClass().getClassLoader();
        InputStream is;// = cl.getResourceAsStream("res/levels.txt");
        is = ResourceLoader.loadResource("resource/levels.txt");
        
        try (Scanner sc = new Scanner(is)){
            String line = readNextLine(sc);
            ArrayList<String> gameLevelRows = new ArrayList<>();
            
            while (!line.isEmpty()){
                GameID id = readGameID(line);
                if (id == null) return;
                
                // System.out.println(id.difficulty + " " + id.id);

                gameLevelRows.clear();
                line = readNextLine(sc);
                while (!line.isEmpty() && line.trim().charAt(0) != ';'){
                    gameLevelRows.add(line);                    
                    line = readNextLine(sc);
                }
                addNewGameLevel(new GameLevel(gameLevelRows, id));
            }
            //if (is != null) is.close();
        } catch (Exception e){
            System.out.println("Ajaj");
        }
        
    }
    
    private void addNewGameLevel(GameLevel gameLevel){
        HashMap<Integer, GameLevel> levelsOfDifficulty;
        if (gameLevels.containsKey(gameLevel.gameID.difficulty)){
            levelsOfDifficulty = gameLevels.get(gameLevel.gameID.difficulty);
            levelsOfDifficulty.put(gameLevel.gameID.level, gameLevel);
        } else {
            levelsOfDifficulty = new HashMap<>();
            levelsOfDifficulty.put(gameLevel.gameID.level, gameLevel);
            gameLevels.put(gameLevel.gameID.difficulty, levelsOfDifficulty);
        }
    }
  
    private String readNextLine(Scanner sc){
        String line = "";
        while (sc.hasNextLine() && line.trim().isEmpty()){
            line = sc.nextLine();
        }
        return line;
    }
    
    private GameID readGameID(String line){
        line = line.trim();
        if (line.isEmpty() || line.charAt(0) != ';') return null;
        Scanner s = new Scanner(line);
        s.next(); // skip ';'
        if (!s.hasNext()) return null;
        String difficulty = s.next().toUpperCase();
        if (!s.hasNextInt()) return null;
        int id = s.nextInt();
        return new GameID(difficulty, id);
    }    

}
