package epiczerg.snaek;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JOptionPane;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.openal.SoundStore;
import org.newdawn.slick.util.ResourceLoader;

public class Main {
	int w, h;
	int BODY_LEN = 4;
	int[][] body;
	int[] head = new int[2];
	long deltaDir = 0, dir = 0;
	Clock clk = new Clock();
	MatrixRenderer mr;
	int cx = 1,cy = 1;
	int delay = 100;
	boolean start = true;
	boolean LSDMODE = false;
	private Audio LSD,LSO;
	boolean isPlaying = false;
	TrueTypeFont font;
	String Username  ="";
	int SCORE = 0;
	public void start(int wi, int he) throws IOException {
		LSDMODE = !LSDMODE;

		LSD = AudioLoader.getAudio("OGG", ResourceLoader.getResourceAsStream("res/LSD.ogg"));
		LSO = AudioLoader.getAudio("OGG", ResourceLoader.getResourceAsStream("res/LSO.ogg"));
		w = wi < 200 ? 400 : wi;
		h = he < 200 ? 400 : he;
		try {
			Display.setDisplayMode(new DisplayMode(w, h));
			Display.create();
			Display.setTitle("Snaek");
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, w, 0, h, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		//Render Init		
		mr = new MatrixRenderer(w, h, 1.0f, 0.5f, 0.3f,this);
//		mr.drawBoxes(10, 5);
		mr.init();
		head[0] = 2;
		head[1] = 4;
		randomizeCandy();
		//Main Loop		
		while (!Display.isCloseRequested()) {
			if(LSDMODE && !isPlaying){
				LSO.playAsMusic(1.0f, 1.0f, true);
				isPlaying = true;
			}
			if(!LSO.isPlaying() && isPlaying){
				LSD.playAsMusic(1.0f, 1.0f, true);
			}
			else if(!LSDMODE && isPlaying){
				LSO.stop();
				LSD.stop();
			}
			

				
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			mr.setBox(cx,cy, true,true);
			Font awtFont = new Font("Times New Roman", Font.BOLD, 48);
			font = new TrueTypeFont(awtFont, false);
			if (coosInsideMatrix(head[0], head[1])) {
				if(head[0] == cx && head[1] == cy){
					BODY_LEN++;
					randomizeCandy();
				}							
				if (start) {
					clk.delay(1700);
					start = false;
				}
				while (Keyboard.next()) {
					if (Keyboard.getEventKeyState()) {
						if (Keyboard.getEventKey() == Keyboard.KEY_LEFT)
							deltaDir = -1;
						else if (Keyboard.getEventKey() == Keyboard.KEY_RIGHT)
							deltaDir = 1;
						else if (Keyboard.getEventKey() == Keyboard.KEY_F1)
							LSDMODE = !LSDMODE;
							
					}
				}
				int[] buffer = new int[2];
				int[] temp = new int[2];
				body = new int[BODY_LEN][2];

				buffer[0] = head[0];
				buffer[1] = head[1];
				dir += 4;
				dir += deltaDir;
				deltaDir = 0;
				dir %= 4;
				switch ((int) dir) {
				case 0:
					head[0] += 1;
					break; // right
				case 1:
					head[1] -= 1;
					break; // top
				case 2:
					head[0] -= 1;
					break; // left
				case 3:
					head[1] += 1;
					break; // down
				}
				for (int i = 0; i < BODY_LEN; i++) {
					temp[0] = body[i][0];
					temp[1] = body[i][1];

					body[i][0] = buffer[0];
					body[i][1] = buffer[1];

					buffer[0] = temp[0];
					buffer[1] = temp[1];
				}

				draw(body, head);
				clk.delay(delay);
				Display.update();
				SoundStore.get().poll(0);
			} else{
				LSD.stop();
				LSO.stop();
				JOptionPane.showMessageDialog(null, "Score:\n" + ((BODY_LEN-4)*10000)/delay, "Game Over", JOptionPane.INFORMATION_MESSAGE);
				JOptionPane.showMessageDialog(null, "Song:\n One by Swedish House Mafia (Instrumental)", "Game Over", JOptionPane.INFORMATION_MESSAGE);	
				Display.destroy();
			}

		}
		Display.destroy();

	}

	public static void main(String[] args) throws IOException {
		Scanner in = new Scanner(System.in);
		Main m = new Main();
		m.Username = JOptionPane.showInputDialog("Enter Username:");
		m.delay = Integer.parseInt(JOptionPane.showInputDialog("Set speed: 100 = Fast, 500 = Slow"));
		m.start(800, 800);
	}

	public void draw(int[][] body, int[] head) {
		for (int i = 0; i < body.length; i++)
			mr.setBox(body[i][0], body[i][1], true,false);
		mr.setBox(head[0], head[1], true,false);

	}

	public boolean coosInsideMatrix(int x, int y) {
		return x < 9 && y < 9 && x > -1 && y > -1;
	}


	boolean bodyContains(int x, int y) {
		for (int i = 0; i < BODY_LEN; ++i)
			if (body[i][0] == x && body[i][1] == y)
				return true;
		return false;
	}

	void randomizeCandy() {
		Random r = new Random();
		cx = r.nextInt(9);
		cy = r.nextInt(9);
	}
	boolean isLSDMODE(){
		return LSDMODE;
	}
}
