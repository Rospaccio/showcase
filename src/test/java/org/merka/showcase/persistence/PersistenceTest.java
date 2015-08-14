package org.merka.showcase.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.merka.showcase.entity.Rank;
import org.merka.showcase.entity.RankItem;
import org.merka.showcase.entity.User;
import org.merka.showcase.entity.UserRole;
import org.merka.showcase.listener.HsqlDBStarterListener;
import org.merka.showcase.listener.StartupManager;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class) // this requires jUnit 4.9 or higher
@ContextConfiguration(locations = {"classpath:/spring/test-persistence-context.xml"})
public class PersistenceTest implements InitializingBean
{
	private static EntityManagerFactory entityManagerFactory;
	
//	@Autowired
	static HsqlDBStarterListener hsqlStarter;
	
	public HsqlDBStarterListener getHsqlStarter() {
		return hsqlStarter;
	}

	public void setHsqlStarter(HsqlDBStarterListener hsqlStarter) {
		this.hsqlStarter = hsqlStarter;
	}

	@BeforeClass
	public static void staticSetup() {
		hsqlStarter = new HsqlDBStarterListener();
		hsqlStarter.setDatabaseName("showcase-test");
		hsqlStarter.initDataBase();
		entityManagerFactory = Persistence.createEntityManagerFactory("org.merka.showcase.test.jpa");
	}
	
	@Override
	public void afterPropertiesSet()
	{
//		StartupManager startupManager = new StartupManager();
//		startupManager.setEntityManagerFactory(entityManagerFactory);	
	}
	
	@Test
	public void testPersistUser()
	{
		EntityManager manager = entityManagerFactory.createEntityManager();
		
		User testUser = new User();
		testUser.setUsername("test");
		
		manager.getTransaction().begin();
		manager.persist(testUser);
		manager.getTransaction().commit();
		
		assertNotNull(testUser.getId());
		
		// retrieves the persisted user with the manager
		User found = manager.find(User.class, testUser.getId());
		assertEquals(testUser.getUsername(), found.getUsername());
		assertEquals(testUser.getId(), found.getId());
		assertNotNull(found.getRanks());
		assertTrue(found.getRanks().isEmpty());
		assertTrue(found.isEnabled());
		
		// deletes the user:
		manager.getTransaction().begin();
		manager.remove(found);
		manager.getTransaction().commit();
		// verifies the deletion
		found = manager.find(User.class, testUser.getId());
		assertNull(found);
	}
	
	@Test
	public void testPersistUserWithInnerRanks()
	{
		EntityManager manager = entityManagerFactory.createEntityManager();
		
		User user = new User();
		user.setUsername("venerabile");
		// adds some ranks
		for(int i = 0; i < 3; i++){
			Rank rank = new Rank();
			rank.setName("rank#" + i);
			rank.setDescription("A test rank");
			user.getRanks().add(rank);
		}
		
		manager.getTransaction().begin();
		manager.persist(user);
		manager.getTransaction().commit();
		
		assertNotNull(user.getId());
		
		User found = manager.find(User.class, user.getId());
		assertNotNull(user);
		assertEquals(user.getId(), found.getId());
		assertTrue(! found.getRanks().isEmpty());
		long rankId;
		assertNotNull(rankId = found.getRanks().get(0).getId());
		
		// deletes all the data
		manager.getTransaction().begin();
		manager.remove(found);
		manager.getTransaction().commit();
		
		found = manager.find(User.class, user.getId());
		assertNull(found);
		Rank checkRank = manager.find(Rank.class, rankId);
		assertNull(checkRank);
	}
	
	@Test(expected = Exception.class)
	public void testFailingRankPersistence(){
		Rank rank = new Rank();
		
		rank.setName("failing");
		rank.setDescription("description");
		// this null value causes a failure
		rank.setOwner(null);
		
		EntityManager manager = entityManagerFactory.createEntityManager();
		manager.getTransaction().begin();
		manager.persist(rank);
		manager.getTransaction().commit();
	}
	
	// Exception expected because "rank" is not set
	@Test(expected = PersistenceException.class)
	public void testPersistRankItems()
	{
		EntityManager manager = entityManagerFactory.createEntityManager();
		
		RankItem item = RankItem.create("item", "a new item");
		
		manager.getTransaction().begin();
		manager.persist(item);
		manager.getTransaction().commit();
		
		assertNotNull(item.getId());
		assertEquals(-1, item.getPositionInRank());
	}
	
	@Test
	public void testPersistUserRankAndItems()
	{
		User user = User.create("testUser");
		EntityManager manager = entityManagerFactory.createEntityManager();
		
		manager.getTransaction().begin();
		manager.persist(user);
		manager.getTransaction().commit();
		
		Rank rank = Rank.create("rank#0", "A test rank");
		rank.setOwner(user);
		user.getRanks().add(rank);
		
		manager.getTransaction().begin();
		manager.persist(rank);
		manager.getTransaction().commit();
		
		RankItem item = RankItem.create("rankItem#0", "A test RankItem");
		rank.appendRankItem(item);
		
		manager.getTransaction().begin();
		manager.persist(item);
		manager.getTransaction().commit();
		
		// retrieves the user
		User found = manager.find(User.class, user.getId());
		assertNotNull(found);
		assertTrue(! found.getRanks().isEmpty());
		assertTrue(! found.getRanks().get(0).getItems().isEmpty());
		
		// deletes all
		manager.getTransaction().begin();
		manager.remove(found);
		manager.getTransaction().commit();
	}
	
	@Test
	public void testPersistUserWithRoles()
	{
		User userWithRoles = User.create("test");
		userWithRoles.setEnabled(true);
		userWithRoles.addRole(UserRole.ROLE_ADMIN);
		
		assertTrue(userWithRoles.getRoles() != null && userWithRoles.getRoles().size() == 2);
		
		EntityManager manager = entityManagerFactory.createEntityManager();
		
		manager.getTransaction().begin();
		manager.persist(userWithRoles);
		
//		for(UserRole role : userWithRoles.getRoles()){
//			manager.persist(role);
//		}
		
		manager.getTransaction().commit();
	
		User found = manager.find(User.class, userWithRoles.getId());
		assertNotNull(found);
		assertNotNull(found.getRoles());
		assertTrue(! found.getRoles().isEmpty());
		assertEquals(2, found.getRoles().size());
		
		UserRole theRole = manager.find(UserRole.class, found.getRoles().get(0).getId());
		assertNotNull(theRole);
		assertTrue(theRole.getRole().equals(UserRole.ROLE_ADMIN) || theRole.getRole().equals(UserRole.ROLE_USER));
		
		manager.getTransaction().begin();
		manager.remove(found);
		manager.getTransaction().commit();
	}
}
