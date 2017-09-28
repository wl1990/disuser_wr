package com.test.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class NioTest {
	private static ExecutorService exec=Executors.newFixedThreadPool(2);
	public static void main(String[] args) throws IOException {
		try{
/*			Thread t1=new Thread(()->{
				server();
			});*/
			Thread t2=new Thread(()->{
//				socketClient();
				NioClientSocket.handleMsg();
			});
			Thread t3=new Thread(()->{
				NioServerSocket.selector();
			});
			exec.execute(t2);
			exec.execute(t3);
		}finally{
			exec.shutdown();
		}
	}
	
	public static void socketClient(){
		ByteBuffer bbuffer=ByteBuffer.allocate(1024);
		SocketChannel socketChannel=null;
		try {
			socketChannel=SocketChannel.open();
			socketChannel.configureBlocking(false);
			socketChannel.connect(new InetSocketAddress("127.0.0.1",9090));
			while(true){
				if(!socketChannel.finishConnect()){
					System.out.println("-----wait server ------");
					Thread.sleep(1000);
					continue;
				}
				int i=0;
				while(true){
					TimeUnit.SECONDS.sleep(1);
					String info="info msg"+i++;
					bbuffer.clear();
					bbuffer.put(info.getBytes());
					bbuffer.flip();
					while(bbuffer.hasRemaining()){
						System.out.println("---client---------"+bbuffer);
						socketChannel.write(bbuffer);
					}
				}
			}
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}finally{
			try {
				if(socketChannel!=null){
					socketChannel.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * io socket server
	 */
	public static void server(){
		ServerSocket serverSocket=null;
		InputStream in=null;
		try{
			serverSocket=new ServerSocket(9090);
			byte[] buf=new byte[1024];
			while(true){
				System.out.println("----wait client----");
				Socket socket=serverSocket.accept();
				SocketAddress clientAddress=socket.getRemoteSocketAddress();
				System.out.println("client address:"+clientAddress);
				in=socket.getInputStream();
				while(in.read(buf)!=-1){
					System.out.println("-----server-------"+new String(buf));
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
			if(serverSocket!=null)
				serverSocket.close();
			if(in!=null)
				in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		}
	}
	
	/**
	 * nio server socket
	 * @author wanglei-ds20
	 *
	 */
	
	public static void nioFile(){
		RandomAccessFile afile=null;
		RandomAccessFile wfile=null;
		try{
		afile=new RandomAccessFile("D:\\lua\\iotest.txt","rw");
		wfile=new RandomAccessFile("D:\\lua\\iotestcopy.txt","rw");
		FileChannel fileChannel=afile.getChannel();
		FileChannel wfileChannel=wfile.getChannel();
		ByteBuffer buf=ByteBuffer.allocate(1024);
		int bytereader=fileChannel.read(buf);
		while(bytereader!=-1){
			buf.flip();
			while(buf.hasRemaining()){
				System.out.println(buf.get());
				wfileChannel.write(buf);
			}
			buf.compact();
			bytereader=fileChannel.read(buf);
		}
		}catch(Exception e){
			
		}
		finally{
			try{
				if(afile!=null)
					afile.close();
				if(wfile!=null)
					wfile.close();
			}catch(Exception e){
				
			}
		}
		System.out.println("-------end--");
	}
	
}

class NioServerSocket{
	private static final int BUF_SIZE=1024;
	private static final int PORT=9090;
	private static final int TIMEOUT=3000;
	
	public static void handleAccept(SelectionKey key) throws IOException{
		ServerSocketChannel sschannel=(ServerSocketChannel) key.channel();
		SocketChannel sc=sschannel.accept();
		sc.configureBlocking(false);
		sc.register(key.selector(),SelectionKey.OP_READ,ByteBuffer.allocateDirect(BUF_SIZE));
	}
	
	public static void handleRead(SelectionKey key) throws IOException{
		long byteRead=0L;
		SocketChannel sc=null;
		try{
		sc=(SocketChannel) key.channel();
		ByteBuffer buf=(ByteBuffer) key.attachment();
		byteRead=sc.read(buf);
		while(byteRead>0){
			buf.flip();
			while(buf.hasRemaining()){
				System.out.println("-----server read----"+buf.get());
			}
			buf.clear();
			byteRead=sc.read(buf);
		}
		// send info to client
		ByteBuffer wbuf=ByteBuffer.allocate(1024);
		wbuf.put("--service info--".getBytes());
		key.attach(wbuf);
		key.interestOps(key.interestOps() | SelectionKey.OP_WRITE);
		}finally{
			if(byteRead==-1){
				sc.close();
			}
		}
	}
	
	public static void handleWrite(SelectionKey key) throws IOException{
		ByteBuffer buf=(ByteBuffer) key.attachment();
		System.out.println(buf.position());
		buf.flip();
		SocketChannel sc=(SocketChannel) key.channel();
		while(buf.hasRemaining()){
			System.out.println("---server write---"+sc.write(buf));
		}
		key.interestOps(SelectionKey.OP_READ);
	}
	
	public static void selector(){
		Selector selector=null;
		ServerSocketChannel ssc=null;
		try{
			selector=Selector.open();
			ssc=ServerSocketChannel.open();
			ServerSocket ss=ssc.socket();
			ssc.bind(new InetSocketAddress(PORT));
			ssc.configureBlocking(false);
			ssc.register(selector,SelectionKey.OP_ACCEPT);
			while(true){
				if(selector.select(TIMEOUT)==0){
					System.out.println("---wait---");
					continue;
				}
				Set<SelectionKey> keyset=selector.selectedKeys();
				for(SelectionKey skey:keyset){
					if(skey.isAcceptable()){
						handleAccept(skey);
					}
					if(skey.isReadable()){
						handleRead(skey);
					}
					if(skey.isWritable() && skey.isValid()){
						handleWrite(skey);
					}
					if(skey.isConnectable()){
						System.out.println("----is connectable-----");
					}
				}
				keyset.clear();
			}
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			try {
				if(selector!=null){
					selector.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
class NioClientSocket{
	private static final int BUF_SIZE=1024;
	private static final int PORT=9090;
	private static final int TIMEOUT=3000;
	
	public static SocketChannel initClient(){
		try {
			SocketChannel socketChannel=SocketChannel.open();
			socketChannel.configureBlocking(false);
			socketChannel.connect(new InetSocketAddress("127.0.0.1",9090));
			return socketChannel;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void handleMsg(){
		SocketChannel socketChannel=initClient();
		try {
			if(socketChannel==null)
				System.out.println("-------socketChannel is null-------");
			while(!socketChannel.finishConnect()){
				System.out.println("-----wait server ------");
				Thread.sleep(1000);
			}
			Thread t1=new Thread(()->{
				sendMsg(socketChannel);
			});
			t1.start();
			Thread t2=new Thread(()->{
				receiveMsg(socketChannel);
			});
			t2.start();
			t1.join();
			t2.join();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally{
			try {
				if(socketChannel!=null){
						socketChannel.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static void receiveMsg(SocketChannel socketChannel) {
		try {
			ByteBuffer bbuffer=ByteBuffer.allocate(1024);
			while(true){
				int result=socketChannel.read(bbuffer);
				while(result>0){
					bbuffer.flip();
					while(bbuffer.hasRemaining()){
						System.out.println("-------client read-----"+bbuffer.get());
					}
					bbuffer.clear();
					result=socketChannel.read(bbuffer);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void sendMsg(SocketChannel socketChannel) {
		try {
		ByteBuffer bbuffer=ByteBuffer.allocate(1024);
		int i=0;
		while(true){
			Thread.sleep(1000);
			String info="info msg"+i++;
			bbuffer.clear();
			bbuffer.put(info.getBytes());
			bbuffer.flip();
			while(bbuffer.hasRemaining()){
				System.out.println("---client write---------"+bbuffer);
				socketChannel.write(bbuffer);
			}
		}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
}


