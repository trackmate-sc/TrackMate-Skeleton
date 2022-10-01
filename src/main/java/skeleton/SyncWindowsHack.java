/*-
 * #%L
 * TrackMate detector based on Skeletonization.
 * %%
 * Copyright (C) 2022 TrackMate developers.
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

import ij.plugin.frame.SyncWindows;

import java.lang.reflect.Method;
import java.util.Vector;
import java.util.concurrent.ExecutorService;

public class SyncWindowsHack extends SyncWindows {

	public SyncWindowsHack() {
		super();
	}

	public void syncAll() {

		if (wList == null)
			return;
		// Select all items on list.
		Vector v = new Vector();
		Integer I;
		for (int i = 0; i < wList.getItemCount(); ++i) {
			wList.select(i);
			I = (Integer) vListMap.elementAt(i);
			v.addElement(I);
		}

		Method m = null;
		try {
			m = SyncWindows.class.getDeclaredMethod("addWindows", Vector.class);
			m.setAccessible(true); // bypasses the private modifier
			m.invoke(this, v);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
