package com.anshulsharma.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture[] birds;
	//ShapeRenderer shapeRenderer;
	Circle birdCircle;
	Rectangle topPipeRectangle;
	Rectangle bottomPipeRectangle;
	int score=0;
	int scoringTube=0;

	int flapstate=0;
	float birdY=0;
	float velocity=0;
	int gameState=0;
	Texture topPipe;
	Texture bottomPipe;
	int gap=400;
	float maxTubeOffset;
	Random random;
	int numberOfTubes=4;
	float[] tubeOffset=new float[numberOfTubes];
	float[] tubex=new float[numberOfTubes];
	float distanceBetweenTubes;
	int tubeVelocity=4;
	BitmapFont font;
	Texture gameOver;

	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		birds=new Texture[2];
		birds[0]=new Texture("bird.png");
		birds[1]=new Texture("bird2.png");
		topPipe=new Texture("toptube.png");
		bottomPipe=new Texture("bottomtube.png");
		//shapeRenderer=new ShapeRenderer();
		birdCircle=new Circle();
		topPipeRectangle=new Rectangle();
		bottomPipeRectangle=new Rectangle();
		font=new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10);
		gameOver=new Texture("over.png");
		distanceBetweenTubes=Gdx.graphics.getWidth()*3/4;
		maxTubeOffset=Gdx.graphics.getHeight()/2-gap/2-100;
		random=new Random();

		startGame();

	}

	public void startGame(){

		birdY=Gdx.graphics.getHeight()/2-birds[0].getHeight()/2;
		for(int i=0;i<numberOfTubes;i++){

			tubeOffset[i]=(random.nextFloat()-0.5f)*(Gdx.graphics.getHeight()-gap-200);
			tubex[i]=Gdx.graphics.getWidth()/2+Gdx.graphics.getWidth()+i*distanceBetweenTubes;
		}

	}

	@Override
	public void render () {

		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		if(gameState==1) {

			if(tubex[scoringTube]<Gdx.graphics.getWidth()/2){
				score++;
				if(scoringTube<numberOfTubes-1)
					scoringTube++;
				else
					scoringTube=0;

			}

			if(Gdx.input.justTouched()){
				velocity=-30;
			}
			for(int i=0;i<numberOfTubes;i++) {

				if(tubex[i]< -bottomPipe.getWidth()){
					tubex[i]+=numberOfTubes*distanceBetweenTubes;
					tubeOffset[i]=(random.nextFloat()-0.5f)*(Gdx.graphics.getHeight()-gap-200);
				}
				tubex[i]=tubex[i]-tubeVelocity;

				batch.draw(topPipe, tubex[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i]);
				batch.draw(bottomPipe, tubex[i], Gdx.graphics.getHeight() / 2 - bottomPipe.getHeight() - gap / 2 + tubeOffset[i]);

				topPipeRectangle.set(tubex[i],Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i],topPipe.getWidth(),topPipe.getHeight());
				bottomPipeRectangle.set(tubex[i],Gdx.graphics.getHeight() / 2 - bottomPipe.getHeight() - gap / 2 + tubeOffset[i],topPipe.getWidth(),topPipe.getHeight());

				//shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
				//shapeRenderer.setColor(Color.RED);
				//shapeRenderer.rect(topPipeRectangle.x,topPipeRectangle.y,topPipeRectangle.width,topPipeRectangle.height);
				//shapeRenderer.rect(bottomPipeRectangle.x,bottomPipeRectangle.y,bottomPipeRectangle.width,bottomPipeRectangle.height);
				if(Intersector.overlaps(birdCircle,topPipeRectangle) || Intersector.overlaps(birdCircle,bottomPipeRectangle)){

					gameState=2;
				}
				//shapeRenderer.end();

			}

			if(birdY>0) {
				velocity += 2;
				birdY -= velocity;
			}else{
				gameState=2;
			}

		}else if(gameState==0){
			if(Gdx.input.justTouched()){
				gameState=1;
			}
		}else if(gameState==2){
			batch.draw(gameOver,Gdx.graphics.getWidth()/2-gameOver.getWidth()/2,Gdx.graphics.getHeight()/2-gameOver.getHeight()/2);
			if(Gdx.input.justTouched()){

				gameState=1;
				score=0;
				scoringTube=0;
				velocity=0;
				startGame();
			}
		}

		if (flapstate == 0)
			flapstate = 1;
		else
			flapstate = 0;


		batch.draw(birds[flapstate], Gdx.graphics.getWidth() / 2 - birds[flapstate].getWidth() / 2, birdY);
		font.draw(batch,String.valueOf(score),100,200);
		batch.end();

		birdCircle.set(Gdx.graphics.getWidth()/2,birdY+birds[flapstate].getHeight()/2,birds[flapstate].getWidth()/2);
		//shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		//shapeRenderer.setColor(Color.RED);
		//shapeRenderer.circle(birdCircle.x,birdCircle.y,birdCircle.radius);
		//shapeRenderer.end();

	}
	
	@Override
	public void dispose () {
		batch.dispose();
		background.dispose();
	}
}
