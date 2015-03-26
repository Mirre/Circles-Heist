package com.mirre.heist.objects.blocks;

import com.badlogic.gdx.graphics.Color;
import com.mirre.heist.objects.core.PixelObject;

public class Air extends PixelObject {

	public Air(){}
	
	public Air(int x, int y) {
		super(x, y, 1, 1);
	}

	@Override
	public Color getColor() {
		return Color.BLACK;
	}

}
