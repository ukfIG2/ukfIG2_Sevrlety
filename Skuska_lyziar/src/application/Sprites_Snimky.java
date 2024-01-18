package application;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Sprites_Snimky extends ImageView{
	private Game game;
	private Image[] images;
	public int smer = 0;
	public int Aktualny_Snimok = 0;
	private double  sirkaS = 142.0, vyskaS = 152.0;
	
	public Sprites_Snimky(String nazov, double polohaX, double polohaY, Game game ) {
		super(); this.game=game;
		images = new Image[6];
		for(int i=1; i<=6; i++) {
			images[i-1] = new Image("0" + i + nazov, polohaX, polohaY, false, false);
		}
		setImage(images[0]);
		setLayoutX(polohaX);
		setLayoutY(polohaY);
		
	}
	
	public void Dalsi_Snimok() {
		if (smer == 1) Aktualny_Snimok = 							 4-1;	//doprava
		else if (smer == 2) Aktualny_Snimok = 							 5-1;	//dolava
		else if (smer == 3) Aktualny_Snimok =                             6-1;	//brzdi
		else if (smer == 0) Aktualny_Snimok =     (Aktualny_Snimok + 1) % 3;	//dole
	}
	
	private void Vykresli() {
		Dalsi_Snimok();
		setImage(images[Aktualny_Snimok]);
	}
	
	public boolean Kolizia(Sprites_Snimky iny_snimok) {
		double d1 = getLayoutX() - iny_snimok.getLayoutX();
		double d2 = getLayoutY() - iny_snimok.getLayoutY();
		if((Math.abs(d1)<getWidth()) && Math.abs(d2)< getHeight()) {
			return true;
		}
		else {return false;}
	}
	
	public double getWidth() {
		return sirkaS;
	}
	
	public double getHeight() {
		return vyskaS;
	}
	
	public void dole(double delta, double maxY) {
		setLayoutY(getLayoutY() + delta);
		if (getLayoutY() > maxY - vyskaS) {
			setLayoutY(maxY - vyskaS);
		}
		smer = 0;
		Vykresli();
	}
	
	public void dolava(double delta, double maxX) {
		setLayoutX(getLayoutX() - delta);
		if (getLayoutX() < 0) {
			setLayoutX(0);
		}
		smer = 2;
		Vykresli();
	}
	
	public void doprava(double delta, double maxX) {
		setLayoutX(getLayoutX() + delta);
		if (getLayoutX() > maxX - sirkaS) {
			setLayoutX(maxX - sirkaS);
		}
		smer = 1;
		Vykresli();
	}
	
	public void brzdi(double delta, double maxX) {		
		smer = 3;
		Vykresli();
	}
}
