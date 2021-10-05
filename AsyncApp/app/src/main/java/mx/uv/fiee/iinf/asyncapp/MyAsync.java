package mx.uv.fiee.iinf.asyncapp;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class MyAsync<T> extends FutureTask<T> {
    private TaskDone listener;

    public MyAsync (Callable<T> callable) {
        super (callable);
    }

    public void setTaskDoneListener (TaskDone listener) {
        this.listener = listener;
    }

    @Override
    protected void done () {
        super.done ();
        listener.finished ();
    }
}

interface TaskDone {
    void finished ();
}
