package model;

public enum LevelItem {
    BASKET('$'), TREE('#'), CLIFF('.'), GREEN(' '), RANGER('!');
    LevelItem(char rep){ representation = rep; }
    public final char representation;

       
        
}
