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

package net.ustyugov.jtalk;

import com.jtalkmod.R;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;

public class ClientIcons {
	public static void loadClientIcon(final Activity activity, final ImageView imageView, final String node) {
		new Thread() {
			@Override
			public void run() {
				activity.runOnUiThread(new Runnable() {
					public void run() {
						if (node != null) {
							imageView.setVisibility(View.VISIBLE);
							if (node.toLowerCase().contains("adium")) {
								imageView.setImageResource(R.drawable.client_adium);
                            } else if (node.toLowerCase().contains("github.com/snuk182/aceim")) {
								imageView.setImageResource(R.drawable.client_aceim);
                            } else if (node.toLowerCase().contains("aqq.eu")) {
								imageView.setImageResource(R.drawable.client_aqq);
                            } else if (node.toLowerCase().contains("www.barobin.com/caps")) {
								imageView.setImageResource(R.drawable.client_bayan);
                            } else if (node.toLowerCase().contains("www.beem-project.com")) {
								imageView.setImageResource(R.drawable.client_beem);
                            } else if (node.toLowerCase().contains("bitlbee.org/xmpp/caps")) {
								imageView.setImageResource(R.drawable.client_bitlbee);
                            } else if (node.toLowerCase().contains("simpleapps.ru/caps") || node.toLowerCase().contains("blacksmith-2.googlecode.com/svn/")) {
								imageView.setImageResource(R.drawable.client_blacksmith_bot);
                            } else if (node.toLowerCase().contains("bluejabb")) {
								imageView.setImageResource(R.drawable.client_bluejabb);
							} else if (node.toLowerCase().contains("bombusmod.net.ru")) {
								imageView.setImageResource(R.drawable.client_bombusmod);
							} else if (node.toLowerCase().contains("bombusmod-qd.wen.ru")) {
								imageView.setImageResource(R.drawable.client_bombusqd);
							} else if (node.toLowerCase().contains("bombus-im.org/ng") || node.toLowerCase().contains("bombus-ng")) {
								imageView.setImageResource(R.drawable.client_bombusng);
							} else if (node.toLowerCase().contains("bombus.pl")) {
								imageView.setImageResource(R.drawable.client_bombuspl);
							} else if (node.toLowerCase().contains("bombus+") || node.toLowerCase().contains("voffk.org.ru")) {
								imageView.setImageResource(R.drawable.client_bombusplus);
							} else if (node.toLowerCase().contains("bombus-im.org/java")) {
								imageView.setImageResource(R.drawable.client_bombus);
							} else if (node.toLowerCase().contains("exodus.jabberstudio.org/caps")) {
								imageView.setImageResource(R.drawable.client_exodus);
                            } else if (node.toLowerCase().contains("urn:xmpp:rtt:0")) {
								imageView.setImageResource(R.drawable.client_fasttext);
                            } else if (node.toLowerCase().contains("jabga.ru")) {
								imageView.setImageResource(R.drawable.client_fj);
                            } else if (node.toLowerCase().contains("freomessenger.com/caps")) {
								imageView.setImageResource(R.drawable.client_freomessenger);
                            } else if (node.toLowerCase().contains("www.freq-bot.net")) {
								imageView.setImageResource(R.drawable.client_freq);
                            } else if (node.toLowerCase().contains("www.climm.org/xmpp/caps")) {
								imageView.setImageResource(R.drawable.client_climm);
                            } else if (node.toLowerCase().contains("coccinella.sourceforge.net/protocol/caps")) {
								imageView.setImageResource(R.drawable.client_coccinella);
							} else if (node.toLowerCase().contains("gajim.org")) {
								imageView.setImageResource(R.drawable.client_gajim);
							} else if (node.toLowerCase().contains("gmail")) {
								imageView.setImageResource(R.drawable.client_gmail);
                            } else if (node.toLowerCase().contains("isida-bot.com")) {
								imageView.setImageResource(R.drawable.client_isida);
							} else if (node.toLowerCase().contains("jabbim")) {
								imageView.setImageResource(R.drawable.client_jabbim);
                            } else if (node.toLowerCase().contains("jabbroid.akuz.de/caps")) {
								imageView.setImageResource(R.drawable.client_jabbroid);
							} else if (node.toLowerCase().contains("jajc.jrudevels.org/caps")) {
								imageView.setImageResource(R.drawable.client_jajc);
							} else if (node.toLowerCase().contains("jimm.net.ru/caps")) {
								imageView.setImageResource(R.drawable.client_jimm);
                            } else if (node.toLowerCase().contains("jitsi.org")) {
								imageView.setImageResource(R.drawable.client_jitsi);
                            } else if (node.toLowerCase().contains("kadu.im/caps")) {
								imageView.setImageResource(R.drawable.client_kadu);
							} else if (node.toLowerCase().contains("jtalk.ustyugov.net/caps")) {
								imageView.setImageResource(R.drawable.client_jtalk);
							} else if (node.toLowerCase().contains("kopete.kde.org/jabber/caps")) {
								imageView.setImageResource(R.drawable.client_kopete);
                            } else if (node.toLowerCase().contains("bluendo.com/protocol/caps")) {
								imageView.setImageResource(R.drawable.client_lampiro);
							} else if (node.toLowerCase().contains("leechcraft")) {
								imageView.setImageResource(R.drawable.client_leechcraft);
							} else if (node.toLowerCase().contains("mchat")) {
								imageView.setImageResource(R.drawable.client_mchat);
							} else if (node.toLowerCase().contains("miranda-im.org/caps")) {
								imageView.setImageResource(R.drawable.client_miranda);
                            } else if (node.toLowerCase().contains("miranda-ng.org/caps")) {
								imageView.setImageResource(R.drawable.client_miranda_ng);
							} else if (node.toLowerCase().contains("mail.google.com")) {
								imageView.setImageResource(R.drawable.client_gmail);
                            } else if (node.toLowerCase().contains("tomclaw.com/mandarin_im/caps")) {
								imageView.setImageResource(R.drawable.client_mandarin);
                            } else if (node.toLowerCase().contains("mayplaces.com/im/")) {
								imageView.setImageResource(R.drawable.client_mayplaces);
							} else if (node.toLowerCase().contains("mcabber")) {
								imageView.setImageResource(R.drawable.client_mcabber);
                            } else if (node.toLowerCase().contains("monal.im/caps")) {
								imageView.setImageResource(R.drawable.client_monal);
							} else if (node.toLowerCase().contains("nimbuzz")) {
								imageView.setImageResource(R.drawable.client_nimbuzz);
                            } else if (node.toLowerCase().contains("sleekxmpp.com/ver/")) {
                                imageView.setImageResource(R.drawable.client_poezio);
                            } else if (node.toLowerCase().contains("www.profanity.im")) {
                                imageView.setImageResource(R.drawable.client_profanity);
                            } else if (node.toLowerCase().contains("psi-im.org")) {
								imageView.setImageResource(R.drawable.client_psi);
							} else if (node.toLowerCase().contains("psi+") || node.toLowerCase().contains("psi-dev.googlecode.com")) {
								imageView.setImageResource(R.drawable.client_psiplus);
                            } else if (node.toLowerCase().contains("qip")) {
								imageView.setImageResource(R.drawable.client_qip);
                            } else if (node.toLowerCase().contains("2010.qip.ru/caps")) {
								imageView.setImageResource(R.drawable.client_qip2010);
							} else if (node.toLowerCase().contains("pda.qip.ru")) {
								imageView.setImageResource(R.drawable.client_qippda);
                            } else if (node.toLowerCase().contains("pako.googlecode.com")) {
								imageView.setImageResource(R.drawable.client_pako);
                            } else if (node.toLowerCase().contains("pandion.im")) {
								imageView.setImageResource(R.drawable.client_pandion);
							} else if (node.toLowerCase().contains("pidgin")) {
								imageView.setImageResource(R.drawable.client_pidgin);
							} else if (node.toLowerCase().contains("oneteam.im/caps")) {
								imageView.setImageResource(R.drawable.client_oneteam);
							} else if (node.toLowerCase().contains("oneteam_iphone")) {
								imageView.setImageResource(R.drawable.client_oneteamiphone);
                            } else if (node.toLowerCase().contains("code.google.com/p/qxmpp")) {
								imageView.setImageResource(R.drawable.client_qt);
							} else if (node.toLowerCase().contains("qutim.org")) {
								imageView.setImageResource(R.drawable.client_qutim);
                            } else if (node.toLowerCase().contains("riddim")) {
								imageView.setImageResource(R.drawable.client_riddim);
                            } else if (node.toLowerCase().contains("sawim.ru/caps")) {
								imageView.setImageResource(R.drawable.client_sawim);
                            } else if (node.toLowerCase().contains("www.igniterealtime.org/projects/smack/")) {
								imageView.setImageResource(R.drawable.client_xabber);
                            } else if (node.toLowerCase().contains("safetyjabber.com/caps")) {
								imageView.setImageResource(R.drawable.client_sj);
							} else if (node.toLowerCase().contains("smack")) {
								imageView.setImageResource(R.drawable.client_smack);
                            } else if (node.toLowerCase().contains("swift.im")) {
								imageView.setImageResource(R.drawable.client_swift);
							} else if (node.toLowerCase().contains("www.google.com/xmpp/client/caps")) {
								imageView.setImageResource(R.drawable.client_talkonaut);
							} else if (node.toLowerCase().contains("talk.google.com")) {
								imageView.setImageResource(R.drawable.client_gtalk);
                            } else if (node.toLowerCase().contains("tigase.org/messenger")) {
								imageView.setImageResource(R.drawable.client_tigase);
							} else if (node.toLowerCase().contains("tkabber.jabber.ru/")) {
								imageView.setImageResource(R.drawable.client_tkabber);
                            } else if (node.toLowerCase().contains("trillian.im/caps")) {
								imageView.setImageResource(R.drawable.client_trillian);
                            } else if (node.toLowerCase().contains("palringo.com/caps")) {
								imageView.setImageResource(R.drawable.client_utalk);
                            } else if (node.toLowerCase().contains("vacuum-im.googlecode.com")) {
								imageView.setImageResource(R.drawable.client_vacuum);
                            } else if (node.toLowerCase().contains("wtw.k2t.eu/")) {
								imageView.setImageResource(R.drawable.client_wtw);
							} else if (node.toLowerCase().contains("telepathy.freedesktop.org")) {
								imageView.setImageResource(R.drawable.client_telepathy);
							} else if (node.toLowerCase().contains("online.yandex.ru")) {
								imageView.setImageResource(R.drawable.client_yaonline);
							} else if (node.toLowerCase().contains("pjc.googlecode.com")) {
								imageView.setImageResource(R.drawable.client_pjc);
							} else if (node.toLowerCase().contains("www.android.com/gtalk/client")) {
								imageView.setImageResource(R.drawable.client_android);
                            } else if (node.toLowerCase().contains("habahaba.im/")) {
								imageView.setImageResource(R.drawable.client_habahaba);
							} else if (node.toLowerCase().contains("imov")) {
								imageView.setImageResource(R.drawable.client_imov);
                            } else if (node.toLowerCase().contains("chat.jabbercity.ru/caps")) {
								imageView.setImageResource(R.drawable.client_jabbercity);
                            } else if (node.toLowerCase().contains("emacs-jabber.sourceforge.net")) {
								imageView.setImageResource(R.drawable.client_jabber_el);
							} else if (node.toLowerCase().contains("jabiru.mzet.net/caps")) {
								imageView.setImageResource(R.drawable.client_jabiru);
							} else if (node.toLowerCase().contains("jappix")) {
								imageView.setImageResource(R.drawable.client_jappix);
							} else if (node.toLowerCase().contains("pjc")) {
								imageView.setImageResource(R.drawable.client_pjc);
							} else if (node.toLowerCase().contains("mobileagent")) {
								imageView.setImageResource(R.drawable.client_mobileagent);
							} else if (node.toLowerCase().contains("meebo")) {
								imageView.setImageResource(R.drawable.client_meebo);
							} else if (node.toLowerCase().contains("jasmineicq.ru/caps")) {
								imageView.setImageResource(R.drawable.client_jasmine);
                            } else if (node.toLowerCase().contains(":")) {
								imageView.setImageResource(R.drawable.client_question);
							} else {
								imageView.setVisibility(View.GONE);
							}
						} else {
							imageView.setVisibility(View.GONE);
						}
					}
				});
			}
		}.start();
	}
}
