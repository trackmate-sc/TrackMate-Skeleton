/*-
 * #%L
 * TrackMate detector based on Skeletonization.
 * %%
 * Copyright (C) 2022 - 2024 TrackMate developers.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package skeleton;

import ij.IJ;

import java.io.File;
import java.util.concurrent.atomic.AtomicInteger;

public class Logger {
	public static void debug(String s) {
		// IJ.log( s );
	}

	public static void log(String message) {
		IJ.log(message);
		System.out.println(message);

		if (Utils.logFilePath != null) {
			File logFile = new File(Utils.logFilePath);

			if (!logFile.exists()) {
				Utils.createLogFile();
			} else {
				Utils.writeToLogFile(message + "\n");
			}
		}

	}

	public static void error(String s) {
		IJ.showMessage(s);
		System.err.println(s);
	}

	public static void progress(final long total, final int count, final long startTimeMillis, String msg) {
		double secondsSpent = (1.0 * System.currentTimeMillis() - startTimeMillis) / (1000.0);
		double secondsPerTask = secondsSpent / count;
		double secondsLeft = (total - count) * secondsPerTask;

		String unit = "s";
		double divisor = 1;

		if (secondsSpent > 3 * 60) {
			unit = "min";
			divisor = 60;
		}

		Logger.progress(msg,
				"" + count + "/" + total + "; time ( spent, left, task ) [ " + unit + " ]: "
						+ (int) (secondsSpent / divisor) + ", " + (int) (secondsLeft / divisor) + ", "
						+ String.format("%.3g", secondsPerTask / divisor) + "; memory: " + IJ.freeMemory());
	}

	public static void progress(String msg, String progress) {
		progress = msg + ": " + progress;

		if (IJ.getLog() != null) {
			String[] logs = IJ.getLog().split("\n");
			if (logs.length > 1) {
				if (logs[logs.length - 1].contains(msg)) {
					progress = "\\Update:" + progress;
				}
			}
		}

		IJ.log(progress);
		System.out.println(progress);
	}

}
