package am.foursteps.pexel.ui.base.util;

import java.util.ArrayList;
import java.util.List;

public final class DownloadIds {
    private static final DownloadIds ourInstance = new DownloadIds();
    private List<Integer> downloadIds = new ArrayList<>();

    public static DownloadIds getInstance() {
        return ourInstance;
    }

    private DownloadIds() {
    }

    public void removeDownloadIds(Integer downloadId) {
        downloadIds.remove(downloadId);
    }

    public void addDownloadId(Integer downloadId) {
        downloadIds.add(downloadId);
    }

    public List<Integer> getDownloadIds() {
        return downloadIds;
    }
}
