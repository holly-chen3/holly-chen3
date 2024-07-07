package dungeonmania.entities.movingentities;

import dungeonmania.Dungeon;

public interface Observer {
	public void update(Subject obj, Dungeon dungeon);
}
