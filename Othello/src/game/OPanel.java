package game;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.Timer;
import javax.swing.border.Border;

public class OPanel extends JPanel implements MouseListener, ActionListener {
	private int WIDTH = 640, HEIGHT = 690;
	private int unitSize = 80;
	private int numRows = (HEIGHT - 25) / unitSize;
	private int numCols = WIDTH / unitSize;
	private JPanel bottom = new JPanel();
	private JLabel[][] labels = new JLabel[8][8];
	private JLabel title = new JLabel("Othello");
	private JLabel turnLBL = new JLabel(" Turn: Black ");
	private JLabel timerLBL = new JLabel(" Time: 10 ");
	private Border border = BorderFactory.createLineBorder(Color.BLACK, 2);
	private JPanel grid = new JPanel();
	private int turn = 0;
	private Icon white = new ImageIcon("src/img/white.png");
	private Icon black = new ImageIcon("src/img/black.png");
	private Timer timer = new Timer(1000, this);
	int time = 10;

	public OPanel() {
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setSize(HEIGHT - WIDTH, WIDTH);
		title.setOpaque(true);
		title.setBackground(new Color(204, 229, 255));
		title.setFont(new Font("Serif", Font.PLAIN, 50));
		turnLBL.setHorizontalAlignment(SwingConstants.LEADING);
		turnLBL.setOpaque(true);
		turnLBL.setBackground(new Color(0, 0, 75));
		turnLBL.setForeground(Color.WHITE);
		turnLBL.setFont(new Font("Serif", Font.BOLD, 45));
		timerLBL.setHorizontalAlignment(SwingConstants.TRAILING);
		timerLBL.setOpaque(true);
		timerLBL.setBackground(new Color(0, 0, 75));
		timerLBL.setForeground(Color.WHITE);
		timerLBL.setFont(new Font("Serif", Font.BOLD, 45));
		this.setLayout(new BorderLayout(10, 10));
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		grid.setLayout(new GridLayout(numRows, numCols, 5, 5));
		grid.setBorder(border);
		bottom.setLayout(new BorderLayout());
		setLayout(new BorderLayout());
		add(bottom, BorderLayout.SOUTH);
		bottom.add(title, BorderLayout.CENTER);
		bottom.add(turnLBL, BorderLayout.WEST);
		bottom.add(timerLBL, BorderLayout.EAST);
		add(grid, BorderLayout.CENTER);
		grid.setBorder(BorderFactory.createLineBorder(new Color(135, 195, 255), 3));
		grid.setBackground(new Color(0, 0, 0));
		setLabels();
		timer.start();
	}

	private void setLabels() {
		for (int row = 0; row < numRows; row++) {
			for (int col = 0; col < numCols; col++) {
				labels[row][col] = new JLabel();
				labels[row][col].setOpaque(true);
				labels[row][col].setBackground(new Color(0, 51, 25));
				labels[row][col].addMouseListener(this);
				grid.add(labels[row][col]);
			}
		}
		labels[4][4].setIcon(white);
		labels[3][3].setIcon(white);
		labels[4][3].setIcon(black);
		labels[3][4].setIcon(black);
		repaint();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		time--;
		timerLBL.setText(" Time: " + time + " ");
		if (time <= 0) {
			turn++;
			time = 11;
			if (turn % 2 == 1) {
				turnLBL.setText(" Turn: White ");
			} else {
				turnLBL.setText(" Turn: Black ");
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		for (int row = 0; row < numRows; row++) {
			for (int col = 0; col < numCols; col++) {
				if (e.getSource().equals(labels[row][col]) && turn % 2 == 1 && labels[row][col].getIcon() == null) {
					if (checkFlip(row, col, white, 0, 1) > 0 || checkFlip(row, col, white, 0, -1) > 0
							|| checkFlip(row, col, white, 1, 0) > 0 || checkFlip(row, col, white, -1, 0) > 0
							|| checkFlip(row, col, white, 1, 1) > 0 || checkFlip(row, col, white, 1, -1) > 0
							|| checkFlip(row, col, white, -1, 1) > 0 || checkFlip(row, col, white, -1, -1) > 0) {
						labels[row][col].setIcon(white);
						flip(row, col, white);
						turn++;
						time = 10;
						turnLBL.setText(" Turn: Black ");
						checkWin();
					}
				} else if (e.getSource().equals(labels[row][col]) && turn % 2 == 0
						&& labels[row][col].getIcon() == null) {
					;
					if (checkFlip(row, col, black, 0, 1) > 0 || checkFlip(row, col, black, 0, -1) > 0
							|| checkFlip(row, col, black, 1, 0) > 0 || checkFlip(row, col, black, -1, 0) > 0
							|| checkFlip(row, col, black, 1, 1) > 0 || checkFlip(row, col, black, 1, -1) > 0
							|| checkFlip(row, col, black, -1, 1) > 0 || checkFlip(row, col, black, -1, -1) > 0) {
						labels[row][col].setIcon(black);
						flip(row, col, black);
						turn++;
						time = 11;
						turnLBL.setText(" Turn: White ");
						checkWin();
					}
				}
			}
		}
	}

	private void checkWin() {
		int bCount = 0;
		int wCount = 0;
		int count = 0;
		for (int row = 0; row < numRows; row++) {
			for (int col = 0; col < numCols; col++) {
				if (labels[row][col].getIcon() != null) {
					if (labels[row][col].getIcon().equals(black)) {
						bCount++;
						count++;
					} else if (labels[row][col].getIcon().equals(white)) {
						wCount++;
						count++;
					}
				}
			}
		}
		if (wCount == 0) {
			gameOver(1);
		}
		if (bCount == 0) {
			gameOver(0);
		}
		if (count == 64) {
			if (bCount > wCount) {
				gameOver(1);
			} else {
				gameOver(0);
			}
		}
	}

	private void gameOver(int i) {
		timer.stop();
		int x = 0;
		if (i == 1) {
			x = JOptionPane.showConfirmDialog(this, "Restart?", "Black Wins!", 0, JOptionPane.INFORMATION_MESSAGE);
		} else {
			x = JOptionPane.showConfirmDialog(this, "Restart?", "White Wins!", 0, JOptionPane.INFORMATION_MESSAGE);
		}
		if (x == 0) {
			restart();
		} else {
			System.exit(0);
		}
	}

	private void restart() {
		for (int row = 0; row < numRows; row++) {
			for (int col = 0; col < numCols; col++) {
				labels[row][col].setIcon(null);
			}
		}
		labels[4][4].setIcon(white);
		labels[3][3].setIcon(white);
		labels[4][3].setIcon(black);
		labels[3][4].setIcon(black);
		turn = 0;
		turnLBL.setText(" Turn: Black");
		time = 10;
		timer.start();
	}

	private void flip(int row, int col, Icon color) {
		int flip = checkFlip(row, col, color, 0, -1);
		if (flip != -1) {
			for (int i = 0; i < flip; i++) {
				labels[row][col - i - 1].setIcon(color);
			}
		}
		flip = checkFlip(row, col, color, 0, 1);
		if (flip != -1) {
			for (int i = 0; i < flip; i++) {
				labels[row][col + i + 1].setIcon(color);
			}
		}
		flip = checkFlip(row, col, color, -1, 0);
		if (flip != -1) {
			for (int i = 0; i < flip; i++) {
				labels[row - i - 1][col].setIcon(color);
			}
		}
		flip = checkFlip(row, col, color, 1, 0);
		if (flip != -1) {
			for (int i = 0; i < flip; i++) {
				labels[row + i + 1][col].setIcon(color);
			}
		}

		flip = checkFlip(row, col, color, -1, 1);
		if (flip != -1) {
			for (int i = 0; i < flip; i++) {
				labels[row - i - 1][col + i + 1].setIcon(color);
			}
		}
		flip = checkFlip(row, col, color, -1, -1);
		if (flip != -1) {
			for (int i = 0; i < flip; i++) {
				labels[row - i - 1][col - i - 1].setIcon(color);
			}
		}
		flip = checkFlip(row, col, color, 1, 1);
		if (flip != -1) {
			for (int i = 0; i < flip; i++) {
				labels[row + i + 1][col + i + 1].setIcon(color);
			}
		}
		flip = checkFlip(row, col, color, 1, -1);
		if (flip != -1) {
			for (int i = 0; i < flip; i++) {
				labels[row + i + 1][col - i - 1].setIcon(color);
			}
		}
	}

	private int checkFlip(int row, int col, Icon color, int h, int v) {
		int count = 0;
		Icon notColor;
		if (color.equals(black)) {
			notColor = white;
		} else {
			notColor = black;
		}

		if (col + v >= 0 && row + h >= 0 && col + v < 8 && row + h < 8) {
			if (labels[row + h][col + v].getIcon() != null) {
				if (labels[row + h][col + v].getIcon().equals(notColor)) {
					int flip = checkFlip(row + h, col + v, color, h, v);
					if (flip != -1) {
						count = count + 1 + flip;
					} else {
						return (-1);
					}
				} else if (labels[row + h][col + v].getIcon().equals(color)) {
					return (count);
				}
			} else {
				return (-1);
			}
			return (count);
		} else {
			return (-1);
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

}
