package com.limeapi.threading;

import com.limeapi.util.CyclicArrayList;
import com.limeapi.util.MaximumCapacityReachedException;

/**
 * 
 * @author Calvin Gene Hall
 *
 */
// TODO Find a way to modify worker thread count, while not risking loosing
// tasks,etc.
public final class StaticThreadPool extends Thread {

    private static final class WorkerThread extends Thread {
	private ThreadTask task = null;

	protected WorkerThread() {
	    this.start();
	}

	@Override
	public void run() {
	    while (true) {
		synchronized (StaticThreadPool.SINGLETON) {
		    StaticThreadPool.SINGLETON.notifyAll();
		}
		this.setPriority(3);
		while (!StaticThreadPool.hasTask()) {
		    synchronized (StaticThreadPool.TASK_NOTIFICATION) {
			try {
			    StaticThreadPool.TASK_NOTIFICATION.wait();
			} catch (InterruptedException e) {
			}
		    }
		}
		task = StaticThreadPool.getNextTask();
		if (task != null) {
		    this.setPriority(task.getPriority());
		    try {
			do {
			    task.doTask();
			} while (!task.reachedEnd());
			task.end();
		    } catch (Exception e) {
			System.err
				.println("Worker thread caught uncaught exception thrown by task: "
					+ task.toString()
					+ ".\nSee below for stack trace.");
			e.printStackTrace();
		    }
		}
	    }
	}
    }

    public static boolean addTask(ThreadTask task) {
	try {
	    synchronized (queuedTasks) {
		queuedTasks.add(task);
	    }
	    synchronized (TASK_NOTIFICATION) {
		TASK_NOTIFICATION.notify();
	    }
	    return true;
	} catch (MaximumCapacityReachedException e) {
	    System.err.println("Caught MaximumCapacityReachedException.");
	}
	return false;
    }

    private static ThreadTask getNextTask() {
	synchronized (queuedTasks) {
	    return queuedTasks.removeNext();
	}
    }

    private static boolean hasTask() {
	return queuedTasks.size() > 0;
    }

    // TEST Needs testing to ensure that it exits wait block
    public static void setTaskQueueSize(int size) {
	while (queuedTasks.size() > 0) {
	    synchronized (SINGLETON) {
		try {
		    SINGLETON.wait();
		} catch (InterruptedException e) {
		}
	    }
	}
	queuedTasks.setCapacity(size);
    }

    private static final int DEFAULT_WORKERTHREADS_LENGTH = 20;
    private static final int DEFAULT_QUEUEDTASKS_LENGTH = 10000;
    private static WorkerThread[] workerThreads = new WorkerThread[DEFAULT_WORKERTHREADS_LENGTH];

    private static final CyclicArrayList<ThreadTask> queuedTasks = new CyclicArrayList<ThreadTask>(
	    DEFAULT_QUEUEDTASKS_LENGTH);

    private static final StaticThreadPool SINGLETON = new StaticThreadPool();

    private static final Object TASK_NOTIFICATION = new Object();

    static {
	for (int i = 0; i < workerThreads.length; i++) {
	    workerThreads[i] = new WorkerThread();
	}
	SINGLETON.start();
    }

    private StaticThreadPool() {
	// To prevent instantiation.
    }

    @Override
    public void run() {
	while (true) {
	    // Regular mode (Doesn't do anything)
	    while (queuedTasks.size() == 0) {
		synchronized (SINGLETON) {
		    try {
			SINGLETON.wait();
		    } catch (InterruptedException e) {
		    }
		}
	    }
	    // Queue mode (Only use queue tasks)
	    // Trying to get rid of them.
	    while (queuedTasks.size() > 0) {
		synchronized (TASK_NOTIFICATION) {
		    TASK_NOTIFICATION.notify();
		}
		// TEST Next block is theoretical
		synchronized (SINGLETON) {
		    try {
			SINGLETON.wait();
		    } catch (InterruptedException e) {
		    }
		}
	    }
	}
    }
}
