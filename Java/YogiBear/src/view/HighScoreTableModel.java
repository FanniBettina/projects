package view;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import persistence.HighScore;

public class HighScoreTableModel extends AbstractTableModel{
    private final ArrayList<HighScore> highScores;
    private final String[] colName = new String[]{ "Név", "Összegyűjtött kosarak"};
    
    public HighScoreTableModel(ArrayList<HighScore> highScores){
        this.highScores = highScores;
    }

    @Override
    public int getRowCount() { return highScores.size(); }

    @Override
    public int getColumnCount() { return 2; }

    @Override
    public Object getValueAt(int r, int c) {
        HighScore h = highScores.get(r);
        if (c == 0) return h.name;
        else return h.numOfbaskets;
    }

    @Override
    public String getColumnName(int i) { return colName[i]; }    
    
}
