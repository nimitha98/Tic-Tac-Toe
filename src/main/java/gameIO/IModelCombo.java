package gameIO;

import model.modelToView.IViewManager;
import model.viewToModel.IModelManager;
import model.viewToModel.ITurnManager;

/**
 * Combines the running and management interfaces of the model.
 */
public interface IModelCombo extends IGameModel, IModelManager{

	/**
	 * Sets the management interface of the view used by the model.
	 * @param viewManager
	 * @param turnManager
	 */
	public abstract void setViewManager(IViewManager viewManager, ITurnManager turnManager);
	

}
