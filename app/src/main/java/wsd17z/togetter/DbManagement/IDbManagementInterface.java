package wsd17z.togetter.DbManagement;

/**
 * Created by Kosmos on 05/12/2017.
 */

public interface IDbManagementInterface
{
    DbUserObject getUser(String email);
    void addUser(DbUserObject userObject);
    void deleteUser(DbUserObject userObject);
}