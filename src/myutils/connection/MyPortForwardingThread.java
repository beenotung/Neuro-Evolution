package myutils.connection;

import javax.swing.JOptionPane;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UIKeyboardInteractive;
import com.jcraft.jsch.UserInfo;

/**
 * @author beenotung
 */
public class MyPortForwardingThread implements Runnable {
	private String host;
	private String user;
	private String password;
	private int lport;
	private String rhost;
	private int rport;
	private int assinged_port;

	private Thread thread;
	private boolean alive = false;

	public MyPortForwardingThread(String host, String user, String password,
			int lport, String rhost, int rport) {
		this.host = host;
		this.user = user;
		this.password = password;
		this.lport = lport;
		this.rhost = rhost;
		this.rport = rport;
	}

	public MyPortForwardingThread(MySSHInfo mySSHInfoForm,
			MyPortforwardInfo myPortforwardInfoForm) {
		this.host = mySSHInfoForm.getHost();
		this.user = mySSHInfoForm.getUsername();
		this.password = mySSHInfoForm.getPassword();
		this.lport = myPortforwardInfoForm.getLocalPort();
		this.rhost = myPortforwardInfoForm.getRemoteHost();
		this.rport = myPortforwardInfoForm.getRemotePort();
	}

	private void connect() {
		try {
			JSch jsch = new JSch();
			Session session = jsch.getSession(user, host, 22);

			// username and password will be given via UserInfo interface.
			UserInfo ui = new MyUserInfo(password);

			session.setUserInfo(ui);

			session.connect();

			// Channel channel=session.openChannel("shell");
			// channel.connect();

			assinged_port = session.setPortForwardingL(lport, rhost, rport);
		} catch (Exception e) {
			// TODO [beenotung] Throw Exception when SSH tunneling failed
		}
	}

	private static class MyUserInfo implements UserInfo, UIKeyboardInteractive {
		private String password;

		public MyUserInfo(String password) {
			this.password = password;
		}

		@Override
		public String getPassword() {
			return password;
		}

		@Override
		public boolean promptYesNo(String str) {
			return true;
		}

		@Override
		public String getPassphrase() {
			return null;
		}

		@Override
		public boolean promptPassphrase(String message) {
			return true;
		}

		@Override
		public boolean promptPassword(String message) {
			return true;
		}

		@Override
		public void showMessage(String message) {
			JOptionPane.showMessageDialog(null, message);
		}

		@Override
		public String[] promptKeyboardInteractive(String destination, String name,
				String instruction, String[] prompt, boolean[] echo) {
			return null;
		}
	}

	/** thread staff **/
	@SuppressWarnings("static-access")
	@Override
	public void run() {
		connect();
		while (alive) {
			try {
				thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}
	}

	public void start() {
		if (thread == null)
			thread = new Thread(this);
		alive = true;
		thread.start();
	}

	public void stop() {
		alive = false;
	}

}
