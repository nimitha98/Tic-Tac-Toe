package gameIO;

import model.board.IBoardModel;
import model.modelToView.ICommand;

/**
 * This interface represents the model for a game in an MVC architecture.
 */

public interface IGameModel {

	void setCommand(final ICommand p0);
    	IBoardModel getBoardModel();
	
}
