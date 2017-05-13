package org.throwable.session;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2017/4/22 12:19
 */
public class FluentCreateSessionSample extends BaseConnectionInfo{

	public static void main(String[] args) throws Exception{
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
		CuratorFramework client =
		CuratorFrameworkFactory.builder()
				.connectString(connectionInfo)
				.sessionTimeoutMs(5000)
				.connectionTimeoutMs(5000)
				.retryPolicy(retryPolicy)
				.namespace("base")
				.build();
		client.start();
		//创建节点
		client.create().forPath("path");
		client.create().forPath("path","init".getBytes());
		client.create().withMode(CreateMode.EPHEMERAL).forPath("path");
		client.create().withMode(CreateMode.EPHEMERAL).forPath("path","init".getBytes());
		client.create()
				.creatingParentContainersIfNeeded()
				.withMode(CreateMode.EPHEMERAL)
				.forPath("path","init".getBytes());
		//删除节点
		client.delete().forPath("path");
		client.delete().deletingChildrenIfNeeded().forPath("path");
        client.delete().guaranteed().deletingChildrenIfNeeded().withVersion(10086).forPath("path");
        client.delete().guaranteed().forPath("path");
        //读取数据
		client.getData().forPath("path");
		Stat stat = new Stat();
		client.getData().storingStatIn(stat).forPath("path");

		//更新(设置)数据
		client.setData().forPath("path","data".getBytes());
		client.setData().withVersion(10086).forPath("path","data".getBytes());

		//检查是否存在
		client.checkExists().forPath("path");

		//获取所有的子节点
		client.getChildren().forPath("path");

		//原子事务
		client.inTransaction().check().forPath("path")
				.and()
				.create().withMode(CreateMode.EPHEMERAL).forPath("path","data".getBytes())
				.and()
				.setData().withVersion(10086).forPath("path","data2".getBytes())
				.and()
				.commit();

		//异步化Api
		Executor executor = Executors.newFixedThreadPool(2);
		client.create()
				.creatingParentsIfNeeded()
				.withMode(CreateMode.EPHEMERAL)
				.inBackground((curatorFramework, curatorEvent) -> {
					System.out.println(String.format("eventType:%s,resultCode:%s",curatorEvent.getType(),curatorEvent.getResultCode()));
				},executor)
				.forPath("path");
		Thread.sleep(Integer.MAX_VALUE);
	}
}
