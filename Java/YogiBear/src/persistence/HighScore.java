package persistence;

import java.util.Objects;
import model.GameID;

public class HighScore {
    public final String name;
    public final int numOfbaskets;
    //public final String name;
    
    public HighScore(String name, int baskets){
        this.name = name;
        this.numOfbaskets = baskets;
    }
    

    @Override
    public int hashCode() {
        return Objects.hash(name, numOfbaskets);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final HighScore other = (HighScore) obj;
      return numOfbaskets == other.numOfbaskets && Objects.equals(name, other.name);
    
    }   

    @Override
    public String toString() {
        return name + ":" + numOfbaskets;
    }
    
    
}
