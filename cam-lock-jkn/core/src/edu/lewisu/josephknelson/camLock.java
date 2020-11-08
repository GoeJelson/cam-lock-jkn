package edu.lewisu.josephknelson;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class camLock extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img, background;
	float imgX, imgY;
	float imgWidth, imgHeight, boxWidth, boxHeight;
	float WIDTH, HEIGHT;
	OrthographicCamera cam;
	float WORLDMINX, WORLDMAXX, WORLDMINY, WORLDMAXY;
	boolean isLocked = false;
	float lockPointX, lockPointY;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		background = new Texture("image.jpg");
		WIDTH = Gdx.graphics.getWidth();
		HEIGHT = Gdx.graphics.getHeight();
		imgX = 0;
		imgY = 0;
		imgWidth = img.getWidth();
		imgHeight = img.getHeight();
		WORLDMAXX = 2000;
		WORLDMAXY = 2000;
		WORLDMINX = 0;
		WORLDMINY = 0;
		cam = new OrthographicCamera(WIDTH, HEIGHT);
		cam.translate(0, 0);
		cam.update();
		batch.setProjectionMatrix(cam.combined);
		System.out.println(cam.position.x + " " + cam.position.y);
	}

	public void handleInput() {
		if(Gdx.input.isKeyPressed(Keys.A)){
			imgX-=10;
		}
		if(Gdx.input.isKeyPressed(Keys.D)){
			imgX+=10;
		}
		if(Gdx.input.isKeyPressed(Keys.W)){
			imgY+=10;
		}
		if(Gdx.input.isKeyPressed(Keys.S)){
			imgY-=10;
		}
		if(Gdx.input.isKeyPressed(Keys.J)){
			lockPointX = imgX;
			lockPointY = imgY;
			isLocked = true;
		}
		if(Gdx.input.isKeyPressed(Keys.U)){
			isLocked = false;
		}
	}

	public void lockedIn(float lockPointX, float lockPointY) {
		System.out.println(lockPointX + " " + lockPointY);
		if (imgX > imgWidth/4 + lockPointX) {
			imgX = imgWidth/4 + lockPointX;
		} else if (imgX < lockPointX - imgWidth/4 ) {
		 	imgX = lockPointX -imgWidth/4 ;
		}
		if (imgY > lockPointY + imgHeight/4) {
			imgY = imgHeight/4 + lockPointY;
		} else if (imgY < lockPointY - imgHeight/4) {
			imgY = lockPointY - imgHeight/4;
		}	}	
	
	

	public void lockCoordinates( float targetHeight) {
		// if (imgX > targetWidth - imgWidth) {
		// 	imgX = targetWidth - imgWidth;
		// } else if (imgX < 0 ) {
		// 	imgX = 0;
		// }
		if (imgY > targetHeight - imgHeight) {
			imgY = targetHeight - imgHeight;
		} else if (imgY < 0) {
			imgY = 0;
		}	}
	public void lockCoordinates() {
		lockCoordinates();
	}

	public void wrapCoordinates(float targetWidth) {
		if (imgX > targetWidth) {
			imgX = -imgWidth;
		} else if (imgX < -imgWidth ) {
			imgX = targetWidth;
		}
		if (imgX < targetWidth) {
			imgX = +imgWidth;
		} else if (imgY < +imgWidth) {
			imgX = targetWidth;
		}
		// if (imgY > targetHeight) {
		// 	imgY = -imgHeight;
		// } else if (imgY < -imgHeight) {
		// 	imgY = targetHeight;
		// }	
	}
	public void wrapCoordinates() {
		wrapCoordinates(WIDTH);
	}

	public Vector2 getViewPortOrigin() {
		return new Vector2(cam.position.x-WIDTH/2, cam.position.y - HEIGHT/2);
	}

	public Vector2 getScreenCoordinates() {
		Vector2 viewportOrigin = getViewPortOrigin();
		return new Vector2(imgX-viewportOrigin.x, imgY-viewportOrigin.y);
	}

    public void panCoordinates(float border) {
        Vector2 screenPos = getScreenCoordinates();
        if (screenPos.x > WIDTH - imgWidth - border) {
            if (imgX + imgWidth > WORLDMAXX - border) {
				imgX = 0;
				cam.position.x = 0;
				System.out.println(cam.position.x);
                cam.update();
                batch.setProjectionMatrix(cam.combined);
            } else {
                cam.position.x = cam.position.x + screenPos.x - WIDTH + imgWidth + border;
                System.out.println(cam.position.x);
                cam.update();
                batch.setProjectionMatrix(cam.combined);
            }
        } 
        if (screenPos.x < border) { 
			if (imgX < 0 + border) {
				imgX = WORLDMAXX - imgWidth;
				cam.position.x = WORLDMAXX - WIDTH;
				System.out.println(cam.position.x);
				cam.update();
				batch.setProjectionMatrix(cam.combined);
			} else {
				cam.position.x = cam.position.x - (border - screenPos.x);
				System.out.println(cam.position.x);
				cam.update();
				batch.setProjectionMatrix(cam.combined);
			}
        }
        if (screenPos.y > HEIGHT - imgHeight - border) { 
            if (imgY + imgHeight > WORLDMAXY - border) { 
                lockCoordinates(WORLDMAXY);
            } else { 
                cam.position.y = cam.position.y + screenPos.y - HEIGHT + imgHeight + border;
                System.out.println(cam.position.y);
                cam.update();
                batch.setProjectionMatrix(cam.combined);
            }
        }
        if (screenPos.y < border) {
			if (imgY < 0){
				imgY = 0;
			} else {
				cam.position.y = cam.position.y - (border - screenPos.y);
				System.out.println(cam.position.y);
				cam.update();
				batch.setProjectionMatrix(cam.combined);
			}
        }
    }


	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		handleInput();
		panCoordinates(20);
		if (isLocked == true) {
			lockedIn(lockPointX, lockPointY);
		}
		batch.begin();
		batch.draw(background, 0 , 0);
		batch.draw(img, imgX, imgY);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
