package de.cpg_gilching.informatik11.gamelobby.shared;

import java.util.Deque;
import java.util.LinkedList;

public class TaskScheduler {
	
	private Deque<Runnable> wartendeTasks = new LinkedList<Runnable>();
	
	public void taskHinzufügen(Runnable task) {
		synchronized (wartendeTasks) {
			wartendeTasks.add(task);
		}
	}
	
	public void tasksAbarbeiten() {
		while (true) {
			Runnable task;
			synchronized (wartendeTasks) {
				task = wartendeTasks.poll();
			}
			if (task == null)
				break;
			
			task.run();
		}
	}
	
}
