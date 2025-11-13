// BV Ue3 WS2025/26 Vorgabe
//
// Copyright (C) 2025 by Klaus Jung
// All rights reserved.
// Date: 2025-09-29
 		   		   	 	

package bv_ws2526;

public class MorphologicFilter {
 		   		   	 	
	// filter implementations go here:
	
	public void copy(RasterImage src, RasterImage dst) {
		System.arraycopy(src.argb, 0, dst.argb, 0, src.argb.length);
	}
	
	public void dilation(RasterImage src, RasterImage dst, boolean[][] kernel) {
		// kernel's first dimension: y (row), second dimension: x (column)
		// TODO: dilate the image using the given kernel
		int width = src.width;
		int height = src.height;
		int[] srcPixels = src.argb;
		int[] dstPixels = dst.argb;

		int kHeight = kernel.length;
		int kWidth = kernel[0].length;
		int kCenterY = kHeight / 2;
		int kCenterX = kWidth / 2;

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				boolean shouldBeWhite = false;

				for (int ky = 0; ky < kHeight; ky++) {
					for (int kx = 0; kx < kWidth; kx++) {
						if (!kernel[ky][kx]) continue;

						int nx = x + kx - kCenterX;
						int ny = y + ky - kCenterY;

						if (nx >= 0 && nx < width && ny >= 0 && ny < height) {
							int neighborPixel = srcPixels[ny * width + nx];
							if ((neighborPixel & 0xFFFFFF) == 0xFFFFFF) {
								shouldBeWhite = true;
								break;
							}
						}
					}
					if (shouldBeWhite) break;
				}

				dstPixels[y * width + x] = shouldBeWhite ? 0xFFFFFFFF : 0xFF000000;
			}
		}

	}
 		   		   	 	
	public void erosion(RasterImage src, RasterImage dst, boolean[][] kernel) {
		// This is already implemented. Nothing to do.
		// It will function once you implemented dilation and RasterImage invert()
		src.invert();
		dilation(src, dst, kernel);
		dst.invert();
		src.invert();
	}
	

	public void opening(RasterImage src, RasterImage dst, boolean[][] kernel) {
		RasterImage temp1 = new RasterImage(src.width, src.height);
		RasterImage temp2 = new RasterImage(src.width, src.height);

		// 1️⃣ invertiere Eingangsbild, um korrekte Dualität zu wahren
		src.invert();

		// 2️⃣ führe die eigentliche "Opening"-Reihenfolge durch (Erosion -> Dilatation)
		erosion(src, temp1, kernel);
		dilation(temp1, temp2, kernel);

		// 3️⃣ wieder zurück-invertieren
		temp2.invert();

		// 4️⃣ Ergebnis kopieren
		System.arraycopy(temp2.argb, 0, dst.argb, 0, temp2.argb.length);

		// 5️⃣ Ursprungsbild wieder zurücksetzen
		src.invert();
	}

	public void closing(RasterImage src, RasterImage dst, boolean[][] kernel) {
		// TODO: implement closing by using dilation() and erosion()

			RasterImage temp1 = new RasterImage(src.width, src.height);
        RasterImage temp2 = new RasterImage(src.width, src.height);

        // 1 führe die eigentliche "Closing"-Reihenfolge durch (Dilatation -> Erosion)
        dilation(src, temp1, kernel);
        erosion(temp1, temp2, kernel);

        // 2 Ergebnis kopieren
        System.arraycopy(temp2.argb, 0, dst.argb, 0, temp2.argb.length);
	}
	

}
 		   		   	 	




