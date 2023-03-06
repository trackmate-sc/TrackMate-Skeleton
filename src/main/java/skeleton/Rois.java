/*-
 * #%L
 * TrackMate detector based on Skeletonization.
 * %%
 * Copyright (C) 2022 - 2023 TrackMate developers.
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

import ij.gui.Roi;
import net.imglib2.FinalInterval;

import java.awt.*;
import java.util.ArrayList;

public abstract class Rois {
	public static ArrayList<FinalInterval> asIntervals(Roi[] rois) {
		final ArrayList<FinalInterval> intervals = new ArrayList<>();
		for (Roi roi : rois) {
			final Rectangle bounds = roi.getBounds();

			intervals.add(new FinalInterval(new long[] { bounds.x, bounds.y },
					new long[] { bounds.x + bounds.width - 1, bounds.y + bounds.height - 1 }));
		}
		return intervals;
	}
}
