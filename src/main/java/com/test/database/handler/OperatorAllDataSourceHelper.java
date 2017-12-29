package com.test.database.handler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;

import com.test.utils.HandleDataSource;

/**
 * 遍历数据库,表
 * @author wanglei-ds20
 *
 */
public class OperatorAllDataSourceHelper {
	private static final List<String> datakey=Arrays.asList("read","write");// data key
	private static ExecutorService exec=Executors.newFixedThreadPool(2);
	Logger logger = Logger.getLogger("OperatorAllDataSourceHelper");
	
	public  void operatorAllDataSource(OperatorData OperatorData,int tableCount){
		try {
			CountDownLatch cd=new CountDownLatch(2);
			List<Future<Boolean>> list=new ArrayList<Future<Boolean>>();
			for(String key:datakey){
				Future<Boolean> future=exec.submit(new Operation(cd,key,OperatorData,tableCount));
			}
			cd.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	class Operation implements Callable<Boolean>{
		private CountDownLatch cd;
		private String key;
		private OperatorData operatorDate;
		private int tableCount;
		public Operation(CountDownLatch cd,String key,OperatorData operatorDate,int tableCount){
			this.cd=cd;
			this.key=key;
			this.operatorDate=operatorDate;
			this.tableCount=tableCount;
		}
		
		@Override
		public Boolean call() throws Exception {
			try{
				HandleDataSource.putDataSource(key);
				for(int i=0;i<tableCount;i++){
					operatorDate.operator(String.valueOf(i));
				}
				return true;
			}catch(Exception e){
				logger.error("error:",e);
			}finally{
				cd.countDown();
			}
			return false;
		}
		
	}
}

