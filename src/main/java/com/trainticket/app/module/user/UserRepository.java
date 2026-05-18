package com.trainticket.app.module.user;

import java.sql.*;
import java.util.*;
import com.trainticket.app.common.Repository;
import com.trainticket.app.config.DBConfig;

public class UserRepository implements Repository<UserDTO> {
        public UserDTO save(UserDTO userDTO) {
                String insert = "insert into users(name,email,is_disabled,address,password) values(?,?,?,?,?)";
                String update = "update users set name =?,email=?,is_disabled =?,address=?,password=? where user_id =?";
                try (Connection connection = DBConfig.getConnection();) {
                        if (userDTO.getId() != null) {
                                PreparedStatement preparedStatement = connection.prepareStatement(update);
                                preparedStatement.setString(1, userDTO.getName());
                                preparedStatement.setString(2, userDTO.getEmail());
                                preparedStatement.setBoolean(3, userDTO.isDisabled());
                                preparedStatement.setString(4, userDTO.getAddress());
                                preparedStatement.setString(5, userDTO.getPassword());
                                preparedStatement.setLong(6, userDTO.getId());

                                preparedStatement.executeUpdate();

                        } else {
                                PreparedStatement preparedStatement = connection.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);
                                preparedStatement.setString(1, userDTO.getName());
                                preparedStatement.setString(2, userDTO.getEmail());
                                preparedStatement.setBoolean(3, userDTO.isDisabled());
                                preparedStatement.setString(4, userDTO.getAddress());
                                preparedStatement.setString(5, userDTO.getPassword());

                                preparedStatement.executeUpdate();

                                ResultSet rs = preparedStatement.getGeneratedKeys();
                                if (rs.next()) {
                                        userDTO.setId(rs.getLong(1));
                                }

                        }
                } catch (SQLException e) {
                        System.out.println("Error at user repo " + e);
                }
                return userDTO;
        }

        public UserDTO findById(Long id) {
                String sql = "select * from users where user_id=?";
                UserDTO userDTO = null;
                try (Connection connection = DBConfig.getConnection();
                                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                        preparedStatement.setLong(1, id);
                        ResultSet rs = preparedStatement.executeQuery();
                        while (rs.next()) {
                                UserDTO.UserBuilder builder = new UserDTO.UserBuilder(id)
                                                .setAddress(rs.getString("address"))
                                                .setName(rs.getString("name")).setEmail(rs.getString("email"))
                                                .setIsDisabled(rs.getBoolean("is_disabled"));

                                userDTO = new UserDTO(builder);

                        }
                } catch (Exception e) {
                        // TODO: handle exception
                        System.out.println("Exception at findbyid" + e);
                }
                return userDTO;
        }

        public List<UserDTO> findAll() {
                String sql = "Select * from users";
                List<UserDTO> users = new ArrayList<>();
                try (Connection connection = DBConfig.getConnection();
                                PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
                        ResultSet rs = preparedStatement.executeQuery();
                        while (rs.next()) {

                                UserDTO.UserBuilder builder = new UserDTO.UserBuilder(rs.getLong("user_id"))
                                                .setAddress(rs.getString("address"))
                                                .setName(rs.getString("name")).setEmail(rs.getString("email"))
                                                .setIsDisabled(rs.getBoolean("is_disabled"));

                                UserDTO userDTO = new UserDTO(builder);
                                users.add(userDTO);

                        }
                } catch (SQLException e) {
                        System.out.println(e);
                }
                return users;
        }

        public UserDTO getUserByEmail(String email,String password){
                String sql = "Select * from users where email = ?";
                UserDTO userDTO = null;
                try (Connection connection = DBConfig.getConnection()){
                        PreparedStatement ps = connection.prepareStatement(sql);
                        ps.setString(1, email);
                        ResultSet rs = ps.executeQuery();
                        if(rs.next()){
                                String crtPassword = rs.getString("password");
                                if(crtPassword.equals(password)){
                                       UserDTO.UserBuilder builder = new UserDTO.UserBuilder(rs.getLong("user_id"))
                                                .setAddress(rs.getString("address"))
                                                .setName(rs.getString("name")).setEmail(rs.getString("email"))
                                                .setIsDisabled(rs.getBoolean("is_disabled"));

                                userDTO = new UserDTO(builder);

                                }
                        }





                } catch (Exception e) {
                        // TODO: handle exception
                        System.out.println(e);
                }


                return userDTO;
        }




@Override
    
public void deleteById(Long id) {
        String sql = "Delete from users where user_id =?";
        try (Connection connection = DBConfig.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.execute();

        } catch (SQLException e) {
            System.out.println(e);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public boolean existsById(Long id) {
        String sql = "Select 1 from users where user_id=?";
        try (Connection connection = DBConfig.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println(e);
        }

        catch (Exception e) {
            System.out.println(e);
        }

        return false;
    }

}
