package myutils;

public class Colors {
	public static class RGB {
		public int r, g, b;

		public RGB() {
			r = g = b = 0;
		}

		public void add(RGB rgb) {
			r += rgb.r;
			g += rgb.g;
			b += rgb.b;
		}

		public void subtract(RGB rgb) {
			r -= rgb.r;
			g -= rgb.g;
			b -= rgb.b;
		}
	}

	private static int get(double d) {
		return (int) Math.round(d * 255) & 255;
	}

	public static int get(double r, double g, double b) {
		// System.out.println("r:"+r+", g:"+g+", b:"+b+", res:"+(int)(get(r) <<
		// 16 | get(g) << 8 | get(b)));
		return get(r) << 16 | get(g) << 8 | get(b);
	}

	public static void decode(int rawCode, RGB rgbCode) {
		rgbCode.r = (rawCode >> 16) & 0xFF;
		rgbCode.g = (rawCode >> 8) & 0xFF;
		rgbCode.b = rawCode & 0xFF;
	}

	public static RGB decode(int rawCode) {
		RGB rgbCode = new RGB();
		rgbCode.r = (rawCode >> 16) & 0xFF;
		rgbCode.g = (rawCode >> 8) & 0xFF;
		rgbCode.b = rawCode & 0xFF;
		return rgbCode;
	}
}
