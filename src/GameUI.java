import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

/**
 * The GameUI holds the code for the rendering of the game. Thus, this class
 * will perform the game loop as well. The game logic can be delegated to the
 * game class.
 * 
 * @author z5115782
 */
public class GameUI extends JPanel{
	private static final long serialVersionUID = -5285564050945629510L;
	private final int tileSize = 48;
	private final int shadowRadius = 8;
	private MainUI parent;
	private Game gameObj;
	private boolean animating;
	
	private BufferedImage wallImage;
	private BufferedImage playerImage;
	private BufferedImage boxImage;
	private BufferedImage goalImage;
	private BufferedImage shadowImage;
	
	private double animOffsetX;
	private double animOffsetY;

	public GameUI(MainUI parent) {
		this.parent = parent;
		
		MapGenerator mapGen = new MapGenerator();
		Map map = new Map(mapGen);
		this.gameObj = new Game(map);
		this.initGameScreen();
		this.animating = false;
		
		this.animOffsetX = 0.0f;
		this.animOffsetY = 0.0f;
	}

	private void initGameScreen() {
		this.setBackground(Color.BLACK);
		this.setDoubleBuffered(true);
		this.createExitBtn();
		this.createUndoBtn();
		try {
			this.wallImage = ImageIO.read(new File("src/Images/wall.png"));
			this.playerImage = ImageIO.read(new File("src/Images/player.png"));
			this.boxImage = ImageIO.read(new File("src/Images/crate.png"));
			this.goalImage = ImageIO.read(new File("src/Images/goal.png"));
			this.shadowImage = ImageIO.read(new File("src/Images/shadow.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("S"), "goDown");
		this.getActionMap().put("goDown", new DownAction());
		
		this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("D"), "goRight");
		this.getActionMap().put("goRight", new RightAction());
		
		this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("A"), "goLeft");
		this.getActionMap().put("goLeft", new LeftAction());
		
		this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("W"), "goUp");
		this.getActionMap().put("goUp", new UpAction());
	}
	
	private void createExitBtn(){
		JButton btnExit = new JButton("Exit");
		
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				parent.changeInterface(MainUI.PanelName.MAIN_MENU);
			}
		});
		
		this.add(btnExit);
	}
	
	public void createUndoBtn() {
		JButton btnUndo = new JButton("Undo");
		btnUndo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gameObj.undoMove();
			}
		});
		this.add(btnUndo);
	}

	public void generateGame(Game.Difficulty diff) {
		
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		clearMap(g);
		drawMap(g);
	}
	
	void clearMap(Graphics g){
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, this.parent.getWidth(), this.parent.getHeight());
	}
	
	void drawMap(Graphics g){
		Graphics2D g2d = (Graphics2D) g;
		int[][] grid = this.gameObj.getGrid();
		int offset = 48;
		
		g2d.setColor(Color.GRAY);
		g2d.fillRect(0, offset, grid.length * this.tileSize, grid.length * this.tileSize);
	
		for(int x = 0; x < grid.length; x++){
			for(int y = 0; y < grid.length; y++){
				if (grid[x][y] == Game.GOAL){
					g2d.drawImage(this.goalImage, x * this.tileSize, offset + y * this.tileSize, this.tileSize, this.tileSize, null);
				}
				
				if(grid[x][y] == Game.OBSTACLE || grid[x][y] == Game.BOX){
					g2d.drawImage(this.shadowImage, x * this.tileSize - this.shadowRadius, offset + y * this.tileSize - this.shadowRadius, 
						this.tileSize + this.shadowRadius * 2, this.tileSize + this.shadowRadius * 2, null);
				}
			}
		}
		
		for(int x = 0; x < grid.length; x++){
			for(int y = 0; y < grid.length; y++){
				if(grid[x][y] == Game.OBSTACLE){
					g2d.drawImage(this.wallImage, x * this.tileSize, offset + y * this.tileSize, this.tileSize, this.tileSize, null);
					continue;
				}

				if (grid[x][y] == Game.PLAYER){
					if(animating && grid[x][y] == Game.PLAYER){
						g2d.drawImage(this.playerImage, x * this.tileSize + (int)this.animOffsetX,
							offset + y * this.tileSize + (int)this.animOffsetY, this.tileSize, this.tileSize, null);
					} else {
						g2d.drawImage(this.playerImage, x * this.tileSize, offset + y * this.tileSize, this.tileSize, this.tileSize, null);
					}
					continue;
				}
				
				if (grid[x][y] == Game.BOX){
					g2d.drawImage(this.boxImage, x * this.tileSize, offset + y * this.tileSize, this.tileSize, this.tileSize, null);
					continue;
				}
			}
		}
	}
	
	class DownAction extends AbstractAction {
		private static final long serialVersionUID = 6929552707401993275L;

		public void actionPerformed(ActionEvent e){
			if(!animating){
				if(gameObj.move('s')){
					animating = true;
					animOffsetY = -tileSize;
					repaint();
				}
			}
		}
	}
	
	class RightAction extends AbstractAction {
		private static final long serialVersionUID = -4184857271481754440L;

		public void actionPerformed(ActionEvent e){
			if(!animating){
				if(gameObj.move('d')) {
					animating = true;
					animOffsetX = -tileSize;
					repaint();
				}
				
			}
		}
	}
	
	class UpAction extends AbstractAction {
		private static final long serialVersionUID = -1478214378801170960L;

		public void actionPerformed(ActionEvent e){
			if(!animating){
				if(gameObj.move('w')) {
					animating = true;
					animOffsetY = tileSize;
					repaint();
				}
			}
		}
	}
	
	class LeftAction extends AbstractAction {
		private static final long serialVersionUID = -8932029940888012027L;

		public void actionPerformed(ActionEvent e){
			if(!animating){
				if(gameObj.move('a')) {
					animating = true;
					animOffsetX = tileSize;
					repaint();
				}
			}
		}
}
}
