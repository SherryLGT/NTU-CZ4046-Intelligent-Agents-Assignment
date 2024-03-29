package classes;

import java.util.HashMap;

import main.Main;

/**
 * Calculate optimal utilities of all and respective states.
 * @author Sherry Lau Geok Teng
 *
 */
public class Utility {

	// Intended outcome probability
	private static final double PROB_INTENDED = 0.8;
	// Probability of moving right angle to the intended
	private static final double PROB_RIGHT_ANGLED = 0.1;

	/**
	 * Get the best action to take according to the highest utilities
	 * Use of getActionUtility method to calculate the utility of the respective action
	 * @param maze Maze environment (reward values)
	 * @param curUtilFunc Hashmap of all State (column,row) and ActionUtilPair (movement direction and utility value)
	 * @param s Initial state
	 * @return The optimal movement direction and its utility
	 */
	public static ActionUtilPair getBestAction(Reward[][] maze, HashMap<State, ActionUtilPair> curUtilFunc, State s) {
		double upUtil = getActionUtility(maze, curUtilFunc, s, Action.UP);
		double max = upUtil;
		Action bestAct = Action.UP;
		double downUtil = getActionUtility(maze, curUtilFunc, s, Action.DOWN);
		if (downUtil > max) {
			max = downUtil;
			bestAct = Action.DOWN;
		}
		double leftUtil = getActionUtility(maze, curUtilFunc, s, Action.LEFT);
		if (leftUtil > max) {
			max = leftUtil;
			bestAct = Action.LEFT;
		}
		double rightUtil = getActionUtility(maze, curUtilFunc, s, Action.RIGHT);
		if (rightUtil > max) {
			max = rightUtil;
			bestAct = Action.RIGHT;
		}
		return new ActionUtilPair(bestAct, max);
	}

	/**
	 * Get utility of the respective action
	 * @param maze Maze environment (reward values)
	 * @param curUtilFunc Hashmap of all State (column,row) and ActionUtilPair (movement direction and utility value)
	 * @param s Initial state
	 * @param a Specific movement direction
	 * @return Utility of state
	 */
	public static double getActionUtility(Reward[][] maze, HashMap<State, ActionUtilPair> curUtilFunc, State s,	Action a) {
		Action leftAngled, rightAngled;
		// Change respective direction according to the action taken
		switch (a) {
		case UP:
			leftAngled = Action.LEFT;
			rightAngled = Action.RIGHT;
			break;
		case DOWN:
			leftAngled = Action.RIGHT;
			rightAngled = Action.LEFT;
			break;
		case LEFT:
			leftAngled = Action.DOWN;
			rightAngled = Action.UP;
			break;
		case RIGHT:
			leftAngled = Action.UP;
			rightAngled = Action.DOWN;
			break;
		default:
			leftAngled = null;
			rightAngled = null;
		}
		// Copy of initial state
		State intendedS = s.clone();
		State leftAngledS = s.clone();
		State rightAngledS = s.clone();
		// Gets the change of state according to action (movement direction)
		intendedS.move(maze, a);
		leftAngledS.move(maze, leftAngled);
		rightAngledS.move(maze, rightAngled);
		// Calculate the expected utility
		double balance = curUtilFunc.get(intendedS).getUtil() * PROB_INTENDED
				+ curUtilFunc.get(leftAngledS).getUtil() * PROB_RIGHT_ANGLED
				+ curUtilFunc.get(rightAngledS).getUtil() * PROB_RIGHT_ANGLED;
		// Utility of individual state reward discounted by the discount factor
		return balance * Main.DISCOUNT_FACTOR + maze[s.getCol()][s.getRow()].value();
	}
}
