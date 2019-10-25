package am.foursteps.pexel.ui.base.util;

import android.util.SparseIntArray;

public final class DownloadIds {
    private static final DownloadIds ourInstance = new DownloadIds();
    private SparseIntArray downloadIds = new SparseIntArray();

    public static DownloadIds getInstance() {
        return ourInstance;
    }

    private DownloadIds() {
    }

    public void removeDownloadIds(int position) {
        downloadIds.delete(position);
    }

    public void addDownloadId(int position, Integer downloadId) {
        downloadIds.put(position, downloadId);
    }

    public SparseIntArray getDownloadIds() {
        return downloadIds;
    }

    public int getDownloadId(int position) {
        return downloadIds.get(position, -1);
    }
}
