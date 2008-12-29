/*
 * Tigase Jabber/XMPP Server
 * Copyright (C) 2004-2008 "Artur Hefczyc" <artur.hefczyc@tigase.org>
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. Look for COPYING file in the top folder.
 * If not, see http://www.gnu.org/licenses/.
 * 
 * $Rev$
 * Last modified by $Author$
 * $Date$
 */

package tigase.server.sreceiver.sysmon;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Queue;
import java.util.Set;
import tigase.server.Packet;
import tigase.xmpp.StanzaType;

/**
 * Created: Dec 10, 2008 8:37:28 PM
 *
 * @author <a href="mailto:artur.hefczyc@tigase.org">Artur Hefczyc</a>
 * @version $Rev$
 */
public abstract class AbstractMonitor implements ResourceMonitorIfc {

	protected Set<Object> warningsSent = new LinkedHashSet<Object>();
	private String jid = null;
	private SystemMonitorTask smTask = null;
	protected double treshold = 0.9;

	public void init(String jid, double treshold, SystemMonitorTask smTask) {
		this.jid = jid;
		this.treshold = treshold;
		this.smTask = smTask;
		resetWarnings();
	}

	public void resetWarnings() {
		warningsSent = new LinkedHashSet<Object>();
	}

	public void prepareWarning(String text, Queue<Packet> results,
					Object warning) {
		if (!warningsSent.contains(warning)) {
			Packet result = Packet.getMessage("", jid, StanzaType.normal,
							"Warning! High resource usage alert from: " +
							getClass().getSimpleName() + "\n" +
							new Date() + " - " + text,
							"System Monitor Alert", null);
			results.add(result);
			warningsSent.add(warning);
		}
	}

	public void prepareCalmDown(String text, Queue<Packet> results,
					Object warning) {
		if (warningsSent.contains(warning)) {
			Packet result = Packet.getMessage("", jid, StanzaType.normal,
							"Calm down! Resource usage notification from: " +
							getClass().getSimpleName() + "\n" +
							new Date() + " - " + text,
							"System Monitor Alert", null);
			results.add(result);
			warningsSent.remove(warning);
		}
	}

	public void sendWarningOut(String text, Object warning) {
		if (warning == null || !warningsSent.contains(warning)) {
			Packet result = Packet.getMessage("", jid, StanzaType.normal,
							"Warning! High resource usage alert from: " +
							getClass().getSimpleName() + "\n" +
							new Date() + " - " + text,
							"System Monitor Alert", null);
			smTask.sendPacketOut(result);
			if (warning != null) {
				warningsSent.add(warning);
			}
		}
	}

	public int setValueInArr(double[] arr, int idx, double val) {
		arr[idx] = val;
		++idx; idx %= arr.length;
		return idx;
	}

	public void check10Secs(Queue<Packet> results) {
	}
	
	public void check1Day(Queue<Packet> results) {
	}

	public void check1Hour(Queue<Packet> results) {
	}

	public void check1Min(Queue<Packet> results) {
	}

	public String commandsHelp() {
		return "";
	}

	public String runCommand(String[] command) {
		return null;
	}

	public boolean isMonitorCommand(String command) {
		return false;
	}

}