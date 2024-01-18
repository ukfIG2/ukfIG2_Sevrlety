package application;

import javafx.scene.Group;

public class Game extends Group{
	private int sirka, vyska;
	private String input = "";
	private Sprites_Snimky Hrac;
	MyTimer timer = new MyTimer(this);
	public double rychlost_hraca = 50;
	
	public Game(int sirka, int vyska) {
		this.sirka = sirka;
		this.vyska = vyska;

        Hrac = new Sprites_Snimky("-removebg-preview.png", 100, 100, this);
        getChildren().add(Hrac);
        
    	setFocusTraversable(true);
		setFocused(true);
		
        setOnKeyPressed(evt -> input = evt.getCode().toString());
	}

	public void update(double deltaTime) {
		Hrac.dole(deltaTime / 1000000000 * rychlost_hraca, vyska);
		switch (input) {
		case "DOWN": 
			rychlost_hraca+=5;    break;
		case "RIGHT":
			Hrac.doprava(deltaTime / 1000000000 * rychlost_hraca*5, sirka); System.out.println("doprava"); break;
		case "LEFT":
			Hrac.dolava(deltaTime / 1000000000 * rychlost_hraca*10, sirka); System.out.println("dolava"); break;
		case "UP":
			Hrac.brzdi(deltaTime, deltaTime) ;
			rychlost_hraca-=15;
			if (rychlost_hraca < 0 ){
                rychlost_hraca = 0;
            }
			break;
		}
		input = "";
		//Kontrola Smeru
		System.out.println(input);
		System.out.println(Hrac.smer);
		System.out.println(Hrac.Aktualny_Snimok);
		

		}
	}

