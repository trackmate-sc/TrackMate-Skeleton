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

import net.imglib2.roi.labeling.LabelRegion;

public class RegionAndSize implements Comparable<RegionAndSize> {
	final private LabelRegion region;
	final private Long size;

	public RegionAndSize(LabelRegion region, Long size) {
		this.region = region;
		this.size = size;
	}

	public LabelRegion getRegion() {
		return region;
	}

	public Long getSize() {
		return size;
	}

	public int compareTo(RegionAndSize r) {
		return ((int) (r.size - this.size));
	}
}
