package a7;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.util.List;
import java.util.ArrayList;

public class OthelloWidget extends JPanel implements ActionListener, SpotListener {

	private enum Player {
		WHITE, BLACK
	};

	private JSpotBoard board;
	private JLabel message;
	private boolean gameEnd;
	private Player nextPlayer;

	public OthelloWidget() {
		board = new JSpotBoard(8, 8);
		message = new JLabel();

		setLayout(new BorderLayout());
		add(board, BorderLayout.CENTER);

		JPanel resetMessagePanel = new JPanel();
		resetMessagePanel.setLayout(new BorderLayout());

		JButton resetButton = new JButton("Restart");
		resetButton.addActionListener(this);
		resetMessagePanel.add(resetButton, BorderLayout.EAST);
		resetMessagePanel.add(message, BorderLayout.CENTER);

		add(resetMessagePanel, BorderLayout.SOUTH);
		board.addSpotListener(this);

		resetGame();
	}

	private void resetGame() {
		for (Spot s : board) {
			s.clearSpot();
		}
		board.getSpotAt(3, 4).setSpotColor(Color.BLACK);
		board.getSpotAt(3, 3).setSpotColor(Color.WHITE);
		board.getSpotAt(4, 3).setSpotColor(Color.BLACK);
		board.getSpotAt(4, 4).setSpotColor(Color.WHITE);
		board.getSpotAt(3, 4).setSpot();
		board.getSpotAt(3, 3).setSpot();
		board.getSpotAt(4, 3).setSpot();
		board.getSpotAt(4, 4).setSpot();

		gameEnd = false;
		nextPlayer = Player.BLACK;
		message.setText("Welcome to Othello. Black to play.");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		resetGame();
	}

	@Override
	public void spotClicked(Spot spot) {
		if (gameEnd || !isValid(spot.getSpotX(), spot.getSpotY())) {
			return;
		}

		flankSpot(spot.getSpotX(), spot.getSpotY());
		spot.unhighlightSpot();

		String nextPlayerName = null;
		Color playerColor = null;

		if (nextPlayer == Player.WHITE) {
			playerColor = Color.WHITE;
			nextPlayerName = "Black";
			nextPlayer = Player.BLACK;
		} else {
			playerColor = Color.BLACK;
			nextPlayerName = "White";
			nextPlayer = Player.WHITE;
		}

		spot.setSpotColor(playerColor);
		spot.setSpot();

		message.setText(nextPlayerName + " to play.");
		if (checkEnd()) {
			if (nextPlayer == Player.WHITE) {
				playerColor = Color.WHITE;
				nextPlayerName = "Black";
				nextPlayer = Player.BLACK;
			} else {
				playerColor = Color.BLACK;
				nextPlayerName = "White";
				nextPlayer = Player.WHITE;
			}
			message.setText(nextPlayerName + " to play again.");
		}

		gameEnd = checkEnd();

		if (gameEnd) {
			int white = 0;
			int black = 0;
			for (Spot s : board) {
				if (!s.isEmpty() && s.getSpotColor() == Color.WHITE) {
					white++;
				}
				if (!s.isEmpty() && s.getSpotColor() == Color.BLACK) {
					black++;
				}
			}
			if (white < black) {
				message.setText("Game over. Black wins. Score: " + white + ":" + black + ".");
			} else if (white > black) {
				message.setText("Game over. White wins. Score: " + white + ":" + black + ".");
			} else {
				message.setText("Game over. Draw game. Score: " + white + ":" + black + ".");
			}
			return;
		}
	}

	private boolean checkEnd() {
		for (Spot s : board) {
			if (isValid(s.getSpotX(), s.getSpotY())) {
				return false;
			}
		}
		return true;
	}

	private Color playerColor() {
		return nextPlayer == Player.WHITE ? Color.WHITE : Color.BLACK;
	}

	private Color rivalColor() {
		return nextPlayer == Player.WHITE ? Color.BLACK : Color.WHITE;
	}

	private boolean isUpValid(int x, int y) {
		if (y >= 2 && !board.getSpotAt(x, y - 1).isEmpty()
				&& board.getSpotAt(x, y - 1).getSpotColor().equals(rivalColor())) {
			for (int i = y - 2; i >= 0; i--) {
				if (board.getSpotAt(x, i).isEmpty()) {
					return false;
				}
				if (board.getSpotAt(x, i).getSpotColor().equals(playerColor())) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean isDownValid(int x, int y) {
		if (y <= 5 && !board.getSpotAt(x, y + 1).isEmpty()
				&& board.getSpotAt(x, y + 1).getSpotColor().equals(rivalColor())) {
			for (int i = y + 2; i <= 7; i++) {
				if (board.getSpotAt(x, i).isEmpty()) {
					return false;
				}
				if (board.getSpotAt(x, i).getSpotColor().equals(playerColor())) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean isLeftValid(int x, int y) {
		if (x >= 2 && !board.getSpotAt(x - 1, y).isEmpty()
				&& board.getSpotAt(x - 1, y).getSpotColor().equals(rivalColor())) {
			for (int i = x - 2; i >= 0; i--) {
				if (board.getSpotAt(i, y).isEmpty()) {
					return false;
				}
				if (board.getSpotAt(i, y).getSpotColor().equals(playerColor())) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean isRightValid(int x, int y) {
		if (x <= 5 && !board.getSpotAt(x + 1, y).isEmpty()
				&& board.getSpotAt(x + 1, y).getSpotColor().equals(rivalColor())) {
			for (int i = x + 2; i <= 7; i++) {
				if (board.getSpotAt(i, y).isEmpty()) {
					return false;
				}
				if (board.getSpotAt(i, y).getSpotColor().equals(playerColor())) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean isLeftUpDiagonalValid(int x, int y) {
		if (x >= 2 && y >= 2 && !board.getSpotAt(x - 1, y - 1).isEmpty()
				&& board.getSpotAt(x - 1, y - 1).getSpotColor().equals(rivalColor())) {
			for (int i = 2; x - i >= 0 && y - i >= 0; i++) {
				if (board.getSpotAt(x - i, y - i).isEmpty()) {
					return false;
				}
				if (board.getSpotAt(x - i, y - i).getSpotColor().equals(playerColor())) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean isRightDownDiagonalValid(int x, int y) {
		if (x <= 5 && y <= 5 && !board.getSpotAt(x + 1, y + 1).isEmpty()
				&& board.getSpotAt(x + 1, y + 1).getSpotColor().equals(rivalColor())) {
			for (int i = 2; x + i <= 7 && y + i <= 7; i++) {
				if (board.getSpotAt(x + i, y + i).isEmpty()) {
					return false;
				}
				if (board.getSpotAt(x + i, y + i).getSpotColor().equals(playerColor())) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean isLeftDownAntiDiagonalValid(int x, int y) {
		if (x >= 2 && y <= 5 && !board.getSpotAt(x - 1, y + 1).isEmpty()
				&& board.getSpotAt(x - 1, y + 1).getSpotColor().equals(rivalColor())) {
			for (int i = 2; x - i >= 0 && y + i <= 7; i++) {
				if (board.getSpotAt(x - i, y + i).isEmpty()) {
					return false;
				}
				if (board.getSpotAt(x - i, y + i).getSpotColor().equals(playerColor())) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean isRightUpAntiDiagonalValid(int x, int y) {
		if (x <= 5 && y >= 2 && !board.getSpotAt(x + 1, y - 1).isEmpty()
				&& board.getSpotAt(x + 1, y - 1).getSpotColor().equals(rivalColor())) {
			for (int i = 2; x + i <= 7 && y - i >= 0; i++) {
				if (board.getSpotAt(x + i, y - i).isEmpty()) {
					return false;
				}
				if (board.getSpotAt(x + i, y - i).getSpotColor().equals(playerColor())) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean isValid(int x, int y) {
		if (!board.getSpotAt(x, y).isEmpty()) {
			return false;
		}

		return isUpValid(x, y) || isDownValid(x, y) || isLeftValid(x, y) || isRightValid(x, y)
				|| isLeftUpDiagonalValid(x, y) || isRightDownDiagonalValid(x, y) || isLeftDownAntiDiagonalValid(x, y)
				|| isRightUpAntiDiagonalValid(x, y);
	}

	private void flankSpot(int x, int y) {

		/* Flank Up */
		if (isUpValid(x, y)) {
			for (int i = y - 1; board.getSpotAt(x, i).getSpotColor().equals(rivalColor()); i--) {
				board.getSpotAt(x, i).setSpotColor(playerColor());
			}
		}

		/* Flank Down */
		if (isDownValid(x, y)) {
			for (int i = y + 1; board.getSpotAt(x, i).getSpotColor().equals(rivalColor()); i++) {
				board.getSpotAt(x, i).setSpotColor(playerColor());
			}
		}

		/* Flank Left */
		if (isLeftValid(x, y)) {
			for (int i = x - 1; board.getSpotAt(i, y).getSpotColor().equals(rivalColor()); i--) {
				board.getSpotAt(i, y).setSpotColor(playerColor());
			}
		}

		/* Flank Right */
		if (isRightValid(x, y)) {
			for (int i = x + 1; board.getSpotAt(i, y).getSpotColor().equals(rivalColor()); i++) {
				board.getSpotAt(i, y).setSpotColor(playerColor());
			}
		}

		/* Flank LeftUp Diagonal */
		if (isLeftUpDiagonalValid(x, y)) {
			for (int i = 1; board.getSpotAt(x - i, y - i).getSpotColor().equals(rivalColor()); i++) {
				board.getSpotAt(x - i, y - i).setSpotColor(playerColor());
			}
		}

		/* Flank RightDown Diagonal */
		if (isRightDownDiagonalValid(x, y)) {
			for (int i = 1; board.getSpotAt(x + i, y + i).getSpotColor().equals(rivalColor()); i++) {
				board.getSpotAt(x + i, y + i).setSpotColor(playerColor());
			}
		}

		/* Flank LeftDown AntiDiagonal */
		if (isLeftDownAntiDiagonalValid(x, y)) {
			for (int i = 1; board.getSpotAt(x - i, y + i).getSpotColor().equals(rivalColor()); i++) {
				board.getSpotAt(x - i, y + i).setSpotColor(playerColor());
			}
		}

		/* Flank RightUp AntiDiagonal */
		if (isRightUpAntiDiagonalValid(x, y)) {
			for (int i = 1; board.getSpotAt(x + i, y - i).getSpotColor().equals(rivalColor()); i++) {
				board.getSpotAt(x + i, y - i).setSpotColor(playerColor());
			}
		}
	}

	@Override
	public void spotEntered(Spot spot) {
		if (gameEnd) {
			return;
		}
		if (isValid(spot.getSpotX(), spot.getSpotY())) {
			spot.highlightSpot();
		}
	}

	@Override
	public void spotExited(Spot spot) {
		spot.unhighlightSpot();
	}

}
