package wsd17z.togetter.DbManagement;

/**
 * Created by Kosmos on 10/12/2017.
 */

public class DbUserObject {
    private String mEmail;
    private int mPassHash;
    private String mName;
    private String mSurname;

    public DbUserObject(String name, String surname, String email, int passHash) {
        super();
        mEmail = email;
        mPassHash = passHash;
        mName = name;
        mSurname = surname;
    }

    public DbUserObject(String email, int passHash) {
        super();
        mEmail = email;
        mPassHash = passHash;
    }

    public String getEmail() {
        return mEmail;
    }

    public String getName() {
        return mName;
    }

    public String getSurname() {
        return mSurname;
    }

    public int getPassHash() {
        return mPassHash;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public void setName(String name) {
        mName = name;
    }

    public void setSurname(String surname) {
        mSurname = surname;
    }

    public void setPassHash(int passHash) {
        mPassHash = passHash;
    }

    @Override
    public String toString() {
        return "User [Name=" + mName + ", Surname=" + mSurname + ", Email=" + mEmail
                + ", PasswordHash=" + mPassHash + "]";
    }
}
