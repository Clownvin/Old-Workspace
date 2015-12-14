package com.dew.threading;

import com.dew.io.ServerIO;
import com.dew.util.CyclicArrayList;

/**
 * 
 * @author Calvin Gene Hall
 *
 */
//TODO Find a way to modify worker thread count, while not risking loosing tasks,etc.
public final class ThreadPool extends Thread {

	private static final class WorkerThread extends Thread {
		private ThreadTask task = null;

		protected WorkerThread() {
			this.start();
		}

		@Override
		public void run() {
			while (true) {
				synchronized (ThreadPool.SINGLETON) {
					ThreadPool.SINGLETON.notifyAll();
				}
				this.setPriority(3);
				while (!ThreadPool.hasTask()) {
					synchronized (ThreadPool.TASK_NOTIFICATION) {
						try {
							ThreadPool.TASK_NOTIFICATION.wait();
						} catch (InterruptedException e) {
						}
					}
				}
				task = ThreadPool.getNextTask();
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
		} catch (com.dew.util.MaximumCapacityReachedException e) {
			ServerIO.printErr("[ThreadPool] Caught MaximumCapacityReached exception for task queue.");
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

	//TEST Needs testing to ensure that it exits wait block
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

	private static final ThreadPool SINGLETON = new ThreadPool();

	private static final Object TASK_NOTIFICATION = new Object();

	static {
		for (int i = 0; i < workerThreads.length; i++) {
			workerThreads[i] = new WorkerThread();
		}
		SINGLETON.start();
	}

	private ThreadPool() {
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
				//TEST Next block is theoretical
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

/*public static void addTaskWhenPossible(ThreadTask task) {
boolean addedTask = false;
synchronized (queuedTasks) {
	while (!addedTask) {
		while (queuedTasks.size() == queuedTasks.getCapacity()) {
			synchronized (SINGLETON) {
				try {
					SINGLETON.wait();
				} catch (InterruptedException e) {
				}
			}
		}
		// Try block for extra insurance.
		try {
			queuedTasks.add(task);
			addedTask = true;
		} catch (MaximumCapacityReachedException e) {
			addTaskWhenPossible(task);
		}
	}
}
synchronized (TASK_NOTIFICATION) {
	TASK_NOTIFICATION.notify();
}
}*/
