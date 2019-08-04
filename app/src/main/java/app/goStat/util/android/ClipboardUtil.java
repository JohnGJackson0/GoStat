package app.goStat.util.android;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.PorterDuff;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Toast;

import app.goStat.R;

public class ClipboardUtil {

    public void copyToClipboard(String copyText, Activity activity) {
        ClipboardManager clipboard = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(activity.getString(R.string.meta_label_copy_to_clipboard_result), copyText);
        clipboard.setPrimaryClip(clip);
    }

    public void showCopyToClipboardMessage(Activity activity) {
        Toast toast = Toast.makeText(activity,
                activity.getResources().getString(R.string.toast_copy_to_clipboard_generic),
                Toast.LENGTH_SHORT);
        View view = toast.getView();
        int color = ContextCompat.getColor(activity, R.color.colorPrimary);
        view.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_IN);
        toast.show();
    }

    public void copyToClipboard(String copyText, FragmentActivity activity) {
        ClipboardManager clipboard = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(activity.getString(R.string.meta_label_copy_to_clipboard_result), copyText);
        clipboard.setPrimaryClip(clip);
    }
}
