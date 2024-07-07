package dungeonmania.entities.movingentities;

import dungeonmania.Dungeon;

public interface Subject {
    public void attach(Observer o);
	public void detach(Observer o);
	public void notifyObservers(Dungeon dungeon);
}
