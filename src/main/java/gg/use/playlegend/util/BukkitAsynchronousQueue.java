package gg.use.playlegend.util;

import co.aikar.taskchain.AsyncQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class BukkitAsynchronousQueue implements AsyncQueue {

  private final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(0,
      50 * Runtime.getRuntime().availableProcessors(),
      180, TimeUnit.SECONDS, new SynchronousQueue<>());

  public BukkitAsynchronousQueue() {
    threadPoolExecutor.setRejectedExecutionHandler((run, threadPoolExecutor) -> run.run());
  }

  @Override
  public void postAsync(Runnable runnable) {
    this.threadPoolExecutor.execute(runnable);
  }

  @Override
  public void shutdown(int timeout, TimeUnit unit) {
    this.threadPoolExecutor.shutdown();
    try {
      if (!this.threadPoolExecutor.awaitTermination(1, TimeUnit.MINUTES)) {
        // Log.warn("Some tasks did not finish in time");
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
