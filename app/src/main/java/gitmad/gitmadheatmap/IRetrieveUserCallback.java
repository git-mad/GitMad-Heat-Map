package gitmad.gitmadheatmap;

import gitmad.gitmadheatmap.model.User;

public interface IRetrieveUserCallback {
    void onFinish(User user);
}
