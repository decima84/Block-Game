import java.awt.Color;

public class BlobGoal extends Goal{

	public BlobGoal(Color c) {
		super(c);
	}

	@Override
	public int score(Block board) {
		int total = 0;
		Color[][] flattenedBoard = board.flatten();

		for (int i = 0; i < flattenedBoard.length; i++) {
			for (int j = 0; j < flattenedBoard[i].length; j++) {
				if (flattenedBoard[i][j].equals(targetGoal)) {
					boolean[][] visited = new boolean[flattenedBoard.length][flattenedBoard[i].length];
					int blobSize = undiscoveredBlobSize(i, j, flattenedBoard, visited);
					total = Math.max(total, blobSize);
				}
			}
		}

		return total;
	}


	@Override
	public String description() {
		return "Create the largest connected blob of " + GameColors.colorToString(targetGoal)
		+ " blocks, anywhere within the block";
	}


	public int undiscoveredBlobSize(int i, int j, Color[][] unitCells, boolean[][] visited) {
		int size = 0;

		int rows = unitCells.length;
		int cols = unitCells[0].length;

		if (i < 0 || i >= rows || j < 0 || j >= cols) {
			return size;
		}

		if (visited[i][j] || !unitCells[i][j].equals(targetGoal)) {
			return size;
		}

		visited[i][j] = true;
		size++;

		size += undiscoveredBlobSize(i-1, j, unitCells, visited); // above
		size += undiscoveredBlobSize(i+1, j, unitCells, visited); // below
		size += undiscoveredBlobSize(i, j-1, unitCells, visited); // left
		size += undiscoveredBlobSize(i, j+1, unitCells, visited); // right

		return size;
	}

}
