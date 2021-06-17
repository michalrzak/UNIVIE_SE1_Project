package rules;

import MessagesBase.HalfMap;
import MessagesBase.PlayerMove;
import MessagesBase.PlayerRegistration;
import MessagesBase.UniqueGameIdentifier;
import MessagesBase.UniquePlayerIdentifier;

public interface IRules {
	public void validateNewPlayer(UniqueGameIdentifier gameID, PlayerRegistration playerRegistration);

	public void validateNewHalfMap(UniqueGameIdentifier gameID, HalfMap halfmap);

	public void validateGetGameState(UniqueGameIdentifier gameID, UniquePlayerIdentifier playerID);

	public void validateReceiveMove(UniqueGameIdentifier gameID, PlayerMove playerMove);

}
