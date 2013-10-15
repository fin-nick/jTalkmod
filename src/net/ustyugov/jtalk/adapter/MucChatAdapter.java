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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import net.ustyugov.jtalk.Colors;
import net.ustyugov.jtalk.Constants;
import net.ustyugov.jtalk.MessageItem;
import net.ustyugov.jtalk.smiles.Smiles;
import net.ustyugov.jtalk.listener.TextLinkClickListener;
import net.ustyugov.jtalk.view.MyTextView;

import com.jtalkmod.R;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MucChatAdapter extends ArrayAdapter<MessageItem> implements TextLinkClickListener {
    private String searchString = "";

    private Context context;
    private Smiles smiles;
    private String nick;
    private String group;
    private boolean firstClick = false;
    private boolean showtime = false;
    private Timer doubleClickTimer = new Timer();

    private SharedPreferences prefs;

    public MucChatAdapter(Context context, Smiles smiles) {
        super(context, R.id.chat1);
        this.context = context;
        this.smiles = smiles;
        this.prefs = PreferenceManager.getDefaultSharedPreferences(context);
        this.showtime = prefs.getBoolean("ShowTime", false);
    }

    public String getGroup() { return this.group; }

    public void update(String group, String nick, List<MessageItem> messages, String searchString) {
        this.group = group;
        this.nick = nick;
        this.searchString = searchString;
        clear();

        boolean showStatuses = prefs.getBoolean("ShowStatus", false);
        for (int i = 0; i < messages.size(); i++) {
            MessageItem item = messages.get(i);
            MessageItem.Type type = item.getType();
            if (searchString.length() > 0) {
                String name = item.getName();
                String body = item.getBody();
                String time = createTimeString(item.getTime());
                if (type == MessageItem.Type.status) {
                    if (showtime) body = time + "  " + body;
                } else {
                    if (showtime) body = time + " " + name + ": " + body;
                    else body = name + ": " + body;
                }

                if (body.toLowerCase().contains(searchString.toLowerCase())) {
                    if (showStatuses || (!showStatuses && type != MessageItem.Type.status)) add(item);
                }
            } else {
                if (showStatuses || (!showStatuses && type != MessageItem.Type.status)) add(item);
            }
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        boolean enableCollapse = prefs.getBoolean("EnableCollapseMessages", true);
        int fontSize = Integer.parseInt(context.getResources().getString(R.string.DefaultFontSize));
        try {
            fontSize = Integer.parseInt(prefs.getString("FontSize", context.getResources().getString(R.string.DefaultFontSize)));
        } catch (NumberFormatException ignored) {	}

        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.chat_item, null);
        }

        final MessageItem item = getItem(position);
        String time = createTimeString(item.getTime());
        String body = item.getBody();
        String name = item.getName();
        String n    = item.getName();
        MessageItem.Type type = item.getType();
        final boolean collapsed = item.isCollapsed();

        if (showtime) name = time + " " + name;

        String message;
        if (type == MessageItem.Type.status) message = name + " " + body;
        else {
            if (body.length() > 4 && body.substring(0, 3).equals("/me")) {
                if (showtime && time.length() > 2) message = time + " * " + n + " " + body.substring(3);
                else message = " * " + n + " " + body.substring(3);
            } else {
                message = name + ": " + body;
            }
        }

        SpannableStringBuilder ssb = new SpannableStringBuilder();
        ssb.append(message);
        if (type == MessageItem.Type.status) {
            ssb.setSpan(new ForegroundColorSpan(Colors.STATUS_MESSAGE), 0, message.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            if (showtime && time.length() > 2) {
                ssb.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, time.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            if (n.equals(nick)) {
                int idx = message.indexOf(n);
                ssb.setSpan(new ForegroundColorSpan(Colors.SECONDARY_TEXT), 0, message.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                ssb.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), idx, idx + n.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else {
                int idx = message.indexOf(n);
                if (message.contains(nick)) {
                    ssb.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, message.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    ssb.setSpan(new ForegroundColorSpan(Colors.HIGHLIGHT_TEXT), 0, message.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                ssb.setSpan(new ForegroundColorSpan(Colors.INBOX_MESSAGE), idx, idx + n.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                ssb.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), idx, idx + n.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        // Search highlight
        if (searchString.length() > 0) {
            if (ssb.toString().toLowerCase().contains(searchString.toLowerCase())) {
                int from = 0;
                int start = -1;
                while ((start = ssb.toString().toLowerCase().indexOf(searchString.toLowerCase(), from)) != -1) {
                    from = start + searchString.length();
                    ssb.setSpan(new BackgroundColorSpan(Colors.SEARCH_BACKGROUND), start, start + searchString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }



        LinearLayout linear = (LinearLayout) v.findViewById(R.id.chat_item);
        linear.setMinimumHeight(Integer.parseInt(prefs.getString("SmilesSize", "24")));

        final ImageView expand = (ImageView) v.findViewById(R.id.expand);
        final MyTextView t1 = (MyTextView) v.findViewById(R.id.chat1);
        t1.setTextSize(fontSize);
        t1.setOnTextLinkClickListener(this);
        t1.setTextColor(Colors.PRIMARY_TEXT);

        if (prefs.getBoolean("ShowSmiles", true)) {
            int startPosition = message.length() - body.length();
            ssb = smiles.parseSmiles(t1, ssb, startPosition);
        }

        t1.setTextWithLinks(ssb, n);
        if (enableCollapse) {
            t1.setOnTouchListener(new OnTouchListener() {
                View oldView = null;
                public boolean onTouch(View view, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            if (!firstClick) {
                                oldView = view;
                                firstClick = true;
                                doubleClickTimer.purge();
                                doubleClickTimer.cancel();
                                doubleClickTimer = new Timer();
                                doubleClickTimer.schedule(new TimerTask(){
                                    @Override
                                    public void run() {
                                        firstClick = false;
                                    }
                                }, 500);
                            } else {
                                firstClick = false;
                                if (oldView != null && oldView.equals(view)) {
                                    if (item.isCollapsed()) {
                                        item.setCollapsed(false);
                                        t1.setSingleLine(false);
                                        expand.setVisibility(View.GONE);
                                    } else {
                                        item.setCollapsed(true);
                                        t1.setSingleLine(true);
                                        expand.setVisibility(View.VISIBLE);
                                    }
                                }
                            }
                            break;
                        default:
                            break;
                    }
                    return false;
                }
            });
        }

        if (collapsed && enableCollapse) {
            t1.setSingleLine(true);
            expand.setVisibility(View.VISIBLE);
        } else {
            t1.setSingleLine(false);
            expand.setVisibility(View.GONE);
        }

        MovementMethod m = t1.getMovementMethod();
        if ((m == null) || !(m instanceof LinkMovementMethod)) {
            if (t1.getLinksClickable()) {
                t1.setMovementMethod(LinkMovementMethod.getInstance());
            }
        }

        if (item.isSelected()) v.setBackgroundColor(Colors.SELECTED_MESSAGE);
        else v.setBackgroundColor(0X00000000);
        return v;
    }

    public void onTextLinkClick(View textView, String s) {
        if (s.length() > 1) {
            int idx = s.indexOf(":");
            if (idx > 0) {
                Uri uri = Uri.parse(s);
                if (uri != null) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(uri);
                    context.startActivity(intent);
                }
            } else {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                String separator = prefs.getString("nickSeparator", ", ");

                Intent intent = new Intent(Constants.PASTE_TEXT);
                intent.putExtra("text", s + separator);
                context.sendBroadcast(intent);
            }
        }
    }

    private String createTimeString(String time) {
        try {
            Date d = new Date();
            java.text.DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
            String currentDate = df.format(d).substring(0,10);
            if (currentDate.equals(time.substring(0,10))) return "(" + time.substring(11) + ")";
            else return "(" + time + ")";
        } catch (Exception e) { return "( )"; }
    }
}
