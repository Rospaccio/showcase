package org.merka.showcase.service;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.merka.showcase.entity.Rank;
import org.merka.showcase.entity.User;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring/test-persistence-context.xml"})
public class RankServiceTest extends BaseServiceTest{
	
	@Test
	public void testFindById() 
	{
		User user = User.create("testUser");
		user.addRank(Rank.create("testRank", "a test"));
		
		userService.save(user);
		
		List<Rank> all = rankService.findAllByUsername(user.getUsername());
		
		assertEquals(1, all.size());;
		Rank fromList = all.get(0);
		
		Rank found = rankService.findById(fromList.getId());
		
		assertEquals(found.getName(), fromList.getName());
		assertEquals(found.getId(), fromList.getId());
		
		// cleanup
		userService.delete(user);
	}

	@Test
	public void testDeleteRank()
	{
		User owner = User.create("owner");
		
		for(int i = 0; i < 4; i++)
		{
			owner.addRank(Rank.create("test", "test"));
		}
		// saves a user with 4 ranks
		userService.save(owner);
				
		User upToDate = userService.findUserById(owner.getId());
		assertEquals(owner.getId(), upToDate.getId());
		assertEquals(owner.getUsername(), upToDate.getUsername());
		assertEquals(4, upToDate.getRanks().size());
		
		Rank removed = upToDate.getRanks().remove(3);
//		userService.save(upToDate);
		rankService.delete(removed);
		upToDate = userService.findUserById(upToDate.getId());
		assertEquals(3, upToDate.getRanks().size());
	}
	
	@Test
	public void testUpdateRank()
	{
		User user = User.create("test");
		user.addRank(Rank.create("rank", "rank"));
		
		
	}
}
