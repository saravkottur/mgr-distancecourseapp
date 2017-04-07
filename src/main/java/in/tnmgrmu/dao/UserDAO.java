package in.tnmgrmu.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import in.tnmgrmu.model.Role;
import in.tnmgrmu.model.User;

@Repository
public class UserDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private User convert(ResultSet rs) throws SQLException {
		User user = new User();
		user.setId(rs.getLong("id"));
		user.setName(rs.getString("name"));
		user.setEmail(rs.getString("email"));
		user.setMobileNo(rs.getLong("mobile_no"));
		user.setActive(rs.getBoolean("active"));
		user.setCreatedDate(rs.getDate("created_at").toLocalDate());
		user.setModifiedDate(rs.getDate("modified_at").toLocalDate());

		Role r = new Role();
		r.setId(rs.getLong("id"));
		r.setName(rs.getString("role_name"));

		user.setRole(r);
		return user;
	}

	public User findByEmailAndPassword(String emailId, String password) {

		String sql = "select u.id, u.name, u.role_id ,r.name as role_name, u.email, u.mobile_no, u.active, u.created_at, u.modified_at from user_accounts u, roles r where u.role_id = r.id AND u.email = ? and password = ?";

		User user = null;

		try {
			user = jdbcTemplate.queryForObject(sql, new Object[] { emailId, password }, (rs, rowNum) -> {

				return convert(rs);

			});
		} catch (EmptyResultDataAccessException e) {
			e.printStackTrace();

		}
		return user;

	}
	
	public User findById(Long id) {

		String sql = "select u.id, u.name, u.role_id ,r.name as role_name, u.email, u.mobile_no, u.active, u.created_at, u.modified_at from user_accounts u, roles r where u.role_id = r.id AND u.id = ?";

		User user = jdbcTemplate.queryForObject(sql, new Object[] { id }, (rs, rowNum) -> {

			return convert(rs);

		});
		return user;

	}

	public void save(User user) {

		String sql = "insert into user_accounts ( name,email, password, mobile_no, role_id )"
				+ "VALUES ( ?, ?, ?,?,?)";

		int rows = jdbcTemplate.update(sql, user.getName(), user.getEmail(), user.getPassword(), user.getMobileNo(),
				user.getRole().getId());

		System.out.println("No of rows inserted:" + rows);
	}

	public List<User> list() {

		String sql = "select u.id, u.name, u.role_id ,r.name as role_name, u.email, u.mobile_no, u.active, u.created_at, u.modified_at from user_accounts u, roles r where u.role_id = r.id";

		List<User> list = jdbcTemplate.query(sql, new Object[] {}, (rs, rowNum) -> {
			return convert(rs);
		});
		return list;

	}

	public void update(User user) {

		String sql = "update user_accounts set name= ?, role_id = ? ,email=?, mobile_no =? where id=? ";

		Integer rows = jdbcTemplate.update(sql,  user.getName(), user.getRole().getId(),
				user.getEmail(), user.getMobileNo(), user.getId());

		System.out.println("No of rows updated:" + rows);

	}


	public void delete(Long empId) {

		String sql = "delete from user_accounts where id = ? ";
		int rows = jdbcTemplate.update(sql, empId);
		System.out.println("No of rows deleted:" + rows);

	}
	
	public User findByEmailId(String emailId) {

		String sql = "select u.id, u.name, u.role_id ,r.name as role_name, u.email, u.mobile_no, u.active, u.created_at, u.modified_at from user_accounts u, roles r where u.role_id = r.id AND u.email = ? ";

		User user = null;

		try {
			user = jdbcTemplate.queryForObject(sql, new Object[] { emailId }, (rs, rowNum) -> {

				User convert = convert(rs);
				convert.setPassword(rs.getString("PASSWORD"));
				return convert;

			});
		} catch (EmptyResultDataAccessException e) {
			e.printStackTrace();
		}
		return user;

	}
}