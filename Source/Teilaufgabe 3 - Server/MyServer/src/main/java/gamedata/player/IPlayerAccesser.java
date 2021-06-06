package gamedata.player;

import gamedata.player.helpers.SUniquePlayerIdentifier;

public interface IPlayerAccesser {

	SUniquePlayerIdentifier getPlayerID();

	String getFirstName();

	String getLastName();

	String getStudentID();

	boolean getCollectedTreasure();

}