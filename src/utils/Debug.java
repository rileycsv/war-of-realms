package utils;

/**
 * Custom logging utility to handle debug output.
 * Priority levels:
 * 0: Disabled / Critical only
 * 1: Warnings only
 * 2: Important alerts
 * 3: Low severity / general info
 */
public class Debug {
	private static boolean enabled = true;
	private static int priority = -1;
	private static String lastMessage = "";

	public static void setDebug(boolean state) {
		print(3, state ? "Debug enabled" : "Debug disabled");
		enabled = state;
	}

	/**
     * Enables or disables debugging and sets the minimum priority level.
     * @param state true to enable, false to disable.
     * @param level The verbosity level (1=Warnings, 2=Alerts, 3=All Info).
     */
	public static void setDebug(boolean state, int level) {
		print(3, state ? "Debug enabled, Level: " + level : "Debug disabled");
		enabled = state;
		priority = level;
	}

	public static void setDebugPriority(int level) {
		print(3, "Debug level set: " + level);
		priority = level;
	}

	public static void disable() {
		print(3, "Debug disabled");
		enabled = false;
	}

	public static void enable() {
		print(3, "Debug enabled");
		enabled = true;
	}

	public static boolean isEnabled() {
		return enabled;
	}

	public static void log(String... args) {
		if (enabled) {
			if (args[0].equals(lastMessage)) {
				System.out.print(".");
				return;
			}
			lastMessage = args[0];
			for (String s : args) {
				System.out.println(s);
			}
		}
	}

	public static void log(int l, String... args) {
		if (enabled && priority >= l) {
			log(args);
		}
	}

	public static void print(String... args) {
		if (enabled) {
			if (args[0].equals(lastMessage)) {
				System.out.print(".");
				return;
			}
			lastMessage = args[0];
			for (String s : args) {
				System.out.print(s);
			}
		}
	}

	public static void print(int l, String... args) {
		if (enabled && priority >= l) {
			print(args);
		}
	}

	public static void println(String... args) {
		if (enabled) {
			if (args[0].equals(lastMessage)) {
				System.out.print(".");
				return;
			}
			lastMessage = args[0];
			for (String s : args) {
				System.out.print(s);
			}
			System.out.println();
		}
	}

	public static void println(int l, String... args) {
		if (enabled && priority >= l) {
			println(args);
		}
	}

	public static void printf(String formatString, Object... args) {
		if (enabled) {
			if (formatString.equals(lastMessage)) {
				System.out.print(".");
				return;
			}
			lastMessage = formatString;
			System.out.printf(formatString, args);
		}
	}

	public static void printf(int l, String fS, Object... args) {
		if (enabled && priority >= l) {
			printf(fS, args);
		}
	}

	public static void error(Exception e) {
		if (enabled) {
			e.printStackTrace(System.err);
		}
	}

	public static void error(String... args) {
		if (enabled) {
			for (String s : args) {
				System.err.println(s);
			}
		}
	}
}
