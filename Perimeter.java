import java.awt.Color;

public class PerimeterGoal extends Goal{

	public PerimeterGoal(Color c) {
		super(c);


	}

	@Override
	public int score(Block board) {
		int score = 0;
		Color[][] flattenedBoard = board.flatten();

		for (int i = 0; i < flattenedBoard.length; i++) {
			for (int j = 0; j < flattenedBoard[0].length; j++) {
				if (flattenedBoard[i][j].equals(targetGoal)) {
					if (i == 0 || i == flattenedBoard.length - 1 || j == 0 || j == flattenedBoard[0].length - 1) {
						score += 1;
						if ((i == 0 && j == 0) || (i == 0 && j == flattenedBoard[0].length - 1) ||
								(i == flattenedBoard.length - 1 && j == 0) || (i == flattenedBoard.length - 1 && j == flattenedBoard[0].length - 1)) {
							score += 1;
						}
					}
				}
			}
		}
		return score;
	}

	@Override
	public String description() {
		return "Place the highest number of " + GameColors.colorToString(targetGoal) 
		+ " unit cells along the outer perimeter of the board. Corner cell count twice toward the final score!";
	}

}
