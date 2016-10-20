/**
 * 
 */
package com.dreamforce.demo.composite.impl;

import java.util.concurrent.BlockingQueue;

import com.dreamforce.demo.composite.Executor;
import com.dreamforce.demo.composite.SObject;

/**
 * @author achadda
 */
public class TransactionalityScript extends SpeedComparisonScript {

	private final String accountName;
	private final String contact1Name;
	private final String contact2Name;
	private final String contact3Name;
	public TransactionalityScript(Executor executor, String instanceIdentifier, BlockingQueue<String> queue, int size, 
			String accountName, String contact1Name, String contact2Name, String contact3Name) {
		super(executor, instanceIdentifier, queue, size);
		this.accountName = accountName;
		this.contact1Name = contact1Name;
		this.contact2Name = contact2Name;
		this.contact3Name = contact3Name;
	}
	
	@Override
	public void executeScript() {
		_createAccountTree();
	}
	
	/**
	 * Creates an account tree with one account and three contacts under it.
	 */
	private void _createAccountTree() {
		SObject account1 = new SObject("account1", "Account");
		account1.setField("Name", accountName);				
		String account1Id = _createSobject(account1, 0);
		
		SObject contact = new SObject("contact1", "Contact");
		contact.setField("lastName", contact1Name);
		contact.setField("accountId", account1Id);
		_createSobject(contact, 1);
		
		SObject contact2 = new SObject("contact2", "Contact");
		contact2.setField("lastName", contact2Name);
		contact2.setField("accountId", account1Id);
		_createSobject(contact2, 1);
		
		SObject contact3 = new SObject("contact3", "Contact");
		contact3.setField("lastName", contact3Name);
		contact3.setField("accountId", account1Id);
		_createSobject(contact3, 1);
	}
}
