package com.mirre.heist.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;

public class ChainedTextButton {

	private TextButton textButton;
	private Skin skin = new Skin();
	private TextButtonStyle textButtonStyle = new TextButtonStyle();
	private String text;
	
	public ChainedTextButton(String text){
		setText(text);
	}
	
	public ChainedTextButton addFont(float scaleX, float scaleY, Color color){
		Pixmap pixmap = new Pixmap(1,1, Format.RGBA8888);
		pixmap.setColor(color);
		pixmap.fill();
		
		getSkin().add("white", new Texture(pixmap));
		BitmapFont font = new BitmapFont();
		font.setScale(scaleX, scaleY);
		getSkin().add("default", font);
		return this;
	}
	
	public ChainedTextButton styleUp(Color color){
		getTextButtonStyle().up = getSkin().newDrawable("white", color);
		return this;
	}
	
	public ChainedTextButton styleDown(Color color){
		getTextButtonStyle().down = getSkin().newDrawable("white", color);
		return this;
	}
	
	public ChainedTextButton styleChecked(Color color){
		getTextButtonStyle().checked = getSkin().newDrawable("white", color);
		return this;
	}
	
	public ChainedTextButton styleOver(Color color){
		getTextButtonStyle().over = getSkin().newDrawable("white", color);
		return this;
	}
	
	
	public TextButton create(){
		getTextButtonStyle().font = getSkin().getFont("default");
		getSkin().add("default", getTextButtonStyle());
		TextButton button = new TextButton(getText(), getSkin());
		return button;
	}
	

	public TextButton getTextButton() {
		return textButton;
	}

	public void setTextButton(TextButton textButton) {
		this.textButton = textButton;
	}

	public Skin getSkin() {
		return skin;
	}

	public void setSkin(Skin skin) {
		this.skin = skin;
	}

	public TextButtonStyle getTextButtonStyle() {
		return textButtonStyle;
	}

	public void setTextButtonStyle(TextButtonStyle textButtonStyle) {
		this.textButtonStyle = textButtonStyle;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}


//BitmapFont font = new BitmapFont();
//font.setScale(2F, 2F);

//LabelStyle labelStyle = new LabelStyle(font, Color.OLIVE);
//Label heading = new Label("Circle's Heist", labelStyle);
//heading.setX(getStage().getWidth()/2 - heading.getWidth()/2);
//heading.setY(getStage().getHeight()/3 * 2);

//getStage().addActor(heading);