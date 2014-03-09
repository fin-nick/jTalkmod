/*
 * Copyright (C) 2012, Igor Ustyugov <igor@ustyugov.net>
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see http://www.gnu.org/licenses/
 */

package net.ustyugov.jtalk.adapter;

import java.util.Enumeration;

import android.app.Activity;
import net.ustyugov.jtalk.Colors;
import net.ustyugov.jtalk.IconPicker;
import net.ustyugov.jtalk.RosterItem;
import net.ustyugov.jtalk.db.AccountDbHelper;
import net.ustyugov.jtalk.db.JTalkProvider;
import net.ustyugov.jtalk.service.JTalkService;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.RosterPacket;
import org.jivesoftware.smack.util.StringUtils;

import com.jtalkmod.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class OpenChatsAdapter extends ArrayAdapter<RosterItem> {
	private JTalkService service;
	private boolean isFragment;
    private Activity activity;
    private SharedPreferences prefs;
    private IconPicker ip;
	
	public OpenChatsAdapter(Activity activity, boolean isFragment) {
		super(activity, R.id.name);
        this.activity = activity;
        this.service = JTalkService.getInstance();
        this.isFragment = isFragment;
        this.prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        this.ip = service.getIconPicker();
    }
	
	public void update() {
		clear();
		add(null);

		Cursor cursor = activity.getContentResolver().query(JTalkProvider.ACCOUNT_URI, null, AccountDbHelper.ENABLED + " = '" + 1 + "'", null, null);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			do {
				String account = cursor.getString(cursor.getColumnIndex(AccountDbHelper.JID)).trim();
				XMPPConnection connection = service.getConnection(account);

                for(String jid : service.getActiveChats(account)) {
                    if (!service.getConferencesHash(account).containsKey(jid)) {
                        Roster roster = service.getRoster(account);
                        RosterEntry entry = null;
                        if (roster != null) entry = roster.getEntry(jid);
                        if (entry == null) entry = new RosterEntry(jid, jid, RosterPacket.ItemType.both, RosterPacket.ItemStatus.SUBSCRIPTION_PENDING, roster, connection);
                        RosterItem item = new RosterItem(account, RosterItem.Type.entry, entry);
                        add(item);
                    }
                }

				Enumeration<String> groupEnum = service.getConferencesHash(account).keys();
				while(groupEnum.hasMoreElements()) {
					String name = groupEnum.nextElement();
					RosterItem item = new RosterItem(account, RosterItem.Type.muc, null);
					item.setName(name);
					add(item);
				}
			} while (cursor.moveToNext());
			cursor.close();
		}
	}
	
	@Override
	public View getView(int position, View v, ViewGroup parent) {
		boolean minimal = prefs.getBoolean("CompactMode", true);

        int fontSize = 14;
		try {
			fontSize = Integer.parseInt(prefs.getString("RosterSize", activity.getResources().getString(R.string.DefaultFontSize)));
		} catch (NumberFormatException ignored) { }

        if (v == null) {
            LayoutInflater vi = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.entry, null);
        }

        if (position == 0) {
			TextView counter = (TextView) v.findViewById(R.id.msg_counter);
			counter.setVisibility(View.GONE);
			ImageView msg  = (ImageView) v.findViewById(R.id.msg);
			msg.setVisibility(View.GONE);

			ImageView icon = (ImageView)v.findViewById(R.id.status_icon);
	      	if (minimal && activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
	      		icon.setVisibility(View.GONE);
	      	} else {
	      		icon.setVisibility(View.VISIBLE);
	      		icon.setImageBitmap(ip.getMsgBitmap());
	      	}

			TextView label = (TextView) v.findViewById(R.id.name);
			label.setTypeface(Typeface.DEFAULT_BOLD);
	       	label.setTextSize(fontSize);
	        label.setText(activity.getString(R.string.Chats) + ": " + (getCount()-1));
            label.setTextColor(Colors.PRIMARY_TEXT);
            v.setBackgroundColor(Colors.GROUP_BACKGROUND);
        } else {
        	RosterItem ri = getItem(position);
            String account = ri.getAccount();
            String jid = "";
            if (ri.isEntry() || ri.isSelf()) jid = ri.getEntry().getUser();
            else if (ri.isMuc()) jid = ri.getName();
            String name = ri.getName();

        	if (service.getJoinedConferences().containsKey(jid)) {
            	name = StringUtils.parseName(jid);
            } else if (service.getJoinedConferences().containsKey(StringUtils.parseBareAddress(jid))) {
            	name = StringUtils.parseResource(jid);
            } else {
            	RosterEntry re = ri.getEntry();
                if (re != null) name = re.getName();
                if (name == null || name.equals("")) name = jid;
            }

            TextView label = (TextView) v.findViewById(R.id.name);
           	label.setTextSize(fontSize);
            label.setText(name);
           	if (service.getComposeList().contains(jid)) label.setTextColor(Colors.HIGHLIGHT_TEXT);
           	else if (service.isHighlight(account, jid)) label.setTextColor(Colors.HIGHLIGHT_TEXT);
    		else label.setTextColor(Colors.PRIMARY_TEXT);

            ImageView icon = (ImageView)v.findViewById(R.id.status_icon);
            if (minimal && activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT && !isFragment) {
            	icon.setVisibility(View.GONE);
            } else {
            	icon.setVisibility(View.VISIBLE);
            	if (service.getJoinedConferences().containsKey(jid)) {
                	icon.setImageBitmap(ip.getMucBitmap());
                } else {
                	Presence presence = service.getPresence(ri.getAccount(), jid);
                	icon.setImageBitmap(ip.getIconByPresence(presence));
                }
            }

            ImageView msg = (ImageView) v.findViewById(R.id.msg);
            if (ip != null) msg.setImageBitmap(ip.getMsgBitmap());

            TextView counter = (TextView) v.findViewById(R.id.msg_counter);
    		counter.setTextSize(fontSize);
            int count = service.getMessagesCount(account, jid);
    		if (count > 0) {
    			msg.setVisibility(View.VISIBLE);
    			counter.setVisibility(View.VISIBLE);
    			counter.setText(count+"");
    		} else {
    			msg.setVisibility(View.GONE);
    			counter.setVisibility(View.GONE);
    		}

            if (jid.equals(service.getCurrentJid())) {
            	label.setTypeface(Typeface.DEFAULT_BOLD);
                label.setTextColor(Colors.PRIMARY_TEXT);
                v.setBackgroundColor(Colors.ENTRY_BACKGROUND);
            } else {
            	label.setTypeface(Typeface.DEFAULT);
            	v.setBackgroundColor(0x00000000);
            }
        }
        return v;
	}
}
