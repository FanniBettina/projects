package model;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class GameLevel {
    public final GameID        gameID;
    public final int           rows, cols;
    public final LevelItem[][] level;
    public Position            player = new Position(0, 0);
    public ArrayList<Position> baskets = new ArrayList<Position>();
    public ArrayList<Position> rangers = new ArrayList<Position>();
    public ArrayList<String> directions = new ArrayList<String>();
    public ArrayList<Direction> rangerDirection = new ArrayList<Direction>();
    private int                numBaskets, numBasketsTotal = 0, numSteps, levelNumBasketTotal, life, score = 0;
    private Random             rand = new Random();
    //private Direction rangerDirection;
    
    public GameLevel(ArrayList<String> gameLevelRows, GameID gameID){
        directions.add("UP");
        directions.add("DOWN");
        directions.add("LEFT");
        directions.add("RIGHT");
        int maxNumber = 3;
        int randomNumber = rand.nextInt(maxNumber);
        //this.rangerDirection = Direction.LEFT;
        this.gameID = gameID;
        int c = 0;
        for (String s : gameLevelRows) if (s.length() > c) c = s.length();
        rows = gameLevelRows.size();
        cols = c;
        level = new LevelItem[rows][cols];
        numBaskets = 0;
        numSteps = 0;
        levelNumBasketTotal = 0;
        life = 0;
        
        for (int i = 0; i < rows; i++){
            String s = gameLevelRows.get(i);
            for (int j = 0; j < s.length(); j++){
                switch (s.charAt(j)){
                    case '@': player = new Position(j, i);
                              level[i][j] = LevelItem.GREEN; break;
                    case '#': level[i][j] = LevelItem.TREE; break;
                    case '.': level[i][j] = LevelItem.CLIFF; break;
                    case '$': level[i][j] = LevelItem.BASKET; 
                              baskets.add( new Position(j, i));
                              levelNumBasketTotal++;
                              break;
                    case '!': rangers.add( new Position(j, i));
                               String d = directions.get(randomNumber);
                               if("UP".equals(d)){
                                   rangerDirection.add(Direction.UP);
                               }
                               if("DOWN".equals(d)){
                                   rangerDirection.add(Direction.DOWN);
                               }
                               if("LEFT".equals(d)){
                                   rangerDirection.add(Direction.LEFT);
                               }
                                if("RIGHT".equals(d)){
                                   rangerDirection.add(Direction.RIGHT);
                               }
                              
                              level[i][j] = LevelItem.RANGER; 
                              break;
                    default:  level[i][j] = LevelItem.GREEN; break;
                }
                numBasketsTotal += baskets.size();
            }
            for (int j = s.length(); j < cols; j++){
                level[i][j] = LevelItem.GREEN;
            }
        }
        
    }

    public GameLevel(GameLevel gl) {
        rangerDirection = gl.rangerDirection;
        gameID = gl.gameID;
        rows = gl.rows;
        cols = gl.cols;
        life = gl.life;
        numBaskets = gl.numBaskets;
        numBasketsTotal = gl.numBasketsTotal;
        levelNumBasketTotal = gl.levelNumBasketTotal;
        numSteps = gl.numSteps;
        level = new LevelItem[rows][cols];
        player = new Position(gl.player.x, gl.player.y);
        rangers = gl.rangers;
        for (int i = 0; i < rows; i++){
            System.arraycopy(gl.level[i], 0, level[i], 0, cols);
        }
      
        
        
    }

    public boolean isValidPosition(Position p){
        return (p.x >= 0 && p.y >= 0 && p.x < cols && p.y < rows);
    }
    
    public boolean isFree(Position p){
        if (!isValidPosition(p)) return false;
        LevelItem li = level[p.y][p.x];
        return (li == LevelItem.GREEN || li == LevelItem.BASKET );
    }
    
    public boolean isBasket(Position p) {
        if (!isValidPosition(p)) return false;
        LevelItem li = level[p.y][p.x];
        return (li == LevelItem.BASKET );
    }
    
    public boolean collides(Position p, int tile_size){
        if (!isValidPosition(p)) return false;
            for(Position r: rangers){
                if(level[player.y][player.x+1] == LevelItem.RANGER || level[player.y][player.x-1] == LevelItem.RANGER || level[player.y + 1][player.x] == LevelItem.RANGER || level[player.y - 1][player.x] == LevelItem.RANGER){
                    //life--;
                    return true;
                 }
            }

        return false;
    }
    public boolean onBasket(){
        LevelItem li = level[player.y][player.x];
        if(li == LevelItem.BASKET ){
            level[player.y][player.x] = LevelItem.GREEN;
            score++;
            numBaskets++;
            return true;
        }
        return false;
    }
    
    
    
    public void moveRanger(){
        int i = 0;
        for(Position ranger :  rangers){
            Position current = ranger;
            Position next = current.translate(rangerDirection.get(i));
            level[current.y][current.x] = LevelItem.GREEN;
            LevelItem li = level[next.y][next.x];
            if(li == LevelItem.GREEN ){
                current = next;
            }
            else{
                if(rangerDirection.get(i) == Direction.RIGHT){
                    rangerDirection.set(i, Direction.LEFT);
                }
                else if(rangerDirection.get(i) == Direction.LEFT){
                    rangerDirection.set(i,Direction.RIGHT);
                }
                else if(rangerDirection.get(i) == Direction.DOWN){
                    rangerDirection.set(i,Direction.UP);
                }
                else if(rangerDirection.get(i) == Direction.UP){
                    rangerDirection.set(i,Direction.DOWN);
                }
            }
            ranger.x = current.x;
            ranger.y = current.y;
            if(rangerDirection.get(i) == Direction.LEFT ||  rangerDirection.get(i) == Direction.RIGHT){
                level[current.y][current.x] = LevelItem.RANGER;
            }
            if(rangerDirection.get(i) == Direction.UP ||  rangerDirection.get(i) == Direction.DOWN){
                level[current.y][current.x] = LevelItem.RANGER;
            }
            i++;

        }
    }
    
    public boolean movePlayer(Direction d){
        Position curr = player;
        Position next = curr.translate(d);
        if (numBasketsTotal > numBaskets && isFree(next)) {
            player = next;
            numSteps++;
            return true;
        } 
        return false;
    }
    
    
    public void printLevel(){
        int x = player.x, y = player.y;
        for (int i = 0; i < rows; i++){
            for (int j = 0; j < cols; j++){
                if (i == y && j == x)
                    System.out.print('@');
                else 
                    System.out.print(level[i][j].representation);
            }
            System.out.println("");
        }
    }
    public ArrayList<Position> getRangers(){
        return rangers;
    }
   
    public int getScore(){
        return score;
    }
    public int getNumBaskets() {
        return numBaskets;
    }
   
    public int getLevelNumBasketsTotal() {
        return levelNumBasketTotal;
    }

    public int getNumBasketsTotal() {
        return numBasketsTotal;
    }

    public int getNumSteps() {
        return numSteps;
    }
    
    public void setNumBaskets(int num){
        numBaskets = num;
    }
    
    
    public int getLifeNum(int number){
        return number;
    }
    public void setLifeNum(int number){
        life = number;
    }
    
  
   

    
}
