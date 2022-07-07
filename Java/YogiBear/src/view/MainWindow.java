package view;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.WindowConstants;
import java.awt.event.ActionListener;
import model.Direction;
import model.Game;
import model.GameID;
import model.Position;

public class MainWindow extends JFrame{
    private JPanel panel = new JPanel();
    private final Game game;
    private Board board;
    private final JLabel gameStatLabel;    
    private int tile_size = 32;
    private final int FPS = 240;
    private Timer timer;
    private int level = 0;
    private int counter = 0; 
    private int life;
    private int allBaskets = 0;
    
    
    
    public MainWindow() throws IOException{
        game = new Game();
        
        setTitle("Yogi Bear");
        setSize(600, 600);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        URL url = MainWindow.class.getClassLoader().getResource("resource/basket.png");
        setIconImage(Toolkit.getDefaultToolkit().getImage(url));
        
        
        JMenuBar menuBar = new JMenuBar();
        JMenu menuGame = new JMenu("Játék");
       
        JMenu menuGameScale = new JMenu("Nagyítás");
        createScaleMenuItems(menuGameScale, 1.0, 2.0, 0.5);
        

        JMenuItem menuHighScores = new JMenuItem(new AbstractAction("Legjobb eredmények") {
            @Override
            public void actionPerformed(ActionEvent e) {
                new HighScoreWindow(game.getHighScores(), MainWindow.this);
            }
        });
        
        JMenuItem menuGameExit = new JMenuItem(new AbstractAction("Kilépés") {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        
        JMenuItem menuRestart = new JMenuItem(new AbstractAction("Új játék") {
            @Override
            public void actionPerformed(ActionEvent e) {
                setResizable(false);
                setLocationRelativeTo(null);
                game.loadGame(new GameID("EASY", 1));
                board.repaint();
                life = 3;
                game.setLifeNum(life);
                timer.start();
                game.onBasket();
                refreshGameStatLabel();
                board.refresh();
                setVisible(true);
                pack();
                
                
            }
        });
        timer = new Timer(1000, new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                if(!game.isLevelLoaded()) return;
                if(life != 0){
                    game.moveRangers();
                    //System.out.println("Direction:" + rangerDirection);
                    board.repaint();
                    
                }
            }
        });
        

        menuGame.add(menuGameScale);
        menuGame.add(menuRestart);
        menuGame.addSeparator();
        menuGame.add(menuGameExit);
        menuBar.add(menuGame);
        menuBar.add(menuHighScores);
        setJMenuBar(menuBar);
        
        
        setLayout(new BorderLayout(0, 10));
        gameStatLabel = new JLabel("label");

        add(gameStatLabel, BorderLayout.NORTH);
        try { add(board = new Board(game), BorderLayout.CENTER); } catch (IOException ex) {}
        
        
        
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent ke) {
                super.keyPressed(ke); 
                if (!game.isLevelLoaded()) return;
                int kk = ke.getKeyCode();
                Direction d = null;
                switch (kk){
                    case KeyEvent.VK_LEFT:  d = Direction.LEFT; break;
                    case KeyEvent.VK_RIGHT: d = Direction.RIGHT; break;
                    case KeyEvent.VK_UP:    d = Direction.UP; break;
                    case KeyEvent.VK_DOWN:  d = Direction.DOWN; break;
                    case KeyEvent.VK_ESCAPE: game.loadGame(game.getGameID());
                }
                if ( game.step(d,game.getName())){
                    if (game.getLevelNumBaskets() == game.getNumBasketsTotal()){
                        JOptionPane.showMessageDialog(MainWindow.this, "Gratulálok! Nyertél!", "Gratulálok!", JOptionPane.INFORMATION_MESSAGE);
                        counter = 1;
                        level = 1;
                        life = 3;
                        game.setAllBaskets(0);
                        game.loadGame(new GameID("EASY", 1));
                        game.setLifeNum(life);
                    }
                }
           
                    if (life == 0){
                        timer.stop();
                        game.setLifeNum(0);
                        
                        refreshGameStatLabel();
                        game.setAllBaskets(game.getScore());
     
                        JOptionPane.showMessageDialog(MainWindow.this, "A játéknak vége!", "Vesztettél!", JOptionPane.INFORMATION_MESSAGE);
                        String name = JOptionPane.showInputDialog(MainWindow.this ,"Add meg a neved:","Eredmény elmentése", JOptionPane.INFORMATION_MESSAGE);
                        game.setName(name);
                        if(name != null){
                            game.getDatabase().storeHighScore(name,game.getAllBaskets());
                        }
                        game.setAllBasketsToNull();
                        
                    }
                
                if(level == 6){
                    level = 1;
                }
                
                if (game.getLevelNumBaskets() == game.getLevelNumBasketsTotal()){
                    game.setAllBaskets(game.getScore());
                    setResizable(false);
                    setLocationRelativeTo(null);
                    level++;
                    counter++;
                    if(counter <= 5 && counter != 1){
                        game.loadGame(new GameID("EASY", level));
                    }
                    if(counter > 5 && counter <= 10){
                        if(counter == 6){ game.loadGame(new GameID("MEDIUM", 1)); }
                        else{ game.loadGame(new GameID("MEDIUM", level));}
                    }
                    if(counter > 10 && counter <= 15){
                        if(counter == 11){ game.loadGame(new GameID("HARD", 1)); }
                        else{ game.loadGame(new GameID("HARD", level));}
                    }
                    if(counter > 15 ){
                        if(counter == 16){ game.loadGame(new GameID("MISSION IMPOSSIBLE", 1)); }
                        else{ game.loadGame(new GameID("MISSION IMPOSSIBLE", level));}
                    }
                    
                }
                Position p = game.getPlayerPos();
                if(game.collidesOrNot(p, tile_size)){
                    life--;
                }
                game.onBasket();
                board.refresh();
                refreshGameStatLabel();
                setVisible(true);
                pack();
                
              
               
            }
             
           
            
        });
        timer.start();
        setResizable(false);
        setLocationRelativeTo(null);
        level++;
        counter++;
        if(counter == 1) {
            
            game.loadGame(new GameID("EASY", 1));
            life = 3;
            game.setLifeNum(life);
            game.setAllBasketsToNull();
            
        }
        game.onBasket();
        board.refresh();
        refreshGameStatLabel();
        setVisible(true);
        pack();
            
        
        
    }
    
    private void refreshGameStatLabel(){
        String s = "Lépések száma: " + game.getNumberOfSteps();
        s += "    Begyűjtött kosarak száma: " + game.getLevelNumBaskets() + "/" + game.getLevelNumBasketsTotal() + "    Élet: " + game.getLifeNumber(life);
        gameStatLabel.setText(s);
    }
   
    private void createScaleMenuItems(JMenu menu, double from, double to, double by){
        while (from <= to){
            final double scale = from;
            JMenuItem item = new JMenuItem(new AbstractAction(from + "x") {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (board.setScale(scale)) pack();
                }
            });
            menu.add(item);
            
            if (from == to) break;
            from += by;
            if (from > to) from = to;
        }
    }

    
    
    

    
     public static void main(String[] args) {
        try {
            new MainWindow();
        } catch (IOException ex) {}
    }  
}
