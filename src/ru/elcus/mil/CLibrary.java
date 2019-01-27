package ru.elcus.mil;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;

public class CLibrary {
	
	static CLibraryInterface INSTANCE = (CLibraryInterface) Native.load((Platform.isWindows() ? "msvcrt" : "c"), CLibraryInterface.class);
	public static short POLLIN = 0x0001;

	interface CLibraryInterface extends Library {
		int open(String pathname, int flags);

		int close(int fd);

		int read(int fd, byte[] data, NativeLong len);

		int write(int fd, byte[] data, NativeLong len);

		int ioctl(int fd, int cmd, int p);
		
		int ioctl(int fd, int cmd, Pointer p);
		
		int ioctl(int fd, int cmd, int[] p);

		int ioctl(int fd, int cmd, byte[] p);

		//int poll(pollfd[] fds, int nfds, int timeout);

		public int pipe(int[] fds);
	}
/*
	static public class pollfd extends Structure {

		public static class ByReference extends pollfd implements Structure.ByReference {
		}

		public int fd;
		public short events;
		public short revents;

		@Override
		protected List getFieldOrder() {
			return Arrays.asList(//
					"fd",//
					"events",//
					"revents"//
			);
		}

		public pollfd() {
		}

		public pollfd(int fd, short events, short revents) {
			this.fd = fd;
			this.events = events;
			this.revents = revents;
		}
	}
*/
	public static int open(String pathname, int flags) {
		return INSTANCE.open(pathname, flags);
	}

	public static void close(int fd) {
		INSTANCE.close(fd);
	}

	public static int ioctl(int fd, int cmd, int[] p) {
		return INSTANCE.ioctl(fd, cmd, p);
	}

	public static int ioctl(int fd, int cmd, byte[] p) {
		return INSTANCE.ioctl(fd, cmd, p);
	}


	public static int read(int fd, byte[] buffer, int len) {
		return INSTANCE.read(fd, buffer, new NativeLong(len));
	}

	public static int write(int fd, byte[] buffer, int len) {
		return INSTANCE.write(fd, buffer, new NativeLong(len));
	}

	//public static int poll(pollfd fds[], int nfds, int timeout) {
	//	return INSTANCE.poll(fds, nfds, timeout);
	//}

	public static int pipe(int[] fds) {
		return INSTANCE.pipe(fds);
	}

}
