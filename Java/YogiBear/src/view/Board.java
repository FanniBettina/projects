package view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JPanel;
import model.Game;
import model.LevelItem;
import model.Position;
import resource.ResourceLoader;
import javax.swing.Timer;

public class Board extends JPanel {
    private Game game;
    private final Image basket, yogi, tree,cliff, green, ranger;
    private double scale;
    private int scaled_size;
    public final int tile_size = 32;
    
    public Board(Game g) throws IOException{
        game = g;
        scale = 1.0;
        scaled_size = (int)(scale * tile_size);
        basket = ResourceLoader.loadImage("resource/basket2.jpg");
        yogi = ResourceLoader.loadImage("resource/yogi2.jpg");
        tree = ResourceLoader.loadImage("resource/tree1.png");
        cliff = ResourceLoader.loadImage("resource/cliff.jpg");
        green = ResourceLoader.loadImage("resource/green.jpg");
        ranger = ResourceLoader.loadImage("resource/ranger.png");
    }
    
    public boolean setScale(double scale){
        this.scale = scale;
        scaled_size = (int)(scale * tile_size);
        return refresh();
    }
    
    public boolean refresh(){
        if (!game.isLevelLoaded()) return false;
        Dimension dim = new Dimension(game.getLevelCols() * scaled_size, game.getLevelRows() * scaled_size);
        setPreferredSize(dim);
        setMaximumSize(dim);
        setSize(dim);
        repaint();
        return true;
    }
    
   

    
    @Override
    protected void paintComponent(Graphics g) {
        LevelItem[][] level;
        boolean ok = false;
        int posx = -1;
        int posy = -1;
        int num = 0;
        if (!game.isLevelLoaded()) return;
        Graphics2D gr = (Graphics2D)g;
        int w = game.getLevelCols();
        int h = game.getLevelRows();
        level = new LevelItem[h][w];
        Position p = game.getPlayerPos();
        for (int y = 0; y < h; y++){
            for (int x = 0; x < w; x++){
                Image img = null;
                LevelItem li = game.getItem(y, x);
                switch (li){
                    case BASKET: img = basket; break;
                    case TREE: img = tree; break;
                    case CLIFF: img = cliff; break;
                    case GREEN: img = green; break;
                    case RANGER: img = ranger; break;
                }
                if (p.x == x && p.y == y) img = yogi;
               
                if (img == null) continue;
                gr.drawImage(img, x * scaled_size, y * scaled_size, scaled_size, scaled_size, null);
                
            }
        }
    }
    
}
