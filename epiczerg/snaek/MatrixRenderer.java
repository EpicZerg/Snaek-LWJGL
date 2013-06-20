package epiczerg.snaek;

import java.util.Random;

import org.lwjgl.opengl.GL11;

public class MatrixRenderer {
	
	int w,h;
	float r,g,b;
	int n = 9;
	int dist = 3;
	Main m;
	public MatrixRenderer(int w, int h,float r, float g, float b, Main m){
		this.w = w;
		this.h = h;
		this.r = r;
		this.g = g;
		this.b = g;
		this.m = m;
	
	}
	public void setBox(int x, int y, boolean state, boolean isCandy){
		Random rn = new Random();
		if(state){
			
			GL11.glColor3f(0,1,0);}
		else{
			
			GL11.glColor3f(r,g,b);}
		
		if(m.isLSDMODE())
			GL11.glColor3f(rn.nextFloat(),rn.nextFloat(),rn.nextFloat());
		
		if(isCandy)
			GL11.glColor3f(1,0,1);

		int wconst = w / n;
		int hconst = h / n;
		
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2i(x*wconst, y*hconst);
		GL11.glVertex2i(x*wconst + wconst-dist, y*hconst);
		GL11.glVertex2i(x*wconst + wconst-dist, y*hconst + hconst-dist);
		GL11.glVertex2i(x*wconst, y*hconst + hconst-dist);
	GL11.glEnd();
	}
	public void init(){
		GL11.glColor3f(r,g,b);
		this.w = w+dist;
		this.h = h+dist;
	}

	
	public void drawScore(int score){
		
		
	}
}
