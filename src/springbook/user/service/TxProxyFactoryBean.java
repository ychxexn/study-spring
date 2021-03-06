package springbook.user.service;

import java.lang.reflect.Proxy;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import springbook.user.proxy.TransactionHandler;

public class TxProxyFactoryBean implements FactoryBean<Object> {

	Object target;
	PlatformTransactionManager transactionHandler;
	String pattern;
	Class<?> serviceInterface;
	
	public void setTarget(Object target) {
		this.target = target;
	}
	
	public void setTransactionHandler(PlatformTransactionManager transactionHandler) {
		this.transactionHandler = transactionHandler;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public void setServiceInterface(Class<?> serviceInterface) {
		this.serviceInterface = serviceInterface;
	}

	@Override
	public Object getObject() throws Exception {
		TransactionHandler txHandler = new TransactionHandler();
		txHandler.setTarget(target);
		txHandler.setTransactionManager(transactionHandler);
		txHandler.setPattern(pattern);
		
		return Proxy.newProxyInstance(getClass().getClassLoader(), new Class[] {serviceInterface}, txHandler);
	}

	@Override
	public Class<?> getObjectType() {
		return serviceInterface;
	}

	@Override
	public boolean isSingleton() {
		return false;
	}
	
}
