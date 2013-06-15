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

package net.ustyugov.jtalk.activity.muc;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.jtalk2.R;
import net.ustyugov.jtalk.Colors;
import net.ustyugov.jtalk.Constants;
import net.ustyugov.jtalk.activity.RosterActivity;
import net.ustyugov.jtalk.service.JTalkService;
import org.jivesoftware.smack.util.StringUtils;

public class Invite extends SherlockActivity implements View.OnClickListener {
    private Button ok, cancel;
    private EditText nickEd;
    private String account, room, password;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setTheme(Colors.isLight ? R.style.AppThemeLight : R.style.AppThemeDark);
        setContentView(R.layout.invite);
        setTitle(R.string.Invite);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayout linear = (LinearLayout) findViewById(R.id.linear);
        linear.setBackgroundColor(Colors.BACKGROUND);

        account = getIntent().getStringExtra("account");
        room = getIntent().getStringExtra("room");
        password = getIntent().getStringExtra("password");
        String from = getIntent().getStringExtra("from");
        String reason = getIntent().getStringExtra("reason");

        TextView toTV = (TextView) findViewById(R.id.to);
        toTV.setText(account);

        TextView fromTV = (TextView) findViewById(R.id.from);
        fromTV.setText(from);

        TextView roomTV = (TextView) findViewById(R.id.room);
        roomTV.setText(room);

        TextView reasonTV = (TextView) findViewById(R.id.reason);
        reasonTV.setText(reason);

        nickEd = (EditText) findViewById(R.id.nick);

        ok = (Button) findViewById(R.id.ok);
        ok.setOnClickListener(this);

        cancel = (Button) findViewById(R.id.cancel);
        cancel.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(this, RosterActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        if (view == ok) {
            JTalkService service = JTalkService.getInstance();
            if (service != null && service.isAuthenticated(account)) {
                String nick = nickEd.getText().toString();
                if (nick == null && nick.length() < 1) nick = StringUtils.parseName(account);
                service.joinRoom(account, room, nick, password);
                sendBroadcast(new Intent(Constants.PRESENCE_CHANGED));
                finish();
            }
        } else if (view == cancel) {
            finish();
        }
    }
}
