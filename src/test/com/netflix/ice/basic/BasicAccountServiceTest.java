/*
 *
 *  Copyright 2013 Netflix, Inc.
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 *
 */
package com.netflix.ice.basic;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.netflix.ice.processor.config.AccountConfig;
import com.netflix.ice.tag.Account;

public class BasicAccountServiceTest {

	@Test
	public void testAccountConfigMapConstructor() {
		Map<String, AccountConfig> configs = Maps.newHashMap();
		
		configs.put("123456789012", new AccountConfig("123456789012", "account1", "account 1", Lists.newArrayList("Org"), Lists.newArrayList("ec2"), "role", "12345"));
		configs.put("234567890123", new AccountConfig("234567890123", "account2", "account 2", null, null, null, null));
		BasicAccountService bas = new BasicAccountService(configs);
		
		assertEquals("Wrong number of accounts", 2, bas.getAccounts().size());
		assertEquals("Wrong name for account1 by ID", "account1", bas.getAccountById("123456789012").name);
		assertEquals("Wrong id for account1 by name", "123456789012", bas.getAccountByName("account1").id);
		assertEquals("Wrong number of accounts with reserved instances", 1, bas.getReservationAccounts().size());
		assertEquals("Wrong number of reserved instance products", 1, bas.getReservationAccounts().values().iterator().next().size());
		assertEquals("Wrong number of account parents", 1, bas.getAccountById("123456789012").parents.size());
		assertEquals("Wrong root name for account parent", "Org", bas.getAccountById("123456789012").parents.get(0));
	}
	
	@Test
	public void testAccountListConstructor() {
		Account a = new Account("123456789012", "account1", null);
		BasicAccountService bas = new BasicAccountService(Lists.newArrayList(a));
		
		assertEquals("Wrong name for account1 by ID", "account1", bas.getAccountById("123456789012").name);
		assertEquals("Wrong id for account1 by name", "123456789012", bas.getAccountByName("account1").id);
	}
	
	@Test
	public void testUpdateAccounts() {
		List<Account> accounts = Lists.newArrayList();
		String id = "123456789012";
		accounts.add(new Account(id, "OldName", null));
		
		BasicAccountService bas = new BasicAccountService(accounts);
		
		assertEquals("Wrong number of accounts before update", 1, bas.getAccounts().size());
		assertNotNull("Missing account before update fetch by ID", bas.getAccountById(id));
		assertNotNull("Missing account before update fetch by Name", bas.getAccountByName("OldName"));
		assertEquals("Wrong account name before update", "OldName", bas.getAccountById(id).name);
		assertEquals("Wrong account id before update", id, bas.getAccountById(id).id);
		
		accounts = Lists.newArrayList();		
		accounts.add(new Account(id, "NewName", null));
		
		bas.updateAccounts(accounts);
		assertEquals("Wrong number of accounts after update", 1, bas.getAccounts().size());
		assertNotNull("Missing account after update fetch by ID", bas.getAccountById(id));
		assertNotNull("Missing account after update fetch by Name", bas.getAccountByName("NewName"));
		assertEquals("Wrong account name after update", "NewName", bas.getAccountById(id).name);
		assertEquals("Wrong account id after update", id, bas.getAccountById(id).id);
		
	}

}
