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

package net.ustyugov.jtalk.listener;

import java.io.File;
import java.text.SimpleDateFormat;

import net.ustyugov.jtalk.Constants;
import net.ustyugov.jtalk.MessageItem;
import net.ustyugov.jtalk.MessageLog;
import net.ustyugov.jtalk.service.JTalkService;

import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.muc.ParticipantStatusListener;
import org.jivesoftware.smackx.packet.MUCUser;

import com.jtalkmod.R;

public class MucParticipantStatusListener implements ParticipantStatusListener {
        private String g;
        private String account;
        private JTalkService service;
        
        public MucParticipantStatusListener(String account, String group) {
                this.account = account;
                this.g = group; 
                this.service = JTalkService.getInstance();
        }
        
        public void statusChanged(String participant) {
                String[] statusArray = service.getResources().getStringArray(R.array.statusArray);
                String nick = StringUtils.parseResource(participant);
        
                
                Presence p = service.getPresence(account, g + "/" + nick);
                Presence.Mode mode = p.getMode();
                if (mode == null) mode = Presence.Mode.available;
                String status = p.getStatus();
                if (status != null && status.length() > 0) status = "(" + status + ")";
                else status = "";
                
        String time = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new java.util.Date());
        
                MessageItem item = new MessageItem(account, participant);
        item.setName(nick);
        item.setTime(time);
        item.setType(MessageItem.Type.status);
        item.setBody(statusArray[getPosition(mode)] + " " + status);
        
        MessageLog.writeMucMessage(account, g, nick, item);
        }
        
        public void nicknameChanged(String p, String newNick) {
                String nick = StringUtils.parseResource(p);
        String time = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new java.util.Date());
        
                MessageItem item = new MessageItem(account, p);
                item.setBody(service.getResources().getString(R.string.ChangedNicknameTo) + " " + newNick);
                item.setName(nick);
                item.setTime(time);
                item.setType(MessageItem.Type.status);
                
        try {
                File oldFile = new File(Constants.PATH + "/" + p.replaceAll("/", "%"));
                if (oldFile.exists()) {
                        File newFile = new File(Constants.PATH + "/" + g + "%" + newNick);
                        oldFile.renameTo(newFile);
                }
        } catch (Exception ignored) { }

        MessageLog.writeMucMessage(account, g, nick, item);
        }

        public void banned(String p, String actor, String reason) {
                String nick = StringUtils.parseResource(p);
        String time = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new java.util.Date());
        
        MessageItem item = new MessageItem(account, p);
                item.setBody("banned (" + reason + ")");
                item.setType(MessageItem.Type.status);
        item.setName(nick);
        item.setTime(time);
        
        MessageLog.writeMucMessage(account, g, nick, item);
        }
        
        public void kicked(String p, String actor, String reason) {
                String nick = StringUtils.parseResource(p);
        String time = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new java.util.Date());

        MessageItem item = new MessageItem(account, p);
                item.setBody("kicked (" + reason + ")");
                item.setReceived(false);
        item.setName(nick);
        item.setTime(time);
        item.setType(MessageItem.Type.status);
        
        MessageLog.writeMucMessage(account, g, nick, item);
        }
        
        public void joined(String participant) { 
                String nick = StringUtils.parseResource(participant);
        String jid = "";
        String stat = "";
        String time = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new java.util.Date());

        Presence p = service.getConferencesHash(account).get(g).getOccupantPresence(participant);
        MUCUser mucUser = (MUCUser) p.getExtension("x", "http://jabber.org/protocol/muc#user");
        if (mucUser != null) {
            MUCUser.Item item = mucUser.getItem();
            String affi = item.getAffiliation();
            String role = item.getRole();
            if (affi != null && role != null) stat = " " + affi + " " + service.getString(R.string.and) + " " + role + " ";
            String j = item.getJid();
            if (j != null && j.length() > 3) jid = " (" + j + ")";
        }

        MessageItem item = new MessageItem(account, participant);
                item.setBody(service.getResources().getString(R.string.UserJoined) + stat + jid);
                item.setType(MessageItem.Type.status);
        item.setName(nick);
        item.setTime(time);
        
        MessageLog.writeMucMessage(account, g, nick, item);
        }
        
        public void left(String participant) {
                String nick = StringUtils.parseResource(participant);
        String time = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new java.util.Date());

        MessageItem item = new MessageItem(account, participant);
                item.setBody(service.getString(R.string.UserLeaved));
                item.setType(MessageItem.Type.status);
        item.setName(nick);
        item.setTime(time);
        
        MessageLog.writeMucMessage(account, g, nick, item);
        }
        
        public void adminGranted(String participant) { stateChanged(participant); }
        public void adminRevoked(String participant) { stateChanged(participant); }
        public void membershipGranted(String participant) { stateChanged(participant); }
        public void membershipRevoked(String participant) { stateChanged(participant); }
        public void moderatorGranted(String participant) { stateChanged(participant); }
        public void moderatorRevoked(String participant) { stateChanged(participant); }
        public void ownershipGranted(String participant) { stateChanged(participant); }
        public void ownershipRevoked(String participant) { stateChanged(participant); }
        public void voiceGranted(String participant) { stateChanged(participant); }
        public void voiceRevoked(String participant) { stateChanged(participant); }
        
        private void stateChanged(String participant) {
                String role = "", affiliation = "";
                String nick = StringUtils.parseResource(participant);
                
                Presence p = service.getConferencesHash(account).get(g).getOccupantPresence(participant);
                
                MUCUser mucUser = (MUCUser) p.getExtension("x", "http://jabber.org/protocol/muc#user");
                if (mucUser != null) {
                        role = mucUser.getItem().getRole();
                        affiliation = mucUser.getItem().getAffiliation();

            String time = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new java.util.Date());
                
                int id = service.getResources().getIdentifier(role, null, null);
                if (id > 0) role = service.getResources().getString(id);
                id = service.getResources().getIdentifier(affiliation, null, null);
                if (id > 0) affiliation = service.getResources().getString(id);
                
                MessageItem item = new MessageItem(account, participant);
                        item.setBody(role + " & " + affiliation);
                        item.setType(MessageItem.Type.status);
                item.setName(nick);
                item.setTime(time);
                
                MessageLog.writeMucMessage(account, g, nick, item);
                }
        }
        
        private int getPosition(Presence.Mode m) {
        if (m == Presence.Mode.available) return 0;
        else if (m == Presence.Mode.chat) return 4;
        else if (m == Presence.Mode.away) return 1;
        else if (m == Presence.Mode.xa)   return 2;
        else if (m == Presence.Mode.dnd)  return 3;
        else return 5;
        }
}
