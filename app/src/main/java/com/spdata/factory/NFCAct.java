/* NFCard is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 3 of the License, or
(at your option) any later version.

NFCard is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Wget.  If not, see <http://www.gnu.org/licenses/>.

Additional permission under GNU GPL version 3 section 7 */

package com.spdata.factory;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.SystemProperties;
import android.provider.Settings;
import android.text.ClipboardManager;
import android.text.Editable;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.spdata.factory.application.App;
import com.spdata.factory.view.CustomTitlebar;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.xml.sax.XMLReader;

import java.util.Arrays;

import common.base.act.FragActBase;
import common.event.ViewMessage;
import common.xnfc.Util;
import common.xnfc.card.CardManager;

@EActivity(R.layout.act_nfcard)
public class NFCAct extends FragActBase implements OnClickListener,
        Html.ImageGetter, Html.TagHandler {
    @ViewById
    CustomTitlebar titlebar;
    @ViewById
    Button btnNfc;
    @ViewById
    Button btnPass;
    @ViewById
    Button btnNotPass;
    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    private Resources res;
    private TextView board;

    private enum ContentType {
        HINT, DATA, MSG
    }

    @Click
    void btnNotPass() {
        setXml(App.KEY_NFC, App.KEY_UNFINISH);
        finish();
    }

    @Click
    void btnPass() {
        setXml(App.KEY_NFC, App.KEY_FINISH);
        finish();
    }

    @Override
    protected Context regieterBaiduBaseCount() {
        return null;
    }

    @Override
    protected void initTitlebar() {
        titlebar.setTitlebarStyle(CustomTitlebar.TITLEBAR_STYLE_NORMAL);
        titlebar.setAttrs(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }, "NFC测试", null);
    }

    @Override
    public void onEventMainThread(ViewMessage viewMessage) {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSwipeEnable(false);
        NfcManager manager = (NfcManager) this.getSystemService(Context.NFC_SERVICE);
        nfcAdapter = manager.getDefaultAdapter();
        try {
            if (!nfcAdapter.isEnabled()) {
//				SystemProperties.set("persist.sys.nfc.restore","true");
                startActivity(new Intent("android.settings.NFC_SETTINGS"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterViews
    protected void main() {
        initTitlebar();
        final Resources res = getResources();
        this.res = res;
        final View decor = getWindow().getDecorView();
        final TextView board = (TextView) decor.findViewById(R.id.board);
        this.board = board;
        decor.findViewById(R.id.btnCopy).setOnClickListener(this);
        decor.findViewById(R.id.btnNfc).setOnClickListener(this);
        board.setMovementMethod(LinkMovementMethod.getInstance());
        board.setFocusable(false);
        board.setClickable(false);
        board.setLongClickable(false);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            setXml(App.KEY_NFC, App.KEY_UNFINISH);
            showToast("NFC不可用！");
            finish();
        }
        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
                getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        Intent intent = getIntent();
        if (intent != null) {
            onNewIntent(intent);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (nfcAdapter != null)
            nfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (nfcAdapter != null)
            nfcAdapter.enableForegroundDispatch(this, pendingIntent,
                    CardManager.FILTERS, CardManager.TECHLISTS);
        refreshStatus();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        final Parcelable p = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if (tagFromIntent == null) {
            return;
        }
        MifareClassic mfc = MifareClassic.get(tagFromIntent);
        if (p != null) {
            System.out.println("p!=null");
        }
        String data = (p != null) ? CardManager.load(p, res, mfc) : null;
        showData(data);
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.btnCopy: {
                copyData();
                break;
            }
            case R.id.btnNfc: {
                startActivityForResult(new Intent(
                        Settings.ACTION_WIRELESS_SETTINGS), 0);
                break;
            }
            default:
                break;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        refreshStatus();
    }

    private void refreshStatus() {
        final Resources r = this.res;

        final String tip;
        if (nfcAdapter == null)
            tip = r.getString(R.string.tip_nfc_notfound);
        else if (nfcAdapter.isEnabled())
            tip = r.getString(R.string.tip_nfc_enabled);
        else
            tip = r.getString(R.string.tip_nfc_disabled);

        showToast(tip);
        final StringBuilder s = new StringBuilder(
                r.getString(R.string.app_name));

        s.append("  --  ").append(tip);
        setTitle(s);

        final CharSequence text = board.getText();
        if (text == null || board.getTag() == ContentType.HINT)
            showHint();
    }

    @SuppressWarnings("deprecation")
    private void copyData() {
        final CharSequence text = board.getText();
        if (text == null || board.getTag() != ContentType.DATA)
            return;

        ((ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE))
                .setText(text);

        final String msg = res.getString(R.string.msg_copied);
        final Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    private void showData(String data) {
        if (data == null || data.length() == 0) {
            showHint();
            return;
        }

        final TextView board = this.board;
        final Resources res = this.res;

        final int padding = res.getDimensionPixelSize(R.dimen.pnl_margin);

        board.setPadding(padding, padding, padding, padding);
        board.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        board.setTextSize(res.getDimension(R.dimen.text_small));
        board.setTextColor(res.getColor(R.color.text_default));
        board.setGravity(Gravity.NO_GRAVITY);
        board.setTag(ContentType.DATA);
        board.setText(Html.fromHtml(data, this, null));
    }


    private void showHint() {
        final TextView board = this.board;
        final Resources res = this.res;
        final String hint;

        if (nfcAdapter == null)
            hint = res.getString(R.string.msg_nonfc);
        else if (nfcAdapter.isEnabled())
            hint = res.getString(R.string.msg_nocard);
        else
            hint = res.getString(R.string.msg_nfcdisabled);

        final int padding = res.getDimensionPixelSize(R.dimen.text_middle);

        board.setPadding(padding, padding, padding, padding);
        board.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD_ITALIC));
        board.setTextSize(res.getDimension(R.dimen.text_middle));
        board.setTextColor(res.getColor(R.color.text_tip));
        board.setGravity(Gravity.CENTER_VERTICAL);
        board.setTag(ContentType.HINT);
        board.setText(Html.fromHtml(hint));
    }

    @Override
    public void handleTag(boolean opening, String tag, Editable output,
                          XMLReader xmlReader) {
        if (!opening && "version".equals(tag)) {
            try {
                output.append(getPackageManager().getPackageInfo(
                        getPackageName(), 0).versionName);
            } catch (NameNotFoundException e) {
            }
        }
    }

    private Drawable spliter;

    @Override
    public Drawable getDrawable(String source) {
        final Resources res = this.res;
        final Drawable ret;
        if (source.startsWith("spliter")) {
            if (spliter == null) {
                final int w = res.getDisplayMetrics().widthPixels;
                final int h = (int) (res.getDisplayMetrics().densityDpi / 72f + 0.5f);

                final int[] pix = new int[w * h];
                Arrays.fill(pix, res.getColor(R.color.bg_default));
                spliter = new BitmapDrawable(Bitmap.createBitmap(pix, w, h,
                        Bitmap.Config.ARGB_8888));
                spliter.setBounds(0, 3 * h, w, 4 * h);
            }
            ret = spliter;

        } else if (source.startsWith("icon_main")) {
            ret = res.getDrawable(R.drawable.icon_nfc);

            final String[] params = source.split(",");
            final float f = res.getDisplayMetrics().densityDpi / 72f;
            final float w = Util.parseInt(params[1], 10, 16) * f + 0.5f;
            final float h = Util.parseInt(params[2], 10, 16) * f + 0.5f;
            ret.setBounds(0, 0, (int) w, (int) h);

        } else {
            ret = null;
        }
        return ret;
    }
}
