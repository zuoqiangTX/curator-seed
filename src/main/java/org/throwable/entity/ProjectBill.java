package org.throwable.entity;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.throwable.locks.SegmentLock;

import java.util.List;
import java.util.concurrent.locks.Condition;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2017/4/23 22:21
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProjectBill {


	private String userId;
	private String orderNo;
	private int currentPeroid;
	private int totalPeroids;
	private String amount;

	public static void main(String[] args) throws Exception {
		String userId = "userIdxxxxx";
		String orderNo = "orderNoxxxxx";
		String amount = "100";
		int totalPeroids = 12;
		final Object MONITOR = new Object();
		List<ProjectBill> bills = Lists.newArrayList();
		bills.add(new ProjectBill(userId, orderNo, 3, totalPeroids, amount));
		bills.add(new ProjectBill(userId, orderNo, 1, totalPeroids, amount));
		bills.add(new ProjectBill(userId, orderNo, 2, totalPeroids, amount));
		bills.add(new ProjectBill(userId, orderNo, 4, totalPeroids, amount));
		bills.add(new ProjectBill(userId, orderNo, 12, totalPeroids, amount));
		bills.add(new ProjectBill(userId, orderNo, 5, totalPeroids, amount));
		bills.add(new ProjectBill(userId, orderNo, 7, totalPeroids, amount));
		bills.add(new ProjectBill(userId, orderNo, 6, totalPeroids, amount));
		bills.add(new ProjectBill(userId, orderNo, 9, totalPeroids, amount));
		bills.add(new ProjectBill(userId, orderNo, 8, totalPeroids, amount));
		bills.add(new ProjectBill(userId, orderNo, 10, totalPeroids, amount));
		bills.add(new ProjectBill(userId, orderNo, 11, totalPeroids, amount));
		for (ProjectBill bill : bills) {
			new Thread(new Comsumer(bill, MONITOR)).start();
		}
		Thread.sleep(Integer.MAX_VALUE);
	}


	private static class Comsumer implements Runnable {

		private final ProjectBill bill;

		private final Object monitor;

		public Comsumer(ProjectBill bill, Object monitor) {
			this.bill = bill;
			this.monitor = monitor;
		}

		@Override
		public void run() {
			try {
				synchronized (monitor) {
					if (bill.getCurrentPeroid() == bill.getTotalPeroids()) {
						monitor.wait();
					} else if (bill.getCurrentPeroid() == bill.getTotalPeroids() - 1) {
						monitor.notifyAll();
					}
					System.out.println(bill.toString());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
