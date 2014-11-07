package myutils;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

public abstract class CanvasShell extends Canvas implements Runnable {
	protected static final long serialVersionUID = 1L;

	protected boolean running = false;
	protected long tickCount = 0;
	protected int ticks = 0;
	protected int renders = 0;

	public int WIDTH, HEIGHT, SCALE;
	public float cx, cy;
	protected String TITLE;
	protected final double DEFAULTnsPerTick, DEFAULTnsPerRender;
	protected double nsPerTick, nsPerRender;
	protected double deltaTick = 0;
	protected double deltaRender = 0;
	protected int background = Colors.get(0, 0, 0);

	protected JFrame frame;
	protected Graphics graphics;
	protected BufferStrategy bufferStrategy;
	protected BufferedImage image;
	public Pixels screen;
	protected int x, y, xPos, yPos;

	public KeyHandler keyHandler;
	public MouseHandler mouseHandler;

	public CanvasShell(int width, int height, int scale, String title, double nsPerTick, double nsPerRender) {
		WIDTH = width / scale;
		HEIGHT = height / scale;
		cx = WIDTH / 2f;
		cy = HEIGHT / 2f;
		SCALE = scale;
		TITLE = title;
		this.DEFAULTnsPerTick = nsPerTick;
		this.DEFAULTnsPerRender = nsPerRender;
		this.nsPerTick = DEFAULTnsPerTick;
		this.nsPerRender = DEFAULTnsPerRender;

		setMinimumSize(new Dimension(WIDTH * SCALE / 2, HEIGHT * SCALE / 2));
		setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		setMaximumSize(new Dimension(WIDTH * SCALE * 2, HEIGHT * SCALE * 2));

		frame = new JFrame(TITLE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.add(this, BorderLayout.CENTER);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);

		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		screen = new Pixels(((DataBufferInt) image.getRaster().getDataBuffer()).getData(), this);

		createBufferStrategy(3);
		bufferStrategy = getBufferStrategy();
		graphics = bufferStrategy.getDrawGraphics();

		keyHandler = new KeyHandler(this);
		mouseHandler = new MouseHandler(this);
	}

	@Override
	public void run() {
		init();
		long last = System.nanoTime();
		long current;
		long debugTimer = System.currentTimeMillis();
		boolean shouldRender = false;
		while (running) {
			current = System.nanoTime();
			deltaTick += (current - last) / nsPerTick;
			deltaRender += (current - last) / nsPerRender;
			last = current;
			if (deltaTick > 1) {
				tick();
				ticks++;
				deltaTick -= 1;
				shouldRender = true;
			}
			if ((deltaRender > 1) && (shouldRender)) {
				render();
				renders++;
				deltaRender -= 1;
				shouldRender = false;
			}
			if (System.currentTimeMillis() - debugTimer >= 1000) {
				debugInfo();
				debugTimer += 1000;
			}
		}
		System.exit(0);
	}

	protected abstract void init();

	protected void tick() {
		tickCount++;
		defaultKeyHandling();
		defaultMouseHandling();
		myTick();
	}

	protected void render() {
		myRender();

		graphics.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		bufferStrategy.show();
	}

	protected void debugInfo() {
		System.out.println(ticks + " TPS, " + renders + "FPS");
		myDebugInfo();
		ticks = renders = 0;
	}

	protected abstract void myTick();

	protected abstract void myRender();

	protected abstract void myDebugInfo();

	private void defaultKeyHandling() {
		if (keyHandler.esc.pressed) {
			stop();
		}
		if (keyHandler.up.pressed) {
			screen.scrollY(-1);
		}
		if (keyHandler.down.pressed) {
			screen.scrollY(1);
		}
		if (keyHandler.left.pressed) {
			screen.scrollX(-1);
		}
		if (keyHandler.right.pressed) {
			screen.scrollX(1);
		}
		if (keyHandler.pageup.pressed) {
			screen.zoom(1);
		}
		if (keyHandler.pagedown.pressed) {
			screen.zoom(-1);
		}
		if (keyHandler.equal.pressed) {
			screen.resetOffsetScale();
			resetnsPerTickRender();
		}
		myKeyHandling();
	}

	protected void resetnsPerTickRender() {
		nsPerTick=DEFAULTnsPerTick;
		nsPerRender=DEFAULTnsPerRender;		
	}

	private void defaultMouseHandling() {
		if (mouseHandler.right.clicked) {
			screen.setOffset(mouseHandler.right.locationRelativeScaled);
			mouseHandler.right.clicked = false;
		}
		if (mouseHandler.amountScrolled != 0) {
			screen.zoom(mouseHandler.amountScrolled);
			mouseHandler.amountScrolled = 0;
		}
		myMouseHandling();
	}

	protected abstract void myKeyHandling();

	protected abstract void myMouseHandling();

	public synchronized void start() {
		System.out.println("CanvasShell start");
		running = true;
		new Thread(this, TITLE + "-Thread").start();
	}

	public void stop() {
		System.out.println("CanvasShell stop");
		running = false;
	}

}
